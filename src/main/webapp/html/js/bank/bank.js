
var admApp = angular.module('admApp', []);

admApp.factory("addFactory", function(){
	
	return {
		add: function($scope, $http, $sce){
			
			if($scope.bankForm.$invalid) return;
			
			console.log(">> bankCode: "+$scope.bankCode+" bankName: "+$scope.bankName+" parentBank: "+$scope.parentBank);			
			//var config = {params:{bankCode: $scope.bankCode, bankName: $scope.bankName, branchCode: $scope.branchCode, branchName: $scope.branchName}};
			
			//$http.get("addBank.html",config)
			
			var url = 'addBank.html';
			var data = {bankCode: $scope.bankCode, bankName: $scope.bankName, branchCode: $scope.branchCode, branchName: $scope.branchName};
			var transFn = function(data) {
                return $.param(data);
            };
            var postCfg = {
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                transformRequest: transFn
            };
           
			$http.post(url, data, postCfg)
			
			.success(function(data){
				$scope.bankDate = $sce.trustAsHtml(data); 
			})
			.error(function(data){
				console.log(">> error!!");
		    });
		}
	};
});


admApp.controller('bankCtrl', function ($scope, $http, $window, $sce, addFactory) {
	
	$scope.doAdd = function(){
		addFactory.add($scope, $http, $sce);		
	};	
	
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