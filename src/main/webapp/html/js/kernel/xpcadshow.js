var res = document.URL;
var docurl = encodeURIComponent(res);
var keywordValue = "";
var pageValue = "";
var pid = "";
var ptype = "";
var seway = "";
var padssl = "";
if (typeof pad_pchad != 'object') {
    pad_pchad = []
}
if (typeof pad_precise != 'undefined') {
    seway = pad_precise
} else {
    seway = false
}
if (typeof pad_ssl != 'undefined') {
    padssl = pad_ssl
} else {
    padssl = true
}
if (typeof pad_positionId != 'undefined') {
    pid = pad_positionId.substring(0, 16);
    ptype = pad_positionId.substring(16, 17);
    pad_pchad.push(pid);
    if (ptype == "") {
        ptype = "C"
    }
}
if (ptype == "S") {
    if (typeof pad_keyword != 'undefined') {
        keywordValue = pad_keyword
    }
    if (typeof pad_page != 'undefined') {
        pageValue = pad_page
    }
    if (keywordValue.length == 0) {
        if (res.indexOf("nicolee.pchome.com.tw") > 1) {
            var testurl = res;
            var kis = testurl.indexOf("q=");
            if (kis > 1) {
                var pis = testurl.indexOf("page=");
                var tis = testurl.indexOf("precise=");
                keywordValue = testurl.substring(kis + 2, pis - 1);
                if (pis < 1) {
                    pageValue = 1
                } else {
                    pageValue = testurl.substring(pis + 5, tis - 1)
                }
            }
        }
        if (res.indexOf("search.pchome.com.tw") > 1) {
            var testurl = res;
            var kis = testurl.indexOf("q=");
            if (kis > 1) {
                var pis = testurl.indexOf("ch=");
                keywordValue = testurl.substring(kis + 2, pis - 1)
            }
        }
        if (res.indexOf("search.ruten.com.tw") > 1) {
            var testurl = res;
            var kis = testurl.indexOf("k=");
            if (kis > 1) {
                var tis = testurl.indexOf("t=");
                var pis = testurl.indexOf("p=");
                keywordValue = testurl.substring(kis + 2, tis - 1);
                if (pis < 1) {
                    pageValue = 1
                } else {
                    pageValue = testurl.substring(pis + 2, testurl.length)
                }
            }
        }
    }
} else {
    keywordValue = "";
    pageValue = "";
    seway = ""
}
try {
    for (var i = 0; i < document.getElementsByTagName("script").length; i++) {
    	console.log(document.getElementsByTagName("script")[i].src);
        if (document.getElementsByTagName("script")[i].src.includes('xpcadshow.js')) {
            if ("ADBACKUP" == document.getElementsByTagName("script")[i].previousElementSibling.previousElementSibling.previousElementSibling.tagName) {
                docurl = "www.pchome.com.tw"
            }
        }
    }
} catch (err) {};
var fig = "";
var adurl = "";
if (padssl == "false") {
    adurl += "http://kdcl.pchome.com.tw/adshow2.html?pfbxCustomerInfoId=" + pad_customerId
} else {
    adurl += "https://kdcl.pchome.com.tw/adshow2.html?pfbxCustomerInfoId=" + pad_customerId
}
adurl += "&positionId=" + pid;
adurl += "&padWidth=" + pad_width;
adurl += "&padHeight=" + pad_height;
adurl += "&keyword=" + keywordValue;
adurl += "&page=" + pageValue;
adurl += "&precise=" + seway;
adurl += "&fig=" + fig;
adurl += "&t=" + Math.floor(Math.random() * 1000 + 1);
if (docurl.indexOf("kdcl") > 1 || docurl.indexOf("kwstg") > 1) {
    adurl += "&docurl="
} else {
    adurl += "&docurl=" + docurl
}
var showadscript = "<script type=text/javascript src=" + adurl + "></script>";
if (pad_pchad.length <= 10) {
    if (ptype == "S") {
        document.write(showadscript)
    } else {
        var head = document.getElementsByTagName("head");
        document.write(
            '<iframe class="akb_iframe" scrolling="no" frameborder="0" marginwidth="0" marginheight="0" vspace="0" hspace="0" id="pchome8044_ad_frame1" width="' +
            pad_width + '" height="' + pad_height + '" allowtransparency="true" allowfullscreen="true" src="javascript:\'' + showadscript + '\'"></iframe>'
        )
    }
} else {
    alert("超過廣告上限，最多只能貼10則廣告!")
}
window.onresize = function(a) {
    try {
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
            var m = f.height.replace(';px', '');
            var n = m / 2;
            var o = (h + n) - m;
            adInfo = '{"adInfo":{"scrollTop":' + g + ',"viewHeight":' + h + ',"iframeOffSetTop":' + j + ',"iframeBottom":' + k + ',"iframeTop":' + l +
                ',"iframeHalf":' + n + ',"controllerHeight":' + o + ',"visibilitychange":' + false + '}}';
            e.postMessage(adInfo, "*")
        }
    } catch (err) {}
};
window.document.addEventListener('scroll', function() {
    try {
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
            var l = e.height.replace(';px', '');
            var m = l / 2;
            var n = (g + m) - l;
            adInfo = '{"adInfo":{"scrollTop":' + f + ',"viewHeight":' + g + ',"iframeOffSetTop":' + h + ',"iframeBottom":' + j + ',"iframeTop":' + k +
                ',"iframeHalf":' + m + ',"controllerHeight":' + n + ',"visibilitychange":' + false + '}}';
            d.postMessage(adInfo, "*")
        }
    } catch (err) {}
}, false);
window.document.addEventListener('visibilitychange', function() {
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
        var l = e.height.replace(';px', '');
        var m = l / 2;
        var n = (g + m) - l;
        var o = false;
        if (document.hidden) {
            o = true
        }
        adInfo = '{"adInfo":{"scrollTop":' + f + ',"viewHeight":' + g + ',"iframeOffSetTop":' + h + ',"iframeBottom":' + j + ',"iframeTop":' + k +
            ',"iframeHalf":' + m + ',"controllerHeight":' + n + ',"visibilitychange":' + o + '}}';
        d.postMessage(adInfo, "*")
    }
}, false);
window.addEventListener("message", getMessage0, false);

function getMessage0(a) {
    try {
        if (a.data.adBackup != undefined && a.data.adBackup.iframeIndex != null && a.data.adBackup.ALEX == 'pcadshow') {
            var b = a.data.adBackup.htmlContent;
            if (b != null) {
                var c = document.getElementsByClassName("akb_iframe");
                if (b == 'blank') {
                    var d = c[a.data.adBackup.iframeIndex];
                    d.height = 0;
                    d.width = 0
                }
                if (b != 'blank' && b != undefined) {
                    var d = c[a.data.adBackup.iframeIndex];
                    d.height = 0;
                    d.width = 0;
                    var e = d.parentElement;
                    if (d.nextElementSibling == null || d.nextElementSibling.className != 'ad_backup_pchome') {
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
                        if (d.nextElementSibling == null) {
                            e.appendChild(g, d.parentElement)
                        } else {
                            e.insertBefore(g, d.nextElementSibling)
                        }
                    }
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
                        var d = null;
                        var e = window.document.body.scrollTop || window.document.documentElement.scrollTop;
                        var f = window.innerHeight;
                        var g = c.offsetTop;
                        var h = c.getBoundingClientRect().bottom;
                        var i = c.getBoundingClientRect().top;
                        var j = c.height.replace(';px', '');
                        var l = j / 2;
                        var m = (f + l) - j;
                        var d = '{"adInfo":{"scrollTop":' + e + ',"viewHeight":' + f + ',"iframeOffSetTop":' + g + ',"iframeBottom":' + h + ',"iframeTop":' +
                            i + ',"iframeHalf":' + l + ',"controllerHeight":' + m + ',"visibilitychange":' + false + '},"adBackup":{"iframeIndex":' + k +
                            ',"ALEX":"asynpcadshow","httpType":' + padssl + '}}';
                        b.postMessage(d, "*")
                    }
                }
            } else {
                for (var k = 0; k < iframeArray.length; k++) {
                    if (this == iframeArray[k]) {
                        var a = this.contentDocument.body.children[0];
                        var b = a.contentWindow;
                        var c = this;
                        var d = null;
                        var e = window.document.body.scrollTop || window.document.documentElement.scrollTop;
                        var f = window.innerHeight;
                        var g = c.offsetTop;
                        var h = c.getBoundingClientRect().bottom;
                        var i = c.getBoundingClientRect().top;
                        var j = c.height.replace(';px', '');
                        var l = j / 2;
                        var m = (f + l) - j;
                        var d = '{"adInfo":{"scrollTop":' + e + ',"viewHeight":' + f + ',"iframeOffSetTop":' + g + ',"iframeBottom":' + h + ',"iframeTop":' +
                            i + ',"iframeHalf":' + l + ',"controllerHeight":' + m + ',"visibilitychange":' + false + '},"adBackup":{"iframeIndex":' + k +
                            ',"ALEX":"pcadshow","httpType":' + padssl + '}}';
                        b.postMessage(d, "*")
                    }
                }
            }
        }
    }
} catch (err) {}
