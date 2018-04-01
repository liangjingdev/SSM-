package cn.jing.campusShop.util;

import java.io.InputStream;

/**
 * function:上传文件的工具类（保存上传文件的名称以及文件的输入流）
 */
public class ImageHolder {
	private String imageName;
	private InputStream image;

	public ImageHolder(String imageName, InputStream image) {
		this.imageName = imageName;
		this.image = image;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public InputStream getImage() {
		return image;
	}

	public void setImage(InputStream image) {
		this.image = image;
	}
}
