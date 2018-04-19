package cn.jing.campusShop.web.shopadmin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.jing.campusShop.dto.ShopAuthMapExecution;
import cn.jing.campusShop.dto.UserAwardMapExecution;
import cn.jing.campusShop.dto.WechatInfo;
import cn.jing.campusShop.entity.Award;
import cn.jing.campusShop.entity.PersonInfo;
import cn.jing.campusShop.entity.Shop;
import cn.jing.campusShop.entity.ShopAuthMap;
import cn.jing.campusShop.entity.UserAccessToken;
import cn.jing.campusShop.entity.UserAwardMap;
import cn.jing.campusShop.entity.WechatAuth;
import cn.jing.campusShop.enums.UserAwardMapStateEnum;
import cn.jing.campusShop.service.AwardService;
import cn.jing.campusShop.service.PersonInfoService;
import cn.jing.campusShop.service.ShopAuthMapService;
import cn.jing.campusShop.service.UserAwardMapService;
import cn.jing.campusShop.service.WechatAuthService;
import cn.jing.campusShop.util.HttpServletRequestUtil;
import cn.jing.campusShop.util.wechat.WechatUtil;

@Controller
@RequestMapping("/shopadmin")
public class UserAwardManagementController {
	@Autowired
	private UserAwardMapService userAwardMapService;
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private ShopAuthMapService shopAuthMapService;
	@Autowired
	private WechatAuthService wechatAuthService;

	/**
	 * function:根据查询条件分页返回指定店铺的顾客与奖品的映射信息的列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listuserawardmapsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserAwardMapsByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从session中获取店铺信息
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 空值判断
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			UserAwardMap userAwardMap = new UserAwardMap();
			userAwardMap.setShop(currentShop);
			// 从请求中获取奖品名
			String awardName = HttpServletRequestUtil.getString(request, "awardName");
			if (awardName != null) {
				// 如果需要按照奖品名称搜索，则添加搜索条件
				Award award = new Award();
				award.setAwardName(awardName);
				userAwardMap.setAward(award);
			}
			// 分页返回结果
			UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(userAwardMap, pageIndex, pageSize);
			modelMap.put("userAwardMapList", ue.getUserAwardMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}

	/**
	 * function:操作员扫描顾客的奖品二维码并派发奖品，证明顾客已经领取过
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/exchangeaward", method = RequestMethod.POST)
	@ResponseBody
	private String exchangeAward(HttpServletRequest request) throws UnsupportedEncodingException {
		// 获取负责扫描二维码的店员信息
		WechatAuth auth = getOperatorInfo(request);
		if (auth != null) {
			// 通过userId获取店员信息
			PersonInfo operator = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
			// 设置上用户的session
			request.getSession().setAttribute("user", operator);
			// 解析微信回传过来的自定义参数state，由于之前进行了编码，这里需要解码以下
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
			// 获取用户奖品映射主键
			Long userAwardId = wechatInfo.getUserAwardId();
			// 获取顾客Id
			Long customerId = wechatInfo.getCustomerId();
			// 将顾客信息，操作员信息以及奖品信息封装成userAwardMap
			UserAwardMap userAwardMap = compactUserAwardMap4Exchange(customerId, userAwardId, operator);
			if (userAwardId != null) {
				try {
					// 检查该员工是否具有扫码权限
					if (!checkShopAuth(operator.getUserId(), userAwardMap)) {
						return "shop/operationfail";
					}
					// 修改奖品的领取状态
					UserAwardMapExecution se = userAwardMapService.modifyUserAwardMap(userAwardMap);
					if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
						return "shop/operationsuccess";
					}
				} catch (RuntimeException e) {
					return "shop/operationfail";
				}
			}
		}
		return "shop/operationfail";
	}

	private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
		if (wechatInfo != null && wechatInfo.getUserAwardId() != null && wechatInfo.getCustomerId() != null
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

	/**
	 * function:组建UserAwardMap实体类对象信息，进行更新
	 * 
	 * @param customerId
	 * @param userAwardId
	 * @param operator
	 * @return
	 */
	private UserAwardMap compactUserAwardMap4Exchange(Long customerId, Long userAwardId, PersonInfo operator) {
		UserAwardMap userAwardMap = null;
		if (customerId != null && userAwardId != null) {
			userAwardMap = new UserAwardMap();
			PersonInfo customer = new PersonInfo();
			customer.setUserId(customerId);
			userAwardMap.setUser(customer);
			userAwardMap.setUserAwardId(userAwardId);
			userAwardMap.setOperator(operator);
			userAwardMap.setUsedStatus(1);
		}
		return userAwardMap;
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

	/**
	 * function:检查扫码的人员是否有操作权限
	 * 
	 * @param userId
	 * @param userProductMap
	 * @return
	 */
	private boolean checkShopAuth(Long userId, UserAwardMap userAwardMap) {
		// 获取该店铺的所有授权信息
		ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService
				.listShopAuthMapByShopId(userAwardMap.getShop().getShopId(), 1, 1000);
		for (ShopAuthMap shopAuthMap : shopAuthMapExecution.getShopAuthMapList()) {
			// 看看是否给过该扫码人员进行授权
			if (shopAuthMap.getEmployee().getUserId() == userId) {
				return true;
			}
		}
		return false;
	}
}
