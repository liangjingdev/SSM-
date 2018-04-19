package cn.jing.campusShop.web.shopadmin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.jing.campusShop.dto.EchartSeries;
import cn.jing.campusShop.dto.EchartXAxis;
import cn.jing.campusShop.dto.ShopAuthMapExecution;
import cn.jing.campusShop.dto.UserProductMapExecution;
import cn.jing.campusShop.dto.WechatInfo;
import cn.jing.campusShop.entity.PersonInfo;
import cn.jing.campusShop.entity.Product;
import cn.jing.campusShop.entity.ProductSellDaily;
import cn.jing.campusShop.entity.Shop;
import cn.jing.campusShop.entity.ShopAuthMap;
import cn.jing.campusShop.entity.UserAccessToken;
import cn.jing.campusShop.entity.UserProductMap;
import cn.jing.campusShop.entity.WechatAuth;
import cn.jing.campusShop.enums.UserProductMapStateEnum;
import cn.jing.campusShop.service.ProductSellDailyService;
import cn.jing.campusShop.service.ProductService;
import cn.jing.campusShop.service.ShopAuthMapService;
import cn.jing.campusShop.service.UserProductMapService;
import cn.jing.campusShop.service.WechatAuthService;
import cn.jing.campusShop.util.HttpServletRequestUtil;
import cn.jing.campusShop.util.wechat.WechatUtil;

@Controller
@RequestMapping("/shopadmin")
public class UserProductManagementController {

	@Autowired
	private UserProductMapService userProductMapService;
	@Autowired
	private ProductSellDailyService productSellDailyService;
	@Autowired
	private WechatAuthService wechatAuthService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ShopAuthMapService shopAuthMapService;

	/**
	 * function:查询指定店铺的顾客消费商品信息(商品消费记录)（还可根据商品名模糊查询）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listuserproductmapsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserProductMapsByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取分页情况
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 获取当前的店铺信息
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		// 空值校验，主要确保shopId不为空
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			// 添加查询条件
			UserProductMap userProductMapCondition = new UserProductMap();
			userProductMapCondition.setShop(currentShop);
			String productName = HttpServletRequestUtil.getString(request, "productName");
			if (productName != null) {
				// 若前端想要按照商品名模糊查询，则传入productName
				Product product = new Product();
				product.setProductName(productName);
				userProductMapCondition.setProduct(product);
			}
			// 根据传入的查询条件获取该店铺的顾客消费商品信息
			UserProductMapExecution ue = userProductMapService.listUserProductMap(userProductMapCondition, pageIndex,
					pageSize);
			modelMap.put("userProductMapList", ue.getUserProductMapList());
			modelMap.put("count", ue.getCount()); // 将count值返回，用来做分页
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}

	/**
	 * function:列出指定店铺的所有商品在指定时间段的日销售量信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listproductselldailyinfobyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductSellDailyInfoByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取当前的店铺
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		// 空值校验，主要确保shopId不为空
		if (currentShop != null && currentShop.getShopId() != null) {
			// 添加查询条件
			ProductSellDaily productSellDailyCondition = new ProductSellDaily();
			productSellDailyCondition.setShop(currentShop);
			// 通过Calendar实体类对象来获取日期信息
			Calendar calendar = Calendar.getInstance();
			// 获取昨天的日期
			calendar.add(Calendar.DATE, -1);
			Date endTime = calendar.getTime();
			// 获取七天前的日期(因为之前已经-1了，现在再-6，那么合起来就是-7，即七天前的日期)
			calendar.add(Calendar.DATE, -6);
			Date beginTime = calendar.getTime();
			// 根据传入的查询条件获取店铺的商品销售情况
			List<ProductSellDaily> productSellDailyList = productSellDailyService
					.listProductSellDaily(productSellDailyCondition, beginTime, endTime);
			// 指定日期格式
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			// 商品名列表，保证唯一性
			HashSet<String> legendData = new HashSet<String>();
			// x轴数据
			HashSet<String> xData = new HashSet<String>();
			// 定义series
			List<EchartSeries> series = new ArrayList<EchartSeries>();
			// 日销量列表
			List<Integer> totalList = new ArrayList<Integer>();
			// 当前商品名，默认为空
			String currentProductName = "";
			for (int i = 0; i < productSellDailyList.size(); i++) {
				ProductSellDaily productSellDaily = productSellDailyList.get(i);
				// 自动去重，因为使用的是set集合
				legendData.add(productSellDaily.getProduct().getProductName());
				xData.add(format.format(productSellDaily.getCreateTime()));
				if (!currentProductName.equals(productSellDaily.getProduct().getProductName())
						&& !currentProductName.isEmpty()) {
					// 如果currentProductName不等于当前获取的商品名，则说明是遍历到下一个商品的日销量信息了，那么需要将前一轮遍历遍历的信息放入series当中
					// 包括商品名以及与商品对应的统计日期以及当日销量
					EchartSeries es = new EchartSeries();
					es.setName(currentProductName);
					// 为什么要调用subList呢？--因为totalList是一个引用对象，如果直接把totalList集合对象赋给Data的话，那么接下来对totalList进行重置的话，Data的数据
					// 也会被重置掉的。所以呢，调用subList方法是为了克隆出一个新的List集合对象，以保证接下来对totalList集合对象的重置不会影响到Data中现有的数据
					es.setData(totalList.subList(0, totalList.size()));
					series.add(es);
					// 重置totalList
					totalList = new ArrayList<Integer>();
					// 变换下currentProductName为当前的productName
					currentProductName = productSellDaily.getProduct().getProductName();
					// 继续添加新的值
					totalList.add(productSellDaily.getTotal());
				} else {
					// 如果还是当前的productId则继续添加新值
					totalList.add(productSellDaily.getTotal());
					currentProductName = productSellDaily.getProduct().getProductName();
				}
				// 队列之末，别忘记还需要将最后一个商品的销量信息也添加到series中
				if (i == productSellDailyList.size() - 1) {
					EchartSeries es = new EchartSeries();
					es.setName(currentProductName);
					es.setData(totalList.subList(0, totalList.size()));
					series.add(es);
				}
			}
			modelMap.put("series", series);
			modelMap.put("legendData", legendData);
			// 拼接出xAxis
			List<EchartXAxis> xAxis = new ArrayList<EchartXAxis>();
			EchartXAxis exa = new EchartXAxis();
			exa.setData(xData);
			xAxis.add(exa);
			modelMap.put("xAxis", xAxis);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}

	@RequestMapping(value = "/adduserproductmap", method = RequestMethod.GET)
	private String addUserProductMap(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		// 获取微信授权信息
		WechatAuth auth = getOperatorInfo(request);
		if (auth != null) {
			PersonInfo operator = auth.getPersonInfo();
			request.getSession().setAttribute("user", operator);
			// 获取二维码里state所携带的content信息并解码
			String qrCodeInfo = new String(
					URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8"));
			ObjectMapper mapper = new ObjectMapper();
			WechatInfo wechatInfo = null;
			try {
				wechatInfo = mapper.readValue(qrCodeInfo.replace("aaa", "\""), WechatInfo.class);
			} catch (Exception e) {
				return "shop/operationfail";
			}
			// 检验二维码是否过期
			if (!checkQRCodeInfo(wechatInfo)) {
				return "shop/operationfail";
			}
			// 获取添加消费记录所需要的参数并组装成UserProductMap实例
			Long productId = wechatInfo.getProductId();
			Long customerId = wechatInfo.getCustomerId();
			UserProductMap userProductMap = compactUserProductMap4Add(customerId, productId, auth.getPersonInfo());
			// 空值检验
			if (userProductMap != null && customerId != -1) {
				try {
					if (!checkShopAuth(operator.getUserId(), userProductMap)) {
						return "shop/operationfail";
					}
					// 添加消费记录
					UserProductMapExecution se = userProductMapService.addUserProductMap(userProductMap);
					if (se.getState() == UserProductMapStateEnum.SUCCESS.getState()) {
						return "shop/operationsuccess";
					}
				} catch (RuntimeException e) {
					return "shop/operationfail";
				}
			}
		}
		return "shop/operationfail";
	}

	/**
	 * function:检查扫码的人员是否有操作权限
	 * 
	 * @param userId
	 * @param userProductMap
	 * @return
	 */
	private boolean checkShopAuth(Long userId, UserProductMap userProductMap) {
		// 获取该店铺的所有授权信息
		ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService
				.listShopAuthMapByShopId(userProductMap.getShop().getShopId(), 1, 1000);
		for (ShopAuthMap shopAuthMap : shopAuthMapExecution.getShopAuthMapList()) {
			// 看看是否给过该扫码人员进行授权
			if (shopAuthMap.getEmployee().getUserId() == userId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * function:根据传入的customerId，productId以及操作员信息组建用户消费记录
	 * 
	 * @param customerId
	 * @param productId
	 * @param operator
	 * @return
	 */
	private UserProductMap compactUserProductMap4Add(Long customerId, Long productId, PersonInfo operator) {
		UserProductMap userProductMap = null;
		if (customerId != null && productId != null) {
			userProductMap = new UserProductMap();
			// 主要是为了获取商品积分
			Product product = productService.getProductById(productId);
			userProductMap.setProduct(product);
			userProductMap.setShop(product.getShop());
			PersonInfo customer = new PersonInfo();
			customer.setUserId(customerId);
			userProductMap.setUser(customer);
			userProductMap.setPoint(product.getPoint());
			userProductMap.setCreateTime(new Date());
			userProductMap.setOperator(operator);
		}
		return userProductMap;
	}

	/**
	 * function:根据微信回传的code获取用户信息
	 * 
	 * @param request
	 * @return
	 */
	private WechatAuth getOperatorInfo(HttpServletRequest request) {
		String code = request.getParameter("code");
		WechatAuth wechatAuth = null;
		if (null != code) {
			UserAccessToken token;
			try {
				token = WechatUtil.getUserAccessToken(code);
				String openId = token.getOpenId();
				request.getSession().setAttribute("openId", openId);
				// 获取该微信用户所对应的WechatAuth实体类对象
				wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return wechatAuth;
	}

	private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
		if (wechatInfo != null && wechatInfo.getProductId() != null && wechatInfo.getCustomerId() != null
				&& wechatInfo.getCreateTime() != null) {
			long nowTime = System.currentTimeMillis();
			if ((nowTime - wechatInfo.getCreateTime()) <= 5000) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
