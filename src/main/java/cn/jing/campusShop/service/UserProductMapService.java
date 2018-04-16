package cn.jing.campusShop.service;

import cn.jing.campusShop.dto.UserProductMapExecution;
import cn.jing.campusShop.entity.UserProductMap;
import cn.jing.campusShop.exceptions.UserAwardMapOperationException;
import cn.jing.campusShop.exceptions.UserProductMapOperationException;

public interface UserProductMapService {

	/**
	 * function:通过传入的查询条件分页列出用户消费信息列表
	 * 
	 * @param userProductMap
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	UserProductMapExecution listUserProductMap(UserProductMap userProductMap, Integer pageIndex, Integer pageSize);

	/**
	 * function:function:添加顾客购买商品的映射信息
	 * 
	 * @param userProductMap
	 * @return
	 * @throws UserAwardMapOperationException
	 */
	UserProductMapExecution addUserProductMap(UserProductMap userProductMap) throws UserProductMapOperationException;
}
