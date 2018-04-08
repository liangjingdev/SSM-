package cn.jing.campusShop.entity;

import java.util.Date;

/**
 * function:微信账号实体类
 * @author liangjing
 *
 */
public class WechatAuth {

	//自身Id
	private Long wechatAuthId;
	private String openId;
	private Date createTime;
	//由于此处与用户表实体类相关联，所以这里直接以PersonInfo为类型(在数据库表中是用户的ID,因为数据库不支持对象)
	private PersonInfo personInfo;
	
	public Long getWeChatAuthId() {
		return wechatAuthId;
	}
	public void setWeChatAuthId(Long weChatAuthId) {
		this.wechatAuthId = weChatAuthId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public PersonInfo getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
}
