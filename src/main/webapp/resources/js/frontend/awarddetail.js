/**
 * 
 */
$(function() {
	// 从地址栏的URL里获取awardId
	var awardId = getQueryString('awardId');
	// 获取商品信息的URL
	var awardUrl = '/campusShop/frontend/listawarddetailpageinfo?awardId='
		+ awardId;
	// 访问后台获取该奖品的信息并渲染
	$.getJSON(awardUrl, function(data) {
		if (data.success) {
			// 获取商品信息
			var award = data.award;
			// 给商品信息相关HTML控件赋值
			// 商品缩略图
			$('#award-img').attr('src', award.awardImg);
			// 商品更新时间
			$('#award-time').text(
				new Date(award.lastEditTime).Format("yyyy-MM-dd"));
			if (award.point != undefined) {
				$('#award-point').text('兑换该奖品需要' + award.point + '积分');
				// }
				// 商品名称
				$('#award-name').text(award.awardName);
				// 商品简介
				$('#award-desc').text('奖品描述：' + award.awardDesc);
			}
		}
	});

	// 点击后打开右侧栏
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
	$.init();
});