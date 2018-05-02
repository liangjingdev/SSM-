/**
 * 
 */
$(function() {
	getlist();
	function getlist(e) {
		$.ajax({
			url : "/campusShop/shopadmin/getshoplist",
			type : "get",
			dataType : "json",
			success : function(data) {
				if (data.success) {
					//渲染店铺列表
					handleList(data.shopList);
					//渲染用户名(显示用户名)
					handleUser(data.user);
				}
			}
		});
	}
	function handleUser(data) {
		$('#user-name').text(data.name);
	}

	function handleList(data) {
		var html = '';
		data.map(function(item, index) {
			html += '<div class="row row-shop"><div class="col-40">'
				+ item.shopName + '</div><div class="col-40">'
				+ shopStatus(item.enableStatus)
				+ '</div><div class="col-20">'
				+ goShop(item.enableStatus, item.shopId) + '</div></div>';

		});
		$('.shop-wrap').html(html);
	}

	//显示店铺状态
	function shopStatus(status) {
		if (status == 0) {
			return '审核中';
		} else if (status == -1) {
			return '店铺非法';
		} else if (status == 1) {
			return '审核通过';
		}
	}

	//进入某个店铺的相关操作的页面(只有审核通过的店铺才能够进入该店铺的管理页面去发布商品，修改店铺信息等)
	function goShop(status, id) {
		if (status == 1) {
			return '<a href="/campusShop/shopadmin/shopmanagement?shopId=' + id
				+ '">进入</a>';
		} else {
			return '';
		}
	}
});