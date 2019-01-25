$(document).ready(function(){
	
//	// first example
//	$("#browser").treeview();
//	
	// second example
	$("#browser").treeview({
		persist: "cookie",
		animated: "fast",
		collapsed: true,
		unique: false
	});
//	
//	// third example
//	$("#red").treeview({
//		animated: "fast",
//		collapsed: true,
//		unique: true,
//		persist: "cookie",
//		toggle: function() {
//			window.console && console.log("%o was toggled", this);
//		}
//	});
//	
//	// fourth example
//	$("#black, #gray").treeview({
//		control: "#treecontrol",
//		persist: "cookie",
//		cookieId: "treeview-black"
//	});

});