/**
 * 
 */
$(function() {
	$('#log-out').click(function() {
		// 清除session
		$.ajax({
			url : "/campusShop/local/logout",
			type : "post",
			async : false,
			cache : false,
			dataType : 'json',
			success : function(data) {
				if (data.success) {
					var usertype = $("#log-out").attr("usertype");
					// 清除成功后退出到登录界面（并且此处将usertype传递到登录页面，若用户登录成功之后则会根据usertype来跳转到不同的页面）
					// 也就是指示是在哪个页面登出的(前端展示系统页面or店家管理系统页面),在哪个页面登出的，那么登录之后就会显示出该页面
					// 注意：卖家也可以是顾客
					window.location.href = "/campusShop/local/login?usertype=" + usertype;
					return false;
				}
			},
			error : function(data, error) {
				alert(error);
			}
		});
	});
});