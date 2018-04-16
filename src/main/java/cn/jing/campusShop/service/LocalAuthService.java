package cn.jing.campusShop.service;

import cn.jing.campusShop.dto.LocalAuthExecution;
import cn.jing.campusShop.entity.LocalAuth;
import cn.jing.campusShop.exceptions.LocalAuthOperationException;

public interface LocalAuthService {

	/**
	 * function:通过账号和密码获取平台账号信息F
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	LocalAuth getLocalAuthByUsernameAndPwd(String username, String password);

	/**
	 * function:通过userId获取平台账号信息
	 * 
	 * @param userId
	 * @return
	 */
	LocalAuth getLocalAuthByUserId(long userId);

	/**
	 * function:绑定微信，生成平台专属的账号
	 * 
	 * @param localAuth
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

	/**
	 * function：修改平台账号的登录密码
	 * 
	 * @param userId
	 * @param username
	 * @param password
	 * @param newPassword
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
			throws LocalAuthOperationException;
}
