package cn.jing.campusShop.entity;

import java.util.Date;

/**
 * function:商品类别
 * 
 * @author liangjing
 *
 */
public class ProductCategory {

	private Long productCategoryId;
	 // 表明该商品类别是哪个店铺下的类别（为什么这里使用shopId而不用Shop对象，因为我们只需要获取shopId这个属性值即可，并不需要Shop对象中的其它属性值，可根据shopId去选出对应的ProductCategory）
	private Long shopId;
	private String productCategoryName;
	private Integer priority;
	private Date createTime;

	public Long getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Long productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
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

}
