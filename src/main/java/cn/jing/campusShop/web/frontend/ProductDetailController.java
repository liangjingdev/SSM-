package cn.jing.campusShop.web.frontend;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import cn.jing.campusShop.entity.PersonInfo;
import cn.jing.campusShop.entity.Product;
import cn.jing.campusShop.service.ProductService;
import cn.jing.campusShop.util.CodeUtil;
import cn.jing.campusShop.util.HttpServletRequestUtil;
import cn.jing.campusShop.util.ShortNetAddress;
import cn.jing.campusShop.util.wechat.WechatPathUtil;

/**
 * function:商品详情页开发
 * 
 * @author liangjing
 *
 */
@Controller
@RequestMapping("/frontend")
public class ProductDetailController {

	@Autowired
	private ProductService productService;
	/*********** 与微信相关的url ************/
	// 微信获取用户信息的api前缀
	private static String urlPrefix = WechatPathUtil.wechat_prefix;
	// 微信获取用户信息的api中间部分
	private static String urlMiddle = WechatPathUtil.wechat_middle;
	// 微信获取用户信息的api后缀
	private static String urlSuffix = WechatPathUtil.wechat_suffix;
	// 微信回传给的响应添加顾客商品映射信息的url
	private static String productMapUrl = WechatPathUtil.wechat_product_map_url;

	/**
	 * function:根据商品Id获取商品详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listproductdetailpageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductDetailPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取前台传递过来的productId
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		Product product = null;
		// 空值判断
		if (productId != -1) {
			// 根据productId获取商品信息，包含商品详情图列表
			product = productService.getProductById(productId);
			// 2.0新增(若登录了就可以从session中获取到用户的信息)
			PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
			if (user == null) {
				// 如果没有登录，那么就表示不需要显示二维码
				modelMap.put("needQRCode", false);
			} else {
				// 如果登录了，那么就表示需要显示二维码
				modelMap.put("needQRCode", true);
			}
			modelMap.put("product", product);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty productId");
		}
		return modelMap;
	}

	/**
	 * function:生成商品的消费凭证二维码，供操作员扫描，证明已经消费，微信扫一扫就能链接到对应的url里面
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/generateqrcode4product", method = RequestMethod.GET)
	@ResponseBody
	private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) {
		// 获取前端传递过来的商品Id
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		// 从session中获取当前顾客的信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// 空值判断
		if (productId != -1 && user != null && user.getUserId() != null) {
			// 获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
			long timpStamp = System.currentTimeMillis();
			// 将商品Id，顾客Id和timeStamp传入content，赋值到state中，这样微信获取到这些信息后会回传到顾客商品映射信息的添加方法中
			// 加入aaa是为了一会的在添加信息的方法里替换这些信息使用
			String content = "{aaaproductIdaaa:" + productId + ",aaacustomerIdaaa:" + user.getUserId()
					+ ",aaacreateTimeaaa:" + timpStamp + "}";
			try {
				// 将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标URL
				String longUrl = urlPrefix + productMapUrl + urlMiddle + URLEncoder.encode(content, "UTF-8")
						+ urlSuffix;
				// 将目标URL转换成短的URL
				String shortUrl = ShortNetAddress.getShortURL(longUrl);
				// 调用二维码生成的工具类方法，传入短的URL，生成二维码
				BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(shortUrl, response);
				// 将二维码以图片流的形式输出到前端
				MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
