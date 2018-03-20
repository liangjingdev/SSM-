package cn.jing.campusShop.entity;

import java.util.Date;

/**
 * function:区域实体类
 * 问题：为什么下面的属性的类型都是用到了引用类型和不用基本类型（如用Integer而不用int）？
 * --------因为如果用的是基本类型的话，那么其将会为空值赋上一个默认的值。
 * 
 * @author liangjing
 *
 */
public class Area {

	// ID
	private Integer areaId;
	// 名称
	private String areaName; 
	// 权重
	private Integer priority;
	// 创建时间
	private Date createTime;
	// 更新时间
	private Date lastEditTime;
	
	
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
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
