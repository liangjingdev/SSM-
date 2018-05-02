/**
 * 
 */
$(function() {
	var shopId = getQueryString('shopId');
	var shopInfoUrl = '/campusShop/shopadmin/getshopmanagementinfo?shopId=' + shopId;
	$.getJSON(shopInfoUrl, function(data) {
		if (data.redirect) {
			//重定向
			window.location.href = data.url;
		} else {
			if (data.shopId != undefined && data.shopId != null) {
				shopId = data.shopId;
			}
			//如果一切都正常的话，那么就会往/campusShop/shopadmin/shopoperation该url后面添加参数shopId并且赋值(shopmanagement.html页面中商铺信息按钮的url),这样就转变成了该店铺的修改更新店铺信息的页面(查看shopoperation.js可知)
			$('#shopInfo')
				.attr('href', '/campusShop/shopadmin/shopoperation?shopId=' + shopId);
		}
	});
});