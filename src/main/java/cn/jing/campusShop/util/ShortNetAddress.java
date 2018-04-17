package cn.jing.campusShop.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * function:由于微信对接的url拼接起来是非常长的，可能会使得二维码生成失败，所以需要先
 * 将该长的url转成短的url，短的url与长的url具有同等效应
 * 
 * @author liangjing
 *
 */
public class ShortNetAddress {
	private static Logger logger = LoggerFactory.getLogger(ShortNetAddress.class);

	// 30秒
	public static int TIMEOUT = 30 * 1000;
	public static String ENCODING = "UTF-8";

	/**
	 * function：JSON string 依据传入的key获取value
	 * 
	 * @param replyText
	 * @param key
	 * @return
	 */
	private static String getValueByKey(String replyText, String key) {
		ObjectMapper mapper = new ObjectMapper();
		// 定义json节点
		JsonNode node;
		String tinyUrl = null;
		try {
			// 把调用返回的json字符串转换成json对象
			node = mapper.readTree(replyText);
			// 依据key从json对象里获取对应的值
			tinyUrl = node.get(key).asText();
		} catch (JsonProcessingException e) {
			logger.error("getValueByKey error:" + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("getValueByKey error:" + e.toString());
		}
		return tinyUrl;
	}

	/**
	 * function：通过HttpConnection 获取返回的字符串
	 * 
	 * @param connection
	 * @return
	 * @throws IOException
	 */
	private static String getResponseStr(HttpURLConnection connection) throws IOException {
		StringBuffer result = new StringBuffer();
		// 从连接中获取http状态码
		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			// 如果返回的状态码是OK的，那么取出连接的输入流
			InputStream in = connection.getInputStream();
			// 逐行读取
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, ENCODING));
			String inputLine = "";
			while ((inputLine = reader.readLine()) != null) {
				// 将消息逐行读入结果中
				result.append(inputLine);
			}
		}
		// 将结果转换成json String并返回
		return String.valueOf(result);
	}

	/**
	 * function:根据传入的url，通过访问http://suo.im/短网址网站的JSON格式短网址API接口，将其转化成短的URL
	 * 
	 * @param originURL
	 * @return
	 */
	public static String getShortURL(String originURL) {
		String tinyUrl = null;
		try {
			// 指定短网址的的JSON格式短网址API接口
			URL url = new URL("http://suo.im/api.php?format=json");
			// 建立连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// 设置连接的参数
			// 使用连接进行输出
			connection.setDoOutput(true);
			// 使用连接进行输入
			connection.setDoInput(true);
			// 不使用缓存
			connection.setUseCaches(false);
			// 设置连接超时时间为30秒
			connection.setConnectTimeout(TIMEOUT);
			// 设置请求模式为POST
			connection.setRequestMethod("POST");
			// 设置POST信息，这里为传入的原始url
			String postData = URLEncoder.encode(originURL.toString(), "utf-8");
			// 输出原始的url给百度短视频接口
			connection.getOutputStream().write(("&url=" + postData).getBytes());
			// 连接短网址的JSON格式短网址API接口(正式进行连接)
			connection.connect();
			// 获取短网址的JSON格式短网址API接口返回的json字符串
			String responseStr = getResponseStr(connection);
			logger.info("response string: " + responseStr);
			// 在json字符串里获取tinyurl，即短链接
			tinyUrl = getValueByKey(responseStr, "url");
			logger.info("tinyurl: " + tinyUrl);
			connection.disconnect();
		} catch (IOException e) {
			logger.error("getshortURL error:" + e.toString());
		}
		return tinyUrl;

	}

	/**
	 * 注意：短网址的JSON格式短网址API接口 无法处理不知名网站，会安全识别报错
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		getShortURL("https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login");
	}
}
