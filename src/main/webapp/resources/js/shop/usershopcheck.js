$(function() {
	var username = '';
	getList();
	function getList() {
		//获取该店铺用户积分的URL
		var listUrl = '/campusShop/shopadmin/listusershopmapsbyshop?pageIndex=1&pageSize=999&userName=' + username;
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var userShopMapList = data.userShopMapList;
				var tempHtml = '';
				//拼接成展示列表
				userShopMapList.map(function(item, index) {
					tempHtml += ''
						+ '<div class="row row-usershopcheck">'
						+ '<div class="col-50">' + item.user.name + '</div>'
						+ '<div class="col-50">' + item.point + '</div>'
						+ '</div>';
				});
				$('.usershopcheck-wrap').html(tempHtml);
			}
		});
	}

	//搜索框绑定，获取并按照用户名模糊查询
	$('#search').on('change', function(e) {
		username = e.target.value;
		$('.usershopcheck-wrap').empty();
		getList();
	});
});