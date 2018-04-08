package cn.jing.campusShop.dao;

import cn.jing.campusShop.entity.WechatAuth;

public interface WechatAuthDao {
	/**
	 * function:通过openId查询对应本平台的微信账号
	 * 
	 * @param openId
	 * @return
	 */
	WechatAuth queryWechatInfoByOpenId(String openId);

	/**
	 * function：添加对应本平台的微信账号
	 * 
	 * @param wechatAuth
	 * @return
	 */
	int insertWechatAuth(WechatAuth wechatAuth);

	/**
	 * 
	 * @param wechatAuthId
	 * @return
	 */
	int deleteWechatAuth(Long wechatAuthId);
}
