package cn.jing.campusShop.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.jing.campusShop.dto.AwardExecution;
import cn.jing.campusShop.entity.Award;
import cn.jing.campusShop.util.ImageHolder;

public interface AwardService {

	/**
	 * function：根据传入的查询条件分页返回奖品列表，并返回该查询条件下的总数
	 * 
	 * @param awardCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	AwardExecution getAwardList(Award awardCondition, int pageIndex, int pageSize);

	/**
	 * function：根据awardId查询奖品信息
	 * 
	 * @param awardId
	 * @return
	 */
	Award getAwardById(long awardId);

	/**
	 * function：添加奖品信息，并添加奖品图片
	 * 
	 * @param award
	 * @param thumbnail
	 * @return
	 */
	AwardExecution addAward(Award award, ImageHolder thumbnail);

	/**
	 * function：根据传入的奖品实例修改对应的奖品信息，若传入图片则替换掉原先的图片
	 * 
	 * @param award
	 * @param thumbnail
	 * @param awardImgs
	 * @return
	 */
	AwardExecution modifyAward(Award award, ImageHolder thumbnail);

}
