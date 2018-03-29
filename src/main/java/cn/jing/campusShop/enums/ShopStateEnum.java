package cn.jing.campusShop.enums;

/**
 * function:关于店铺的相关操作所返回的结果状态Ï
 * 
 * @author liangjing
 *
 */
public enum ShopStateEnum {

	CHECK(0, "审核中"), OFFLINE(-1, "非法店铺"), SUCCESS(1, "操作成功"), PASS(2, "通过认证"), INNER_ERROR(-1001,
			"内部系统错误"), NULL_SHOPID(-1002, "ShopId为空"),NULL_SHOP(-1003,"shop信息为空");

	private int state;
	private String stateInfo;

	/**
	 * function:将该构造器方法设置为私有化的原因：不希望外部程序去改变这些Enum值，
	 * 只能通过内部的私有化的构造器方法去创建，也就是说将这些Enum值作为常量来处理，需要的时候在此处进行添加即可。
	 * 
	 * @param state
	 * @param stateInfo
	 */
	private ShopStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	/**
	 * function:依据传入的state值返回相对应的Enum实例对象 values()包含所有的枚举对象
	 * 
	 * @param state
	 * @return
	 */
	public static ShopStateEnum stateOf(int state) {
		for (ShopStateEnum stateEnum : values()) {
			if (stateEnum.getState() == state) {
				return stateEnum;
			}
		}
		return null;
	}

	// 将setter方法去掉的原因是不希望被程序外部所调用来改变ShopStateEnum实例对象的值
	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

}
