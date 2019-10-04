var res = document.URL;
var docurl = encodeURIComponent(res);
var screen_x = screen.availWidth;
var screen_y = screen.availHeight;
var asynpchomeadclass = document.getElementsByClassName("asynpchomead");
var asynpchomeadjs = document.getElementById("asynpchomeadjs");
var asynpchomeadflag = document.getElementById("pchomead8044");



//
//if('https://greatlife.idv.tw/' == window.location.href){
//	console.log(window.location.href);
//	console.log(asynpchomeadclass);
//	console.log(document.getElementsByClassName("textwidget")[0]);
//	document.getElementsByClassName("textwidget").appendChild('<script class="alex" id="asynpchomeadjs" async src="https://kdpic.pchome.com.tw/img/js/xasynpcadshow.js"></script><ins class="asynpchomead"	 pad_width=180;	pad_height=150;	pad_customerId="PFBC20150519001";pad_positionId="PFBP201605050001C";></ins><script>	(asynpchomead = window.asynpchomead || []).push({});</script>');
//	
//}


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

function showad(asynpchomeadObject) {
    var keywordValue = "";
    var pageValue = "";
    var pid = "";
    var ptype = "";
    var seway = "";
    var padssl = "";
    if (asynpchomeadObject.pad_precise != null) {
        seway = asynpchomeadObject.pad_precise
    } else {
        seway = false
    }
    if (asynpchomeadObject.pad_ssl != null) {
        padssl = asynpchomeadObject.pad_ssl
    } else {
        padssl = true
    }
    if (asynpchomeadObject.pad_positionId != null) {
        pid = asynpchomeadObject.pad_positionId.substring(0, 16);
        ptype = asynpchomeadObject.pad_positionId.substring(16, 17);
        if (ptype == "") {
            ptype = "C"
        }
    }
    if (ptype == "S") {
        if (asynpchomeadObject.pad_keyword != null) {
            keywordValue = asynpchomeadObject.pad_keyword
        }
        if (asynpchomeadObject.pad_page != null) {
            pageValue = asynpchomeadObject.pad_page
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
                    keywordValue = document.getElementById("kwd").value;
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
    var fig = "";
    var adurl = "";
    if (padssl) {
        adurl += document.location.protocol+"//kdcl.pchome.com.tw/adshow2.html?pfbxCustomerInfoId=" + asynpchomeadObject.pad_customerId
    } else {
        adurl += document.location.protocol+"//kdcl.pchome.com.tw/adshow2.html?pfbxCustomerInfoId=" + asynpchomeadObject.pad_customerId
    }
    
    console.log(asynpchomeadObject);
    console.log(asynpchomeadObject.pad_width);
    
    adurl += "&positionId=" + pid;
    adurl += "&padWidth=" + asynpchomeadObject.pad_width.replace(';', '');
    adurl += "&padHeight=" + asynpchomeadObject.pad_height.replace(';', '');
    adurl += "&keyword=" + keywordValue.replace(';', '');
    adurl += "&page=" + pageValue.replace(';', '');
    adurl += "&precise=" + seway.replace(';', '');
    adurl += "&fig=" + fig.replace(';', '');
    adurl += "&screenX=" + screen_x;
    adurl += "&screenY=" + screen_y;
    adurl += "&t=" + Math.floor(Math.random() * 1000 + 1);
    if (docurl.indexOf("kdcl") > 1 || docurl.indexOf("kwstg") > 1) {
        adurl += "&docurl="
    } else {
        adurl += "&docurl=" + docurl
    }
    var showadscript = "<scr" + "ipt type=text/javascript src=" + adurl + "></scr" + "ipt>";
    var ifrm = document.createElement("iframe");
    ifrm.setAttribute("class", "akb_iframe");
    ifrm.setAttribute("scrolling", "no");
    ifrm.setAttribute("frameborder", "0");
    ifrm.setAttribute("marginwidth", "0");
    ifrm.setAttribute("marginheight", "0");
    ifrm.setAttribute("vspace", "0");
    ifrm.setAttribute("hspace", "0");
    ifrm.setAttribute("id", "pchome8044_ad_frame1");
    ifrm.setAttribute("width", asynpchomeadObject.pad_width + "px");
    ifrm.setAttribute("height", asynpchomeadObject.pad_height + "px");
    ifrm.setAttribute("allowtransparency", "true");
    ifrm.setAttribute("allowfullscreen", "true");
    ifrm.setAttribute("src", "javascript:\'" + showadscript + "\'");
    asynpchomead[i].classObject.appendChild(ifrm)
}
window.onresize = function(event) {
    var iframeArrayData = document.getElementsByTagName("iframe");
    var iframeArray = [];
    for (var i = 0; i < iframeArrayData.length; i++) {
        if (iframeArrayData[i].id == "pchome8044_ad_frame1") {
            iframeArray.push(iframeArrayData[i])
        }
    }
    for (var i = 0; i < iframeArray.length; i++) {
        var iframe = iframeArray[i].contentDocument.body.children[0];
        var iframeWin = iframe.contentWindow;
        var iframeOriginal = iframeArray[i];
        var scrollTop = window.document.body.scrollTop || window.document.documentElement.scrollTop;
        var viewHeight = window.innerHeight;
        var iframeOffSetTop = iframeOriginal.offsetTop;
        var iframeBottom = iframeOriginal.getBoundingClientRect().bottom;
        var iframeTop = iframeOriginal.getBoundingClientRect().top;
        var iframeHalf = iframeOriginal.height.replace(';px', '') / 2;
        var controllerHeight = (viewHeight + iframeHalf) - iframeOriginal.height.replace(';px', '');
        adInfo = '{"adInfo":{"scrollTop":' + scrollTop + ',"viewHeight":' + viewHeight + ',"iframeOffSetTop":' + iframeOffSetTop + ',"iframeBottom":' +
            iframeBottom + ',"iframeTop":' + iframeTop + ',"iframeHalf":' + iframeHalf + ',"controllerHeight":' + controllerHeight + ',"visibilitychange":' +
            false + '}}';
        iframeWin.postMessage(adInfo, "*")
    }
};
window.document.addEventListener('scroll', function() {
    var iframeArrayData = document.getElementsByTagName("iframe");
    var iframeArray = [];
    for (var i = 0; i < iframeArrayData.length; i++) {
        if (iframeArrayData[i].id == "pchome8044_ad_frame1") {
            iframeArray.push(iframeArrayData[i])
        }
    }
    for (var i = 0; i < iframeArray.length; i++) {
        var iframe = iframeArray[i].contentDocument.body.children[0];
        var iframeWin = iframe.contentWindow;
        var iframeOriginal = iframeArray[i];
        var scrollTop = window.document.body.scrollTop || window.document.documentElement.scrollTop;
        var viewHeight = window.innerHeight;
        var iframeOffSetTop = iframeOriginal.offsetTop;
        var iframeBottom = iframeOriginal.getBoundingClientRect().bottom;
        var iframeTop = iframeOriginal.getBoundingClientRect().top;
        var iframeHalf = iframeOriginal.height.replace(';px', '') / 2;
        var controllerHeight = (viewHeight + iframeHalf) - iframeOriginal.height.replace(';px', '');
        adInfo = '{"adInfo":{"scrollTop":' + scrollTop + ',"viewHeight":' + viewHeight + ',"iframeOffSetTop":' + iframeOffSetTop + ',"iframeBottom":' +
            iframeBottom + ',"iframeTop":' + iframeTop + ',"iframeHalf":' + iframeHalf + ',"controllerHeight":' + controllerHeight +
            ',"visibilitychange":' + false + '}}';
        iframeWin.postMessage(adInfo, "*")
    }
}, false);
window.addEventListener("message", getMessage0, false);

function getMessage0(event) {
    try {
        if (event.data.adBackup != undefined && event.data.adBackup.iframeIndex != null && event.data.adBackup.ALEX == 'asynpcadshow') {
            var htmlContent = event.data.adBackup.htmlContent;
            var pcadshowList = document.getElementsByClassName("asynpchomead");
            if (htmlContent == 'blank') {
                var iframeObj = pcadshowList[event.data.adBackup.iframeIndex];
                iframeObj.height = 0;
                iframeObj.width = 0
            }
            if (htmlContent != 'blank' && htmlContent != undefined) {
                var iframeObj = pcadshowList[event.data.adBackup.iframeIndex];
                if (iframeObj == undefined) {
                    pcadshowList = document.getElementsByClassName("akb_iframe");
                    iframeObj = pcadshowList[event.data.adBackup.iframeIndex].parentElement
                }
                iframeObj.height = 0;
                iframeObj.width = 0;
                iframeObj.setAttribute("style", "display:none");
                if (iframeObj.nextElementSibling.nextElementSibling == null || iframeObj.nextElementSibling.nextElementSibling.className != 'ad_backup_pchome') {
                    var appendDom = iframeObj.parentElement;
                    var script = document.createElement('div');
                    var appendDiv = document.createElement('div');
                    appendDiv.className = 'ad_backup_pchome';
                    script.innerHTML = htmlContent;
                    var elements = script.getElementsByTagName("*");
                    for (var j = 0; j < elements.length; j++) {
                        var tagName = elements[j].tagName;
                        var attributes = elements[j].attributes;
                        var text = elements[j].innerHTML;
                        var content = document.createElement(tagName);
                        for (var k = 0; k < attributes.length; k++) {
                            var attrib = attributes[k];
                            var name = attrib.name;
                            var name = attrib.value;
                            content.setAttribute(attrib.name, attrib.value)
                        }
                        content.text = text;
                        appendDiv.appendChild(content, appendDiv.firstChild)
                    }
                    appendDom.insertBefore(appendDiv, iframeObj.nextElementSibling.nextElementSibling)
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
                        var frame = this.contentDocument.body.children[0];
                        var iframeWin = frame.contentWindow;
                        var iframeOriginal = this;
                        var scrollTop = window.document.body.scrollTop || window.document.documentElement.scrollTop;
                        var viewHeight = window.innerHeight;
                        var iframeOffSetTop = iframeOriginal.offsetTop;
                        var iframeBottom = iframeOriginal.getBoundingClientRect().bottom;
                        var iframeTop = iframeOriginal.getBoundingClientRect().top;
                        var iframeHalf = iframeOriginal.height.replace(';px', '') / 2;
                        var controllerHeight = (viewHeight + iframeHalf) - iframeOriginal.height.replace(';px', '');
                        var adInfo = '{"adBackup":{"iframeIndex":' + k + ',"ALEX":"asynpcadshow"},"adInfo":{"scrollTop":' + scrollTop + ',"viewHeight":' +
                            viewHeight + ',"iframeOffSetTop":' + iframeOffSetTop + ',"iframeBottom":' + iframeBottom + ',"iframeTop":' + iframeTop +
                            ',"iframeHalf":' + iframeHalf + ',"controllerHeight":' + controllerHeight + ',"visibilitychange":' + false + '}}';
                        iframeWin.postMessage(adInfo, "*")
                    }
                }
            } else {
                for (var k = 0; k < iframeArray.length; k++) {
                    if (this == iframeArray[k]) {
                        var frame = this.contentDocument.body.children[0];
                        var iframeWin = frame.contentWindow;
                        var iframeOriginal = this;
                        var scrollTop = window.document.body.scrollTop || window.document.documentElement.scrollTop;
                        var viewHeight = window.innerHeight;
                        var iframeOffSetTop = iframeOriginal.offsetTop;
                        var iframeBottom = iframeOriginal.getBoundingClientRect().bottom;
                        var iframeTop = iframeOriginal.getBoundingClientRect().top;
                        var iframeHalf = iframeOriginal.height.replace(';px', '') / 2;
                        var controllerHeight = (viewHeight + iframeHalf) - iframeOriginal.height.replace(';px', '');
                        var adInfo = '{"adBackup":{"iframeIndex":' + k + ',"ALEX":"pcadshow"},"adInfo":{"scrollTop":' + scrollTop + ',"viewHeight":' +
                            viewHeight + ',"iframeOffSetTop":' + iframeOffSetTop + ',"iframeBottom":' + iframeBottom + ',"iframeTop":' + iframeTop +
                            ',"iframeHalf":' + iframeHalf + ',"controllerHeight":' + controllerHeight + ',"visibilitychange":' + false + '}}';
                        iframeWin.postMessage(adInfo, "*")
                    }
                }
            }
        }
    }
} catch (err) {}