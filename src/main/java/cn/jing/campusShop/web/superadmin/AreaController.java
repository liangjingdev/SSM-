package cn.jing.campusShop.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.qos.logback.classic.Logger;
import cn.jing.campusShop.entity.Area;
import cn.jing.campusShop.service.AreaService;

@Controller
@RequestMapping("/superadmin")
public class AreaController {

	// 利用logback打印日志信息
	Logger logger = (Logger) LoggerFactory.getLogger(AreaController.class);
	@Autowired
	private AreaService areaService;

	/**
	 * function:查询所有的Area的信息
	 * 
	 * @ResponseBody注解类型可将方法返回值的对象转换为json对象返回给前端
	 * @return
	 */
	@RequestMapping(value = "/listarea", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listArea() {
		logger.info("===start===");
		long starttime = System.currentTimeMillis();
		// 用于存放方法的返回值。成功or失败
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<Area> list = new ArrayList<Area>();
		try {
			list = areaService.getAreaList();
			modelMap.put("rows", list); // 表示返回的集合
			modelMap.put("total", list.size());// 表示返回的集合的总条目数
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		long endTime = System.currentTimeMillis();
		logger.debug("costTime:[{}ms]", endTime - starttime); // 记录方法的执行时间
		logger.info("===end===");
		return modelMap;
	}
}
