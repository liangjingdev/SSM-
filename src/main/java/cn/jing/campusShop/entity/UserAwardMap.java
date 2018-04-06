package cn.jing.campusShop.entity;

import java.util.Date;

/**
 * function:顾客使用积分领取的奖品映射
 * 
 * @author liangjing
 *
 */
public class UserAwardMap {
	// 主键ID
	private Long userAwardId;
	// 创建时间
	private Date createTime;
	// 使用状态 0.未兑换 1.已兑换（表示是否已经到店里去兑换该奖品）
	private Integer usedStatus;
	// 领取奖品所消耗的积分
	private Integer point;
	// 顾客信息实体类
	private PersonInfo user;
	// 奖品信息实体类
	private Award award;
	// 店铺信息实体类
	private Shop shop;
	// 操作员信息实体类
	private PersonInfo operator;

	public PersonInfo getOperator() {
		return operator;
	}

	public void setOperator(PersonInfo operator) {
		this.operator = operator;
	}

	public Long getUserAwardId() {
		return userAwardId;
	}

	public void setUserAwardId(Long userAwardId) {
		this.userAwardId = userAwardId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getUsedStatus() {
		return usedStatus;
	}

	public void setUsedStatus(Integer usedStatus) {
		this.usedStatus = usedStatus;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public PersonInfo getUser() {
		return user;
	}

	public void setUser(PersonInfo user) {
		this.user = user;
	}

	public Award getAward() {
		return award;
	}

	public void setAward(Award award) {
		this.award = award;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

}
