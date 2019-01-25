var admApp = angular.module("admApp", []);

admApp.factory("billFactory", function(){
	
	return {
		open: function($scope, $window, pfbId, strYear, strMonth, endYear, endMonth, payDate){		
			$window.open("pfbBonusBill.html?pfbId="+pfbId+"&strYear="+strYear+"&strMonth="+strMonth+"&endYear="+endYear+"&endMonth="+endMonth+"&payDate="+payDate);
		},
		download: function($scope, $window, pfbId, strYear, strMonth, endYear, endMonth, payDate){		
			$window.open("downloadPfbBonusBill.html?pfbId="+pfbId+"&strYear="+strYear+"&strMonth="+strMonth+"&endYear="+endYear+"&endMonth="+endMonth+"&payDate="+payDate);
		}
	};
});

admApp.controller("transCtrl", function($scope, $http, $window, billFactory){

	$scope.doOpen = function(pfbId, strYear, strMonth, endYear, endMonth, payDate){
		//console.log(">>> pfbId: "+pfbId);
		billFactory.open($scope, $window, pfbId, strYear, strMonth, endYear, endMonth, payDate);		
	};
	
	$scope.doDownload = function(pfbId, strYear, strMonth, endYear, endMonth, payDate){
		//console.log(">>> pfbId: "+pfbId);
		billFactory.download($scope, $window, pfbId, strYear, strMonth, endYear, endMonth, payDate);		
	};
});