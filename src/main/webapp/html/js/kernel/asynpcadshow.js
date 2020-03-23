function check_cookie() {
	uuid = getCookie("uuid");
	puuid = getCookie("puuid");
	if (!uuid && typeof uuid != "undefined" && uuid != 0) {
		doCookieSetup("uuid", "xxx-" + _uuid())
	}
	if (!puuid && typeof puuid != "undefined" && puuid != 0) {
		doCookieSetup("puuid", _puuid())
	}
}
function getCookie(a) {
	var b = escape(a) + "=";
	var c = b.length;
	var d = document.cookie.length;
	var i = 0;
	while (i < d) {
		var j = i + c;
		if (document.cookie.substring(i, j) == b) return getCookieValueByIndex(j);
		i = document.cookie.indexOf(" ", i) + 1;
		if (i == 0) break
	}
	return null
}
function getCookieValueByIndex(a) {
	var b = document.cookie.indexOf(";", a);
	if (b == -1) b = document.cookie.length;
	return unescape(document.cookie.substring(a, b))
}
function doCookieSetup(a, b) {
	expire_days = 1;
	var d = new Date();
	d.setTime(d.getTime() + (expire_days * 24 * 60 * 60 * 1000));
	var c = "expires=" + d.toGMTString();
	document.cookie = a + "=" + b + "; " + c + "; domain=.pchome.com.tw; path=/;"
}
function _uuid() {
	var d = Date.now();
	if (typeof performance !== 'undefined' && typeof performance.now === 'function') {
		d += performance.now()
	}
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		var r = (d + Math.random() * 16) % 16 | 0;
		d = Math.floor(d / 16);
		return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16)
	})
}
function _puuid() {
	var d = Date.now();
	var a = "P." + d + ".0";
	return a
}
check_cookie();
var res = document.URL;
var docurl = encodeURIComponent(res);
var screen_x = screen.availWidth;
var screen_y = screen.availHeight;
var asynpchomeadclass = document.getElementsByClassName("asynpchomead");
var asynpchomeadjs = document.getElementById("asynpchomeadjs");
var asynpchomeadflag = document.getElementById("pchomead8044");
if (asynpchomeadflag == null) {
	asynpchomeadflag = document.createElement('div');
	asynpchomeadflag.id = "pchomead8044";
	asynpchomeadjs.appendChild(asynpchomeadflag);
	var hostname = window.location.hostname;
	if (hostname.indexOf('pchome.com.tw') > -1 || hostname.indexOf('megatime.com.tw') > -1) {
		for (i = 0; i < asynpchomead.length; i++) {
			if (i >= asynpchomeadclass.length) {
				break
			}
			asynpchomead[i].classObject = asynpchomeadclass[i];
			asynpchomead[i].pad_width = asynpchomeadclass[i].getAttribute("pad_width");
			asynpchomead[i].pad_height = asynpchomeadclass[i].getAttribute("pad_height");
			asynpchomead[i].pad_customerId = asynpchomeadclass[i].getAttribute("pad_customerId");
			asynpchomead[i].pad_positionId = asynpchomeadclass[i].getAttribute("pad_positionId");
			asynpchomead[i].pad_precise = asynpchomeadclass[i].getAttribute("pad_precise");
			asynpchomead[i].pad_keyword = asynpchomeadclass[i].getAttribute("pad_keyword");
			asynpchomead[i].pad_page = asynpchomeadclass[i].getAttribute("pad_page");
			asynpchomead[i].pad_ssl = asynpchomeadclass[i].getAttribute("pad_ssl");
			showad(asynpchomead[i])
		}
	} else {
		if (asynpchomead.length > 12) {
			alert("超過廣告上限，最多只能貼12則廣告!")
		} else {
			for (i = 0; i < asynpchomead.length; i++) {
				if (i >= asynpchomeadclass.length) {
					break
				}
				asynpchomead[i].classObject = asynpchomeadclass[i];
				asynpchomead[i].pad_width = asynpchomeadclass[i].getAttribute("pad_width");
				asynpchomead[i].pad_height = asynpchomeadclass[i].getAttribute("pad_height");
				asynpchomead[i].pad_customerId = asynpchomeadclass[i].getAttribute("pad_customerId");
				asynpchomead[i].pad_positionId = asynpchomeadclass[i].getAttribute("pad_positionId");
				asynpchomead[i].pad_precise = asynpchomeadclass[i].getAttribute("pad_precise");
				asynpchomead[i].pad_keyword = asynpchomeadclass[i].getAttribute("pad_keyword");
				asynpchomead[i].pad_page = asynpchomeadclass[i].getAttribute("pad_page");
				asynpchomead[i].pad_ssl = asynpchomeadclass[i].getAttribute("pad_ssl");
				showad(asynpchomead[i])
			}
		}
	}
}
function showad(a) {
	var b = "";
	var c = "";
	var d = "";
	var e = "";
	var f = "";
	var g = "";
	if (a.pad_precise != null) {
		f = a.pad_precise
	} else {
		f = false
	}
	if (a.pad_ssl != null) {
		g = a.pad_ssl
	} else {
		g = true
	}
	if (a.pad_positionId != null) {
		d = a.pad_positionId.substring(0, 16);
		e = a.pad_positionId.substring(16, 17);
		if (e == "") {
			e = "C"
		}
	}
	if (e == "S") {
		if (a.pad_keyword != null) {
			b = a.pad_keyword
		}
		if (a.pad_page != null) {
			c = a.pad_page
		}
		if (b.length == 0) {
			if (res.indexOf("nicolee.pchome.com.tw") > 1) {
				var h = res;
				var j = h.indexOf("q=");
				if (j > 1) {
					var k = h.indexOf("page=");
					var l = h.indexOf("precise=");
					b = h.substring(j + 2, k - 1);
					if (k < 1) {
						c = 1
					} else {
						c = h.substring(k + 5, l - 1)
					}
				}
			}
			if (res.indexOf("search.pchome.com.tw") > 1) {
				var h = res;
				var j = h.indexOf("q=");
				if (j > 1) {
					var k = h.indexOf("ch=");
					b = h.substring(j + 2, k - 1)
				}
			}
			if (res.indexOf("search.ruten.com.tw") > 1) {
				var h = res;
				var j = h.indexOf("k=");
				if (j > 1) {
					var l = h.indexOf("t=");
					var k = h.indexOf("p=");
					b = document.getElementById("kwd").value;
					if (k < 1) {
						c = 1
					} else {
						c = h.substring(k + 2, h.length)
					}
				}
			}
		}
	} else {
		b = "";
		c = "";
		f = ""
	}
	var m = "";
	var n = "";
	if (g) {
		n += document.location.protocol + "//kdcl.pchome.com.tw/adshow2.html?pfbxCustomerInfoId=" + a.pad_customerId
	} else {
		n += document.location.protocol + "//kdcl.pchome.com.tw/adshow2.html?pfbxCustomerInfoId=" + a.pad_customerId
	}
	n += "&positionId=" + d;
	n += "&padWidth=" + a.pad_width.replace(';', '');
	n += "&padHeight=" + a.pad_height.replace(';', '');
	n += "&keyword=" + b.replace(';', '');
	n += "&page=" + c.replace(';', '');
	n += "&precise=" + f.replace(';', '');
	n += "&fig=" + m.replace(';', '');
	n += "&screenX=" + screen_x;
	n += "&screenY=" + screen_y;
	n += "&t=" + Math.floor(Math.random() * 1000 + 1);
	if (docurl.indexOf("kdcl") > 1 || docurl.indexOf("kwstg") > 1) {
		n += "&docurl="
	} else {
		n += "&docurl=" + docurl
	}
	var o = "<scr" + "ipt type=text/javascript src=" + n + "></scr" + "ipt>";
	var p = document.createElement("iframe");
	p.setAttribute("class", "akb_iframe");
	p.setAttribute("scrolling", "no");
	p.setAttribute("frameborder", "0");
	p.setAttribute("marginwidth", "0");
	p.setAttribute("marginheight", "0");
	p.setAttribute("vspace", "0");
	p.setAttribute("hspace", "0");
	p.setAttribute("id", "pchome8044_ad_frame1");
	p.setAttribute("width", a.pad_width + "px");
	p.setAttribute("height", a.pad_height + "px");
	p.setAttribute("allowtransparency", "true");
	p.setAttribute("allowfullscreen", "true");
	p.setAttribute("src", "javascript:\'" + o + "\'");
	asynpchomead[i].classObject.appendChild(p)
}
window.onresize = function(a) {
	var b = document.getElementsByTagName("iframe");
	var c = [];
	for (var i = 0; i < b.length; i++) {
		if (b[i].id == "pchome8044_ad_frame1") {
			c.push(b[i])
		}
	}
	for (var i = 0; i < c.length; i++) {
		var d = c[i].contentDocument.body.children[0];
		var e = d.contentWindow;
		var f = c[i];
		var g = window.document.body.scrollTop || window.document.documentElement.scrollTop;
		var h = window.innerHeight;
		var j = f.offsetTop;
		var k = f.getBoundingClientRect().bottom;
		var l = f.getBoundingClientRect().top;
		var m = f.height.replace(';px', '') / 2;
		var n = (h + m) - f.height.replace(';px', '');
		adInfo = '{"adInfo":{"scrollTop":' + g + ',"viewHeight":' + h + ',"iframeOffSetTop":' + j + ',"iframeBottom":' + k + ',"iframeTop":' + l + ',"iframeHalf":' + m + ',"controllerHeight":' + n + ',"visibilitychange":' + false + '}}';
		e.postMessage(adInfo, "*")
	}
};
window.document.addEventListener('scroll', function() {
	var a = document.getElementsByTagName("iframe");
	var b = [];
	for (var i = 0; i < a.length; i++) {
		if (a[i].id == "pchome8044_ad_frame1") {
			b.push(a[i])
		}
	}
	for (var i = 0; i < b.length; i++) {
		var c = b[i].contentDocument.body.children[0];
		var d = c.contentWindow;
		var e = b[i];
		var f = window.document.body.scrollTop || window.document.documentElement.scrollTop;
		var g = window.innerHeight;
		var h = e.offsetTop;
		var j = e.getBoundingClientRect().bottom;
		var k = e.getBoundingClientRect().top;
		var l = e.height.replace(';px', '') / 2;
		var m = (g + l) - e.height.replace(';px', '');
		adInfo = '{"adInfo":{"scrollTop":' + f + ',"viewHeight":' + g + ',"iframeOffSetTop":' + h + ',"iframeBottom":' + j + ',"iframeTop":' + k + ',"iframeHalf":' + l + ',"controllerHeight":' + m + ',"visibilitychange":' + false + '}}';
		d.postMessage(adInfo, "*")
	}
}, false);
window.addEventListener("message", getMessage0, false);

function getMessage0(a) {
	try {
		if (a.data.adBackup != undefined && a.data.adBackup.iframeIndex != null && a.data.adBackup.ALEX == 'asynpcadshow') {
			var b = a.data.adBackup.htmlContent;
			var c = document.getElementsByClassName("asynpchomead");
			if (b == 'blank') {
				var d = c[a.data.adBackup.iframeIndex];
				d.height = 0;
				d.width = 0
			}
			if (b != 'blank' && b != undefined) {
				var d = c[a.data.adBackup.iframeIndex];
				if (d == undefined) {
					c = document.getElementsByClassName("akb_iframe");
					d = c[a.data.adBackup.iframeIndex].parentElement
				}
				d.height = 0;
				d.width = 0;
				d.setAttribute("style", "display:none");
				if (d.nextElementSibling.nextElementSibling == null || d.nextElementSibling.nextElementSibling.className != 'ad_backup_pchome') {
					var e = d.parentElement;
					var f = document.createElement('div');
					var g = document.createElement('div');
					g.className = 'ad_backup_pchome';
					f.innerHTML = b;
					var h = f.getElementsByTagName("*");
					for (var j = 0; j < h.length; j++) {
						var i = h[j].tagName;
						var l = h[j].attributes;
						var m = h[j].innerHTML;
						var n = document.createElement(i);
						for (var k = 0; k < l.length; k++) {
							var o = l[k];
							var p = o.name;
							var p = o.value;
							n.setAttribute(o.name, o.value)
						}
						n.text = m;
						g.appendChild(n, g.firstChild)
					}
					e.insertBefore(g, d.nextElementSibling.nextElementSibling)
				}
			}
		}
	} catch (err) {}
}
try {
	var iframeArrayData = document.getElementsByTagName("iframe");
	var iframeArray = [];
	for (var i = 0; i < iframeArrayData.length; i++) {
		if (iframeArrayData[i].id == "pchome8044_ad_frame1") {
			iframeArray.push(iframeArrayData[i])
		}
	}
	var pcadshowList = document.getElementsByClassName("asynpchomead");
	for (var i = 0; i < iframeArray.length; i++) {
		iframeArray[i].onload = function() {
			if (this.parentElement.className == 'asynpchomead') {
				for (var k = 0; k < iframeArray.length; k++) {
					if (this == iframeArray[k]) {
						var a = this.contentDocument.body.children[0];
						var b = a.contentWindow;
						var c = this;
						var d = window.document.body.scrollTop || window.document.documentElement.scrollTop;
						var e = window.innerHeight;
						var f = c.offsetTop;
						var g = c.getBoundingClientRect().bottom;
						var h = c.getBoundingClientRect().top;
						var i = c.height.replace(';px', '') / 2;
						var j = (e + i) - c.height.replace(';px', '');
						var l = '{"adBackup":{"iframeIndex":' + k + ',"ALEX":"asynpcadshow"},"adInfo":{"scrollTop":' + d + ',"viewHeight":' + e + ',"iframeOffSetTop":' + f + ',"iframeBottom":' + g + ',"iframeTop":' + h + ',"iframeHalf":' + i + ',"controllerHeight":' + j + ',"visibilitychange":' + false + '}}';
						b.postMessage(l, "*")
					}
				}
			} else {
				for (var k = 0; k < iframeArray.length; k++) {
					if (this == iframeArray[k]) {
						var a = this.contentDocument.body.children[0];
						var b = a.contentWindow;
						var c = this;
						var d = window.document.body.scrollTop || window.document.documentElement.scrollTop;
						var e = window.innerHeight;
						var f = c.offsetTop;
						var g = c.getBoundingClientRect().bottom;
						var h = c.getBoundingClientRect().top;
						var i = c.height.replace(';px', '') / 2;
						var j = (e + i) - c.height.replace(';px', '');
						var l = '{"adBackup":{"iframeIndex":' + k + ',"ALEX":"pcadshow"},"adInfo":{"scrollTop":' + d + ',"viewHeight":' + e + ',"iframeOffSetTop":' + f + ',"iframeBottom":' + g + ',"iframeTop":' + h + ',"iframeHalf":' + i + ',"controllerHeight":' + j + ',"visibilitychange":' + false + '}}';
						b.postMessage(l, "*")
					}
				}
			}
		}
	}
} catch (err) {}