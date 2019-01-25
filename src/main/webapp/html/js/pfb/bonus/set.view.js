//var admApp = angular.module("admApp", ["xeditable","dialogs"]);
var admApp = angular.module("admApp", []);

admApp.factory("searchFactory", function(){
	
	return {
		search: function($scope, $http, $sce){
			//console.log(">> config: "+$scope.keyword+","+$scope.category+","+$scope.status);			
			var config = {params:{keyword: $scope.keyword, category: $scope.category, status: $scope.status}};
			$http.get("searchPfbBonusSet.html",config)
			.success(function(data){
				$scope.bonusSetUpData = $sce.trustAsHtml(data); 
			})
			.error(function(data){
				console.log(">> error!!");
		    });
		}
	};
});

admApp.factory("editFactory", function(){
	
	return {
		edit: function($scope, $window, pfbId){
			$window.location.href = "editPfbBonusSet.html?pfbId="+pfbId;
		}
	};
});

admApp.controller("searchCtrl", function($scope, $http, $sce, $window, searchFactory, editFactory){
	
	$scope.doSearch = function(){
		searchFactory.search($scope, $http, $sce);		
	};	
	
	$scope.doEdit = function(pfbId){
		//console.log(">>> pfbId: "+pfbId);
		editFactory.edit($scope, $window, pfbId);		
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

//admApp.directive("compileTemplate", ["$compile", "$parse", "$filter", function($compile, $parse, $filter) {
//	
//	var directiveConfig = null;
//	var linker = function($scope, element, attr){
//		
//		//console.log(">> attr: "+attr);
//		//console.log(">> attr.ngBindHtml: "+attr.ngBindHtml);
//		
//		 var parse = $parse(attr.ngBindHtml);
//		 
//         //console.log(">> parse: "+parse);
//         
//         function value() { 
//         	return (parse($scope) || '').toString(); 
//         }	            
//         
//         $scope.$watch(value, function() {
//             $compile(element, null, -9999)($scope); 
//         });	
//         
//         //console.log(">> attr.ngModel: "+attr.ngModel);
//         
//        
//         $scope.user = {status: 2};
//
//         $scope.statuses = [
//                            {value: 1, text: 'status1'},
//                            {value: 2, text: 'status2'},
//                            {value: 3, text: 'status3'},
//                            {value: 4, text: 'status4'}
//                            ];
//
//         $scope.showStatus = function() {
//        	 var selected = $filter('filter')($scope.statuses, {value: $scope.user.status});
//        	 return ($scope.user.status && selected.length) ? selected[0].text : 'Not set';
//         };
//	};
//	
//	directiveConfig = {
//			restrict: "A",
//			link: linker
//	};
//	
//    return directiveConfig;
//}]);      