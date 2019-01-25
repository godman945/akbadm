$(document).ready(function () {

    //自定義隱藏欄位
    $.tablesorter.addParser({
        // set a unique id
        id: 'rangesort',
        is: function(s) {
        	s = s.replace("$ ", "");
            return /^[0-9]?[0-9,\.]*$/.test(s);
        },
        format: function(s) {
        	s = s.replace("$ ", "");
            return $.tablesorter.formatFloat(s.replace(/,/g, ''));
        },
        // set type, either numeric or text
        type: 'numeric'
    });

});
