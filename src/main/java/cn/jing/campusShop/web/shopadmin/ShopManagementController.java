package cn.jing.campusShop.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.soap.Addressing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.jing.campusShop.dto.ShopExecution;
import cn.jing.campusShop.entity.Area;
import cn.jing.campusShop.entity.PersonInfo;
import cn.jing.campusShop.entity.Shop;
import cn.jing.campusShop.entity.ShopCategory;
import cn.jing.campusShop.enums.ShopStateEnum;
import cn.jing.campusShop.exceptions.ShopOperationException;
import cn.jing.campusShop.service.AreaService;
import cn.jing.campusShop.service.ShopCategoryService;
import cn.jing.campusShop.service.ShopService;
import cn.jing.campusShop.util.CodeUtil;
import cn.jing.campusShop.util.HttpServletRequestUtil;
import cn.jing.campusShop.util.ImageHolder;

/**
 * function:1、获取店铺注册页面所需要的店铺类型以及区域类型返回给前端 2、对提交的店铺(信息)进行注册(添加进入数据库)
 * 
 * @author liangjing
 *
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;

	/**
	 * function:获取店铺注册页面所需要的店铺类型列表以及区域类型列表返回给前端(返回的是json字符串)
	 * 注意：首页显示的是一级店铺类型，而所有店铺都是存在于二级店铺类型里面的，也就是说店铺所属的shopCategoryId的parentId一定是非空的
	 */
	@RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo() {
		// 定义返回值变量
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			// 获取所有的二级店铺类型（即只需要选出ShopCategory的parentId不为空的类别来供店铺选择即可）
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	/**
	 * function:1、获取店铺相关信息 2、获取用户上传的店铺图片 3、对图片进行相关处理后并保存到对应的目录 4、设置店铺拥有者并注册该店铺
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 1、首先判断用户输入的验证码信息是否正确
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		// 2、接收并转化相应的参数，包括店铺信息以及图片信息
		// 获取前端传递过来的店铺相关信息的json字符串
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		// ObjectManager是对json字符串转换的一个工具类(可以将json字符串转换为java对象，也可以将java对象转换为json字符串，来源于第三方框架)
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			// 将前端传递过来的json字符串转换为java对象
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		// CommonsMultipartFile是MultipartFile接口的实现类，用于接收上传的文件并且可以获取该文件的各种信息(getInputStream()方法可以获取上传文件的输入流)
		CommonsMultipartFile shopImg = null;
		// Spring MVC的文件上传是通过MultiResolver(Multipart解析器处理的)
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断传递过来的请求是否含有文件类型(图片)
		if (commonsMultipartResolver.isMultipart(request)) {
			// MultipartHttpServletRequest具备原有HttpServletRequest对象的操作能力，也具备了文件操作的能力。（此处进行转换）
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			// 获取请求上传的文件
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}
		// 3、注册店铺
		if (shop != null && shopImg != null) {
			// 获取用户信息(利用Session来获取用户信息--request.getSession().getAttribute("user"),那么这个user从哪里来呢？进行店铺相关操作的时候是需要先登录的，那么等用户登录之后就可以将用户信息写入到session里面，key为"user")
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
			ShopExecution sExecution;
			try {
				ImageHolder imageHolder;
				imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
				// 添加店铺
				sExecution = shopService.addShop(shop, imageHolder);
				if (sExecution.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					// 显示该用户可以操作的店铺列表--可以将该店铺列表保存在session里面返回给前端进行展示(因为用户跟店铺是一对多的关系，一个用户可以有多个店铺)
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList"); // 从session中获取该用户原有的店铺列表信息，查看是否已经用于其它店铺
					// 如果这是用户第一次注册店铺
					if (shopList == null || shopList.size() == 0) {
						shopList = new ArrayList<Shop>();
						shopList.add(sExecution.getShop());
						request.getSession().setAttribute("shopList", shopList);
					} else {
						// 用户在注册该店铺之前已经有其它店铺了
						shopList.add(sExecution.getShop());
						request.getSession().setAttribute("shopList", shopList);
					}
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", sExecution.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
	}

	/**
	 * function:通过ShopId查询对应的店铺信息以及所有的区域信息
	 * (如：http://localhost:8080/campusShop/shopadmin/getshopbyid?shopId=1)
	 * 注意：要记得先开启redis服务再运行该web程序，否则会出现bug（因为店铺类别信息以及区域信息等的存储与redis相关）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取前端传递过来的shopId
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		// 判断是否已经传递过来
		if (shopId > -1) {
			try {
				Shop shop = shopService.getByShopId(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("success", true);
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}

	/**
	 * function:修改更新店铺信息（注意：店铺名称以及店铺类别不允许被修改，店铺拥有者(用户)信息也不需要修改）(店铺图片可修改可不修改)
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}
		// 确保shop对象以及其shopId不为空,接下来修改店铺信息
		if (shop != null && shop.getShopId() != null) {
			ShopExecution sExecution;
			try {
				if (shopImg == null) {
					sExecution = shopService.modifyShop(shop, null);
				} else {
					ImageHolder imageHolder;
					imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
					sExecution = shopService.modifyShop(shop, imageHolder);
				}
				if (sExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", sExecution.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺Id");
			return modelMap;
		}
	}

	/**
	 * function:获取(指定条件下的)店铺列表信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 模拟用户登录来进行测试
		// user.setUserId(1L);
		// user.setName("test");
		// request.getSession().setAttribute("user", user);
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			// 获取该用户所注册的店铺列表信息
			ShopExecution shopExecution = shopService.getShopList(shopCondition, 1, 100);
			modelMap.put("shopList", shopExecution.getShopList());
			// 列出店铺列表成功之后，将该店铺列表放入session中作为权限验证依据，即该账号只能操作它自己的店铺
			request.getSession().setAttribute("shopList", shopExecution.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	/**
	 * function:从展现的店铺列表中点击某个店铺进入该店铺管理页面之前的验证。（管理页面可以修改店铺信息以及发布商品等。）
	 * 判断如果用户是直接输入链接访问该页面的话(即不经过登录或者是不经过店铺列表中链接过来的话)，
	 * 那么就会获取不到shopId以及Shop对象的，从而就会产生重定向导到店铺列表去，就说明这是违规操作了。
	 * 也就是说判断你是否权限对该店铺进行操作，若有权限，即设置重定向为false（接着由前端获取到返回的json字符串进行判断redirect属性是否为false，再进行下一步操作）。
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getshopmanagementinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取店铺Id，因为这是从店铺列表中选择（点击）其中一个店铺跳转过来的
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId <= 0) {
			// 如果前端没有传shopId过来的话，那么就试着从session中去获取
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if (currentShopObj == null) {
				// 如果在session中还是获取不到的话，那么就重定向回到原来的页面
				modelMap.put("redirect", true);
				modelMap.put("url", "/campusShop/shopadmin/shoplist");
			} else {
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		} else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			// 在session当中添加当前操作的店铺对象
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}
}
