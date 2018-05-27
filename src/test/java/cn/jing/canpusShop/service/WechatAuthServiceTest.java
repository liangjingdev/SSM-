package cn.jing.canpusShop.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.jing.campusShop.dto.WechatAuthExecution;
import cn.jing.campusShop.entity.PersonInfo;
import cn.jing.campusShop.entity.WechatAuth;
import cn.jing.campusShop.enums.WechatAuthStateEnum;
import cn.jing.campusShop.service.WechatAuthService;
import cn.jing.canpusShop.BaseTest;

public class WechatAuthServiceTest extends BaseTest {

	@Autowired
	private WechatAuthService wechatAuthService;

	@Test
	public void testRegister() {
		// 新增一条微信账号
		WechatAuth wechatAuth = new WechatAuth();
		PersonInfo personInfo = new PersonInfo();
		String openId = "hhhhhh";
		// 给微信账号设置上用户信息，但不设置上用户Id
		// 希望创建微信账号的同时自动创建用户信息
		personInfo.setCreateTime(new Date());
		personInfo.setName("测试以下");
		personInfo.setUserType(1);
		wechatAuth.setPersonInfo(personInfo);
		wechatAuth.setOpenId(openId);
		WechatAuthExecution wechatAuthExecution = wechatAuthService.register(wechatAuth);
		assertEquals(WechatAuthStateEnum.SUCCESS.getState(), wechatAuthExecution.getState());
		// 通过openId找到新增的wechatAuth
		wechatAuth = wechatAuthService.getWechatAuthByOpenId("hhhhhh");
		System.out.println(wechatAuth.getPersonInfo().getName());
	}
}
