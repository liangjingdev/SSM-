package cn.jing.campusShop.dto;

import java.util.List;

import cn.jing.campusShop.entity.Shop;
import cn.jing.campusShop.enums.ShopStateEnum;

/**
 * function:进行店铺相关操作之后返回的实体类类型，为什么不直接用实体类Shop？--因为在进行店铺相关操作之后还需要返回一个状态，
 * 就比如说在进行店铺添加操作之后是成功的还是失败的，此时就需要返回具体的结果状态，如果是失败的话就需要返回给controller层去处理，这些都是需要记录的
 * 该实体类实现概述：1、店铺的信息 2、结果状态
 * 
 * @author liangjing
 *
 */
public class ShopExecution {

	// 结果状态
	private int state;

	// 状态标识(与结果状态值所对应的状态描述信息)
	private String stateInfo;

	// 店铺数量
	private int count;

	// 操作的shop(增删改店铺的时候用到)
	private Shop shop;

	// shop列表(查询店铺的时候使用，因为有时候查询返回的店铺数量不只是一个)
	private List<Shop> shopList;

	public ShopExecution() {
	}

	/**
	 * function:该构造器方法是针对操作失败的时候去生成的ShopExecution实例对象 （店铺操作失败的时候使用的构造器）
	 * 
	 * @param stateEnum
	 *            操作结果状态枚举类，存放结果状态值以及对应的状态描述信息
	 */
	public ShopExecution(ShopStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	/**
	 * function:成功的构造器，此时将会返回对应的操作的Shop实例对象 （店铺操作成功的时候使用的构造器）
	 * 
	 * @param stateEnum
	 * @param shop0
	 */
	public ShopExecution(ShopStateEnum stateEnum, Shop shop) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shop = shop;
	}

	/**
	 * function:成功的构造器，此时将会返回对应的Shop实例对象的一个列表（查询的时候用到） （店铺操作成功的时候使用的构造器）
	 * 
	 * @param stateEnum
	 * @param shop0
	 */
	public ShopExecution(ShopStateEnum stateEnum, List<Shop> shopList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shopList = shopList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public List<Shop> getShopList() {
		return shopList;
	}

	public void setShopList(List<Shop> shopList) {
		this.shopList = shopList;
	}

}
