package cn.jing.campusShop.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jing.campusShop.dto.UserProductMapExecution;
import cn.jing.campusShop.entity.PersonInfo;
import cn.jing.campusShop.entity.Product;
import cn.jing.campusShop.entity.Shop;
import cn.jing.campusShop.entity.UserProductMap;
import cn.jing.campusShop.service.UserProductMapService;
import cn.jing.campusShop.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class MyProductController {
	@Autowired
	private UserProductMapService userProductMapService;

	/**
	 * function:列出某个顾客的商品消费记录信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listuserproductmapsbycustomer", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserProductMapsByCustomer(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != -1)) {
			UserProductMap userProductMapCondition = new UserProductMap();
			userProductMapCondition.setUser(user);
			long shopId = HttpServletRequestUtil.getLong(request, "shopId");
			if (shopId > -1) {
				// 若传入店铺信息，则列出某个店铺下该顾客的消费历史
				Shop shop = new Shop();
				shop.setShopId(shopId);
				userProductMapCondition.setShop(shop);
			}
			String productName = HttpServletRequestUtil.getString(request, "productName");
			if (productName != null) {
				// 若传入的商品名不为空，则按照商品名模糊查询
				Product product = new Product();
				product.setProductName(productName);
				userProductMapCondition.setProduct(product);
			}
			UserProductMapExecution ue = userProductMapService.listUserProductMap(userProductMapCondition, pageIndex,
					pageSize);
			modelMap.put("userProductMapList", ue.getUserProductMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
}
