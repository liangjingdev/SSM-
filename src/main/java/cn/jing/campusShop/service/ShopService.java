package cn.jing.campusShop.service;

import java.io.File;

import cn.jing.campusShop.dto.ShopExecution;
import cn.jing.campusShop.entity.Shop;
import cn.jing.campusShop.exceptions.ShopOperationException;
import cn.jing.campusShop.util.ImageHolder;

/**
 * function:店铺相关业务逻辑处理
 * 
 * @author liangjing
 *
 */
public interface ShopService {

	/**
	 * function:注册店铺，包括图片处理
	 * (因为实现该方法过程中会有事务的处理，所以遇到错误时需要抛出RuntimeException异常以达到事务回滚的目的)
	 * 
	 * @param shop
	 * @param shopImg
	 * @return
	 */
	ShopExecution addShop(Shop shop, ImageHolder shopImg) throws ShopOperationException;

	/**
	 * function:通过店铺Id获取店铺信息
	 * 
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(long shopId);

	/**
	 * function:更新店铺信息，包括对图片的处理
	 * 
	 * @param shop
	 * @param shopImg
	 * @return
	 */
	ShopExecution modifyShop(Shop shop, ImageHolder shopImg) throws ShopOperationException;

	/**
	 * function:获取指定条件的店铺列表（为什么使用ShopExecution，因为ShopExecution中既有count属性又有列表属性，整合到一起了）
	 * 注意此处的getShopList()方法的第二个参数是pageIndex而不是rowIndex！
	 * 
	 * @param shopCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

}
