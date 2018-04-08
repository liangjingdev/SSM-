package cn.jing.campusShop.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageReader;
import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.jing.campusShop.dao.ShopAuthMapDao;
import cn.jing.campusShop.dao.ShopDao;
import cn.jing.campusShop.dto.ShopExecution;
import cn.jing.campusShop.entity.Shop;
import cn.jing.campusShop.entity.ShopAuthMap;
import cn.jing.campusShop.enums.ShopStateEnum;
import cn.jing.campusShop.exceptions.ShopOperationException;
import cn.jing.campusShop.service.ShopService;
import cn.jing.campusShop.util.ImageHolder;
import cn.jing.campusShop.util.ImageUtil;
import cn.jing.campusShop.util.PageCalculator;
import cn.jing.campusShop.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {

	@Autowired
	private ShopDao shopDao;
	@Autowired
	private ShopAuthMapDao shopAuthMapDao;

	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder shopImg) throws ShopOperationException {
		// 首先检查传入的参数是否合法（空值判断）
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			// 给店铺信息赋初始值（保证店铺创建成功后的状态值是"审核中"，以及保证创建时间的正确性）
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			// 添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			// 判断是否插入成功
			if (effectedNum <= 0) {
				// 此处为什么用RuntimeException来抛出异常而不用Exception来抛出异常？因为当且仅当我们的程序抛出RuntimeExcetion异常时(或者其子类)事务才会得以终止并且回滚
				throw new ShopOperationException("店铺创建失败");
			} else {
				if (shopImg != null) {
					try {
						addShopImg(shop, shopImg);
					} catch (Exception e) {
						throw new ShopOperationException("addShopImg error:" + e.getMessage());
					}
					// 更新店铺的图片地址
					effectedNum = shopDao.updateShop(shop);
					if (effectedNum <= 0) {
						throw new ShopOperationException("更新店铺图片地址失败");
					}
					//执行增加shopAuthMap操作
					ShopAuthMap shopAuthMap = new ShopAuthMap();
					shopAuthMap.setEmployee(shop.getOwner());
					shopAuthMap.setCreateTime(new Date());
					shopAuthMap.setLastEditTime(new Date());
					shopAuthMap.setTitle("店家");
					shopAuthMap.setTitleFlag(0);
					shopAuthMap.setEnableStatus(1);
					try {
						effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
						if (effectedNum <= 0) {
							throw new ShopOperationException("授权创建失败");
						}
					} catch (Exception e) {
						throw new ShopOperationException("insertShopAuthMap error:" + e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			throw new ShopOperationException("addshop error:" + e.getMessage());
		}
		// 返回所有步骤都执行成功后的ShopExecution实例对象
		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}

	/**
	 * function:存储图片,存储成功的话将会返回该图片存储的相对路径(地址),然后再将其更新到shop实体类中。
	 * 此处需要注意的是：对于java的所有方法来说，它的参数都是以值传递的形式传递到方法里面去的，如果参数是基本类型的话，
	 * 那么它传递的就是基本类型的自变量值的拷贝，也就是说你在方法里面对这个参数做改变了，只要这个参数是基本类型的，那么也就不会影响到这个参数
	 * 的实际的值；如果参数是引用类型的话，它将传递的是这个参数所引用的对象(在堆中地址值的拷贝)，也就是说传递了一个同样的内存值进方法里面，
	 * 既然内存值相同，那么如果我们对这个对象做了一些改变的话，它是能够影响到外面的(影响到该参数所引用的对象)，就跟我们的这个shop一样，
	 * 如果在方法里面给其属性shopImg赋了该图片存储的地址值，那么咱们在外面就是能够取到的。
	 * 
	 * @param shop
	 * @param shopImg
	 */
	private void addShopImg(Shop shop, ImageHolder shopImg) {
		// 首先获取存放shop图片目录的相对路径值
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		// 得到该图片存储的相对路径值
		String shopImgAddr = ImageUtil.generatethumbnail(shopImg, dest);
		// 此时将会自动的调用ShopDao的updateShop()方法对该shop对象进行更新
		shop.setShopImg(shopImgAddr);
	}

	/**
	 * function:通过店铺Id获取店铺信息
	 */
	@Override
	public Shop getByShopId(long shopId) {

		return shopDao.queryByShopId(shopId);
	}

	/**
	 * function:更新店铺信息，包括对图片的处理 过程：1、判断是否需要处理图片 2、更新店铺信息
	 */
	@Override
	public ShopExecution modifyShop(Shop shop, ImageHolder shopImg) throws ShopOperationException {
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		} else {
			try {
				// 1、如果传入的shopImg不为空，则表示需要处理图片
				if (shopImg != null && shopImg.getImageName() != null && !"".equals(shopImg.getImageName())) {
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					// 若原有店铺门面照不为空，则先将其删除，然后再去添加新的图片
					if (tempShop.getShopImg() != null) {
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					}
					// 为什么此处用shop而不用tempShop呢？因为addShopImg()方法会在里面对这个Shop对象进行操作，并且给它赋上一个新的图片地址，然后后面会用该传入的shop对shop信息进行更新
					addShopImg(shop, shopImg);
				}
				// 2、更新店铺信息
				shop.setLastEditTime(new Date());
				int effectNum = shopDao.updateShop(shop);
				if (effectNum <= 0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				} else {
					// 如果更新操作成功，则获取更新后的该Shop对象并将其传回给前台
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS, shop);
				}
			} catch (Exception e) {
				throw new ShopOperationException("modifyShop error:" + e.getMessage());
			}
		}
	}

	/**
	 * function:根据shopCondition分页返回相应的店铺列表(Dao层中是rowIndex，而此处是pageIndex，原因是Dao层只认行数，而前端只认页数，所以此处还要进行转换)
	 */
	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		// 首先进行转换
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 获取在shopCondition指定条件下，从rowIndex行开始的pageSize条数据(店铺信息)--即每一页最多能够显示pageSize条数据
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		// 获取在shopCondition指定条件下，查询到的店铺的总数
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution shopExecution = new ShopExecution();
		if (shopList != null) {
			shopExecution.setShopList(shopList);
			shopExecution.setCount(count);
		} else {
			shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return shopExecution;
	}

}
