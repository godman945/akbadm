
//5

adbackupTEST ="ss";
window.onload = function() {
	if(document.getElementsByTagName("adbackup")[0] != undefined && (document.getElementById("adbackup_type") == null || document.getElementById("adbackup_type") == undefined)){
		var test = document.getElementById('pcadscript').previousElementSibling;
		test.innerHTML = test.innerHTML + 'adbackupTEST="true";'
		test.setAttribute("alex99", "1111");
		var pachomeAdJsSrc = document.getElementById('pcadscript').src;
		document.getElementById('pcadscript').src = pachomeAdJsSrc+"?adbackup=true"
		adbackupTEST = "true";
		console.log(adbackupTEST);
	}
	
    var a = document.getElementsByTagName("style")[0];
    if (a != undefined && a != null) {
        var cssText = a.innerText;
        cssText = cssText.substring(cssText.indexOf('.p8044_ad_container{position:relative;'), cssText.indexOf(';overflow:hidden;}'));
        cssText = cssText.replace('.p8044_ad_container{position:relative;', '');
        var sizeArray = cssText.split(';');
        if (sizeArray.length == 2) {
            if (sizeArray[0] == "width:350px" && sizeArray[1] == "height:292px") {
                var html5Ad = document.getElementsByClassName("p8044_ad_object")[0];
                html5Ad.setAttribute("style", "margin-left:25px;margin-top:21px;");
                document.getElementsByClassName("p8044_ad_container")[0].setAttribute("style", "height:271px;width:325px;")
            }
        }
    }
    if (document.getElementById('priceDom') != undefined && document.getElementById('priceDom') != null) {
        var priceChildren = document.getElementById('priceDom').children;
        if (priceChildren != undefined && priceChildren != null && priceChildren.length == 2) {
            var price01 = priceChildren[0].textContent;
            var price02 = priceChildren[1].textContent;
            if ((price02.indexOf('<#dad') >= 0 || price02 == '') && (price01.indexOf('<#dad') >= 0 || price01 == '')) {
                document.getElementById('priceDom').setAttribute("style", "font-size:12px; color:rgb(255,0,0); line-height:15px;margin: 0; padding: 0;word-break:break-all;display:inline-block;word-wrap: break-word;width:225px;display:none;")
            } else if (price02.indexOf('#dad') >= 0 || price02 == '') {
                var priceText = Number(priceChildren[0].textContent);
                priceChildren[0].textContent = 'NT$' + priceText.toLocaleString('en-US');
                priceChildren[0].setAttribute("style", "color:rgb(102,102,102); text-decoration:none; margin-right:3px;font-size:13px;");
                document.getElementById('priceDom').setAttribute("style", "font-size:12px; color:rgb(255,0,0); line-height:15px;margin: 0; padding: 0;word-break:break-all;display:inline-block;word-wrap: break-word;width:225px;")
            } else {
                var priceText = Number(priceChildren[1].textContent);
                priceChildren[1].textContent = 'NT$' + priceText.toLocaleString('en-US');
                priceChildren[1].setAttribute("style", "font-size:13px; color:rgb(255,0,0); line-height:15px;margin: 0; padding: 0;word-break:break-all;display:inline-block;word-wrap: break-word;text-decoration:none;");
                document.getElementById('priceDom').setAttribute("style", "font-size:12px; color:rgb(255,0,0); line-height:15px;margin: 0; padding: 0;word-break:break-all;display:inline-block;word-wrap: break-word;width:225px;")
            }
        }
    }
    var priceClassObj = document.getElementsByClassName('ad_price');
    if (priceClassObj.length > 0) {
        for (var i = 0; i < priceClassObj.length; i++) {
            if (document.getElementsByClassName('ad_price') != undefined && document.getElementsByClassName('ad_price') != null) {
                var priceObj = priceClassObj[i];
                var price01Obj = priceObj.children[0];
                var price02Obj = priceObj.children[1];
                var price01 = priceObj.children[0].textContent.replace('NT$', '');
                var price02 = priceObj.children[1].textContent.replace('NT$', '');
                if (price01 == '' || price01.indexOf('<#dad') >= 0) {
                    price01Obj.setAttribute("style", 'display:none')
                }
                if (price02 == '' || price02.indexOf('<#dad') >= 0) {
                    price02Obj.setAttribute("style", 'display:none')
                }
                price01 = 'NT$' + parseInt(price01).toLocaleString('en-US');
                price02 = '<span class="psNum">NT$' + parseInt(price02).toLocaleString('en-US') + "</span>";
                priceObj.children[0].textContent = price01;
                priceObj.children[1].textContent = price02;
                price02Obj.innerHTML = price02;
                priceObj.style.display = ''
            }
        }
    }
    var i = 0;
    var coor = [0, 0, 0, 0, 0];
    var pn = 0;
    var adDiv = document.getElementById('pc8044_adv');
    if (adDiv != null) {
        adDiv.onmousemove = function move(e) {
            var x = e.clientX;
            var y = e.clientY;
            i++;
            if (i <= 10) {
                if (i % 2 == 0) {
                    if (pn <= 5) {
                        coor[pn] = x + y;
                        pn++
                    }
                }
            }
        };
        adDiv.onmouseleave = function mouseleave() {
            i = 0;
            pn = 0;
            coor = [0, 0, 0, 0, 0]
        };
        adDiv.onclick = function mouseclick(e) {
            var x = e.clientX;
            var y = e.clientY;
            var ph = window.innerHeight;
            var pw = window.innerWidth;
            var veydata = 0;
            var chflag = 'T';
            var chcount = 0;
            for (pa = 0; pa < 5; pa++) {
                veydata = coor[pa];
                for (pv = 0; pv < 5; pv++) {
                    if (veydata == coor[pv]) {
                        chcount++
                    }
                };
                if (chcount > 5) {
                    chflag = 'F';
                    break
                }
            };
            var mp = chflag + "," + pw + ',' + ph + ',' + x + "," + y;
            var mpr = "";
            for (ct = 0; ct < mp.length; ct++) {
                mpr += String.fromCharCode(mp.charCodeAt(ct) + 3 + ct)
            };
            mpr = encodeURIComponent(mpr);
            setCookie('mpv', mpr)
        }
    }
};

function setCookie(name, value) {
    var Days = 1;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + '=' + value + ';expires=' + exp.toGMTString()
}
window.addEventListener('message', function(event) {
    try {
        var objecj = (JSON.parse(event.data));
        if (objecj.adBackup != undefined && objecj.adBackup != 'undefined' && objecj.adBackup != null) {
        	var adbackupTypeObj = document.getElementById("adbackup_type");
            var objecj = (JSON.parse(event.data));
            if (adbackupTypeObj == null || adbackupTypeObj == undefined) {
                objecj.adBackup['htmlContent'] = null;
                window.parent.parent.postMessage(objecj, '*');
            } else {
                var scriptText = adbackupTypeObj.innerHTML;
                if (scriptText == '') {
                    objecj.adBackup['htmlContent'] = 'blank';
                    window.parent.parent.postMessage(objecj, '*')
                }
            }
        }
    } catch (err) {}
});