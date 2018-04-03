package cn.jing.campusShop.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import cn.jing.campusShop.entity.LocalAuth;

public interface LocalAuthDao {

	/**
	 * function:通过账号和密码查询对应信息，登录用
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	LocalAuth queryLocalByUserNameAndPwd(@Param("username") String username, @Param("password") String password);

	/**
	 * function:通过用户ID查询对应的localAuth（因为通过微信登录的用户Id与用户信息的Id与平台账号的用户Id是一样的(user_id)，关联在一起）
	 * 
	 * @param userId
	 * @return
	 */
	LocalAuth queryLocalByUserId(@Param("userId") long userId);

	/**
	 * function:添加平台账号
	 * 
	 * @param localAuth
	 * @return
	 */
	int insertLocalAuth(LocalAuth localAuth);

	/**
	 * function:通过userId，username，password更改账号密码
	 * 
	 * @param userId
	 * @param username
	 * @param password
	 * @param newPassword
	 * @param lastEditTime
	 * @return
	 */
	int updateLocalAuth(@Param("userId") long userId, @Param("username") String username,
			@Param("password") String password, @Param("newPassword") String newPassword,
			@Param("lastEditTime") Date lastEditTime);
}
