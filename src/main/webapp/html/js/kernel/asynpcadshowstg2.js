var res = document.URL;
var docurl = encodeURIComponent(res);

var asynpchomeadclass = document.getElementsByClassName("asynpchomead");
var asynpchomeadjs = document.getElementById("asynpchomeadjs");
var asynpchomeadflag = document.getElementById("pchomead8044");

// js run one time
// if flag is null then run script else stop
if (asynpchomeadflag == null) {
        // set js one time flag
        asynpchomeadflag = document.createElement('div');
        asynpchomeadflag.id = "pchomead8044";
        asynpchomeadjs.appendChild(asynpchomeadflag);

        // console.log("jsflag create");
        // console.log(asynpchomeadclass.length);
        // console.log(asynpchomead.length);

        if (asynpchomead.length > 10) {

                alert("頞��𤾸誨��𠹺�𢠃�琜�峕�憭𡁜蘨�質票10���誨���!");

        } else {

                for (i = 0; i < asynpchomead.length; i++) {

                        if (i >= asynpchomeadclass.length) {
                                break;
                        }

                        asynpchomead[i].classObject = asynpchomeadclass[i];
                        asynpchomead[i].pad_width = asynpchomeadclass[i]
                                        .getAttribute("pad_width");
                        asynpchomead[i].pad_height = asynpchomeadclass[i]
                                        .getAttribute("pad_height");
                        asynpchomead[i].pad_customerId = asynpchomeadclass[i]
                                        .getAttribute("pad_customerId");
                        asynpchomead[i].pad_positionId = asynpchomeadclass[i]
                                        .getAttribute("pad_positionId");
                        asynpchomead[i].pad_precise = asynpchomeadclass[i]
                                        .getAttribute("pad_precise");
                        asynpchomead[i].pad_keyword = asynpchomeadclass[i]
                                        .getAttribute("pad_keyword");
                        asynpchomead[i].pad_page = asynpchomeadclass[i]
                                        .getAttribute("pad_page");
                        asynpchomead[i].pad_ssl = asynpchomeadclass[i]
                                        .getAttribute("pad_ssl");

                        //console.log(asynpchomead[i].pad_width);
                        //console.log(asynpchomead[i].pad_height);
                        //console.log(asynpchomead[i].pad_customerId);
                        //console.log(asynpchomead[i].pad_positionId);
                        //console.log(asynpchomead[i].pad_precise);
                        //console.log(asynpchomead[i].pad_keyword);
                        //console.log(asynpchomead[i].pad_page);
                        //console.log(asynpchomead[i].pad_ssl);

                        showad(asynpchomead[i]);
                        
                        
                        

                }// end of for
                //�鐤�㙈敶梢𨺗�綉��
                //adVideoController();
        }// end if ad num 5

} else {
        // debug
        // console.log('js run');
}

function showad(asynpchomeadObject) {

        // console.log(asynpchomeadObject.pad_width);
        // console.log(asynpchomeadObject.pad_height);

        var keywordValue = "";
        var pageValue = "";
        var pid = "";
        var ptype = "";
        var seway = "";
        var padssl = "";

        if (asynpchomeadObject.pad_precise != null) {
                seway = asynpchomeadObject.pad_precise;
        } else {

                seway = false;
        }

        if (asynpchomeadObject.pad_ssl != null) {
                padssl = asynpchomeadObject.pad_ssl;
        } else {

                padssl = true;
        }

        if (asynpchomeadObject.pad_positionId != null) {
                pid = asynpchomeadObject.pad_positionId.substring(0, 16);
                ptype = asynpchomeadObject.pad_positionId.substring(16, 17);

                if (ptype == "") {
                        ptype = "C";
                }

        }

        if (ptype == "S") {

                if (asynpchomeadObject.pad_keyword != null) {
                        keywordValue = asynpchomeadObject.pad_keyword;
                }

                if (asynpchomeadObject.pad_page != null) {
                        pageValue = asynpchomeadObject.pad_page;
                }

                if (keywordValue.length == 0) {

                        // test
                        // res="http://search.pchome.com.tw/search/?q=%E6%89%8B%E6%A9%9F%E6%AE%BC&ch=&ac="

                        if (res.indexOf("nicolee.pchome.com.tw") > 1) {

                                // http://nicolee.pchome.com.tw:8080/akbadm_git/adteststg.jsp?q=usb&page=1&precise=false;

                                var testurl = res;

                                var kis = testurl.indexOf("q=");

                                if (kis > 1) {

                                        var pis = testurl.indexOf("page=");
                                        var tis = testurl.indexOf("precise=");
                                        keywordValue = testurl.substring(kis + 2, pis - 1);

                                        if (pis < 1) {
                                                pageValue = 1;
                                        } else {
                                                pageValue = testurl.substring(pis + 5, tis - 1);
                                        }

                                        // alert(kis+","+tis+","+pis+","+keywordValue+","+pageValue);
                                }
                        }

                        if (res.indexOf("search.pchome.com.tw") > 1) {

                                // alert("search");

                                // http://search.pchome.com.tw/search/?q=%E4%B8%AD%E6%96%87&ch=&ac=

                                var testurl = res;

                                var kis = testurl.indexOf("q=");

                                if (kis > 1) {

                                        var pis = testurl.indexOf("ch=");
                                        // var tis=testurl.indexOf("precise=");
                                        keywordValue = testurl.substring(kis + 2, pis - 1);

                                        // alert(kis+","+pis+","+keywordValue);
                                }
                        }

                        if (res.indexOf("search.ruten.com.tw") > 1) {

                                // http://search.ruten.com.tw/search/s000.php?searchfrom=indexbar&k=ipad&t=0&p=4

                                var testurl = res;

                                var kis = testurl.indexOf("k=");

                                if (kis > 1) {

                                        var tis = testurl.indexOf("t=");
                                        var pis = testurl.indexOf("p=");

                                        // keywordValue=testurl.substring(kis+2,tis-1);
                                        keywordValue = document.getElementById("kwd").value;

                                        if (pis < 1) {
                                                pageValue = 1;
                                        } else {
                                                pageValue = testurl.substring(pis + 2, testurl.length);
                                        }

                                        // alert(kis+","+tis+","+pis+","+keywordValue+","+pageValue);
                                }
                        }

                }

        } else {

                keywordValue = "";
                pageValue = "";
                seway = "";

        }
        var fig = "";
        var adurl = "";
        if (padssl) {
                adurl += "http://kwstg1.pchome.com.tw/adshow2.html?pfbxCustomerInfoId=" + asynpchomeadObject.pad_customerId;
        } else {
                adurl += "https://kwstg1.pchome.com.tw/adshow2.html?pfbxCustomerInfoId=" + asynpchomeadObject.pad_customerId;
        }
        adurl += "&positionId=" + pid;
        adurl += "&padWidth=" + asynpchomeadObject.pad_width;
        adurl += "&padHeight=" + asynpchomeadObject.pad_height;
        adurl += "&keyword=" + keywordValue;
        adurl += "&page=" + pageValue;
        adurl += "&precise=" + seway;
        adurl += "&fig=" + fig;
        adurl += "&t=" + Math.floor(Math.random() * 1000 + 1);

        if (docurl.indexOf("kdcl") > 1 || docurl.indexOf("kwstg") > 1) {
                adurl += "&docurl=";
        } else {
                adurl += "&docurl=" + docurl;
        }

        var showadscript = "<scr" + "ipt type=text/javascript src=" + adurl
                        + "></scr" + "ipt>";

        // if(ptype=="S"){
        // search no iframe
        // document.write(showadscript);
        // }else{
        // document.write('<iframe class="akb_iframe" scrolling="no" frameborder="0"
        // marginwidth="0" marginheight="0" vspace="0" hspace="0"
        // id="pchome8044_ad_frame1" width="'+pad_width+'" height="'+pad_height+'"
        // allowtransparency="true" allowfullscreen="true"
        // src="javascript:\''+showadscript+'\'"></iframe>');

        // }
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
        asynpchomead[i].classObject.appendChild(ifrm);
        
}

window.onresize = function(event) {
    var iframeArrayData = document.getElementsByTagName("iframe");
    var iframeArray = [];
    for (var i = 0; i < iframeArrayData.length; i++) {
        if (iframeArrayData[i].id == "pchome8044_ad_frame1") {
            iframeArray.push(iframeArrayData[i]);
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
        var iframeHalf = iframeOriginal.height.replace(';px','') / 2;
        var controllerHeight = (viewHeight + iframeHalf) - iframeOriginal.height.replace(';px','');
        adInfo = '{"adInfo":{"scrollTop":' + scrollTop + ',"viewHeight":' + viewHeight + ',"iframeOffSetTop":' + iframeOffSetTop + ',"iframeBottom":' + iframeBottom + ',"iframeTop":' + iframeTop + ',"iframeHalf":' + iframeHalf + ',"controllerHeight":' + controllerHeight + ',"visibilitychange":' + false + '}}';
        iframeWin.postMessage(adInfo, "*");
    }
};


window.document.addEventListener('scroll', function() {
	 var iframeArrayData = document.getElementsByTagName("iframe");
	    var iframeArray = [];
	    for (var i = 0; i < iframeArrayData.length; i++) {
	        if (iframeArrayData[i].id == "pchome8044_ad_frame1") {
	            iframeArray.push(iframeArrayData[i]);
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
	        var iframeHalf = iframeOriginal.height.replace(';px','') / 2;
	        var controllerHeight = (viewHeight + iframeHalf) - iframeOriginal.height.replace(';px','');
	        adInfo = '{"adInfo":{"scrollTop":' + scrollTop + ',"viewHeight":' + viewHeight + ',"iframeOffSetTop":' + iframeOffSetTop + ',"iframeBottom":' + iframeBottom + ',"iframeTop":' + iframeTop + ',"iframeHalf":' + iframeHalf + ',"controllerHeight":' + controllerHeight + ',"visibilitychange":' + false + '}}';
	        iframeWin.postMessage(adInfo, "*");
	    }
}, false);




//�𧙗�賢�𨜓frame�喲����閮𦠜��
window.addEventListener("message", getMessage0, false);

function getMessage0(event) {
	try {
		if (event.data.adBackup != undefined && event.data.adBackup.iframeIndex != null	&& event.data.adBackup.ALEX == 'asynpcadshow') {
			var htmlContent = event.data.adBackup.htmlContent;
			var pcadshowList = document.getElementsByClassName("asynpchomead");
				/* ��閧��𤣰��� */
				if (htmlContent == 'blank') {
					var iframeObj = pcadshowList[event.data.adBackup.iframeIndex];
					iframeObj.height = 0;
					iframeObj.width = 0;
				}
				/* ��閧�鋆𨀣踎 */
				if (htmlContent != 'blank' && htmlContent != undefined) {
					var iframeObj = pcadshowList[event.data.adBackup.iframeIndex];
					if(iframeObj == undefined){
						pcadshowList = document.getElementsByClassName("akb_iframe");
						iframeObj = pcadshowList[event.data.adBackup.iframeIndex].parentElement;
					}
					iframeObj.height = 0;
					iframeObj.width = 0;
					iframeObj.setAttribute("style","display:none");
					
					if(iframeObj.nextElementSibling.nextElementSibling == null || iframeObj.nextElementSibling.nextElementSibling.className != 'ad_backup_pchome'){
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
								content.setAttribute(attrib.name, attrib.value);
							}
							content.text = text;
							appendDiv.appendChild(content,appendDiv.firstChild);
						}
						appendDom.insertBefore(appendDiv,iframeObj.nextElementSibling.nextElementSibling);	
					}
				}
		}
	} catch (err) {
		console.log(err);
	}
}

/**/
try{
	var iframeArrayData = document.getElementsByTagName("iframe");
	var iframeArray = [];
    for (var i = 0; i < iframeArrayData.length; i++) {
        if (iframeArrayData[i].id == "pchome8044_ad_frame1") {
            iframeArray.push(iframeArrayData[i]);
        }
    }
	
	var pcadshowList = document.getElementsByClassName("asynpchomead");
	for (var i = 0; i < iframeArray.length; i++) {
		iframeArray[i].onload = function() {
			if(this.parentElement.className == 'asynpchomead'){
				for (var k = 0; k < iframeArray.length; k++) {
					if(this == iframeArray[k]){
						var frame = this.contentDocument.body.children[0];
						var iframeWin = frame.contentWindow;
						var iframeOriginal = this;
						var scrollTop = window.document.body.scrollTop || window.document.documentElement.scrollTop;
						var viewHeight = window.innerHeight;
						var iframeOffSetTop = iframeOriginal.offsetTop;
						var iframeBottom = iframeOriginal.getBoundingClientRect().bottom;
						var iframeTop = iframeOriginal.getBoundingClientRect().top;
						var iframeHalf = iframeOriginal.height.replace(';px','') / 2;
						var controllerHeight = (viewHeight + iframeHalf) - iframeOriginal.height.replace(';px','');
						var adInfo = '{"adBackup":{"iframeIndex":' + k +',"ALEX":"asynpcadshow"},"adInfo":{"scrollTop":' + scrollTop+',"viewHeight":' + viewHeight + ',"iframeOffSetTop":' + iframeOffSetTop + ',"iframeBottom":' + iframeBottom + ',"iframeTop":' + iframeTop + ',"iframeHalf":' + iframeHalf + ',"controllerHeight":' + controllerHeight + ',"visibilitychange":' + false +'}}';
						iframeWin.postMessage(adInfo, "*");
					}
				}
			}
			else{
				for (var k = 0; k < iframeArray.length; k++) {
					if(this == iframeArray[k]){
						var frame = this.contentDocument.body.children[0];
						var iframeWin = frame.contentWindow;
						var iframeOriginal = this;
						var scrollTop = window.document.body.scrollTop || window.document.documentElement.scrollTop;
						var viewHeight = window.innerHeight;
						var iframeOffSetTop = iframeOriginal.offsetTop;
						var iframeBottom = iframeOriginal.getBoundingClientRect().bottom;
						var iframeTop = iframeOriginal.getBoundingClientRect().top;
						var iframeHalf = iframeOriginal.height.replace(';px','') / 2;
						var controllerHeight = (viewHeight + iframeHalf) - iframeOriginal.height.replace(';px','');
						var adInfo = '{"adBackup":{"iframeIndex":' + k +',"ALEX":"pcadshow"},"adInfo":{"scrollTop":' + scrollTop+',"viewHeight":' + viewHeight + ',"iframeOffSetTop":' + iframeOffSetTop + ',"iframeBottom":' + iframeBottom + ',"iframeTop":' + iframeTop + ',"iframeHalf":' + iframeHalf + ',"controllerHeight":' + controllerHeight + ',"visibilitychange":' + false +'}}';
						iframeWin.postMessage(adInfo, "*");
					}
				}
			}
		}
	}
}catch(err){
//	console.log(err);
}
