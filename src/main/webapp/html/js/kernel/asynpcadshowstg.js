(function(name, context, definition) {
    'use strict';
    if (typeof window.define === 'function' && window.define.amd) {
        window.define(definition);
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
            ctx.fillText('Cwm fjordbank glyphs vext quiz, \ud83d\ude03', 2, 15);
            ctx.fillStyle = 'rgba(102, 204, 0, 0.2)';
            ctx.font = '18pt Arial';
            ctx.fillText('Cwm fjordbank glyphs vext quiz, \ud83d\ude03', 4, 45);
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

                alert("超過廣告上限，最多只能貼10則廣告!");

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
                //呼叫影音控制
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
        var canvasCpde = new PCHOMEFingerprint().getCanvasFp();
        var fig = new PCHOMEFingerprint().x64hash128(canvasCpde, 1024);
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

        // 補版第2次呼叫不傳 docurl encoder 有問題
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




//監聽子iframe傳送的訊息
window.addEventListener("message", getMessage0, false);

function getMessage0(event) {
	try {
		if (event.data.adBackup != undefined && event.data.adBackup.iframeIndex != null	&& event.data.adBackup.ALEX == 'asynpcadshow') {
			var htmlContent = event.data.adBackup.htmlContent;
			var pcadshowList = document.getElementsByClassName("asynpchomead");
				/* 處理收合 */
				if (htmlContent == 'blank') {
					var iframeObj = pcadshowList[event.data.adBackup.iframeIndex];
					iframeObj.height = 0;
					iframeObj.width = 0;
				}
				/* 處理補板 */
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

