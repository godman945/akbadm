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
} if (typeof pad_ssl != 'undefined') {
    padssl = pad_ssl
} else {
    padssl = true
} if (typeof pad_positionId != 'undefined') {
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


console.log("DDDDD");
/*stg adurl與prd有差異*/
var adurl = "http://kwstg1.pchome.com.tw/adshow2.html?pfbxCustomerInfoId=" + pad_customerId;

adurl += "&positionId=" + pid;
adurl += "&padWidth=" + pad_width;
adurl += "&padHeight=" + pad_height;
adurl += "&keyword=" + keywordValue;
adurl += "&page=" + pageValue;
adurl += "&precise=" + seway;
adurl += "&fig=" + "";
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
        document.write('<iframe class="akb_iframe" scrolling="no" frameborder="0" marginwidth="0" marginheight="0" vspace="0" hspace="0" id="pchome8044_ad_frame1" width="' + pad_width + '" height="' + pad_height + '" allowtransparency="true" allowfullscreen="true" src="javascript:\'' + showadscript + '\'"></iframe>');
    }
} else {
    alert("超過廣告上限，最多只能貼10則廣告!");
}


window.onresize = function(event) {
	try{
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
	        var iframeOriginalHeight = iframeOriginal.height.replace(';px','');
	        var iframeHalf = iframeOriginalHeight / 2;
	        var controllerHeight = (viewHeight + iframeHalf) - iframeOriginalHeight;
	        adInfo = '{"adInfo":{"scrollTop":' + scrollTop + ',"viewHeight":' + viewHeight + ',"iframeOffSetTop":' + iframeOffSetTop + ',"iframeBottom":' + iframeBottom + ',"iframeTop":' + iframeTop + ',"iframeHalf":' + iframeHalf + ',"controllerHeight":' + controllerHeight + ',"visibilitychange":' + false + '}}';
	        iframeWin.postMessage(adInfo, "*");
	    }
	}catch(err){
//		console.log(err)
	}
};

window.document.addEventListener('scroll', function() {
	try{
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
		       var iframeOriginalHeight = iframeOriginal.height.replace(';px','');
		       var iframeHalf = iframeOriginalHeight / 2;
		       var controllerHeight = (viewHeight + iframeHalf) - iframeOriginalHeight;
		       adInfo = '{"adInfo":{"scrollTop":' + scrollTop + ',"viewHeight":' + viewHeight + ',"iframeOffSetTop":' + iframeOffSetTop + ',"iframeBottom":' + iframeBottom + ',"iframeTop":' + iframeTop + ',"iframeHalf":' + iframeHalf + ',"controllerHeight":' + controllerHeight + ',"visibilitychange":' + false + '}}';
		       iframeWin.postMessage(adInfo, "*");
		    }
	}catch(err){
//		console.log(err)
	}
}, false);


window.document.addEventListener('visibilitychange', function() {
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
	       var iframeOriginalHeight = iframeOriginal.height.replace(';px','');
	       var iframeHalf = iframeOriginalHeight / 2;
	       var controllerHeight = (viewHeight + iframeHalf) - iframeOriginalHeight;
           var visibilitychange = false;
           if (document.hidden) {
               visibilitychange = true;
           }
           adInfo = '{"adInfo":{"scrollTop":' + scrollTop + ',"viewHeight":' + viewHeight + ',"iframeOffSetTop":' + iframeOffSetTop + ',"iframeBottom":' + iframeBottom + ',"iframeTop":' + iframeTop + ',"iframeHalf":' + iframeHalf + ',"controllerHeight":' + controllerHeight + ',"visibilitychange":' + visibilitychange + '}}';
           iframeWin.postMessage(adInfo, "*");
	    }
}, false);



window.addEventListener("message", getMessage0, false);

function getMessage0(event) {
	try {
		console.log("---------");
		
		if (event.data.adBackup != undefined &&  event.data.adBackup.iframeIndex != null && event.data.adBackup.ALEX =='pcadshow') {
			var htmlContent = event.data.adBackup.htmlContent;
			if(htmlContent != null){
				var pcadshowList = document.getElementsByClassName("akb_iframe");
				/*處理收合*/
				if(htmlContent == 'blank'){
					var iframeObj = pcadshowList[event.data.adBackup.iframeIndex];
					
					console.log(iframeObj);
					
					iframeObj.height = 0;
					iframeObj.width = 0;
				}
				/*處理補板*/
				
				if (htmlContent != 'blank' && htmlContent != undefined && htmlContent.indexOf('document.write') < 0) {
					var iframeObj = pcadshowList[event.data.adBackup.iframeIndex];
					iframeObj.height = 0;
					iframeObj.width = 0;
					var appendDom = iframeObj.parentElement;
					if(iframeObj.nextElementSibling == null || iframeObj.nextElementSibling.className != 'ad_backup_pchome'){
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
								var value = attrib.value;
								content.setAttribute(attrib.name, attrib.value);
							}
							content.text = text;
							appendDiv.appendChild(content,appendDiv.firstChild);
						}
						if(iframeObj.nextElementSibling == null){
							appendDom.appendChild(appendDiv,iframeObj.parentElement);	
						}else{
							appendDom.insertBefore(appendDiv,iframeObj.nextElementSibling);	
						}
						
					}
				}else if(htmlContent != 'blank' && htmlContent.indexOf('document.write') >= 0 && htmlContent != undefined){
					console.log(htmlContent);
//					var iframeObj = pcadshowList[event.data.adBackup.iframeIndex];
////					iframeObj.height = 0;
////					iframeObj.width = 0;
//					var appendDom = iframeObj.parentElement;					
//					var appendDiv = document.createElement('iframe');
//					appendDiv.className = 'ad_backup_pchome';
//					appendDiv.src = "http://kwstg1.pchome.com.tw/adshow2.html?pfbxCustomerInfoId=PFBC20150519001&positionId=PFBP201512170007&sampleId=us_201805030002&tproId=c_x05_po_tpro_0034&format=0&page=1&padHeight=80&padWidth=250&keyword=&fig=ae1ab9f5db00d24b4b6f3cf9418cb85a&ref=Nzc5ObXXwdOHknzWtdLE1sHKe9Owy7zQspGw0rqRwdp8xLHQfMSx17LWwdbByn6Rt9a9%0D%0A";
//					appendDom.append(appendDiv);
//					console.log(appendDiv);
//					console.log(appendDom);
				}
			}
		}
	}catch (err) {
//			console.log(err);	
	}
}


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
						var iframe = this.contentDocument.body.children[0];
						var iframeWin = iframe.contentWindow;
			            var iframeOriginal = this;
			            var adInfo = null;
			            var scrollTop = window.document.body.scrollTop || window.document.documentElement.scrollTop;
			            var viewHeight = window.innerHeight;
			            var iframeOffSetTop = iframeOriginal.offsetTop;
			            var iframeBottom = iframeOriginal.getBoundingClientRect().bottom;
			            var iframeTop = iframeOriginal.getBoundingClientRect().top;
			 	       	var iframeOriginalHeight = iframeOriginal.height.replace(';px','');
			 	       	var iframeHalf = iframeOriginalHeight / 2;
			 	       	var controllerHeight = (viewHeight + iframeHalf) - iframeOriginalHeight;
			            var adInfo = '{"adInfo":{"scrollTop":' + scrollTop + ',"viewHeight":' + viewHeight + ',"iframeOffSetTop":' + iframeOffSetTop + ',"iframeBottom":' + iframeBottom + ',"iframeTop":' + iframeTop + ',"iframeHalf":' + iframeHalf + ',"controllerHeight":' + controllerHeight + ',"visibilitychange":' + false + '},"adBackup":{"iframeIndex":' + k +',"ALEX":"asynpcadshow"}}';
						iframeWin.postMessage(adInfo, "*");
					}
				}
			}
			else{
				for (var k = 0; k < iframeArray.length; k++) {
					if(this == iframeArray[k]){
						var iframe = this.contentDocument.body.children[0];
						var iframeWin = iframe.contentWindow;
			            var iframeOriginal = this;
			            var adInfo = null;
			            var scrollTop = window.document.body.scrollTop || window.document.documentElement.scrollTop;
			            var viewHeight = window.innerHeight;
			            var iframeOffSetTop = iframeOriginal.offsetTop;
			            var iframeBottom = iframeOriginal.getBoundingClientRect().bottom;
			            var iframeTop = iframeOriginal.getBoundingClientRect().top;
			 	       	var iframeOriginalHeight = iframeOriginal.height.replace(';px','');
			 	       	var iframeHalf = iframeOriginalHeight / 2;
			 	       	var controllerHeight = (viewHeight + iframeHalf) - iframeOriginalHeight;
			 	       	var adInfo = '{"adInfo":{"scrollTop":' + scrollTop + ',"viewHeight":' + viewHeight + ',"iframeOffSetTop":' + iframeOffSetTop + ',"iframeBottom":' + iframeBottom + ',"iframeTop":' + iframeTop + ',"iframeHalf":' + iframeHalf + ',"controllerHeight":' + controllerHeight + ',"visibilitychange":' + false + '},"adBackup":{"iframeIndex":' + k +',"ALEX":"pcadshow"}}';
						iframeWin.postMessage(adInfo, "*");
					}
				}
			}
		}
	}
}catch(err){
//	console.log(err);
}
