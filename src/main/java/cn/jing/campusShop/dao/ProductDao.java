package cn.jing.campusShop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.jing.campusShop.entity.Product;

public interface ProductDao {

	/**
	 * function:查询商品列表并分页，可输入的条件有： 商品名（模糊），商品状态，店铺Id,商品类别
	 * 
	 * @param productCondition
	 * @param beginIndex
	 * @param pageSize
	 * @return
	 */
	List<Product> queryProductList(@Param("productCondition") Product productCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);

	/**
	 * function:查询对应的商品总数（满足productCondition条件的商品总数）
	 * 
	 * @param productCondition
	 * @return
	 */
	int queryProductCount(@Param("productCondition") Product productCondition);

	/**
	 * function:通过productId查询唯一的商品信息
	 * 
	 * @param productId
	 * @return
	 */
	Product queryProductById(long productId);

	/**
	 * 插入商品
	 * 
	 * @param product
	 * @return
	 */
	int insertProduct(Product product);

	/**
	 * 更新商品信息
	 * 
	 * @param product
	 * @return
	 */
	int updateProduct(Product product);

	/**
	 * function:删除某商品类别之前，将该商品类别下的所有商品的商品类别ID置为空
	 * 
	 * @param productCategoryId
	 * @return
	 */
	int updateProductCategoryToNull(long productCategoryId);
}
