package cn.jing.campusShop.web.superadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.jing.campusShop.entity.ShopCategory;
import cn.jing.campusShop.service.AreaService;
import cn.jing.campusShop.service.CacheService;
import cn.jing.campusShop.service.HeadLineService;
import cn.jing.campusShop.service.ShopCategoryService;

/**
 * function:为什么要进行清除呢？
 * 比如我们在数据库中修改了以下的某个信息，那么此时在缓存中原来的信息还没有改变过来(旧信息)，所有要进行删除相关的缓存信息，让其重新进行缓存.
 * 
 * @author liangjing
 *
 */
@Controller
public class CacheController {

	@Autowired
	private CacheService cacheService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private HeadLineService headLineService;
	@Autowired
	private ShopCategoryService shopCategoryService;

	/**
	 * function:清除区域信息相关的所有redis缓存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clearcache4area", method = RequestMethod.GET)
	private String clearCache4Area() {
		cacheService.removeFromCache(areaService.AREALISTKEY);
		return "shop/operationsuccess";
	}

	/**
	 * function:清除头条信息相关的所有redis缓存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clearcache4headline", method = RequestMethod.GET)
	private String clearCache4Headline() {
		cacheService.removeFromCache(headLineService.HLLISTKEY);
		return "shop/operationsuccess";
	}

	/**
	 * function:清除店铺类别相关的所有redis缓存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clearcache4shopcategory", method = RequestMethod.GET)
	private String clearCache4ShopCategory() {
		cacheService.removeFromCache(shopCategoryService.SCLISTKEY);
		return "shop/operationsuccess";
	}

}
