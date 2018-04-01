package cn.jing.campusShop.util;

/**
 * function:路径工具类
 *
 * @author liangjing
 *
 */
public class PathUtil {

	// 获取文件的分隔符，不同操作系统的分隔符不同（以保证这些路径在相对应的操作系统下是有效的）
	private static String separator = System.getProperty("file.separator");

	/**
	 * function:根据执行环境的不同，提供不同的根路径，根路径就是该项目中所有图片存放的基本路径
	 * 
	 * @return
	 */
	public static String getImgBasePath() {
		// 操作系统(通过getProperty()方法可以获取到系统的一些属性)
		String os = System.getProperty("os.name");
		String basePath = "";
		// 若该操作是windows，则执行以下操作
		if (os.toLowerCase().startsWith("win")) {
			basePath = "D:/projectdev/image/";
		} else {
			basePath = "/Users/liangjing/Documents/picture";
		}
		basePath = basePath.replace("/", separator);
		return basePath;
	}

	/**
	 * function:获取存放某个店铺的图片的相对应的子目录（子路径）（每个店铺的图片都是放在各自的文件夹里面）
	 * 
	 * @return
	 */
	public static String getShopImagePath(long shopId) {
		String imagePath = "/upload/item/shop/" + shopId + "/";
		imagePath = imagePath.replace("/", separator);
		return imagePath;
	}
}
