package cn.jing.campusShop.dao;

import java.util.List;

import cn.jing.campusShop.entity.ProductImg;

public interface ProductImgDao {

	/**
	 * function:列出某个商品的详情图列表
	 * 
	 * @param productId
	 * @return
	 */
	List<ProductImg> queryProductImgList(long productId);

	/**
	 * function:批量添加商品详情图片
	 * 
	 * @param productImgList
	 * @return
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);

	/**
	 * function:删除指定productId商品下的所有详情图
	 * 
	 * @param productId
	 * @return
	 */
	int deleteProductImgByProductId(long productId);

}
