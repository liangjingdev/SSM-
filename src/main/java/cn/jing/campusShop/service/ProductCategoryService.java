package cn.jing.campusShop.service;

import java.util.List;

import cn.jing.campusShop.dto.ProductCategoryExecution;
import cn.jing.campusShop.entity.ProductCategory;
import cn.jing.campusShop.exceptions.ProductCategoryOperationException;

public interface ProductCategoryService {

	/**
	 * function:查询指定某个店铺下的所有商品类别信息
	 * 
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> getProductCategoryList(long shopId);

	/**
	 * function:批量添加商品类别
	 * 
	 * @param productCategoriyList
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException;

	/**
	 * FUNCTION:将此类别下的商品里的类别id置为空，再删除该商品类别
	 * 
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException;
}
