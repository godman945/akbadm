var ptagParamater = window.dataLayer;
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
(function() {
    var click = null;
    referer = encodeURIComponent(document.referrer);
    screen_x = screen.availWidth;
    screen_y = screen.availHeight;
    webUrl = encodeURIComponent(location.href);
    convert_click_flag = false;
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
var convert_click_flag = null;
var paclCodeObject = new Object();
var ptagParamater = window.dataLayer;
var paclCodeJson = null;
var paclCodeObject = null;
var ptagParamater = null;

function do_a(callback) {
    paclCodeObject = new Object();
    paclCodeObject["data"] = {};
    ptagParamater = window.dataLayer;
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
            pa_id = ptagType.paid
        }
        if (element[0] == undefined) {
            return
        }
        if (element[1] == 'convert' || element[1] == 'page_view' || element[1] == 'tracking') {
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
        }
    })
};

function doSendPaclData() {
    for (var key in paclCodeObject.data) {
        if (key.includes('convert')) {
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
        if (key.includes('tracking')) {
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
        if (key.includes('page_view')) {
            page_view_opt1 = paclCodeObject.data[key].page_view_opt1;
            page_view_opt2 = paclCodeObject.data[key].page_view_opt2;
            pa_em_value = paclCodeObject.data[key].pa_em_value;
            pa_id = paclCodeObject.data[key].pa_id;
            doPageView()
        }
    }
};

function doConvert() {
    var paclUrl = "https://pacl.pchome.com.tw/api/collect";
    if (location.href.indexOf("http://")) {
        paclUrl = "http://pacl.pchome.com.tw/api/collect"
    }
    var img = new Image();
    img.src = paclUrl + "?" + "fingerId=" + "" + "&paId=" + encodeURIComponent(pa_id) + "&screenX=" + encodeURIComponent(screen_x) + "&screenY=" + encodeURIComponent(screen_y) + "&paEmValue=" + encodeURIComponent(pa_em_value) + "&url=" + encodeURIComponent(webUrl) + "&paEvent=convert&convertId=" + encodeURIComponent(convert_id) + "&convertPrice=" + encodeURIComponent(convert_price) + "&op1=" + encodeURIComponent(convert_opt1) + "&op2=" + encodeURIComponent(convert_opt2) + "&referer=" + encodeURIComponent(referer)
};

function doPageView() {
    var paclUrl = "https://pacl.pchome.com.tw/api/collect";
    if (location.href.indexOf("http://")) {
        paclUrl = "http://pacl.pchome.com.tw/api/collect"
    }
    var img = new Image();
    img.src = paclUrl + "?" + "fingerId=" + "" + "&paId=" + encodeURIComponent(pa_id) + "&screenX=" + encodeURIComponent(screen_x) + "&screenY=" + encodeURIComponent(screen_y) + "&paEmValue=" + encodeURIComponent(pa_em_value) + "&url=" + encodeURIComponent(webUrl) + "&paEvent=page_view&op1=" + encodeURIComponent(page_view_opt1) + "&op2=" + encodeURIComponent(page_view_opt2) + "&referer=" + encodeURIComponent(referer)
};

function doTracking() {
    var paclUrl = "https://pacl.pchome.com.tw/api/collect";
    if (location.href.indexOf("http://")) {
        paclUrl = "http://pacl.pchome.com.tw/api/collect"
    }
    var img = new Image();
    img.src = paclUrl + "?" + "fingerId=" + "" + "&paId=" + encodeURIComponent(pa_id) + "&screenX=" + encodeURIComponent(screen_x) + "&screenY=" + encodeURIComponent(screen_y) + "&paEmValue=" + encodeURIComponent(pa_em_value) + "&url=" + encodeURIComponent(webUrl) + "&paEvent=tracking&trackingId=" + encodeURIComponent(tracking_id) + "&prodId=" + encodeURIComponent(prod_id) + "&prodPrice=" + encodeURIComponent(prod_price) + "&prodDis=" + encodeURIComponent(prod_dis) + "&op1=" + encodeURIComponent(tracking_opt1) + "&op2=" + encodeURIComponent(tracking_opt2) + "&referer=" + encodeURIComponent(referer) + "&ecStockStatus=" + encodeURIComponent(ec_stock_status)
};

function pchome_click(link_url, blank_flag) {
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

function pchome_click() {
    do_a(function() {
        doInitData()
    });
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
}