(function(name, context, definition) {
    'use strict';
    if (typeof window.define === 'function' && window.define.amd) {
        window.define(definition)
    } else if (typeof module !== 'undefined' && module.exports) {
        module.exports = definition();
    } else if (context.exports) {
        context.exports = definition();
    } else {
        context[name] = definition();
    }
})('PCHOMEFingerprint', this, function() {
    'use strict';
    var PCHOMEFingerprint = function(options) {
        if (!(this instanceof PCHOMEFingerprint)) {
            return new PCHOMEFingerprint(options);
        }
        var defaultOptions = {
            swfContainerId: 'PCHOMEFingerprint',
            swfPath: 'flash/compiled/FontList.swf',
            detectScreenOrientation: true,
            sortPluginsFor: [/palemoon/i],
            userDefinedFonts: []
        };
        this.options = this.extend(options, defaultOptions);
        this.nativeForEach = Array.prototype.forEach;
        this.nativeMap = Array.prototype.map;
    };
    PCHOMEFingerprint.prototype = {
        extend: function(source, target) {
            if (source == null) {
                return target;
            }
            for (var k in source) {
                if (source[k] != null && target[k] !== source[k]) {
                    target[k] = source[k];
                }
            }
            return target;
        },
        getCanvasFp: function() {
            var result = [];
            var canvas = document.createElement('canvas');
            canvas.width = 2000;
            canvas.height = 200;
            canvas.style.display = 'inline';
            var ctx = canvas.getContext('2d');
            ctx.rect(0, 0, 10, 10);
            ctx.rect(2, 2, 6, 6);
            result.push('canvas winding:' + ((ctx.isPointInPath(5, 5, 'evenodd') === false) ? 'yes' : 'no'));
            ctx.textBaseline = 'alphabetic';
            ctx.fillStyle = '#f60';
            ctx.fillRect(125, 1, 62, 20);
            ctx.fillStyle = '#069';
            if (this.options.dontUseFakeFontInCanvas) {
                ctx.font = '11pt Arial'
            } else {
                ctx.font = '11pt no-real-font-123'
            }
            ctx.fillText('Cwm fjordbank glyphs vext quiz, ??', 2, 15);
            ctx.fillStyle = 'rgba(102, 204, 0, 0.2)';
            ctx.font = '18pt Arial';
            ctx.fillText('Cwm fjordbank glyphs vext quiz, ??', 4, 45);
            ctx.globalCompositeOperation = 'multiply';
            ctx.fillStyle = 'rgb(255,0,255)';
            ctx.beginPath();
            ctx.arc(50, 50, 50, 0, Math.PI * 2, true);
            ctx.closePath();
            ctx.fill();
            ctx.fillStyle = 'rgb(0,255,255)';
            ctx.beginPath();
            ctx.arc(100, 50, 50, 0, Math.PI * 2, true);
            ctx.closePath();
            ctx.fill();
            ctx.fillStyle = 'rgb(255,255,0)';
            ctx.beginPath();
            ctx.arc(75, 100, 50, 0, Math.PI * 2, true);
            ctx.closePath();
            ctx.fill();
            ctx.fillStyle = 'rgb(255,0,255)';
            ctx.arc(75, 75, 75, 0, Math.PI * 2, true);
            ctx.arc(75, 75, 25, 0, Math.PI * 2, true);
            ctx.fill('evenodd');
            if (canvas.toDataURL) {
                result.push('canvas fp:' + canvas.toDataURL())
            }
            return result.join('~')
        },
        x64Add: function(m, n) {
            m = [m[0] >>> 16, m[0] & 0xffff, m[1] >>> 16, m[1] & 0xffff];
            n = [n[0] >>> 16, n[0] & 0xffff, n[1] >>> 16, n[1] & 0xffff];
            var o = [0, 0, 0, 0];
            o[3] += m[3] + n[3];
            o[2] += o[3] >>> 16;
            o[3] &= 0xffff;
            o[2] += m[2] + n[2];
            o[1] += o[2] >>> 16;
            o[2] &= 0xffff;
            o[1] += m[1] + n[1];
            o[0] += o[1] >>> 16;
            o[1] &= 0xffff;
            o[0] += m[0] + n[0];
            o[0] &= 0xffff;
            return [(o[0] << 16) | o[1], (o[2] << 16) | o[3]]
        },
        x64Multiply: function(m, n) {
            m = [m[0] >>> 16, m[0] & 0xffff, m[1] >>> 16, m[1] & 0xffff];
            n = [n[0] >>> 16, n[0] & 0xffff, n[1] >>> 16, n[1] & 0xffff];
            var o = [0, 0, 0, 0];
            o[3] += m[3] * n[3];
            o[2] += o[3] >>> 16;
            o[3] &= 0xffff;
            o[2] += m[2] * n[3];
            o[1] += o[2] >>> 16;
            o[2] &= 0xffff;
            o[2] += m[3] * n[2];
            o[1] += o[2] >>> 16;
            o[2] &= 0xffff;
            o[1] += m[1] * n[3];
            o[0] += o[1] >>> 16;
            o[1] &= 0xffff;
            o[1] += m[2] * n[2];
            o[0] += o[1] >>> 16;
            o[1] &= 0xffff;
            o[1] += m[3] * n[1];
            o[0] += o[1] >>> 16;
            o[1] &= 0xffff;
            o[0] += (m[0] * n[3]) + (m[1] * n[2]) + (m[2] * n[1]) + (m[3] * n[0]);
            o[0] &= 0xffff;
            return [(o[0] << 16) | o[1], (o[2] << 16) | o[3]]
        },
        x64Rotl: function(m, n) {
            n %= 64;
            if (n === 32) {
                return [m[1], m[0]]
            } else if (n < 32) {
                return [(m[0] << n) | (m[1] >>> (32 - n)), (m[1] << n) | (m[0] >>> (32 - n))]
            } else {
                n -= 32;
                return [(m[1] << n) | (m[0] >>> (32 - n)), (m[0] << n) | (m[1] >>> (32 - n))]
            }
        },
        x64LeftShift: function(m, n) {
            n %= 64;
            if (n === 0) {
                return m
            } else if (n < 32) {
                return [(m[0] << n) | (m[1] >>> (32 - n)), m[1] << n]
            } else {
                return [m[1] << (n - 32), 0]
            }
        },
        x64Xor: function(m, n) {
            return [m[0] ^ n[0], m[1] ^ n[1]]
        },
        x64Fmix: function(h) {
            h = this.x64Xor(h, [0, h[0] >>> 1]);
            h = this.x64Multiply(h, [0xff51afd7, 0xed558ccd]);
            h = this.x64Xor(h, [0, h[0] >>> 1]);
            h = this.x64Multiply(h, [0xc4ceb9fe, 0x1a85ec53]);
            h = this.x64Xor(h, [0, h[0] >>> 1]);
            return h
        },
        x64hash128: function(key, seed) {
            key = key || '';
            seed = seed || 0;
            var remainder = key.length % 16;
            var bytes = key.length - remainder;
            var h1 = [0, seed];
            var h2 = [0, seed];
            var k1 = [0, 0];
            var k2 = [0, 0];
            var c1 = [0x87c37b91, 0x114253d5];
            var c2 = [0x4cf5ad43, 0x2745937f];
            for (var i = 0; i < bytes; i = i + 16) {
                k1 = [((key.charCodeAt(i + 4) & 0xff)) | ((key.charCodeAt(i + 5) & 0xff) << 8) | ((key.charCodeAt(i + 6) & 0xff) << 16) | ((key.charCodeAt(i + 7) & 0xff) << 24), ((key.charCodeAt(i) & 0xff)) | ((key.charCodeAt(i + 1) & 0xff) << 8) | ((key.charCodeAt(i + 2) & 0xff) << 16) | ((key.charCodeAt(i + 3) & 0xff) << 24)];
                k2 = [((key.charCodeAt(i + 12) & 0xff)) | ((key.charCodeAt(i + 13) & 0xff) << 8) | ((key.charCodeAt(i + 14) & 0xff) << 16) | ((key.charCodeAt(i + 15) & 0xff) << 24), ((key.charCodeAt(i + 8) & 0xff)) | ((key.charCodeAt(i + 9) & 0xff) << 8) | ((key.charCodeAt(i + 10) & 0xff) << 16) | ((key.charCodeAt(i + 11) & 0xff) << 24)];
                k1 = this.x64Multiply(k1, c1);
                k1 = this.x64Rotl(k1, 31);
                k1 = this.x64Multiply(k1, c2);
                h1 = this.x64Xor(h1, k1);
                h1 = this.x64Rotl(h1, 27);
                h1 = this.x64Add(h1, h2);
                h1 = this.x64Add(this.x64Multiply(h1, [0, 5]), [0, 0x52dce729]);
                k2 = this.x64Multiply(k2, c2);
                k2 = this.x64Rotl(k2, 33);
                k2 = this.x64Multiply(k2, c1);
                h2 = this.x64Xor(h2, k2);
                h2 = this.x64Rotl(h2, 31);
                h2 = this.x64Add(h2, h1);
                h2 = this.x64Add(this.x64Multiply(h2, [0, 5]), [0, 0x38495ab5])
            };
            k1 = [0, 0];
            k2 = [0, 0];
            switch (remainder) {
                case 15:
                    k2 = this.x64Xor(k2, this.x64LeftShift([0, key.charCodeAt(i + 14)], 48));
                case 14:
                    k2 = this.x64Xor(k2, this.x64LeftShift([0, key.charCodeAt(i + 13)], 40));
                case 13:
                    k2 = this.x64Xor(k2, this.x64LeftShift([0, key.charCodeAt(i + 12)], 32));
                case 12:
                    k2 = this.x64Xor(k2, this.x64LeftShift([0, key.charCodeAt(i + 11)], 24));
                case 11:
                    k2 = this.x64Xor(k2, this.x64LeftShift([0, key.charCodeAt(i + 10)], 16));
                case 10:
                    k2 = this.x64Xor(k2, this.x64LeftShift([0, key.charCodeAt(i + 9)], 8));
                case 9:
                    k2 = this.x64Xor(k2, [0, key.charCodeAt(i + 8)]);
                    k2 = this.x64Multiply(k2, c2);
                    k2 = this.x64Rotl(k2, 33);
                    k2 = this.x64Multiply(k2, c1);
                    h2 = this.x64Xor(h2, k2);
                case 8:
                    k1 = this.x64Xor(k1, this.x64LeftShift([0, key.charCodeAt(i + 7)], 56));
                case 7:
                    k1 = this.x64Xor(k1, this.x64LeftShift([0, key.charCodeAt(i + 6)], 48));
                case 6:
                    k1 = this.x64Xor(k1, this.x64LeftShift([0, key.charCodeAt(i + 5)], 40));
                case 5:
                    k1 = this.x64Xor(k1, this.x64LeftShift([0, key.charCodeAt(i + 4)], 32));
                case 4:
                    k1 = this.x64Xor(k1, this.x64LeftShift([0, key.charCodeAt(i + 3)], 24));
                case 3:
                    k1 = this.x64Xor(k1, this.x64LeftShift([0, key.charCodeAt(i + 2)], 16));
                case 2:
                    k1 = this.x64Xor(k1, this.x64LeftShift([0, key.charCodeAt(i + 1)], 8));
                case 1:
                    k1 = this.x64Xor(k1, [0, key.charCodeAt(i)]);
                    k1 = this.x64Multiply(k1, c1);
                    k1 = this.x64Rotl(k1, 31);
                    k1 = this.x64Multiply(k1, c2);
                    h1 = this.x64Xor(h1, k1)
            };
            h1 = this.x64Xor(h1, [0, key.length]);
            h2 = this.x64Xor(h2, [0, key.length]);
            h1 = this.x64Add(h1, h2);
            h2 = this.x64Add(h2, h1);
            h1 = this.x64Fmix(h1);
            h2 = this.x64Fmix(h2);
            h1 = this.x64Add(h1, h2);
            h2 = this.x64Add(h2, h1);
            return (('00000000' + (h1[0] >>> 0).toString(16)).slice(-8) + ('00000000' + (h1[1] >>> 0).toString(16)).slice(-8) + ('00000000' + (h2[0] >>> 0).toString(16)).slice(-8) + ('00000000' + (h2[1] >>> 0).toString(16)).slice(-8))
        }
    };
    return PCHOMEFingerprint
});
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
var canvasCpde = new PCHOMEFingerprint().getCanvasFp();
var fig = new PCHOMEFingerprint().x64hash128(canvasCpde, 1024);
/*stg adurl與prd有差異*/
var adurl = "http://kwstg1.pchome.com.tw/adshow2.html?pfbxCustomerInfoId=" + pad_customerId;

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

		
		if (event.data.adBackup != undefined &&  event.data.adBackup.iframeIndex != null && event.data.adBackup.ALEX =='pcadshow') {
			var htmlContent = event.data.adBackup.htmlContent;
			if(htmlContent != null){
				var pcadshowList = document.getElementsByClassName("akb_iframe");
				/*處理收合*/
				if(htmlContent == 'blank'){
					var iframeObj = pcadshowList[event.data.adBackup.iframeIndex];
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
