package cn.jing.campusShop.service;

import cn.jing.campusShop.dto.UserAwardMapExecution;
import cn.jing.campusShop.entity.UserAwardMap;

public interface UserAwardMapService {

	/**
	 * function:根据传入的查询条件分页获取映射列表及总数
	 * 
	 * @param userAwardCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex, Integer pageSize);

	/**
	 * function:根据传入的Id获取映射信息
	 * 
	 * @param userAwardMapId
	 * @return
	 */
	UserAwardMap getUserAwardMapById(long userAwardMapId);

	/**
	 * function:领取奖品，添加映射信息
	 * 
	 * @param userAwardMap
	 * @return
	 * @throws RuntimeException
	 */
	UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) throws RuntimeException;

	/**
	 * function：修改映射信息，这里主要修改领取奖品的状态
	 * 
	 * @param userAwardMap
	 * @return
	 * @throws RuntimeException
	 */
	UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) throws RuntimeException;

}
