(function() {
    (function() {
        var paclUrl = "https://pacl.pchome.com.tw/js/ptag2.js";
        if (location.href.indexOf("http://")) {
            paclUrl = "http://pacl.pchome.com.tw/js/ptag2.js"
        }
        var el = document.createElement('script');
        el.src = paclUrl;
        el.async = 'true';
        el.addEventListener('DOMContentLoaded', function() {});
        document.head.appendChild(el)
    })()
})(window);