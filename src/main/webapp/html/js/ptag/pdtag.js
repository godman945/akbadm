console.log("init pdtag js");
var page_view_opt1 = "";
var page_view_opt2 = "";
var convert_opt1 = "";
var convert_opt2 = "";
var tracking_opt1 = "";
var tracking_opt2 = "";
var convert_id = "";
var convert_price = "";
var tracking_id = "";
var prod_id = "";
var prod_price = "";
var prod_dis = "";
var ec_stock_status = "";
var pa_em_value = "";
var pa_id = "";
var mark_id = "";
var mark_layer1 = "";
var referer = "";
var screen_x = "";
var screen_y = "";
var webUrl = "";
var fig = "";
var convert_click_flag = null;
var mark_click_flag = null;

var paclUrl = null;
(function() {
	ptagParamater = window.ptag.q;
	paclUrl = location.protocol + "//pacl.pchome.com.tw/api/collect";
	referer = encodeURIComponent(document.referrer);
    screen_x = screen.availWidth;
    screen_y = screen.availHeight;
    webUrl = encodeURIComponent(location.href);
    paclCodeObject = new Object();
    paclCodeObject["data"] = {};
    doInitData();
    doSendPaclData();
    console.log(ptagParamater);
})();


function doInitData() {
	try{
		screen_x = screen.availWidth;
	    screen_y = screen.availHeight;
		var pa_id = "";
		for ( var obj in ptagParamater) {
			var element = ptagParamater[obj];
			if(element[0].paid != undefined && element[0].paid != ''){
				pa_id = element[0].paid;
			}else if(element[0].event != undefined && element[0].event != ''){
				var eventObj = element[0].event;
				var eventType = element[0].event.type;
				if(eventType == 'convert'){
					convert_id = eventObj.hasOwnProperty('convert_id') ? eventObj.convert_id : '';
					convert_price = eventObj.hasOwnProperty('convert_price') ? eventObj.convert_price : '';
					convert_opt1 = eventObj.hasOwnProperty('op1') ? eventObj.op1 : '';
					convert_opt2 = eventObj.hasOwnProperty('op2') ? eventObj.op2 : '';
					pa_em_value = eventObj.hasOwnProperty('pa_em_value') ? eventObj.pa_em_value : '';
					convert_click_flag = eventObj.hasOwnProperty('click') ? eventObj.click : false;
					paclCodeObject.data['convert_' + convert_id] = {
				              'convert_id': convert_id,
				              'convert_price': convert_price,
				              'convert_opt1': convert_opt1,
				              'convert_opt2': convert_opt2,
				              'pa_em_value': pa_em_value,
				              'pa_id': pa_id,
				              'convert_click_flag': convert_click_flag
					}
				}else if(eventType == 'tracking'){
					tracking_id = eventObj.hasOwnProperty('tracking_id') ? eventObj.tracking_id : '';
					prod_id = eventObj.hasOwnProperty('prod_id') ? eventObj.prod_id : '';
					prod_price = eventObj.hasOwnProperty('prod_price') ? eventObj.prod_price : '';
					prod_dis = eventObj.hasOwnProperty('prod_dis') ? eventObj.prod_dis : '';
					tracking_opt1 = eventObj.hasOwnProperty('op1') ? eventObj.op1 : '';
					tracking_opt2 = eventObj.hasOwnProperty('op2') ? eventObj.op2 : '';
					ec_stock_status = eventObj.hasOwnProperty('ec_stock_status') ? eventObj.ec_stock_status : '';
					pa_em_value = eventObj.hasOwnProperty('pa_em_value') ? eventObj.pa_em_value : '';
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
				}else if(eventType == 'page_view'){
					page_view_opt1 = eventObj.hasOwnProperty('op1') ? eventObj.op1 : '';
					page_view_opt2 = eventObj.hasOwnProperty('op2') ? eventObj.op2 : '';
					pa_em_value = eventObj.hasOwnProperty('pa_em_value') ? eventObj.pa_em_value : '';
					paclCodeObject.data['page_view'] = {
	                  'page_view_opt1': page_view_opt1,
	                  'page_view_opt2': page_view_opt2,
	                  'pa_em_value': pa_em_value,
	                  'pa_id': pa_id
					}
				}else if(eventType == 'mark'){
					mark_id = eventObj.hasOwnProperty('mark_id') ? eventObj.mark_id : '';
					mark_layer1 = eventObj.hasOwnProperty('mark_layer1') ? eventObj.mark_layer1 : '';
					mark_click_flag = eventObj.hasOwnProperty('click') ? eventObj.click : '';
					paclCodeObject.data['mark'] = {
			                  'mark_id': mark_id,
			                  'mark_layer1': '',
			                  'pa_id': pa_id,
			                  'mark_click_flag':mark_click_flag
					}
				}
			}
		}
	}catch(e){
		console.log(e);
	}
};

function doSendPaclData() {
    for (var key in paclCodeObject.data) {
    	if (key.indexOf('page_view') >=0) {
            page_view_opt1 = paclCodeObject.data[key].page_view_opt1;
            page_view_opt2 = paclCodeObject.data[key].page_view_opt2;
            pa_em_value = paclCodeObject.data[key].pa_em_value;
            pa_id = paclCodeObject.data[key].pa_id;
            doPageView();
        }
    	
        if (key.indexOf('tracking') >=0) {
            tracking_id = paclCodeObject.data[key].tracking_id;
            prod_id = paclCodeObject.data[key].prod_id;
            prod_price = paclCodeObject.data[key].prod_price;
            prod_dis = paclCodeObject.data[key].prod_dis;
            tracking_opt1 = paclCodeObject.data[key].tracking_opt1;
            tracking_opt2 = paclCodeObject.data[key].tracking_opt2;
            ec_stock_status = paclCodeObject.data[key].ec_stock_status;
            pa_em_value = paclCodeObject.data[key].pa_em_value;
            pa_id = paclCodeObject.data[key].pa_id;
            doTracking();
        }
        if (key.indexOf('convert') >= 0) {
            if (!paclCodeObject.data[key].convert_click_flag) {
            	convert_id = paclCodeObject.data[key].convert_id;
                convert_price = paclCodeObject.data[key].convert_price;
                convert_opt1 = paclCodeObject.data[key].convert_opt1;
                convert_opt2 = paclCodeObject.data[key].convert_opt2;
                pa_em_value = paclCodeObject.data[key].pa_em_value;
                pa_id = paclCodeObject.data[key].pa_id;
                doConvert();
            }
        }
        
    }
};

function doConvert() {
    var img = new Image();
    img.src = paclUrl + "?" + "paId=" + encodeURIComponent(pa_id) + "&screenX=" + encodeURIComponent(screen_x) + "&screenY=" +
        encodeURIComponent(screen_y) + "&url=" + encodeURIComponent(webUrl) + "&paEvent=convert&convertId=" +
        encodeURIComponent(convert_id) + "&convertPrice=" + encodeURIComponent(convert_price) + "&op1=" + encodeURIComponent(convert_opt1) + "&op2=" +
        encodeURIComponent(convert_opt2) + "&referer=" + encodeURIComponent(referer)
};

function doPageView() {
    var img = new Image();
    img.src = paclUrl + "?" + "paId=" + encodeURIComponent(pa_id) + "&screenX=" + encodeURIComponent(screen_x) + "&screenY=" +
        encodeURIComponent(screen_y)  + "&url=" + encodeURIComponent(webUrl) + "&paEvent=page_view&op1=" +
        encodeURIComponent(page_view_opt1) + "&op2=" + encodeURIComponent(page_view_opt2) + "&referer=" + encodeURIComponent(referer)
};

function doTracking() {
    var img = new Image();
    img.src = paclUrl + "?" + "paId=" + encodeURIComponent(pa_id) + "&screenX=" + encodeURIComponent(screen_x) + "&screenY=" +
        encodeURIComponent(screen_y)  + "&url=" + encodeURIComponent(webUrl) + "&paEvent=tracking&trackingId=" +
        encodeURIComponent(tracking_id) + "&prodId=" + encodeURIComponent(prod_id) + "&prodPrice=" + encodeURIComponent(prod_price) + "&prodDis=" +
        encodeURIComponent(prod_dis) + "&op1=" + encodeURIComponent(tracking_opt1) + "&op2=" + encodeURIComponent(tracking_opt2) + "&referer=" +
        encodeURIComponent(referer) + "&ecStockStatus=" + encodeURIComponent(ec_stock_status)
};


function doMark() {
    var img = new Image();
    img.src = paclUrl + "?" + "markId=" + mark_id + "&paId=" + encodeURIComponent(pa_id)+"&markLayer1="+ encodeURIComponent(mark_layer1);
};



//點擊時觸發轉換事件
var ptag = function(data){
	arguments[0].event.click = true;
	ptagParamater.push(arguments);
	doInitData();
	for (var key in paclCodeObject.data) {
		if (key.indexOf('convert') >= 0 && paclCodeObject.data[key].convert_click_flag) {
			convert_id = paclCodeObject.data[key].convert_id;
			convert_price = paclCodeObject.data[key].convert_price;
			convert_opt1 = paclCodeObject.data[key].convert_opt1;
			convert_opt2 = paclCodeObject.data[key].convert_opt2;
			pa_em_value = paclCodeObject.data[key].pa_em_value;
			pa_id = paclCodeObject.data[key].pa_id;
			doConvert();
		}
	}
}

//點擊時觸發mark事件
var mark_click = function(a,b,markValue){
	for (var key in paclCodeObject.data) {
		if (key.indexOf('mark') >=0) {
			mark_id = paclCodeObject.data[key].mark_id;
			if(markValue != null && markValue.length > 0){
				mark_layer1 = markValue;
			}else{
				mark_layer1 = op1_value;
			}
			pa_id = paclCodeObject.data[key].pa_id;
			doMark();
		}
	}
}