	
function initCustomizedTable(obj,id){
	ajaxGetData(obj,id);
	 if(obj.refreshRate != null ){
		setInterval( function(){
			ajaxGetData(obj,id);
		},obj.refreshRate);
	}
	
}

function ajaxGetData(obj,id) {
	$.ajax({
		type : "get",
		dataType : "json",
		url : "/integrate/platform/" + obj.jobId + "/" + obj.jobId + "_1",
		success : function(result) {
			if (obj.jobId == 'groupOrderTable') {
				setGroupOrderTable(result, id);
			} else if (obj.jobId == 'dbInspection') {
				setDbInspectionTable(result);
			} else if (obj.jobId == 'notice') {
				setNoticeTable(result, obj, id);
			} else {
				setCustomizedTable(result, obj, id);
			}

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(obj.jobId + "js调用错误信息：" + XMLHttpRequest.readyState
					+ "-textStatus:" + textStatus);
		}
	});
}

	function setCustomizedTable(data,objInit,id){
		$("#"+id).html("");
		var ths = "";
		var trs = "";
		
		var field = [] ;
		 $.each(objInit.tableTitle, function(index, obj) {
			 if(obj.width != null && obj.width != 'undefined'){
				 ths += "<th nowrap=\"nowrap\" width=\""+obj.width+"\">"+obj.title +"</th>";
			 }else if(obj.title != null){
				 ths += "<th nowrap=\"nowrap\">"+obj.title +"</th>"; 
			 }
			 field[index] = obj.field;
		 });
		 $.each(data, function(index, obj) {
			 var flag = false;
			 var tds = "";
			 var id  = "";
			for(var i=0;i<field.length;i++){
				if(obj.send_status != '1'){
						flag = true;
				}
				if(objInit.linkUrl != null){
					if(objInit.linkUrl.param != null){
						id = eval("obj."+objInit.linkUrl.param);
					}
				}
				var cellCon = eval("obj."+field[i]);
				if(field[i] == "text" && cellCon.length>6){
					tds += "<td title=\""+cellCon+"\">"+cellCon.substr(0,5)+"...</td>";
				}else if(field[i] == "alarm_text" && cellCon.length>25){
					tds += "<td title=\""+cellCon+"\">"+cellCon.substr(0,24)+"...</td>";
				}else if(field[i] == 'rownum'){
					tds += "<td align=\"center\">"+cellCon+"</td>";
				}else{
					tds += "<td>"+cellCon+"</td>";
				}
			}
			if(flag){
				trs += "<tr class=\"spical\"" ;
			}else{
				trs += "<tr" ;
			}
			if(objInit.linkUrl != null){
				trs += " class=\"cursor_type\" onclick=\"getDetail('"+id+"','"+objInit.linkUrl.url+"','"+objInit.linkUrl.param+"')\" ";
			}
			trs += ">"+tds + "</tr>";
		 });
		 $("#"+id).append(ths);
		 $("#"+id).append(trs);
	}
	
	function getDetail(id,url,param){
		if(url.indexOf("?")>0){
			url += "&"+param+"="+id;
		}else{
			url += "?"+param+"="+id;
		}
		window.open(url);
		
	}
	
	function initBootTable(url,id,tableTitle,ispagination){
		$('#'+id).bootstrapTable('destroy').bootstrapTable({
			//url: obj.url,
			url: url, 
			method : 'get', //请求方式（*）
			contentType : "application/x-www-form-urlencoded", //很重要， 不然取不到值
			contentType : "application/json",
			dataType : "json",
			traditional : true,
			toolbar : '#toolbar', //工具按钮用哪个容器
			striped : true, //是否显示行间隔色
			cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination : true, //是否显示分页（*）
			sortable : false, //是否启用排序
			sortOrder : "asc", //排序方式
			queryParamsType:'',
			paginationLoop:'false',
			sidePagination : "server", //分页方式：client客户端分页，server服务端分页（*）
			pageNumber : 1, //初始化加载第一页，默认第一页
			pageSize : 10, //每页的记录行数（*）
			pageList: [16], //可供选择的每页的行数（*）
			uniqueId : "id", //每一行的唯一标识，一般为主键列
			cardView : false, //是否显示详细视图
			detailView : false, //是否显示父子表
			columns : tableTitle
		});
		
		$('#'+id).bootstrapTable('hideColumn', 'id');
	}
 
 
	
	function setGroupOrderTable(data,id){
	  var row1 = "<tr><th>工单流水号</th>";
   	  var row2 = "<tr><th>事件标题</th>";
   	  var row3 = "<tr><th>生成时间</th>";
   	  var row4 = "<tr><th>超时时间</th>";
   	  var row5 = "<tr><th>当前环节</th>";
   	  var row6 = "<tr><th>反馈</th>";
	  $.each(data, function(index, obj) {
		 row1 += "<td>"+obj.running_id +"</td>";
    	 row2 += "<td>"+obj.title+"</td>";
    	 row3 += "<td>"+obj.create_time+"</td>";
    	 row4 += "<td>"+obj.plan_end_time +"</td>";
    	 row5 += "<td>"+obj.sheet_current_man+"</td>";
    	 row6 += "<td>"+obj.filename+"</td>";
	 });
	 row1 += "</tr>";
	 row2 +=  "</tr>";
	 row3 +=  "</tr>";
	 row4 +=  "</tr>";
	 row5 +=  "</tr>";
	 row6 +=  "</tr>";
	 
	 $("#"+id).append(row1+row2+row3+row4+row5+row6);
	}

	function setNoticeTable(data,objInit,id){
		var trs = "";
		 $.each(data, function(index, obj) {
			 trs += "<tr>";
			 trs += "<td width=\"8%\"  align=\"center\">"+obj.num+"</td>";
			 trs += "<td  width=\"50%\"><a href=\"#\" onclick=\"getDetail('"+obj.seqId+"','"+objInit.linkUrl.url+"','"+objInit.linkUrl.param+"')\">"+obj.title+"</a></td>";
			 trs += "<td width=\"42%\" class=\"newstime\">【"+obj.create_time+"】</td>";
			 trs += "</tr>";
		 });
		 $("#"+id).append(trs);
	}
	

	//数据库巡检数据加载
	function  setDbInspectionTable(data){
		
		$("#db_time").text(data.monitor_time);
		$("#dbInspection>table:eq(0)").find("tr:eq(1)").find("td:eq(1)").text(data.connect_100);
		$("#dbInspection>table:eq(0)").find("tr:eq(1)").find("td:eq(2)").text(data.link_num_100);
		$("#dbInspection>table:eq(0)").find("tr:eq(1)").find("td:eq(3)").text(data.start_time_100);
		
		$("#dbInspection>table:eq(0)").find("tr:eq(2)").find("td:eq(1)").text(data.connect_101);
		$("#dbInspection>table:eq(0)").find("tr:eq(2)").find("td:eq(2)").text(data.link_num_101);
		$("#dbInspection>table:eq(0)").find("tr:eq(2)").find("td:eq(3)").text(data.start_time_101);
		
		$("#dbInspection>table:eq(1)").find("tr:eq(1)").find("td:eq(0)").text(data.rs_file1_name);
		$("#dbInspection>table:eq(1)").find("tr:eq(2)").find("td:eq(0)").text(data.rs_file2_name);
		
		$("#dbInspection>table:eq(2)").find("tr:eq(1)").find("td:eq(0)").text(data.rs_file1_time);
		$("#dbInspection>table:eq(2)").find("tr:eq(1)").find("td:eq(1)").text(data.rs_file1_status);
		
		$("#dbInspection>table:eq(2)").find("tr:eq(2)").find("td:eq(0)").text(data.rs_file2_time);
		$("#dbInspection>table:eq(2)").find("tr:eq(2)").find("td:eq(1)").text(data.rs_file2_status);
		
		$("#dbInspection>table:eq(3)").find("tr:eq(0)").find("th:eq(1)").text(data.tlq_count);
		$("#dbInspection>table:eq(3)").find("tr:eq(1)").find("td:eq(0)").text(data.callout_num);
		$("#dbInspection>table:eq(3)").find("tr:eq(2)").find("td:eq(0)").text(data.restart_info);
		$("#dbInspection>table:eq(3)").find("tr:eq(3)").find("td:eq(0)").text(data.gevt_log_refresh);

	}
		
	

