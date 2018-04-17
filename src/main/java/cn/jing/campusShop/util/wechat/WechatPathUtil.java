package cn.jing.campusShop.util.wechat;

public class WechatPathUtil {

	// 与微信开发相关的url
	public static final String wechat_appid = "wx912c375e8db04ac7";
	public static final String wechat_prefix = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
			+ WechatPathUtil.wechat_appid + "&redirect_uri=";
	public static final String wechat_middle = "&response_type=code&scope=snsapi_userinfo&state=";
	public static final String wechat_suffix = "#wechat_redirect";

	// redirect url
	public static final String wechat_auth_url = "http://campusshop.liangjingdev.top/campusShop/shopadmin/addshopauthmap";
	public static final String wechat_login_url = "http://campusshop.liangjingdev.top/campusShop/shopadmin/wechatlogin/logincheck";
	public static final String wechat_product_map_url = "http://campusshop.liangjingdev.top/campusShop/shopadmin/adduserproductmap";
	public static final String wechat_exchange_url = "http://campusshop.liangjingdev.top/campusShop/shopadmin/exchangeaward";
}
