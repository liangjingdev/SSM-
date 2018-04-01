package cn.jing.campusShop.util;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.apache.ibatis.javassist.expr.NewArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * function:图片处理工具类，封装了图片处理工具thumbnailator的相关操作
 * 
 * @author liangjing
 *
 */
public class ImageUtil {

	// 由于该方法是通过线程去执行的，因此可以通过线程去逆推到其类加载器，然后通过类加载器去得到资源的路径
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	// 定义日期格式
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	// 随机数对象
	private static final Random random = new Random();
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	/*
	 * 范例测试 public static void main(String[] args) throws IOException {
	 * Thumbnails.of(new
	 * File("/Users/liangjing/Documents/xiaohuangren.jpg")).size(200, 200)
	 * .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath +
	 * "/watermark.jpg")), 0.25f) .outputQuality(0.8f).toFile(
	 * "/Users/liangjing/Documents/xiaohuangrennew.jpg"); }
	 */

	/**
	 * function:处理缩略图（缩略图也就是店铺的门面照以及商品的小图）
	 * 
	 * @param thumbnail
	 *            该方法处理的是用户传送过来的文件
	 * @param targetAddr
	 *            即该图片存储的相对目录（由PathUtil的getImgBasePeth()方法得到，然后再与basePath以及图片(文件)名字组成图片存储的绝对路径(全路径)）
	 * @return 返回该图片存储的相对路径，因为需要将其存储在数据表中（根路径可由PathUtil.getImgBasePeth()读取出来，考虑到操作系统切换的情况）
	 */
	public static String generatethumbnail(ImageHolder thumbnail, String targetAddr) {
		String realFileNameString = getRandomFileName();
		String extension = getFileExtension(thumbnail);
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileNameString + extension; // 获取该图片存储的相对路径值（由该图片存储的相对目录值+图片名字以及其后缀组成）
		logger.debug("current relativeAddr is:" + relativeAddr);// debug信息
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr); // 创建File对象，存储该图片
		logger.debug("current completeAddr is:" + PathUtil.getImgBasePath() + relativeAddr);// debug信息
		try {
			Thumbnails.of(thumbnail.getImage()).size(200, 200)
					.watermark(Positions.BOTTOM_RIGHT,
							ImageIO.read(new File("/Users/liangjing/Documents/watermark.jpg")), 0.25f)
					.outputQuality(0.8f).toFile(dest);
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return relativeAddr;
	}

	/**
	 * function:创建目标路径所涉及到的目录，如:/home/work/picture/xxx.jpg，那么home work
	 * picture这三个文件夹都得自动创建
	 * 
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		// 获取图片存储的绝对路径(全路径)以创建相对应的文件夹(不需要加上图片名字，因为此处只需要创建相对应的目录而已)
		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		// 判断该路径是否真实存在，若不存在，则依次创建相对应的目录(文件夹)
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}

	/**
	 * function: 获取传递过来的文件的扩展名(获取输入文件流的扩展名)
	 * 
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(ImageHolder cFile) {
		// 获取原来的文件名
		String originalFileName = cFile.getImageName();
		return originalFileName.substring(originalFileName.indexOf("."));
	}

	/**
	 * function: 因为图片是用户传递过来的且是随意命名的，所以很有可能是重名的，所以不需要用他的名字，而是用系统随机生成的不重名的文字
	 * 生成随机文件名，当前年月日小时分钟秒钟+5位随机数
	 * 
	 * @return
	 */
	private static String getRandomFileName() {
		// 获取随机的五位数(大于10000小于99999)
		int rannum = random.nextInt(89999) + 10000;
		String nowTimeStr = sDateFormat.format(new Date());
		return nowTimeStr + rannum;
	}

	/**
	 * function:将用户传递过来的文件流(CommonsMultipartFile对象)转换成File实例对象(利用其transferTo()方法)，作为generatethumbnail()方法的参数传入
	 * cFile.getOriginalFilename()方法--获取文件名
	 * 
	 * @param cFile
	 * @return
	 */
	public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
		File newFile = new File(cFile.getOriginalFilename());
		try {
			cFile.transferTo(newFile);
		} catch (IllegalStateException | IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return newFile;
	}

	/**
	 * function:首先判断storePath是文件的路径还是目录的路径， 如果storePath是文件路径则删除该文件，
	 * 如果storePath是目录路径则删除该目录下的所以文件。 注意：storePath是相对路径的
	 * 
	 * @param storePath
	 */
	public static void deleteFileOrPath(String storePath) {
		// 首先获取到全路径（拼接起来的全路径可能是目录的路径也可能是文件的路径）
		File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
		// 判断该路径是否存在
		if (fileOrPath.exists()) {
			// 判断是不是一个目录
			if (fileOrPath.isDirectory()) {
				// 列出该目录下的所以文件
				File files[] = fileOrPath.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
			// 若该全路径是目录的话，则在删除完所有文件之外也将该目录删除调；若该全路径是文件的话，则直接进行删除
			fileOrPath.delete();
		}
	}

	/**
	 * function:处理商品详情图，并返回新生成图片的相对值路径
	 * 
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */
	public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
		// 获取不重复的随机名
		String realFileName = getRandomFileName();
		// 获取文件的扩展名如png,jpg等
		String extension = getFileExtension(thumbnail);
		// 如果目标路径不存在，则自动创建
		makeDirPath(targetAddr);
		// 获取文件存储的相对路径(带文件名)
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is :" + relativeAddr);
		// 获取文件要保存到的目标路径
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current complete addr is :" + PathUtil.getImgBasePath() + relativeAddr);
		// 调用Thumbnails生成带有水印的图片
		try {
			Thumbnails.of(thumbnail.getImage()).size(337, 640)
					.watermark(Positions.BOTTOM_RIGHT,
							ImageIO.read(new File("/Users/liangjing/Documents/watermark.jpg")), 0.25f)
					.outputQuality(0.9f).toFile(dest);
		} catch (IOException e) {
			logger.error(e.toString());
			throw new RuntimeException("创建缩图片失败：" + e.toString());
		}
		// 返回图片相对路径地址
		return relativeAddr;
	}
}
