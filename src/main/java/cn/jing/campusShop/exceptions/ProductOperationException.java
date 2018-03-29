package cn.jing.campusShop.exceptions;

public class ProductOperationException extends RuntimeException {

	public ProductOperationException(String msg) {
		// 将传入的错误信息也作为参数传递到父类RuntimeException的构造方法中
		super(msg);
	}

}
