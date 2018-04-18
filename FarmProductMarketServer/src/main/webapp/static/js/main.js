//当前页码
var pageNo = 0;
//总页码
var pageSum = 0;

//请求数据
function list(requestPage) {
    $.ajax({
        url:"production/list",
        type:'post',
        data: {
            key: $("#key-input").val(),
            page: (requestPage - 1)
        },
        dataType: 'json',
        success:function (data) {
        	//改变页码信息
            pageNo = data.pageNo;
            pageSum = data.pageSum;
			$("#pageInfo").text(pageNo +"/"+pageSum);
			var productions = data.data;
			//填充表格
			$("#databody").empty();
			$(productions).each(function (index, item){
				var itembody = 
				"<tr>"
					+	"<td>"+ item.name +"</td>"
					+	"<td>"+ item.weight +"</td>"
					+	"<td>"+ item.typeName +"</td>"
					+	"<td>"+ item.supplierName +"</td>"
					+	"<td>"+ item.createTimeString +"</td>"
					+	"<td><button pid="+ item.id +" onclick=\"offline(this)\" class=\"btn btn-danger btn-xs\">下架</button></td>"
				+	"</tr>";
				$("#databody").append(itembody);
			});
        }
    })
}

//初始化
$(document).ready(function () {
	//获取第一页数据
	list(1);
	//绑定回车事件
	$(window).keyup(function (e) {
        if (e.which === 13) {
            list(1);
            $("#key-input").attr("disabled","disabled");
        }
    });
	//前进
    $("#forward").click(function () {
    	if(pageNo < pageSum){
    		list(pageNo + 1);
    	}
    })
    //后退
    $("#back").click(function () {
    	if(pageNo > 1){
    		list(pageNo - 1);
    	}
    })
    //HOME刷新
    $("#home").click(function(){
		history.go(0);
    })
})

//下架操作
function offline(btn) {
    $.ajax({
        url:"production/offline",
        type:'post',
        data: {
            ids:$(btn).attr("pid")
        },
        dataType: 'json',
        success:function (data) {
        	if(data.result == "succeed"){
        		list(1);
        	}else if(data.result == "failed_access_denied"){
        		$("#tipContent").text("您只能下架自己发布的农产品");
        		$("#tip").modal("show");
        	}else if(data.result == "failed_no_login"){
        		window.location = "user/goLogin"; 
        	}else{
        		$("#tipContent").text("未知错误");
        		$("#tip").modal("show");
        	}
        }
    })
}






