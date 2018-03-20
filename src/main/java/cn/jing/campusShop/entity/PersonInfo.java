package cn.jing.campusShop.entity;

import java.util.Date;

/**
 * function:用户信息类
 * @author liangjing
 *
 */
public class PersonInfo {

	//用户id
	private Long userId;
	//用户姓名
	private String name;
	//用户头像地址
	private String profileImg;
	//用户邮箱
	private String email;
	//用户性别
	private String gender;
	//用户状态
	private Integer enableStatus;
	//用户标识(类型)--约定：1代表顾客 2代表店家 3代表超级管理员
	private Integer userType;
	//创建时间
	private Date createTime;
	//最后一次进行操作的时间
	private Date lastEditTime;
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProfileImg() {
		return profileImg;
	}
	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
}
