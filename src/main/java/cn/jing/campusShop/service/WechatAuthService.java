package cn.jing.campusShop.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.jing.campusShop.dto.WechatAuthExecution;
import cn.jing.campusShop.entity.WechatAuth;
import cn.jing.campusShop.exceptions.WechatAuthoperationException;

public interface WechatAuthService {

	/**
	 * function:通过openId查找平台对应的微信账号
	 * 
	 * @param openId
	 * @return
	 */
	WechatAuth getWechatAuthByOpenId(String openId);

	/**
	 * function:注册本平台的微信账号
	 * 
	 * @param wechatAuth
	 * @param profileImg
	 * @return
	 * @throws RuntimeException
	 */
	WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthoperationException;

}
