package cn.jing.campusShop.web.wechat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.jing.campusShop.dto.WechatAuthExecution;
import cn.jing.campusShop.entity.PersonInfo;
import cn.jing.campusShop.entity.UserAccessToken;
import cn.jing.campusShop.entity.WechatAuth;
import cn.jing.campusShop.entity.WechatUser;
import cn.jing.campusShop.enums.WechatAuthStateEnum;
import cn.jing.campusShop.service.PersonInfoService;
import cn.jing.campusShop.service.WechatAuthService;
import cn.jing.campusShop.util.wechat.WechatUtil;

/**
 * function:获取关注公众号之后的微信用户信息的接口，如果在微信浏览器里访问
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=您的appId&redirect_uri=项目域名/campusShop/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 * 则这里将会获取到code,之后再可以通过code获取到access_token 进而获取到用户信息
 * 
 * @author liangjing
 *
 */
@Controller
@RequestMapping("wechatlogin")
public class WechatLoginController {

	private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);
	private static final String FRONTEND = "1"; // 顾客
	private static final String SHOPEND = "2"; // 店家
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private WechatAuthService wechatAuthService;

	@RequestMapping(value = "/logincheck", method = { RequestMethod.GET })
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("weixin login get...");
		// 获取微信公众号传输过来的code,通过code可获取access_token,进而获取用户信息
		String code = request.getParameter("code");
		// 这个state可以用来传我们自定义的信息，方便程序调用，这里也可以不用
		String roleType = request.getParameter("state"); // 顾客or店家
		log.debug("weixin login code:" + code);
		WechatUser user = null;
		String openId = null;
		WechatAuth wechatAuth = null;
		if (null != code) {
			UserAccessToken token;
			try {
				// 通过code获取access_token
				token = WechatUtil.getUserAccessToken(code);
				log.debug("weixin login token:" + token.toString());
				// 通过token获取accessToken
				String accessToken = token.getAccessToken();
				// 通过token获取openId
				openId = token.getOpenId();
				// 通过access_token和openId获取用户昵称等信息
				user = WechatUtil.getUserInfo(accessToken, openId);
				log.debug("weixin login user:" + user.toString());
				request.getSession().setAttribute("openId", openId);
				wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
			} catch (IOException e) {
				log.error("error in getUserAccessToken or getUserInfo or findByOpenId: " + e.toString());
				e.printStackTrace();
			}
		}
		// ======todo begin======
		// 前面咱们获取到openId后，那么可以通过它去数据库判断该微信帐号是否在我们网站里有对应的帐号了，
		// 没有的话这里可以自动创建上，直接实现微信与网站的无缝对接。
		// 首先判断对应wechatAuth是否为空，若为空就要去创建(表示该微信用户第一次登录该平台)
		if (wechatAuth == null) {
			PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(user);
			wechatAuth = new WechatAuth();
			wechatAuth.setOpenId(openId);
			if (FRONTEND.equals(roleType)) {
				// 若为顾客，则设置userType为1
				personInfo.setUserType(1);
			} else {
				// 若为店家，则设置userType为2
				personInfo.setUserType(2);
			}
			wechatAuth.setPersonInfo(personInfo);
			WechatAuthExecution wechatAuthExecution = wechatAuthService.register(wechatAuth);
			if (wechatAuthExecution.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
				return null;
			} else {
				personInfo = personInfoService.getPersonInfoById(wechatAuth.getPersonInfo().getUserId());
				// 将该对应的PersonInfo实体类对象设置到Session中，供后续操作调用
				request.getSession().setAttribute("user", personInfo);
			}
		}
		// 若用户点击的是前端展示系统按钮则进入前端展示系统（微信公众号中的按钮）
		if (FRONTEND.equals(roleType)) {
			return "frontend/index";
		} else {
			return "shopadmin/shoplist";
		}
		// ======todo end======
	}
}
