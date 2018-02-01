var flowPhase;
var FlowPhaseManage = function() {
	var phaseMapStatus = '[{"id":"dataInput","scope":{"min":"1","max":"1"}},{"id":"bizCheck","scope":{"min":"2","max":"4"}},'+
	                '{"id":"cmdCheck","scope":{"min":"5","max":"7"}},{"id":"cmdDispatch","scope":{"min":"8","max":"12"}},'+
	                '{"id":"resultTest","scope":{"min":"13","max":"15"}},{"id":"file","scope":{"min":"16","max":"16"}},'+
	                '{"id":"cancel","scope":{"min":"17","max":"17"}}]';
	var statusMap = '[{"id":"1","text":"数据录入"},{"id":"2","text":"待核查"},{"id":"3","text":"核查中"},{"id":"4","text":"核查结束"},'+
					'{"id":"5","text":"待审核"},{"id":"6","text":"审核中"},{"id":"7","text":"审核结束"},{"id":"8","text":"待下发"},'+
					'{"id":"9","text":"下发用户停止"},{"id":"10","text":"下发断点停止"},{"id":"11","text":"下发中"},{"id":"12","text":"下发结束"},'+
					'{"id":"13","text":"待测试"},{"id":"14","text":"测试中"},{"id":"15","text":"测试结束"},{"id":"16","text":"归档"},'+
					'{"id":"17","text":"撤销"}]';
	var arrPhaseMapStatus, arrStatusMap;
	var orderId, iStatus, currPhaseIndex, busiId, userId, createUserId;
	
	this.init = function() {
    	orderId = document.getElementById('orderId').value;
    	var status = document.getElementById('status').value;
    	busiId = document.getElementById('busiId').value;
    	userId = document.getElementById('userId').value;
    	createUserId = document.getElementById('createUserId').value;
    	if (orderId == undefined || status == undefined || busiId == undefined || userId == undefined || createUserId == undefined) return;
    	iStatus = parseInt(status);
		var min, max;
		currPhaseIndex = -1;
    	arrPhaseMapStatus = JSON.parse(phaseMapStatus);
    	for (var i=0; i<arrPhaseMapStatus.length; i++) {
    		min = parseInt(arrPhaseMapStatus[i].scope.min);
    		max = parseInt(arrPhaseMapStatus[i].scope.max);
    		if (iStatus >= min && iStatus <= max) {
    			currPhaseIndex = i;
    			break;
    		}
    	}
    	if (currPhaseIndex == -1) return;
    	
    	this.flow();
    	this.changeButtonState();
    	arrStatusMap = JSON.parse(statusMap);
    	this.changeOrderStateText();
	}
	
	this.getStateText = function(statusid) {
		var status = iStatus;
		if (statusid != undefined && statusid != null) {
			status = statusid;
		}
		for (var i=0; i<arrStatusMap.length; i++) {
			if (status == arrStatusMap[i].id) {
			   	return arrStatusMap[i].text;
			}	
		}
	}
	
	this.changeOrderStateText = function() {
		var text = this.getStateText(iStatus);
		window.parent.document.getElementById("orderStatus").innerText = text;
	}
	
	this.changeButtonState = function() {
		var next = document.getElementById('next');
		var cancel = document.getElementById('cancel');
		var file = document.getElementById('file');
		var backorder = document.getElementById('back');
		//add 当测试结束时，下一步置灰 || iStatus == 15
		if (currPhaseIndex > 4 || createUserId != userId || iStatus == 15) {
    		next.classList.remove("rightbt_normal");
    		next.classList.add("rightbt_gray");
		} else {
    		next.classList.remove("rightbt_gray");
    		next.classList.add("rightbt_normal");
		}
    	if (iStatus == 3 || iStatus == 6 || iStatus == 11 || iStatus == 14 || iStatus == 16 || iStatus == 17 || createUserId != userId) {
    		cancel.classList.remove("rightbt_normal");
    		cancel.classList.add("rightbt_gray");
    	} else {
    		cancel.classList.remove("rightbt_gray");
    		cancel.classList.add("rightbt_normal");
    	}
    	if (iStatus == 15 && createUserId == userId) {
    		file.classList.remove("rightbt_gray");
    		file.classList.add("rightbt_normal");
    	} else {
    		file.classList.remove("rightbt_normal");
    		file.classList.add("rightbt_gray");
    	}
    	if ((iStatus == 4 || iStatus == 5 || iStatus == 6 || iStatus == 7 ||
    		iStatus == 9 || iStatus == 10 || iStatus == 12 || iStatus == 15) && createUserId == userId) {
    		back.classList.remove("rightbt_gray");
    		back.classList.add("rightbt_normal");
    	} else {
    		back.classList.remove("rightbt_normal");
    		back.classList.add("rightbt_gray");
    	}
	}
	
	this.flow = function() {
		this.flowToPhase(currPhaseIndex);
		for (var i=0; i<ul.children.length; i++) {
			var li = ul.children[i];
			if (currPhaseIndex == i) {
				li.classList.add("current_step");
			} else {
				li.classList.remove("current_step");
				if (currPhaseIndex < i) {
					li.classList.add("gray_step");
				}
			}
		}
	}
	
	/*工单流转到环节*/
    this.flowToPhase = function(phaseIndex) {
    	var root = window.parent.document.location.origin;
    	var dir = '/' + arrPhaseMapStatus[phaseIndex].id + '/';
    	window.parent.document.getElementById('external-frame').src = root + dir + orderId + '/' + userId;
    	
    	return true;
    }
    
    /*根据工单状态和用户选择的环节判断是否可流转到环节
     * (phaseIndex:环节的arrPhaseMapStatus索引)*/
    this.getCtrl = function (phaseIndex) {
		var min, max;
    	for (var i=0; i<arrPhaseMapStatus.length; i++) {
    		if (i == phaseIndex) {
    			min = parseInt(arrPhaseMapStatus[i].scope.min);
    			max = parseInt(arrPhaseMapStatus[i].scope.max);
    			break;
    		}
    	}
    	var ret = {};
    	ret.isFlow = false;
    	if (iStatus == max) {
    		ret.isFlow = true;
    	}
    	return ret;
    }
    
    this.updateOrderStatus = function(orderid, newstatus) {
    	var self = this;
    	var url = "/updateOrderStatus/" + orderid + "/" + newstatus;
        $.ajax({
            type: "POST",
            url: url,
            dataType: "text",
            success: function (data) {
            	var ul = document.getElementById('ul');
            	for (var i=0; i<ul.children.length; i++) {
            		var li = ul.children[i];
            		if (arrPhaseMapStatus[currPhaseIndex].id == li.getAttribute('id')) {
        	    		li.classList.remove("current_step");
            		}
            		if (arrPhaseMapStatus[currPhaseIndex+1].id == li.getAttribute('id')) {
        	    		li.classList.remove("gray_step");
        	    		li.classList.add("current_step");
            		}
            	}
        		if (newstatus == 17) {
        			currPhaseIndex = arrPhaseMapStatus.length-1;
        		}
        		else {
        			currPhaseIndex++;
        		}
        		iStatus = newstatus;
        		self.changeOrderStateText();
        		self.changeButtonState();
        		self.flowToPhase(currPhaseIndex);
            },
            error: function (msg) {
            	parent.layer.msg('execute updateOrderStatus failed:' + msg);
            }
        });
    }
    
	/*流程回看*/
	this.jump = function(jumpPhaseId) {
		var jumpPhaseIndex;
    	for (var i=0; i<arrPhaseMapStatus.length; i++) {
    		if (arrPhaseMapStatus[i].id == jumpPhaseId) {
    			jumpPhaseIndex = i;
    			break;
    		}
    	}
    	if (jumpPhaseIndex > currPhaseIndex) return false;

    	this.flowToPhase(jumpPhaseIndex);

    	var ul = document.getElementById('ul');
    	for (var i=0; i<ul.children.length; i++) {
    		var li = ul.children[i];
    		var phaseId = li.getAttribute('id');
    		if (phaseId == jumpPhaseId) {
    			li.classList.add("current_step");
    		} else {
    			li.classList.remove("current_step");
    		}
    	}
    	return true;
	}
	
    this.refresh = function() {
    	var self = this;
    	var url = "/getOrder/" + orderId;
        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            cache : false,
            success: function (data) {
            	document.getElementById('orderId').value = data.id;
            	document.getElementById('status').value = data.status;
            	document.getElementById('busiId').value = data.busi_id;
            	self.init();
            },
            error: function (msg) {
            	parent.layer.msg('execute getOrder failed:' + msg);
            }
        });
    }
	
	this.jumpNext = function() {
    	var ul = document.getElementById('ul');
		if (currPhaseIndex >= 4 || createUserId != userId) return false;
    	var ret = this.getCtrl(currPhaseIndex);
    	/*判断是否可以下一步*/
    	if (ret == null || !ret.isFlow) {
    		parent.layer.msg('不能下一步');
    		return false;
    	}
    	var self = this;
    	var nextPhase = this.getStateText(iStatus+1);
    	parent.layer.confirm('确定进入' + nextPhase + '环节吗?', { btn: ['确定','取消'] }, function(index){
        	if (iStatus == 7) {
            	parent.layer.confirm('指令将自动下发，确定吗?', { btn: ['确定','取消'] }, function(index){
        			self.updateOrderStatus(orderId, iStatus+1);
        			parent.layer.close(index);
        		}, function() {
        			return;
        		});
            	return;
        	}
			self.updateOrderStatus(orderId, iStatus+1);
			parent.layer.close(index);
		}, function() {
			return;
		});
	}
    
    this.cancel = function() {
    	if (iStatus == 3 || iStatus == 6 || iStatus == 11 || iStatus == 14 || iStatus == 16 || iStatus == 17) {
    		parent.layer.msg('不能撤销');
    		return false;
    	} else {
        	var self = this;
    		parent.layer.confirm('确定撤销吗?', { btn: ['确定','取消'] }, function(index){
    			self.updateOrderStatus(orderId, 17);
    			parent.layer.close(index);
    		}, function() {
    			return;
    		});
    	}
    }
    
    this.file = function() {
    	if (iStatus == 15) {
        	var self = this;
        	parent.layer.confirm('确定归档吗?', { btn: ['确定','取消'] }, function(index){
    			self.updateOrderStatus(orderId, 16);
    			parent.layer.close(index);
    		}, function() {
    			return;
    		});
    	} else {
    		return false;
    	}
    }
    
    this.backOrder = function() {
    	if (iStatus == 4 || iStatus == 5 || iStatus == 6 || iStatus == 7 ||
        	iStatus == 9 || iStatus == 10 || iStatus == 12 || iStatus == 15) {
        	var self = this;
        	var alarm = '';
        	if (iStatus == 9 || iStatus == 10) {
        		alarm = "下发指令操作未完成，建议先在指令下发页面中回退指令。工单流程环节回退并不自动回退未处理完的下发任务。";
        	}
        	var text = this.getStateText();
        	alarm += '确定从' + text + '环节回退到数据录入环节吗?';
        	parent.layer.confirm(alarm, { btn: ['确定','取消'] }, function(index){
        		self.orderback();
        		parent.layer.close(index);
    		}, function() {
    			return;
    		});
    	} else {
    		return false;
    	}
    }
    
    this.orderback = function() {
    	var self = this;
    	var url = "/backOrder/" + orderId + "/" + iStatus + "/1/" + busiId + "/" + userId;
        $.ajax({
            type: "POST",
            url: url,
            dataType: "text",
            success: function (data) {
            	if (data == '1') {
	            	document.getElementById('orderId').value = orderId;
	            	document.getElementById('status').value = '1';
	            	document.getElementById('busiId').value = busiId;
	            	document.getElementById('userId').value = userId;
	            	self.init();
	            	var text = self.getStateText();
	            	parent.layer.msg('回退成功:已从'+text+'环节回退到数据录入环节');
            	} else {
            		parent.layer.msg('dubbo orderBackOperate返回不等于1');
            	}
            },
            error: function (msg) {
            	parent.layer.msg('dubbo orderBackOperate failed:' + msg);
            }
        });    	
    }
};
$(function () {
	flowPhase = new FlowPhaseManage();
	flowPhase.init();
});
