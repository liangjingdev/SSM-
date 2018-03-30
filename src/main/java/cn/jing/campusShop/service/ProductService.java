package cn.jing.campusShop.service;

import java.util.List;

import cn.jing.campusShop.dto.ProductExecution;
import cn.jing.campusShop.entity.Product;
import cn.jing.campusShop.exceptions.ProductOperationException;
import cn.jing.campusShop.util.ImageHolder;

public interface ProductService {

	/**
	 * function:添加商品信息以及图片的处理
	 * 
	 * @param product
	 * @param thumbnail
	 *            商品缩略图
	 * @param productImgList
	 *            商品详情图片列表
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
			throws ProductOperationException;

	/**
	 * function:通过商品Id查询唯一的商品信息
	 * 
	 * @param productId
	 * @return
	 */
	Product getProductById(long productId);

	/**
	 * function:修改商品信息以及图片处理
	 * 
	 * @param product
	 * @param thumbnail
	 * @param productImgs
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
			throws ProductOperationException;

	/**
	 * function:查询商品列表并分页，可输入的条件有： 商品名（模糊），商品状态，店铺Id,商品类别
	 * 
	 * @param productCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);
}
