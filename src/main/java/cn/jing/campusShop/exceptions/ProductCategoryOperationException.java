package cn.jing.campusShop.exceptions;

public class ProductCategoryOperationException extends RuntimeException {

	private static final long serialVersionUID = -7367164295533274485L;

	public ProductCategoryOperationException(String msg) {
		// 将传入的错误信息也作为参数传递到父类RuntimeException的构造方法中
		super(msg);
	}
}
