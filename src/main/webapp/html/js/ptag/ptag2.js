var ptagParamater = (window.ptag.queue === undefined) ?  window.dataLayer : window.ptag.queue;
var page_view_opt1 = "";
var page_view_opt2 = "";
var convert_opt1 = "";
var convert_opt2 = "";
var tracking_opt1 = "";
var tracking_opt2 = "";
var mark_opt1 = "";
var mark_opt2 = "";
var activeprod_opt1 = "";
var activeprod_opt2 = "";
var convert_id = "";
var convert_price = "";
var tracking_id = "";
var prod_id = "";
var prod_price = "";
var prod_dis = "";
var ec_stock_status = "";
var pa_em_value = "";
var pa_id = "";
var mark_id=""
var mark_value="";
var mark_layer=""
var paId="";	

var paclUrl = location.protocol + "//paclstg.pchome.com.tw/api/collect";
(function() {
    var click = null;
    referer = document.referrer;
    screen_x = screen.availWidth;
    screen_y = screen.availHeight;
    webUrl = location.href;
    convert_click_flag = false;
    mark_click_flag = false;
    do_a(function() {
        doInitData();
        doSendPaclData()
    })
})();

var referer = "";
var screen_x = "";
var screen_y = "";
var webUrl = "";
var fig = "";
var mark_click_flag = null;
var convert_click_flag = null;
var paclCodeObject = new Object();
var paclCodeJson = null;
var paclCodeObject = null;
var ptagParamater = null;



function do_a(callback) {
    paclCodeObject = new Object();
    paclCodeObject["data"] = {};
    ptagParamater = (window.ptag.queue === undefined) ?  window.dataLayer : window.ptag.queue;
    
    document.getElementsByName("pchome_ptag").forEach(function(element) {
    	var data = element.innerHTML.trim();
    	data = data.replace("ptag(","[");
    	data = data.replace(");","]");
    	data = JSON.parse(data);
    	ptagParamater.push(data);
    })
//    console.log(window.ptag.queue);
    if (typeof callback === 'function') {
        callback()
    }
}

function doInitData() {
    var pa_id = "";
    ptagParamater.forEach(function(element) {
    	
        var ptagType = element[0];
        if ((element.length == 1 && element[0].paid == undefined) || (element.length == undefined)) {
            return
        }
        if (element.length == 1 && element[0].paid != undefined) {
            pa_id = ptagType.paid;
            paId = ptagType.paid;
        }
        if (element[0] == undefined) {
            return
        }
        
        if (element[1] == 'convert' 
        	|| element[1] == 'activeprod' 
        	|| element[1] == 'page_view' 
        	|| element[1] == 'tracking' 
        	|| element[1] == 'mark' 
        	|| element[1] == 'record'
        	|| element[1] == 'activeprod') {
            
        	var eventType = element[1];
            if (eventType == "page_view") {
                page_view_opt1 = element[2].hasOwnProperty('op1') ? element[2].op1 : '';
                page_view_opt2 = element[2].hasOwnProperty('op2') ? element[2].op2 : '';
                pa_em_value = element[2].hasOwnProperty('pa_em_value') ? element[2].pa_em_value : '';
                paclCodeObject.data['page_view'] = {
                    'page_view_opt1': page_view_opt1,
                    'page_view_opt2': page_view_opt2,
                    'pa_em_value': pa_em_value,
                    'pa_id': pa_id
                }
            }
            if (eventType == "convert") {
                convert_id = element[2].hasOwnProperty('convert_id') ? element[2].convert_id : '';
                convert_price = element[2].hasOwnProperty('convert_price') ? element[2].convert_price : '';
                convert_opt1 = element[2].hasOwnProperty('op1') ? element[2].op1 : '';
                convert_opt2 = element[2].hasOwnProperty('op2') ? element[2].op2 : '';
                pa_em_value = element[2].hasOwnProperty('pa_em_value') ? element[2].pa_em_value : '';
                convert_click_flag = element[3] === 'click' ? true : false;
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
                tracking_id = element[2].hasOwnProperty('tracking_id') ? element[2].tracking_id : '';
                prod_id = element[2].hasOwnProperty('prod_id') ? element[2].prod_id : '';
                prod_price = element[2].hasOwnProperty('prod_price') ? element[2].prod_price : '';
                prod_dis = element[2].hasOwnProperty('prod_dis') ? element[2].prod_dis : '';
                tracking_opt1 = element[2].hasOwnProperty('op1') ? element[2].op1 : '';
                tracking_opt2 = element[2].hasOwnProperty('op2') ? element[2].op2 : '';
                ec_stock_status = element[2].hasOwnProperty('ec_stock_status') ? element[2].ec_stock_status : '';
                pa_em_value = element[2].hasOwnProperty('pa_em_value') ? element[2].pa_em_value : '';
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
            if (eventType == "record") {
                tracking_id = element[2].hasOwnProperty('tracking_id') ? element[2].tracking_id : '';
                prod_id = element[2].hasOwnProperty('prod_id') ? element[2].prod_id : '';
                prod_price = element[2].hasOwnProperty('prod_price') ? element[2].prod_price : '';
                prod_dis = element[2].hasOwnProperty('prod_dis') ? element[2].prod_dis : '';
                tracking_opt1 = element[2].hasOwnProperty('op1') ? element[2].op1 : '';
                tracking_opt2 = element[2].hasOwnProperty('op2') ? element[2].op2 : '';
                ec_stock_status = element[2].hasOwnProperty('ec_stock_status') ? element[2].ec_stock_status : '';
                pa_em_value = element[2].hasOwnProperty('pa_em_value') ? element[2].pa_em_value : '';
                paclCodeObject.data['record_' + tracking_id] = {
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
        }
        
        if (eventType == "mark") {
        	mark_id = element[2].hasOwnProperty('mark_id') ? element[2].mark_id : '';
        	mark_value = element[2].hasOwnProperty('mark_value') ? element[2].mark_value : '';
        	mark_layer = element[2].hasOwnProperty('mark_layer') ? element[2].mark_layer : '';
        	mark_opt1 = element[2].hasOwnProperty('op1') ? element[2].op1 : '';
        	mark_opt2 = element[2].hasOwnProperty('op2') ? element[2].op2 : '';
        	mark_click_flag = element[3] === 'click' ? true : false;
        	paclCodeObject.data['mark_id_'+ mark_id] = {
        			'mark_id': mark_id,
        			'mark_layer': mark_layer,
        			'mark_value': mark_value,
        			'pa_id': pa_id,
        			'mark_click_flag': mark_click_flag,
        			'mark_opt1': mark_opt1,
                    'mark_opt2': mark_opt2,
        	}
        }
        
        if (eventType == "activeprod") {
        	tracking_id = element[2].hasOwnProperty('tracking_id') ? element[2].tracking_id : '';
        	prod_id = element[2].hasOwnProperty('prod_id') ? element[2].prod_id : '';
        	prod_price = element[2].hasOwnProperty('prod_price') ? element[2].prod_price : '';
        	prod_dis = element[2].hasOwnProperty('prod_dis') ? element[2].prod_dis : '';
        	activeprod_opt1 = element[2].hasOwnProperty('op1') ? element[2].op1 : '';
        	activeprod_opt1 = element[2].hasOwnProperty('op2') ? element[2].op2 : '';
        	paclCodeObject.data['activeprod_' + tracking_id] = {
                    'tracking_id': tracking_id,
                    'prod_id': prod_id,
                    'prod_price': prod_price,
                    'prod_dis': prod_dis,
                    'pa_id': pa_id,
                    'activeprod_opt1': mark_opt1,
                    'activeprod_opt1': mark_opt2,
        	}
        }
    })
    
    console.log(ptagParamater);
};

function doSendPaclData() {
    for (var key in paclCodeObject.data) {
        if (key.indexOf('convert') >=0) {
            convert_id = paclCodeObject.data[key].convert_id;
            convert_price = paclCodeObject.data[key].convert_price;
            convert_opt1 = paclCodeObject.data[key].convert_opt1;
            convert_opt2 = paclCodeObject.data[key].convert_opt2;
            pa_em_value = paclCodeObject.data[key].pa_em_value;
            pa_id = paclCodeObject.data[key].pa_id;
            if (!paclCodeObject.data[key].convert_click_flag) {
                doConvert()
            }
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
            doTracking()
        }
        if (key.indexOf('record') >=0) {
            tracking_id = paclCodeObject.data[key].tracking_id;
            prod_id = paclCodeObject.data[key].prod_id;
            prod_price = paclCodeObject.data[key].prod_price;
            prod_dis = paclCodeObject.data[key].prod_dis;
            tracking_opt1 = paclCodeObject.data[key].tracking_opt1;
            tracking_opt2 = paclCodeObject.data[key].tracking_opt2;
            ec_stock_status = paclCodeObject.data[key].ec_stock_status;
            pa_em_value = paclCodeObject.data[key].pa_em_value;
            pa_id = paclCodeObject.data[key].pa_id;
            doRecord()
        }
        if (key.indexOf('page_view') >=0) {
            page_view_opt1 = paclCodeObject.data[key].page_view_opt1;
            page_view_opt2 = paclCodeObject.data[key].page_view_opt2;
            pa_em_value = paclCodeObject.data[key].pa_em_value;
            pa_id = paclCodeObject.data[key].pa_id;
            doPageView()
        }
        
        if (key.indexOf('activeprod') >=0) {
        	prod_id = paclCodeObject.data[key].prod_id;
        	prod_price = paclCodeObject.data[key].prod_price;
        	prod_dis = paclCodeObject.data[key].prod_dis;
            pa_id = paclCodeObject.data[key].pa_id;
            activeprod_opt1 = paclCodeObject.data[key].activeprod_opt1;
            activeprod_opt2 = paclCodeObject.data[key].activeprod_opt2;
            doActiveprod()
        }
        if (key.indexOf('mark') >=0) {
        	mark_id = paclCodeObject.data[key].mark_id;
            mark_value = paclCodeObject.data[key].mark_value;
            mark_layer = paclCodeObject.data[key].mark_layer;
            mark_opt1 = paclCodeObject.data[key].mark_opt1;
            mark_opt2 = paclCodeObject.data[key].mark_opt2;
            pa_id = paclCodeObject.data[key].pa_id;
            doMark()
        }
        
    }
};

function doConvert() {
    var img = new Image();
    img.src = paclUrl + "?" + "fingerId=" + "" + "&paId=" + encodeURIComponent(pa_id) + "&screenX=" + encodeURIComponent(screen_x) + "&screenY=" + encodeURIComponent(screen_y) + "&paEmValue=" + encodeURIComponent(pa_em_value) + "&url=" + encodeURIComponent(webUrl) + "&paEvent=convert&convertId=" + encodeURIComponent(convert_id) + "&convertPrice=" + encodeURIComponent(convert_price) + "&op1=" + encodeURIComponent(convert_opt1) + "&op2=" + encodeURIComponent(convert_opt2) + "&referer=" + encodeURIComponent(referer)
};

function doPageView() {
    var img = new Image();
    img.src = paclUrl + "?" + "fingerId=" + "" + "&paId=" + encodeURIComponent(pa_id) + "&screenX=" + encodeURIComponent(screen_x) + "&screenY=" + encodeURIComponent(screen_y) + "&paEmValue=" + encodeURIComponent(pa_em_value) + "&url=" + encodeURIComponent(webUrl) + "&paEvent=page_view&op1=" + encodeURIComponent(page_view_opt1) + "&op2=" + encodeURIComponent(page_view_opt2) + "&referer=" + encodeURIComponent(referer)
};

function doTracking() {
    var img = new Image();
    img.src = paclUrl + "?" + "fingerId=" + "" + "&paId=" + encodeURIComponent(pa_id) + "&screenX=" + encodeURIComponent(screen_x) + "&screenY=" + encodeURIComponent(screen_y) + "&paEmValue=" + encodeURIComponent(pa_em_value) + "&url=" + encodeURIComponent(webUrl) + "&paEvent=tracking&trackingId=" + encodeURIComponent(tracking_id) + "&prodId=" + encodeURIComponent(prod_id) + "&prodPrice=" + encodeURIComponent(prod_price) + "&prodDis=" + encodeURIComponent(prod_dis) + "&op1=" + encodeURIComponent(tracking_opt1) + "&op2=" + encodeURIComponent(tracking_opt2) + "&referer=" + encodeURIComponent(referer) + "&ecStockStatus=" + encodeURIComponent(ec_stock_status)
};

function doMark() {

  var img = new Image();
  img.src = paclUrl + "?" + "fingerId=" + "" + "&paId=" + encodeURIComponent(paId) + "&screenX=" + encodeURIComponent(screen_x) + "&screenY=" + encodeURIComponent(screen_y) + "&paEmValue=" + encodeURIComponent(pa_em_value) + "&url=" + encodeURIComponent(webUrl) + "&paEvent=mark&markId=" + encodeURIComponent(mark_id) + "&markValue=" + encodeURIComponent(mark_value) + "&markLayer=" + encodeURIComponent(mark_layer) + "&op1=" + encodeURIComponent(mark_opt1) + "&op2=" + encodeURIComponent(mark_opt2)+ "&referer=" + encodeURIComponent(referer) ;
};

function doActiveprod() {
  var img = new Image();
  img.src = paclUrl + "?" + "fingerId=" + "" + "&paId=" + encodeURIComponent(paId) + "&screenX=" + encodeURIComponent(screen_x) + "&screenY=" + encodeURIComponent(screen_y) + "&paEmValue=" + encodeURIComponent(pa_em_value) + "&url=" + encodeURIComponent(webUrl) + "&paEvent=activeprod&trackingId=" + encodeURIComponent(tracking_id) + "&prodId=" + encodeURIComponent(prod_id) + "&prodPrice=" + encodeURIComponent(prod_price) + "&prodDis=" + encodeURIComponent(prod_dis) +  "&op1=" + encodeURIComponent(activeprod_opt1) + "&op2=" + encodeURIComponent(activeprod_opt2);
};

function doRecord() {
	var img = new Image();
    img.src = paclUrl + "?" + "fingerId=" + "" + "&paId=" + encodeURIComponent(paId) + "&screenX=" + encodeURIComponent(screen_x) + "&screenY=" + encodeURIComponent(screen_y) + "&paEmValue=" + encodeURIComponent(pa_em_value) + "&url=" + encodeURIComponent(webUrl) + "&paEvent=record&trackingId=" + encodeURIComponent(tracking_id) + "&referer=" + encodeURIComponent(referer)
};



/*新版點擊*/
function ptag_click(){
	screen_x = screen.availWidth;
    screen_y = screen.availHeight;
    webUrl = location.href;
	var callFunction = arguments[2].event_callback;
	var eventId = arguments[1];
	var eventContent = arguments[2];
	if(eventId == 'convert' 
     	|| eventId == 'activeprod' 
     	|| eventId == 'page_view' 
        || eventId == 'tracking' 
        || eventId == 'mark' 
        || eventId == 'activeprod'){
		 
		 if (eventId == 'page_view') {
			 page_view_opt1 = eventContent.hasOwnProperty('op1') ? eventContent.op1 : '';
             page_view_opt2 = eventContent.hasOwnProperty('op2') ? eventContent.op2 : '';
             pa_em_value = eventContent.hasOwnProperty('pa_em_value') ? eventContent.pa_em_value : '';
             doPageView();
		 }
		
         if (eventId == "tracking") {
        	 tracking_id = eventContent.hasOwnProperty('tracking_id') ? eventContent.tracking_id : '';
        	 prod_id = eventContent.hasOwnProperty('prod_id') ? eventContent.prod_id : '';
        	 prod_price = eventContent.hasOwnProperty('prod_price') ? eventContent.prod_price : '';
        	 prod_dis = eventContent.hasOwnProperty('prod_dis') ? eventContent.prod_dis : '';
        	 tracking_opt1 = eventContent.hasOwnProperty('op1') ? eventContent.op1 : '';
        	 tracking_opt2 = eventContent.hasOwnProperty('op2') ? eventContent.op2 : '';
        	 ec_stock_status = eventContent.hasOwnProperty('ec_stock_status') ? eventContent.ec_stock_status : '';
        	 pa_em_value = eventContent.hasOwnProperty('pa_em_value') ? eventContent.pa_em_value : '';
        	 doTracking();
         }
	     if (eventId == "mark") {
		 	mark_id = eventContent.hasOwnProperty('mark_id') ? eventContent.mark_id : '';
		 	mark_value = eventContent.hasOwnProperty('mark_value') ? eventContent.mark_value : '';
		 	mark_layer = eventContent.hasOwnProperty('mark_layer') ? eventContent.mark_layer : '';
		 	mark_opt1 = eventContent.hasOwnProperty('op1') ? eventContent.op1 : '';
        	mark_opt2 = eventContent.hasOwnProperty('op2') ? eventContent.op2 : '';
		 	doMark();
		 }
	     
	     if (eventId == "convert") {
		     convert_id = eventContent.hasOwnProperty('convert_id') ? eventContent.convert_id : '';
		     convert_price = eventContent.hasOwnProperty('convert_price') ? eventContent.convert_price : '';
		     convert_opt1 = eventContent.hasOwnProperty('op1') ? eventContent.op1 : '';
		     convert_opt2 = eventContent.hasOwnProperty('op2') ? eventContent.op2 : '';
		     pa_em_value = eventContent.hasOwnProperty('pa_em_value') ? eventContent.pa_em_value : '';
		     doConvert();
		 }
	     if (eventId == "activeprod") {
		 	tracking_id = eventContent.hasOwnProperty('tracking_id') ? eventContent.tracking_id : '';
		 	prod_id = eventContent.hasOwnProperty('prod_id') ? eventContent.prod_id : '';
		 	prod_price = eventContent.hasOwnProperty('prod_price') ? eventContent.prod_price : '';
		 	prod_dis = eventContent.hasOwnProperty('prod_dis') ? eventContent.prod_dis : '';
		 	activeprod_opt1 = eventContent.activeprod_opt1;
            activeprod_opt2 = eventContent.activeprod_opt2;
		 	doActiveprod()
		 }
	     
	}
	
	if(typeof(callFunction) != 'undefined'){
		callFunction.call();
	}
	
}


/*舊版點擊寫法新版以貼code為主*/
function pchome_click(link_url, blank_flag) {
	referer = document.referrer;
    screen_x = screen.availWidth;
    screen_y = screen.availHeight;
    webUrl = location.href;
    if (link_url == null || link_url.length == 0 || link_url == '') {
        alert('link_url 是空值，link_url is null');
        return false
    }
    var blank = false;
    if (typeof blank_flag === "boolean") {
        blank = blank_flag
    }
    for (var key in paclCodeObject.data) {
        if (key.indexOf('convert') >=0) {
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
        if (key.indexOf('mark') >=0) {
            mark_id = paclCodeObject.data[key].mark_id;
            mark_value = paclCodeObject.data[key].mark_value;
            mark_layer = paclCodeObject.data[key].mark_layer;
            pa_id = paclCodeObject.data[key].pa_id;
            if (paclCodeObject.data[key].mark_click_flag) {
            	doMark()
            }
        }
    }
    if (blank) {
        window.open(link_url, '_0')
    } else {
        location.href = link_url
    }
}

function pchome_click() {
	referer = document.referrer;
    screen_x = screen.availWidth;
    screen_y = screen.availHeight;
    webUrl = location.href;
    do_a(function() {
        doInitData()
    });
    for (var key in paclCodeObject.data) {
        if (key.indexOf('convert') >=0) {
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
        
        if (key.indexOf('mark') >=0) {
            mark_id = paclCodeObject.data[key].mark_id;
            mark_value = paclCodeObject.data[key].mark_value;
            mark_layer = paclCodeObject.data[key].mark_layer;
            pa_id = paclCodeObject.data[key].pa_id;
            
            
            if(arguments.length == 1 && arguments[0] == mark_id && paclCodeObject.data[key].mark_click_flag){
            	doMark()
            }else if(arguments.length == 0 && paclCodeObject.data[key].mark_click_flag){
            	doMark()
            }
        }
    }
}