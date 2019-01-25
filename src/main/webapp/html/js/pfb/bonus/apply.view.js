var firstStartDate = "";
var firstendtDate = "";
$(document).ready(function() {
	$('#status option[value=1]').attr('selected', 'selected');
	$('#datepicker').datepicker({
		format : "yyyy-mm-dd",
		language : "zh-TW",
		autoclose : true
	}).on('changeDate', function(ev){
		if(firstStartDate == ""){
			firstStartDate = $('#startDate').val();
		} else {
			if(firstendtDate == ""){
				var newDate = ev.date;
				console.log(ev);
				newDate.setDate(newDate.getDate() + 30);
				var endDate = newDate.getFullYear() + "-" + (newDate.getMonth() + 1) + "-" + newDate.getDate();
				$('#endDate').val(endDate);
			}
			firstendtDate = firstStartDate;
		}
	});
	
	//更新發票/收據狀態
	$(document).on("click", ".doUpInvoiceBT", function() {
		var thisId = $(this).attr("thisId");
		var upType = "invoice";
		
		var URL = "searchPfbApplyBonus.html";
		var config = {
			startDate : $("#startDate").val(),
			endDate : $("#endDate").val(),
			keyword : $("#keyword").val(),
			category : $("#category").val(),
			status : $("#status").val(),
			applyId: thisId,
			upType: upType,
			checkStatus: $("#doUpInvoiceSel_" + thisId).val(), 
			checkNote: $("#doUpInvoiceText_" + thisId).val()
			};
		
		$.post(URL , config , function(rs){
			if(rs != ""){
				console.log(rs);
				$("#tableDIV").html(rs);
				
				alert("更新成功");
			}
		});
	});

	//更新發票狀態
	$(document).on("click", ".doUpInvoiceCheckBT", function() {
		var thisId = $(this).attr("thisId");
		var upType = "invoiceCheck";

		var URL = "searchPfbApplyBonus.html";
		var config = {
			startDate : $("#startDate").val(),
			endDate : $("#endDate").val(),
			keyword : $("#keyword").val(),
			category : $("#category").val(),
			status : $("#status").val(),
			applyId: thisId,
			upType: upType,
			checkStatus: $("#doUpInvoiceCheckSel_" + thisId).val()
			};

		$.post(URL , config , function(rs){
			if(rs != ""){
				console.log(rs);
				$("#tableDIV").html(rs);

				alert("更新成功");
			}
		});
	});
	
	//更新申請單狀態
	$(document).on("click", ".doUpApplyBT", function() {
		var thisId = $(this).attr("thisId");
		var upType = "apply";
		var isDisable = false;
		//判斷狀態是否為Disabled,解除狀態，以利資料傳送
		if($("#doUpApplySel_" + thisId).attr("disabled") == "disabled"){
			isDisable = true;
			$("#doUpApplySel_" + thisId).attr("disabled" , "false");
		}
		var URL = "searchPfbApplyBonus.html";
		var config = {
			startDate : $("#startDate").val(),
			endDate : $("#endDate").val(),
			keyword : $("#keyword").val(),
			category : $("#category").val(),
			status : $("#status").val(),
			applyId: thisId,
			upType: upType,
			checkStatus: $("#doUpApplySel_" + thisId).val(), 
			checkNote: $("#doUpApplyText_" + thisId).val()
			};
		
		$.post(URL , config , function(rs){
			if(rs != ""){
				$("#tableDIV").html(rs);
				
				alert($("#errorMsg").val());
			}
		});
		//將Disabled 屬性加回
		if(isDisable){
			$("#doUpApplySel_" + thisId).attr("disabled" , "disabled");
		}
	});

});

var admApp = angular.module('admApp', [ 'ngDialog' ]);

admApp.factory("searchFactory", function() {
	return {
		search : function($scope, $http, $sce) {
			
			if ($scope.searchForm.$invalid)
				return;
			
			var config = {
				params : {
					startDate : $scope.startDate,
					endDate : $("#endDate").val(),
					keyword : $scope.keyword,
					category : $scope.category,
					status : $("#status").val()
				}
			};
			$http.get("searchPfbApplyBonus.html", config).success(
					function(data) {
						$scope.bonusSetUpData = $sce.trustAsHtml(data);
					}).error(function(data) {
				console.log(">> error!!");
			});
		}
	};
});

admApp.factory("UpdateFactory", function(){
	return {
		Invoice: function($scope, $http, $sce){
			
		}
	};
});

admApp.factory("DoFactory", function(){
	return {
		goDetail: function($scope, $http, $sce , pfbxCustomerId){
			location.href="pfbAccountUpdate.html?targetId=" + pfbxCustomerId;
		}
	};
});


admApp.controller('searchCtrl', function($scope, $http, $window, $sce,
		searchFactory , DoFactory) {

	$scope.doSearch = function() {
		searchFactory.search($scope, $http, $sce);
	};

	$scope.doOpen = function(pfbxApplyBonusId) {
		$window.open("pfbApplyBonusCheck.html?pfbxApplyBonusId=" + pfbxApplyBonusId);
	};
	
	$scope.doCheck = function(pfbxApplyBonusId) {
		location.href="pfbApplyBonusCheck.html?pfbxApplyBonusId=" + pfbxApplyBonusId;
	};
	
	$scope.goDetail = function(pfbxCustomerId) {
		$window.open("pfbAccountUpdate.html?targetId=" + pfbxCustomerId);
	};
});

admApp.directive("compileTemplate", [ "$compile", "$parse",
		function($compile, $parse) {

			var directiveConfig = null;
			var linker = function($scope, element, attr) {

				// console.log(">> attr: "+attr);
				// console.log(">> attr.ngBindHtml: "+attr.ngBindHtml);

				var parse = $parse(attr.ngBindHtml);

				// console.log(">> parse: "+parse);

				function value() {
					return (parse($scope) || '').toString();
				}

				$scope.$watch(value, function() {
					$compile(element, null, -9999)($scope);
				});

			};

			directiveConfig = {
				restrict : "A",
				link : linker
			};

			return directiveConfig;
		} ]);

/**
 * 重跑請款
 */
function renewRunPaymentRequest() {
	$.ajax({
	    type: "post",
	    dataType: "json",
	    url: "renewRunPaymentRequest.html",
	    data: {},
	    timeout: 30000,
	    error: function(xhr){
	        alert('Ajax request 發生錯誤');
	    },
		success : function(response, status) {
			alert(response.msg);
			if (response.status == "SUCCESS") { // 成功狀態重新查詢，非成功都只顯示訊息
				$(".btn.btn-default[ng-click='doSearch()']").trigger("click");
			}
		}
	});
}