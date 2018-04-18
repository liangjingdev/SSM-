package cn.jing.campusShop.web.frontend;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import cn.jing.campusShop.dto.UserAwardMapExecution;
import cn.jing.campusShop.entity.Award;
import cn.jing.campusShop.entity.PersonInfo;
import cn.jing.campusShop.entity.Shop;
import cn.jing.campusShop.entity.UserAwardMap;
import cn.jing.campusShop.enums.UserAwardMapStateEnum;
import cn.jing.campusShop.service.AwardService;
import cn.jing.campusShop.service.PersonInfoService;
import cn.jing.campusShop.service.ShopService;
import cn.jing.campusShop.service.UserAwardMapService;
import cn.jing.campusShop.util.CodeUtil;
import cn.jing.campusShop.util.HttpServletRequestUtil;
import cn.jing.campusShop.util.ShortNetAddress;
import cn.jing.campusShop.util.wechat.WechatPathUtil;

@Controller
@RequestMapping("/frontend")
public class MyAwardController {
	@Autowired
	private UserAwardMapService userAwardMapService;
	@Autowired
	private AwardService awardService;
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private ShopService shopService;

	/*********** 与微信相关的url ************/
	// 微信获取用户信息的api前缀
	private static String urlPrefix = WechatPathUtil.wechat_prefix;
	// 微信获取用户信息的api中间部分
	private static String urlMiddle = WechatPathUtil.wechat_middle;
	// 微信获取用户信息的api后缀
	private static String urlSuffix = WechatPathUtil.wechat_suffix;
	// 微信回传给的响应添加用户奖品映射信息的url
	private static String exchangeUrl = WechatPathUtil.wechat_exchange_url;

	/**
	 * function:获取顾客的兑换列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listuserawardmapsbycustomer", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserAwardMapsByCustomer(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 从session中获取顾客信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// 空值判断，主要确保用户ID不为空
		if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != null)) {
			UserAwardMap userAwardMapCondition = new UserAwardMap();
			userAwardMapCondition.setUser(user);
			// 如果前端传入shopId的话，那么就去查询单个店铺下该顾客的奖品兑换信息，若没有传入ShopId，则就去查询所有的店铺下该顾客的奖品兑换信息
			long shopId = HttpServletRequestUtil.getLong(request, "shopId");
			if (shopId > -1) {
				// 若店铺Id为非空，则将其添加进查询条件，即查询该用户在某个店铺下的兑换信息
				Shop shop = new Shop();
				shop.setShopId(shopId);
				userAwardMapCondition.setShop(shop);
			}
			String awardName = HttpServletRequestUtil.getString(request, "awardName");
			if (awardName != null) {
				// 若奖品名为非空，则将其添加进查询条件里进行模糊查询
				Award award = new Award();
				award.setAwardName(awardName);
				userAwardMapCondition.setAward(award);
			}
			// 根据传入的查询条件分页获取用户奖品映射信息
			UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(userAwardMapCondition, pageIndex, pageSize);
			modelMap.put("userAwardMapList", ue.getUserAwardMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or userId");
		}
		return modelMap;
	}

	/**
	 * function:在线兑换奖品
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/adduserawardmap", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addUserAwardMap(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从session中获取用户信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// 从前端请求中获取奖品Id
		Long awardId = HttpServletRequestUtil.getLong(request, "awardId");
		// 封装成用户奖品映射对象
		UserAwardMap userAwardMap = compactUserAwardMap4Add(user, awardId);
		// 空值判断
		if (userAwardMap != null) {
			try {
				// 添加兑换记录
				UserAwardMapExecution se = userAwardMapService.addUserAwardMap(userAwardMap);
				if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请选择领取的奖品");
		}
		return modelMap;
	}

	/**
	 * function:根据顾客奖品映射Id获取单条顾客奖品的映射信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getawardbyuserawardid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getAwardById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取前端传递过来的userAwardId
		long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
		// 空值判段
		if (userAwardId > -1) {
			// 根据Id获取顾客奖品的映射信息，进而获取奖品Id
			UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
			// 根据奖品Id获取奖品信息
			Award award = awardService.getAwardById(userAwardMap.getAward().getAwardId());
			// 将奖品信息和领取状态返回给前端
			modelMap.put("award", award);
			modelMap.put("usedStatus", userAwardMap.getUsedStatus());
			modelMap.put("userAwardMap", userAwardMap);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty awardId");
		}
		return modelMap;
	}

	/**
	 * function:生成奖品的领取二维码，供操作员扫描，证明已经领取，微信扫一扫就能链接到对应的URL里面
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/generateqrcode4shopauth", method = RequestMethod.GET)
	@ResponseBody
	private void generateQRCode4ShopAuth(HttpServletRequest request, HttpServletResponse response) {
		// 获取前端传递过来的用户奖品映射Id
		long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
		// 根据Id获取顾客奖品映射的实体类对象
		UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
		// 从session中获取顾客的信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// 空值判断(以及判断对应的顾客奖品映射信息中的顾客Id是否与当前登录的用户的Id一致)
		if (userAwardMap != null && user != null && user.getUserId() != null
				&& userAwardMap.getUser().getUserId() == user.getUserId()) {
			// 获取当前时间戳，以保证二维码的时间有效性，精确到毫秒(为了保证安全性，应该给二维码设置上时效)
			long timeStamp = System.currentTimeMillis();
			String content = "{aaauserAwardIdaaa:" + userAwardId + ",aaacustomerIdeaaa:" + user.getUserId()
					+ ",aaacreateTimeaaa:" + timeStamp + "}";
			try {
				// 将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标URL
				String longUrl = urlPrefix + exchangeUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
				// 将目标URL转换成短的URL
				String shortUrl = ShortNetAddress.getShortURL(longUrl);
				// 调用二维码生成的工具类方法，传入短的URL，生成二维码
				BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(shortUrl, response);
				// 将二维码以图片流的形式输出到前端
				MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * function:组合UserAwardMap实体类对象信息
	 * 
	 * @param user
	 * @param awardId
	 * @return
	 */
	private UserAwardMap compactUserAwardMap4Add(PersonInfo user, Long awardId) {
		UserAwardMap userAwardMap = null;
		if (user != null && user.getUserId() != null && awardId != -1) {
			userAwardMap = new UserAwardMap();
			PersonInfo personInfo = personInfoService.getPersonInfoById(user.getUserId());
			Award award = awardService.getAwardById(awardId);
			Shop shop = shopService.getByShopId(award.getShopId());
			userAwardMap.setShop(shop);
			userAwardMap.setUser(personInfo);
			userAwardMap.setAward(award);
			userAwardMap.setPoint(award.getPoint());
			userAwardMap.setCreateTime(new Date());
			userAwardMap.setUsedStatus(0);
		}
		return userAwardMap;
	}
}
