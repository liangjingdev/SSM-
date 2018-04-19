package cn.jing.campusShop.web.shopadmin;

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
@RequestMapping(value = "/shopadmin")
public class UserShopManagementController {

	@Autowired
	private UserShopMapService userShopMapService;

	@RequestMapping(value = "/listusershopmapsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserShopMapsByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 从session当中获取当前店铺的信息
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		// 空值判断
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			UserShopMap userShopMapCondition = new UserShopMap();
			// 传入查询条件
			userShopMapCondition.setShop(currentShop);
			String userName = HttpServletRequestUtil.getString(request, "userName");
			if (userName != null) {
				// 若传入顾客名，则按照顾客名模糊查询
				PersonInfo customer = new PersonInfo();
				customer.setName(userName);
				userShopMapCondition.setUser(customer);
			}
			// 分页获取该店铺下的顾客积分列表
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
