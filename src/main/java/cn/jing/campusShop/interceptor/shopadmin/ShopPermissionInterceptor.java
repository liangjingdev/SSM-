package cn.jing.campusShop.interceptor.shopadmin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.jing.campusShop.entity.Shop;

/**
 * function:店家管理系统操作验证拦截器
 * 
 * @author liangjing
 *
 */
public class ShopPermissionInterceptor extends HandlerInterceptorAdapter {

	/**
	 * function:主要做事前拦截，即用户操作发生前，改写preHandle里的逻辑，进行用户操作权限的拦截
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 从session中获取当前选择的店铺
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		// 从session中获取当前用户可操作的店铺列表(在getshoplist方法中赋值上的,getshoplist方法是不需要拦截的)
		List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
		// 非空判断
		if (currentShop != null && shopList != null) {
			// 遍历可操作的店铺
			for (Shop shop : shopList) {
				// 如果当前店铺在可操作的店铺列表则返回true，进行接下来的用户操作
				return true;
			}
		}
		// 若不满足拦截器的验证则返回false,终止用户操作的执行
		return false;
	}
}
