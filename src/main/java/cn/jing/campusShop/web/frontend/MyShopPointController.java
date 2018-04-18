package cn.jing.campusShop.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jing.campusShop.dto.UserShopMapExecution;
import cn.jing.campusShop.entity.PersonInfo;
import cn.jing.campusShop.entity.Shop;
import cn.jing.campusShop.entity.UserShopMap;
import cn.jing.campusShop.service.UserShopMapService;
import cn.jing.campusShop.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class MyShopPointController {
	@Autowired
	private UserShopMapService userShopMapService;

	/**
	 * function:列出该用户的积分情况
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listusershopmapsbycustomer", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserShopMapsByCustomer(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 从session中获取顾客信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != null)) {
			UserShopMap userShopMapCondition = new UserShopMap();
			userShopMapCondition.setUser(user);
			long shopId = HttpServletRequestUtil.getLong(request, "shopId");
			if (shopId > -1) {
				Shop shop = new Shop();
				shop.setShopId(shopId);
				userShopMapCondition.setShop(shop);
			}
			UserShopMapExecution ue = userShopMapService.listUserShopMap(userShopMapCondition, pageIndex, pageSize);
			modelMap.put("userShopMapList", ue.getUserShopMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
}
