var admApp = angular.module("admApp", []);

admApp.controller("editCtrl", function($scope){

//	$scope.doCancel = function(){
//		$window.location.href = "pfbBonusMaintain.html";		
//	};
	
});

function updSpecial(id){
	$(location).attr( 'href' , 'editPfbxBonusSetSpecial.html?id=' + id);
}

function delSpecial(id){
	if(confirm("確定要刪除該筆優惠設定？")){
		$(location).attr( 'href' , 'delSpecial.html?id=' + id);
	}
}

function backpage(){
	$(location).attr( 'href' , 'pfbBonusSetMaintain.html');
}
