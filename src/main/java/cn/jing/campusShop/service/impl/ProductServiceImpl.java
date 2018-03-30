package cn.jing.campusShop.service.impl;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jing.campusShop.dao.ProductDao;
import cn.jing.campusShop.dao.ProductImgDao;
import cn.jing.campusShop.dto.ProductExecution;
import cn.jing.campusShop.entity.Product;
import cn.jing.campusShop.entity.ProductCategory;
import cn.jing.campusShop.entity.ProductImg;
import cn.jing.campusShop.enums.ProductStateEnum;
import cn.jing.campusShop.exceptions.ProductOperationException;
import cn.jing.campusShop.service.ProductService;
import cn.jing.campusShop.util.ImageHolder;
import cn.jing.campusShop.util.ImageUtil;
import cn.jing.campusShop.util.PageCalculator;
import cn.jing.campusShop.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;
	@Autowired
	private ProductService productService;

	/**
	 * function:function:添加商品信息以及图片的处理。 1、处理缩略图，获取缩略图相对路径并赋值给product
	 * 2、往tb_product写入商品信息，获取productId 3、结合productId批量处理商品详情图
	 * 4、将商品详情图列表批量插入tb_product_img中
	 */
	@Override
	@Transactional
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
			throws ProductOperationException {
		// 空值判断
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			// 给商品设置上默认属性
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			// 默认为上架的状态
			product.setEnableStatus(1);
			// 若商品缩略图不为空则添加
			if (thumbnail != null) {
				addThumbnail(product, thumbnail);
			}
			try {
				// 创建商品信息
				int effectnum = productDao.insertProduct(product);
				if (effectnum <= 0) {
					throw new ProductOperationException("创建商品失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("创建商品失败：" + e.toString());
			}
			// 若商品详情图片不为空则添加
			if (productImgHolderList != null && productImgHolderList.size() > 0) {
				addProductImgList(product, productImgHolderList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		} else {
			// 传参为空则返回空值错误信息
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	/**
	 * function:批量添加商品详情图片
	 * 
	 * @param productImgHolderList
	 */
	private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
		// 获取图片存储路径，这里直接存放到相应店铺的文件夹底下
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		// 遍历详情图片列表依次去处理每张详情图片，并添加进productImg实体类里
		for (ImageHolder productImageHolder : productImgHolderList) {
			// 获取该详情图片存储的相对路径值
			String imgAddr = ImageUtil.generateNormalImg(productImageHolder, dest);
			ProductImg productImg = new ProductImg();
			productImg.setImgAddr(imgAddr);
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImgList.add(productImg);
		}
		// 如果确实是有图片需要添加的话，就执行批量添加操作
		if (productImgList.size() > 0) {
			try {
				int effectNum = productImgDao.batchInsertProductImg(productImgList);
				if (effectNum <= 0) {
					throw new ProductOperationException("创建商品详情图片失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("创建商品详情图片失败：" + e.toString());
			}
		}
	}

	/**
	 * function:处理商品缩略图，并将其存储的相对路径地址添加到对应的Product实例对象中。
	 * 
	 * @param product
	 * @param thumbnail
	 */
	private void addThumbnail(Product product, ImageHolder thumbnail) {
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr = ImageUtil.generatethumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}

	/**
	 * function:通过商品Id查询唯一的商品信息
	 */
	@Override
	public Product getProductById(long productId) {

		return productDao.queryProductById(productId);
	}

	/**
	 * function:修改商品信息以及图片处理
	 * 1、若缩略图参数有值，则处理缩略图（若原先存在缩略图则先删除再添加新的缩略图，之后获取缩略图相对路径值并赋给product）
	 * 2、若商品详情图列表参数有值，则对商品详情图片列表进行同样的操作 3、将tb_product_img下面的该商品原先的商品详情图记录全部清除
	 * 4、更新tb_product的信息
	 */
	@Override
	@Transactional
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgHolderList) throws ProductOperationException {
		// 空值判断
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			// 给商品设置上默认值
			product.setLastEditTime(new Date());
			// 若商品缩略图不为空且原有缩略图不为空则删除原有缩略图再添加新的缩略图
			if (thumbnail != null) {
				// 先获取一遍原有信息，因为原来的信息里有原缩略图地址
				Product tempProduct = productDao.queryProductById(product.getProductId());
				if (tempProduct.getImgAddr() != null) {
					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
				}
				addThumbnail(product, thumbnail);
			}
			// 如果有新存入的商品详情图，则将原先的删除，并添加新的图片
			if (productImgHolderList != null && productImgHolderList.size() > 0) {
				deleteProductImgList(product.getProductId());
				addProductImgList(product, productImgHolderList);
			}
			try {
				// 更新商品信息
				int effectNum = productDao.updateProduct(product);
				if (effectNum < 0) {
					throw new ProductOperationException("更新商品信息失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			} catch (ProductOperationException e) {
				throw new ProductOperationException("更新商品信息失败：" + e.toString());
			}
		} else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	/**
	 * function:删除所有的某个商品下原有的详情图
	 * 
	 * @param productImgList
	 */
	private void deleteProductImgList(long productId) {
		// 首先根据productId获取原来的图片
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		// 然后干掉原来的图片（比如该项目就是在本地硬盘中删除掉原来的图片）
		for (ProductImg productImg : productImgList) {
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		// 最后在数据库中删除原有图片的信息
		productImgDao.deleteProductImgByProductId(productId);
	}

	/**
	 * function:查询商品列表并分页，可输入的条件有： 商品名（模糊），商品状态，店铺Id,商品类别
	 */
	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		// 页码转换成数据库的行码，并调用Dao层取回指定页码的商品列表
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
		// 基于同样的查询条件返回该查询条件下的商品总数
		int count = productDao.queryProductCount(productCondition);
		ProductExecution pe = new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}

}
