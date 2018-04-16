package cn.jing.campusShop.service;

import cn.jing.campusShop.dto.ShopAuthMapExecution;
import cn.jing.campusShop.entity.ShopAuthMap;
import cn.jing.campusShop.exceptions.ShopAuthMapOperationException;

public interface ShopAuthMapService {
	/**
	 * function:根绝店铺Id分页显示该店铺的授权信息
	 * 
	 * @param shopId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ShopAuthMapExecution listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize);

	/**
	 * function：添加授权信息 (增、删、改操作肯定都需要抛出RuntimeException，事务的操作)
	 * 
	 * @param shopAuthMap
	 * @return
	 * @throws RuntimeException
	 */
	ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException;

	/**
	 * function:更新授权信息，包括职位等 (增、删、改操作肯定都需要抛出RuntimeException，事务的操作)
	 * 
	 * @param shopAuthId
	 * @param title
	 * @param titleFlag
	 * @param enableStatus
	 * @return
	 * @throws RuntimeException
	 */
	ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException;

	/**
	 * function:删除授权信息(增、删、改操作肯定都需要抛出RuntimeException，事务的操作)
	 * 
	 * @param shopAuthMapId
	 * @return
	 * @throws RuntimeException
	 */
	ShopAuthMapExecution removeShopAuthMap(Long shopAuthMapId) throws ShopAuthMapOperationException;

	/**
	 * function：根据shopAuthId返回对应的授权信息
	 * 
	 * @param shopAuthId
	 * @return
	 */
	ShopAuthMap getShopAuthMapById(Long shopAuthId);

}
