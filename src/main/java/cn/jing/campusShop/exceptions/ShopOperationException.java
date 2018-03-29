package cn.jing.campusShop.exceptions;

/**
 * function:封装了与店铺操作相关业务处理的异常，也会方便我们在程序代码检验中一旦看到该类名称就可以知道抛出的异常信息是与店铺业务处理相关的
 * 
 * @author liangjing
 *
 */
public class ShopOperationException extends RuntimeException {

	private static final long serialVersionUID = -4510225369822077196L;

	public ShopOperationException(String msg) {
		// 将传入的错误信息也作为参数传递到父类RuntimeException的构造方法中
		super(msg);
	}
}
