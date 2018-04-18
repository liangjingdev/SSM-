package cn.jing.campusShop.web.local;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.bag.TreeBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jing.campusShop.dto.LocalAuthExecution;
import cn.jing.campusShop.entity.LocalAuth;
import cn.jing.campusShop.entity.PersonInfo;
import cn.jing.campusShop.enums.LocalAuthStateEnum;
import cn.jing.campusShop.exceptions.LocalAuthOperationException;
import cn.jing.campusShop.service.LocalAuthService;
import cn.jing.campusShop.util.CodeUtil;
import cn.jing.campusShop.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "local", method = { RequestMethod.GET, RequestMethod.POST })
public class LocalAuthController {

	@Autowired
	private LocalAuthService localAuthService;

	/**
	 * function:将用户信息与平台账号绑定
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bindlocalauth", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> bindLocalAuth(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 验证码校验
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		// 获取输入的账号
		String userName = HttpServletRequestUtil.getString(request, "userName");
		// 获取输入的密码
		String password = HttpServletRequestUtil.getString(request, "password");
		// 从session中获取当前用户信息(用户一旦通过微信登录之后，便能获取到用户的信息)
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// 非空判断，要求账号密码及当前的用户session为非空
		if (userName != null && password != null && user != null && user.getUserId() != null) {
			// 创建LocalAuth对象并赋值
			LocalAuth localAuth = new LocalAuth();
			localAuth.setPersonInfo(user);
			localAuth.setUsername(userName);
			localAuth.setPassword(password);
			// 绑定账号
			LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuth);
			if (localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", localAuthExecution.getStateInfo());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码均不能为空");
		}
		return modelMap;
	}

	/**
	 * function:修改密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/changelocalpwd", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> changeLocalPwd(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 验证码校验
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		// 获取帐号
		String userName = HttpServletRequestUtil.getString(request, "userName");
		// 获取原密码
		String password = HttpServletRequestUtil.getString(request, "password");
		// 获取新密码
		String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
		// 从session中获取当前用户信息(用户一旦通过微信登录之后，便能获取到用户的信息)
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// 非空判断，要求帐号新旧密码以及当前的用户session非空，且新旧密码不相同
		if (userName != null && password != null && newPassword != null && user != null && user.getUserId() != null
				&& !password.equals(newPassword)) {
			// 善于利用try-catch代码块来包含要进行的多个操作，若任意一个操作发生问题的话，都会抛出异常，并且不会再继续向下执行其它操作咯
			try {
				// 查看原先帐号，看看与输入的帐号是否一致，不一致则认为是非法操作
				LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
				if (localAuth == null || !localAuth.getUsername().equals(userName)) {
					// 不一致则直接退出
					modelMap.put("success", false);
					modelMap.put("errMsg", "输入的帐号非本次登录的帐号");
					return modelMap;
				}
				// 修改平台帐号的用户密码
				LocalAuthExecution le = localAuthService.modifyLocalAuth(user.getUserId(), userName, password,
						newPassword);
				if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", le.getStateInfo());
				}
			} catch (LocalAuthOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入密码");
		}
		return modelMap;
	}

	/**
	 * function:对平台账号登录进行验证
	 * 
	 * @param request 
	 * @return
	 */
	@RequestMapping(value = "/logincheck", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> logincheck(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取是否需要进行验证码检验的标志符(在前端中设置了若输入密码错误超过三次的话就需要通过验证码进行登录)
		boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
		if (needVerify && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		// 获取输入的账号
		String userName = HttpServletRequestUtil.getString(request, "userName");
		// 获取输入的密码
		String password = HttpServletRequestUtil.getString(request, "password");
		// 非空校验
		if (userName != null && password != null) {
			// 传入账号和密码去获取平台账号信息
			LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(userName, password);
			if (localAuth != null) {
				// 若能够获取到账号信息则表示登录成功
				modelMap.put("success", true);
				// 同时在session里设置用户信息
				request.getSession().setAttribute("user", localAuth.getPersonInfo());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "用户名或密码错误");
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码均不能为空");
		}
		return modelMap;
	}

	/**
	 * function:当用户点击登出按钮时注销session（退出登录）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> logout(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 将用户session置为空
		request.getSession().setAttribute("user", null);
		modelMap.put("success", true);
		return modelMap;
	}
}
