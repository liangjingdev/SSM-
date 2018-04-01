package cn.jing.campusShop.util;

import javax.servlet.http.HttpServletRequest;

/**
 * function:判断验证码是否符合预期(即判断用户输入的验证码与图片中显示的验证码是否一致)
 * 
 * @author liangjing
 *
 */
public class CodeUtil {

	public static boolean checkVerifyCode(HttpServletRequest request) {
		// 获取前端页面中图片中的验证码(实际的验证码)
		String verifyCodeExpected = (String) request.getSession()
				.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		// 获取用户输入的验证码
		String verfiyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
		// 如果用户输入的验证码不为空且与图片中显示的验证码是一致的话，则返回true
		if (verfiyCodeActual == null || !verfiyCodeActual.equals(verifyCodeExpected)) {
			return false;
		}
		return true;
	}
}
