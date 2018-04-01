package cn.jing.campusShop.util;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.management.RuntimeErrorException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * function:DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法
 * 
 * @author liangjing
 *
 */
public class DESUtil {

	private static Key key;
	// 设置密钥key
	private static String KEY_STR = "myKey";
	// 密钥的编码
	private static String CHARSETNAME = "UTF-8";
	// 使用的是Java自带的DES算法
	private static String ALGORITHM = "DES";

	// 需要编写一个静态代码块来生成DES算法的实例
	static {
		try {
			// 生成DES算法对象
			KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
			// 运用SHA1安全策略
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			// 给安全策略设置上密钥种子
			secureRandom.setSeed(KEY_STR.getBytes());
			// 初始化基于SHA1的DES算法对象
			generator.init(secureRandom);
			// 生成密钥对象
			key = generator.generateKey();
			generator = null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * function:获得加密后的信息
	 * 
	 * @param str
	 * @return
	 */
	public static String getEncryptString(String str) {
		// 基于BASE64编码，接收byte[]并转换成String
		BASE64Encoder base64encoder = new BASE64Encoder();
		try {
			// 按UTF8编码
			byte[] bytes = str.getBytes(CHARSETNAME);
			// 获取加密对象
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			// 初始化密码信息
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// 加密
			byte[] doFinal = cipher.doFinal(bytes);
			// byte[]to encode好的String并返回
			return base64encoder.encode(doFinal);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}

	/**
	 * function：获取解密之后的信息
	 * 
	 * @param str
	 * @return
	 */
	public static String getDecryptString(String str) {
		// 基于BASE64编码，接收byte[]并转换成String
		BASE64Decoder base64decoder = new BASE64Decoder();
		try {
			// 将字符串decode成byte[]
			byte[] bytes = base64decoder.decodeBuffer(str);
			// 获取解密对象
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			// 初始化解密信息
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 解密
			byte[] doFinal = cipher.doFinal(bytes);
			// 返回解密之后的信息
			return new String(doFinal, CHARSETNAME);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}

	/**
	 * function:运行main函数，将MySQL数据库的账号和密码进行DES加密，获取到加密后的字符串之后，
	 * 将其粘贴到jdbc.properties中，然后再利用PropertyPlaceholderConfigurer将加密后的字符串进行
	 * 解密，使得spring-dao.xml配置文件中能够正确的获取到MySQL数据库的账号和密码信息
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getEncryptString("root"));
		System.out.println(getEncryptString("liangjing"));
	}
}
