//埋code傳遞的參數
var ptagParamater = null;
var paclCodeObject = new Object();
paclCodeObject["data"] = {};
(function (a, l, e, x) {
	ptagParamater = a.ptag.queue;
	doInitData();
	doSendPaclData();
})(window, document, location, history);

// 整理所有收取參數到物件中
function doInitData() {
	console.log(ptagParamater);
	var pa_id = "";
	/* ptagParamater event/event_type/paramater/click  */
	for (i = 0; i < ptagParamater.length; i++) {
		if (ptagParamater[i][0].paid != undefined && ptagParamater[0].paid != '' ) {
			pa_id = ptagParamater[i][0].paid;
			continue;
		}
		
		if (ptagParamater[i][0] === 'event' ) {
			var convert_id = "";
			var tracking_id = "";
			var page_view_opt1 = "";
			var page_view_opt2 = "";
			var convert_opt1 = "";
			var convert_opt2 = "";
			var tracking_opt1 = "";
			var tracking_opt2 = "";
			var prod_id = "";
			var prod_price = "";
			var prod_dis = "";
			console.log(ptagParamater[i][1]+":"+JSON.stringify(ptagParamater[i][2]));
			var eventType = ptagParamater[i][1];
			if (eventType == "page_view") {
				page_view_opt1 = ptagParamater[i][2].hasOwnProperty('op1') ? ptagParamater[i][2].op1 : '';
				page_view_opt2 = ptagParamater[i][2].hasOwnProperty('op2') ? ptagParamater[i][2].op2 : '';
				pa_em_value = ptagParamater[i][2].hasOwnProperty('pa_em_value') ? ptagParamater[i][2].pa_em_value : '';
				paclCodeObject.data['page_view'] = {
						'page_view_opt1': page_view_opt1,
						'page_view_opt2': page_view_opt2,
	                    'pa_em_value': pa_em_value,
	                    'pa_id': pa_id
				}
			}
			if (eventType == "convert") {
				convert_id = ptagParamater[i][2].hasOwnProperty('convert_id') ? ptagParamater[i][2].convert_id : '';
				convert_price = ptagParamater[i][2].hasOwnProperty('convert_price') ? ptagParamater[i][2].convert_price : '';
				convert_opt1 = ptagParamater[i][2].hasOwnProperty('op1') ? ptagParamater[i][2].op1 : '';
				convert_opt2 = ptagParamater[i][2].hasOwnProperty('op2') ? ptagParamater[i][2].op2 : '';
				pa_em_value = ptagParamater[i][2].hasOwnProperty('pa_em_value') ? ptagParamater[i][2].pa_em_value : '';
				convert_click_flag = ptagParamater[i][3] === 'click' ? true : false;
				paclCodeObject.data['convert_' + convert_id] = {
						'convert_id': convert_id,
						'convert_price': convert_price,
	                    'convert_opt1': convert_opt1,
	                    'convert_opt2': convert_opt2,
	                    'pa_em_value': pa_em_value,
	                    'pa_id': pa_id,
	                    'convert_click_flag': convert_click_flag
				}
			}
			
			if (eventType == "tracking") {
				tracking_id = ptagParamater[i][2].hasOwnProperty('tracking_id') ? ptagParamater[i][2].tracking_id : '';
				prod_id = ptagParamater[i][2].hasOwnProperty('prod_id') ? ptagParamater[i][2].prod_id : '';
				prod_price = ptagParamater[i][2].hasOwnProperty('prod_price') ? ptagParamater[i][2].prod_price : '';
				prod_dis = ptagParamater[i][2].hasOwnProperty('prod_dis') ? ptagParamater[i][2].prod_dis : '';
				tracking_opt1 = ptagParamater[i][2].hasOwnProperty('op1') ? ptagParamater[i][2].op1 : '';
				tracking_opt2 = ptagParamater[i][2].hasOwnProperty('op2') ? ptagParamater[i][2].op2 : '';
				ec_stock_status = ptagParamater[i][2].hasOwnProperty('ec_stock_status') ? ptagParamater[i][2].ec_stock_status : '';
				pa_em_value = ptagParamater[i][2].hasOwnProperty('pa_em_value') ? ptagParamater[i][2].pa_em_value : '';
				paclCodeObject.data['tracking_' + tracking_id] = {
						'tracking_id': tracking_id,
	                    'prod_id': prod_id,
	                    'prod_price': prod_price,
	                    'prod_dis': prod_dis,
	                    'tracking_opt1': tracking_opt1,
	                    'tracking_opt2': tracking_opt2,
	                    'ec_stock_status': ec_stock_status,
	                    'pa_em_value': pa_em_value,
	                    'pa_id': pa_id
				}
			}
			if (eventType == "activeprod") {

			}
			if (eventType == "mark") {
				
			}
		}
	}
}

function doSendPaclData() {
	  for (var key in paclCodeObject.data) {
	        if (key.indexOf('convert') >= 0) {
	            convert_id = paclCodeObject.data[key].convert_id;
	            convert_price = paclCodeObject.data[key].convert_price;
	            convert_opt1 = paclCodeObject.data[key].convert_opt1;
	            convert_opt2 = paclCodeObject.data[key].convert_opt2;
	            pa_em_value = paclCodeObject.data[key].pa_em_value;
	            pa_id = paclCodeObject.data[key].pa_id;
	            if (!paclCodeObject.data[key].convert_click_flag) {
	                doConvert();
	            }
	        }
	        if (key.indexOf('tracking') >= 0) {
	            tracking_id = paclCodeObject.data[key].tracking_id;
	            prod_id = paclCodeObject.data[key].prod_id;
	            prod_price = paclCodeObject.data[key].prod_price;
	            prod_dis = paclCodeObject.data[key].prod_dis;
	            tracking_opt1 = paclCodeObject.data[key].tracking_opt1;
	            tracking_opt2 = paclCodeObject.data[key].tracking_opt2;
	            ec_stock_status = paclCodeObject.data[key].ec_stock_status;
	            pa_em_value = paclCodeObject.data[key].pa_em_value;
	            pa_id = paclCodeObject.data[key].pa_id;
	            doTracking()
	        }
	        if (key.indexOf('page_view') >= 0) {
	            page_view_opt1 = paclCodeObject.data[key].page_view_opt1;
	            page_view_opt2 = paclCodeObject.data[key].page_view_opt2;
	            pa_em_value = paclCodeObject.data[key].pa_em_value;
	            pa_id = paclCodeObject.data[key].pa_id;
	            doPageView()
	        }
	    }
}

function doConvert() {
	referer = encodeURIComponent(document.referrer);
	screen_x = screen.availWidth;
	screen_y = screen.availHeight;
	webUrl = encodeURIComponent(location.href);
	paclUrl = location.protocol + "//showstg.pchome.com.tw/adm/alex.jsp";
	
    var img = new Image();
    img.src = paclUrl + "?" + "fingerId=" + "" + "&paId=" + encodeURIComponent(pa_id) + "&screenX=" + encodeURIComponent(screen_x) + "&screenY=" +
        encodeURIComponent(screen_y) + "&paEmValue=" + encodeURIComponent(pa_em_value) + "&url=" + encodeURIComponent(webUrl) + "&paEvent=convert&convertId=" +
        encodeURIComponent(convert_id) + "&convertPrice=" + encodeURIComponent(convert_price) + "&op1=" + encodeURIComponent(convert_opt1) + "&op2=" +
        encodeURIComponent(convert_opt2) + "&referer=" + encodeURIComponent(referer)
        
        
    console.log(img.src);
};


function doPageView() {
	referer = encodeURIComponent(document.referrer);
	screen_x = screen.availWidth;
	screen_y = screen.availHeight;
	webUrl = encodeURIComponent(location.href);
	paclUrl = location.protocol + "//showstg.pchome.com.tw/adm/alex.jsp";
	
    var img = new Image();
    img.src = paclUrl + "?" + "fingerId=" + "" + "&paId=" + encodeURIComponent(pa_id) + "&screenX=" + encodeURIComponent(screen_x) + "&screenY=" +
        encodeURIComponent(screen_y) + "&paEmValue=" + encodeURIComponent(pa_em_value) + "&url=" + encodeURIComponent(webUrl) + "&paEvent=page_view&op1=" +
        encodeURIComponent(page_view_opt1) + "&op2=" + encodeURIComponent(page_view_opt2) + "&referer=" + encodeURIComponent(referer)
        
        
    console.log(img.src);
};

function doTracking() {
	referer = encodeURIComponent(document.referrer);
	screen_x = screen.availWidth;
	screen_y = screen.availHeight;
	webUrl = encodeURIComponent(location.href);
	paclUrl = location.protocol + "//showstg.pchome.com.tw/adm/alex.jsp";
	
    var img = new Image();
    img.src = paclUrl + "?" + "fingerId=" + "" + "&paId=" + encodeURIComponent(pa_id) + "&screenX=" + encodeURIComponent(screen_x) + "&screenY=" +
        encodeURIComponent(screen_y) + "&paEmValue=" + encodeURIComponent(pa_em_value) + "&url=" + encodeURIComponent(webUrl) + "&paEvent=tracking&trackingId=" +
        encodeURIComponent(tracking_id) + "&prodId=" + encodeURIComponent(prod_id) + "&prodPrice=" + encodeURIComponent(prod_price) + "&prodDis=" +
        encodeURIComponent(prod_dis) + "&op1=" + encodeURIComponent(tracking_opt1) + "&op2=" + encodeURIComponent(tracking_opt2) + "&referer=" +
        encodeURIComponent(referer) + "&ecStockStatus=" + encodeURIComponent(ec_stock_status);
    
    console.log(img.src);
};


function pchome_click() {
	if(arguments.length == 0){
		for (var key in paclCodeObject.data) {
	        if (key.includes('convert')) {
	            convert_id = paclCodeObject.data[key].convert_id;
	            convert_price = paclCodeObject.data[key].convert_price;
	            convert_opt1 = paclCodeObject.data[key].convert_opt1;
	            convert_opt2 = paclCodeObject.data[key].convert_opt2;
	            pa_em_value = paclCodeObject.data[key].pa_em_value;
	            pa_id = paclCodeObject.data[key].pa_id;
	            if (paclCodeObject.data[key].convert_click_flag) {
	                doConvert();
	            }
	        }
	    }
	}
	if(arguments.length == 2){
		
		var link_url = arguments[0];
		var blank_flag = arguments[1];
		if (link_url == null || link_url.length == 0 || link_url == '') {
	        alert('link_url 是空值，link_url is null');
	        return false
	    }
	    var blank = false;
	    if (typeof blank_flag === "boolean") {
	        blank = blank_flag
	    }
	    for (var key in paclCodeObject.data) {
	        if (key.includes('convert')) {
	            convert_id = paclCodeObject.data[key].convert_id;
	            convert_price = paclCodeObject.data[key].convert_price;
	            convert_opt1 = paclCodeObject.data[key].convert_opt1;
	            convert_opt2 = paclCodeObject.data[key].convert_opt2;
	            pa_em_value = paclCodeObject.data[key].pa_em_value;
	            pa_id = paclCodeObject.data[key].pa_id;
	            if (paclCodeObject.data[key].convert_click_flag) {
	                doConvert()
	            }
	        }
	    }
	    if (blank) {
	        window.open(link_url, '_0')
	    } else {
	        location.href = link_url
	    }
	}
}