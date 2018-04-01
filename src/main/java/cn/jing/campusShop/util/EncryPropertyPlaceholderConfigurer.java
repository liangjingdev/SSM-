package cn.jing.campusShop.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * function:对经过加密的属性的值进行解密，转换回原本的值
 * 
 * @author liangjing
 *
 */
public class EncryPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	// 需要进行解密的字段数组
	private String[] encryptPropNames = { "jdbc.username", "jdbc.password" };

	/**
	 * function:对关键的属性的值进行转换
	 */
	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if (isEncryptProp(propertyName)) {
			// 对已加密的字段进行解密工作
			String decryptValue = DESUtil.getDecryptString(propertyValue);
			return decryptValue;
		} else {
			return propertyValue;
		}
	}

	/**
	 * function:判断该属性是否已被加密
	 * 
	 * @param propertyName
	 * @return
	 */
	private boolean isEncryptProp(String propertyName) {
		// 若等于需要进行解密的field，则进行解密
		for (String encryptpropertyName : encryptPropNames) {
			if (encryptpropertyName.equals(propertyName)) {
				return true;
			}
		}
		return false;
	}
}
