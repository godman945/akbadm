//var paclUrl = location.protocol+"//pacl.pchome.com.tw/js/ptag2.js";
//(function(i, s, o, g, r, a, m) {
//	i['pchome'] = r;
//	i[r] = i[r] || function() {
//		(i[r].q = i[r].q || []).push(arguments);
//	}, i[r].l = 1 * new Date();
//	a = s.createElement(o), m = s.getElementsByTagName(o)[0];
//	a.async = 1;
//	a.src = g;
//	m.parentNode.insertBefore(a, m);
//})(window, document, 'script', paclUrl, 'ptag');

console.log("init ptag js");
var paclUrl = location.protocol+"//alex.pchome.com.tw:8080/akbadm/html/js/ptag/pdtag.js";
(function(i, s, o, g, r, a, m) {
	i['pchome'] = r;
	i[r] = i[r] || function() {
		(i[r].q = i[r].q || []).push(arguments);
	}, i[r].l = 1 * new Date();
	a = s.createElement(o), 
	m = s.getElementsByTagName(o)[0];
	a.async = false;
	a.src = g;
	m.parentNode.insertBefore(a, m);
	
	
	
})(window, document, 'script', paclUrl, 'ptag');


//(function(window, document, script, js, ptag, a, m) {
//	window['pchome'] = ptag;
//	window[ptag] = window[ptag] || function() {
//	(window[ptag].q = window[ptag].q || []).push(arguments)
//	}, window[ptag].l = 1 * new Date();
//	a = document.createElement(script); 
//	m = document.getElementsByTagName(script)[0];
//	a.async = false;
//	a.src = js;
//	m.parentNode.insertBefore(a, m);
//	console.log("BBBBBBBBB");
//})(window, document, 'script', 'http://alex.pchome.com.tw:8080/akbadm/html/js/ptag/pdtag.js', 'ptag');
//	ptag({"paid":"alex_1234568"});










