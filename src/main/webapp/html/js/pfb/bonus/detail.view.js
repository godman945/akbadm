
var admApp = angular.module("admApp", []);

admApp.factory("searchFactory", function(){
	
	return {
		search: function($scope, $http, $sce){
			//console.log(">> config: "+$scope.keyword+","+$scope.category+","+$scope.status);			
			var data = $.param({keyword: $scope.keyword, category: $scope.category, status: $scope.status});;
			var config = {
	                headers : {
	                    'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
	                }
	            }
			$http.post("searchPfbBonusDetail.html",data,config)
			.success(function(data){
				$scope.bonusSetUpData = $sce.trustAsHtml(data); 
			})
			.error(function(data){
				console.log(">> error!!");
		    });
		}/*,
		download: function($scope, $http, $sce){
			//console.log(">> config: "+$scope.keyword+","+$scope.category+","+$scope.status);			
			var config = {params:{keyword: $scope.keyword, category: $scope.category, status: $scope.status, downloadFlag: "yes"}};
			$http.get("downloadPfbBonusDetail.html",config)
			.success(function(data){
				var anchor = angular.element('<a/>');
			     anchor.attr({
			         href: 'data:attachment/csv;charset=big5,' + encodeURI(data),
			         target: '_blank',
			         download: 'filename.csv'
			     })[0].click();
				//$scope.bonusSetUpData = $sce.trustAsHtml(data); 
			})
			.error(function(data){
				console.log(">> error!!");
		    });
		}*/
	};
});

admApp.factory("transFactory", function(){
	
	return {
		open: function($scope, $window, pfbId){
			//$window.open("pfbBonusTransDetail.html?pfbId="+pfbId);
			location.href="pfbBonusTransDetail.html?pfbId="+pfbId;
		}
	};
});

admApp.controller("searchCtrl", function($scope, $http, $sce, $window, searchFactory, transFactory){
	
	$scope.doSearch = function(){
		searchFactory.search($scope, $http, $sce);		
	};	

	$scope.doOpen = function(pfbId){
		//console.log(">>> pfbId: "+pfbId);
		transFactory.open($scope, $window, pfbId);		
	};
	
	/*$scope.doDownload = function(){
		searchFactory.download($scope, $http, $sce);		
	};*/	
});

admApp.directive("compileTemplate", ["$compile", "$parse", function($compile, $parse) {
	
	var directiveConfig = null;
	var linker = function($scope, element, attr){
		
		//console.log(">> attr: "+attr);
		//console.log(">> attr.ngBindHtml: "+attr.ngBindHtml);
		
		 var parse = $parse(attr.ngBindHtml);
		 
         //console.log(">> parse: "+parse);
         
         function value() { 
         	return (parse($scope) || '').toString(); 
         }	            
         
         $scope.$watch(value, function() {
             $compile(element, null, -9999)($scope); 
         });	
         
	};
	
	directiveConfig = {
			restrict: "A",
			link: linker
	};
	
    return directiveConfig;
}]); 

function tableSorter(){
	$("#tableView").tablesorter({
		headers:{
			0 : { sorter: 'fancyNumber' },
			7 : { sorter: 'rangesort' },
			8 : { sorter: 'rangesort' },
			9 : { sorter: 'rangesort' }
			}
	});
}

//下載報表
function doDownlaod() {
	$('#downloadFlag').val("yes");
    document.forms[0].action = "downloadPfbBonusDetail.html";
    document.forms[0].method = "post";
	document.forms[0].submit();
}
      