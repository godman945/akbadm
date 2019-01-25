$(document).ready(
		function() {
			
			//search Function
			function doSearch()
			{
				var sdate = $("#startDate").val();
				var edate = $("#endDate").val();
				
				var config = {
					sdate : sdate,
					edate : edate
				};
				
				if(sdate != "" && edate != ""){
					$(".rightbody").block({
						message : '<h1>資料處理中...</h1>',
						css : {
							border : '3px solid #a00',
							backgroundColor : '#FFF'
						}
					});
					
					$.post("pfbInComeReportSearch.html", config , function(rs){
						$("#AjaxBackData").html(rs);
						$(".rightbody").unblock();
					});
				}
				else{
					//alert("請輸入查詢起訖日期");
				}
			}
			
			//datepicker
			$('#datepicker').datepicker({
				format : "yyyy-mm-dd",
				language : "zh-TW"
			});
			
			//return
			if($("#returnFlag").val() == "Y"){
				doSearch();
			}
			
			//searchAjax
			$("#searchBt").click(function(){
				doSearch();
			});
			
			//detail
			$(document).on("click", ".detailBt", function() {
				var sdate = $("#startDate").val();
				var edate = $("#endDate").val();
				var reportDate = $(this).attr("reportDate");
				var URL = "pfbInComeDetail.html?reportDate=" + reportDate + "&sdate=" + sdate + "&edate=" + edate;
				
				location.href = URL;
			});
			
			//detail Bk
			$("#returnBt").click(function(){
				$("#returnForm").submit();
			});

			//openPFPDetail
			$(document).on("click", ".openPFPDetail", function() {
				var voId = $(this).attr("voId");
				var voDate = $(this).attr("voDate");
				
				var URL = "pfbInComePFPDetailAjax.html";
				var config = {
					reportDate : $(this).attr("voDate")
					};
				
				$.post(URL , config , function(rs){
					if(rs != ""){
						$("#dialog").html(rs);
						
						$("#dialog").dialog("open");
						return false;
					}
				});
			});

			// openPFBDetail
			$(document).on("click", ".openPFBDetail", function() {
				var voId = $(this).attr("voId");
				var voDate = $(this).attr("voDate");
				
				var URL = "pfbInComePFBDetailAjax.html";
				var config = {
					reportDate : $(this).attr("voDate")
					};
				
				$.post(URL , config , function(rs){
					if(rs != ""){
						$("#dialog").html(rs);
						
						$("#dialog").dialog("open");
						return false;
					}
				});
			});
			
			// openPFDDetail
			$(document).on("click", ".openPFDDetail", function() {
				var voId = $(this).attr("voId");
				var voDate = $(this).attr("voDate");
				
				var URL = "pfbInComePFDDetailAjax.html";
				var config = {
					reportDate : $(this).attr("voDate")
					};
				
				$.post(URL , config , function(rs){
					if(rs != ""){
						$("#dialog").html(rs);
						
						$("#dialog").dialog("open");
						return false;
					}
				});
			});

			$("#dialog").dialog(
					{
						autoOpen : false,
						show : "blind",
						hide : "explode",
						width : 800,
						height : 800,
						resizable : false,
						modal : true,
						closeOnEscape : true,
						draggable : false,
						title : "明細",
						open : function(event, ui) {
							// 隱藏「x」關閉按鈕
							$(this).parent().children().children('.ui-dialog-titlebar-close').hide();
						},
						buttons : {
							"Ok" : function() {
								$(this).dialog("close");
							}
						}
					});
		});

var admApp = angular.module('admApp', [ 'ngDialog' ]);

admApp.factory("incomeFactory", function() {

	return {
		search : function($scope, $http, $sce) {

			if ($scope.searchForm.$invalid) {
				return;
			}

		},
		open : function($scope, $window, reportdate) {
			$window.open("pfbInComeDetail.html?reportdate=" + reportdate);
		}
	};
});

admApp.controller('searchCtrl', function($scope, $http, $window, $sce,
		incomeFactory) {

	$scope.doSearch = function() {
		incomeFactory.search($scope, $http, $sce);
	};

	$scope.openDetail = function(reportdate) {
		incomeFactory.open($scope, $http, $sce, reportdate);
	};

	$scope.openDetail1 = function(reportdate) {
		location.href = "pfbInComeDetail.html?reportDate=" + reportdate;
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