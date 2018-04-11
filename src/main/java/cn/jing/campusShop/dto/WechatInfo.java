package cn.jing.campusShop.dto;

/**
 * function:用来接收平台二维码的信息(通过平台二维码传递过来的信息所对应的实体类)
 * 该平台有三种类型的二维码（分别传递着不同的信息）
 * @author liangjing
 *
 */
public class WechatInfo {

	private Long customerId;
	private Long productId;
	private Long userAwardId;
	private Long createTime;
	private Long shopId;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getUserAwardId() {
		return userAwardId;
	}

	public void setUserAwardId(Long userAwardId) {
		this.userAwardId = userAwardId;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

}
