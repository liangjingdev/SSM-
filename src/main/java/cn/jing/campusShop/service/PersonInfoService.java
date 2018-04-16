package cn.jing.campusShop.service;

import cn.jing.campusShop.entity.PersonInfo;

public interface PersonInfoService {

	/**
	 * function:根据用户Id获取personInfo信息
	 * 
	 * @param userId
	 * @return
	 */
	PersonInfo getPersonInfoById(Long userId);

}
