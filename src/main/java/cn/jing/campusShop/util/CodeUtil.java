package cn.jing.campusShop.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

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

	/**
	 * function:生成二维码的图片流
	 * 
	 * @param content
	 *            依据传进来的内容(一般是url)生成对应的二维码的图片流
	 * @param response
	 * @return
	 */
	public static BitMatrix generateQRCodeStream(String content, HttpServletResponse response) {
		// 给响应添加头部信息，主要是告诉浏览器返回的是图片流
		response.setHeader("Cache-Control", "no-store"); // 不需要缓存图片(二维码是会过期的)
		response.setHeader("Pragma", "no-cache");// 不需要缓存图片(二维码是会过期的)
		response.setDateHeader("Expires", 0);// 不需要缓存图片(二维码是会过期的)
		response.setContentType("image/png");
		// 设置图片的文字的编码以及内边框距
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.MARGIN, 0);
		BitMatrix bitMatrix;
		try {
			// 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
			bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
		} catch (WriterException e) {
			e.printStackTrace();
			return null;
		}
		return bitMatrix;
	}
}
