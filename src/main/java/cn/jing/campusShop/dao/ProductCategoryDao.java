package cn.jing.campusShop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.jing.campusShop.entity.ProductCategory;

public interface ProductCategoryDao {

	/**
	 * function:通过shopId查询该店铺中所有的商品类别
	 * 
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> queryProductCategoryList(@Param("shopId") long shopId);

	/**
	 * function:批量新增商品类别（返回值是个int类型，表示将这些数据插入表后影响了多少的行数）
	 * 
	 * @param productCategoryList
	 * @return
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);

	/**
	 * function:删除指定商品类别 
	 * 为什么这里有了productCategoryId还需要shopId？--为了更加安全着想！
	 * 
	 * @param productCategoryId
	 * @return
	 */
	int deleteProductCategory(@Param("productCategoryId") long productCategoryId, @Param("shopId") long shopId);
}
