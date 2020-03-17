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
var paclUrl = location.protocol+"//showstg.pchome.com.tw/adm/html/js/ptag/ptag2.js";
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












