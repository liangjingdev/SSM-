package cn.jing.campusShop.web.shopadmin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jing.campusShop.dto.ProductCategoryExecution;
import cn.jing.campusShop.dto.Result;
import cn.jing.campusShop.entity.ProductCategory;
import cn.jing.campusShop.entity.Shop;
import cn.jing.campusShop.entity.ShopCategory;
import cn.jing.campusShop.enums.ProductCategoryStateEnum;
import cn.jing.campusShop.service.ProductCategoryService;

/**
 * function:@RequestMapping(value="/shopadmin")表明是店家管理系统方面的操作
 * 
 * @author liangjing
 *
 */
@Controller
@RequestMapping(value = "/shopadmin")
public class ProductCategoryManagementController {

	@Autowired
	private ProductCategoryService productCategoryService;

	@RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
	@ResponseBody
	private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
		// 从session中获取用户当前操作的店铺，并获取它的shopId去
		// 询该店铺的所有的商品类别(在getShopManagementInfo(HttpServletRequest
		// request)方法当中已经向session中保存了当前操作的店铺对象)
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		List<ProductCategory> list = null;
		if (currentShop != null && currentShop.getShopId() > 0) {
			list = productCategoryService.getProductCategoryList(currentShop.getShopId());
			return new Result<List<ProductCategory>>(true, list);
		} else {
			return new Result<List<ProductCategory>>(false, ProductCategoryStateEnum.INNER_ERROR.getState(),
					ProductCategoryStateEnum.INNER_ERROR.getStateInfo());
		}
	}

	/**
	 * function:批量添加商品类别
	 * 
	 * @RequestBody 作用:用于将Controller的方法参数，根据HTTP Request
	 *              Header的content-Type的内容,通过适当的HttpMessageConverter转换为JAVA类
	 *              使用时机：POST或者PUT的数据是JSON格式或者XML格式,而不是普通的键值对形式.
	 * 
	 * @param productCategoryList
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addproductcategorys", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 为什么要从session中取出"currentShop"的值呢？因为ProductCategory对象需要shopid(店铺ID)
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		for (ProductCategory productCategory : productCategoryList) {
			productCategory.setShopId(currentShop.getShopId());
		}
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				ProductCategoryExecution productCategoryExecution = productCategoryService
						.batchAddProductCategory(productCategoryList);
				if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", productCategoryExecution.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少输入一个商品类别");
		}
		return modelMap;
	}

	/**
	 * function:将此类别下的商品里的类别id置为空，再删除该商品类别
	 * 
	 * @param productCategoryId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (productCategoryId != null && productCategoryId > 0) {
			try {
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				ProductCategoryExecution pe = productCategoryService.deleteProductCategory(productCategoryId,
						currentShop.getShopId());
				if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少选择一个商品类别");
		}
		return modelMap;
	}
}
