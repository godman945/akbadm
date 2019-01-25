// check necessary
function isFill(id){
    if(id.value == ""){
        id.focus();
        return false;
    }
    return true;
}

// check radio
function checkRadio(obj){
    for(var i=0; i<obj.length; i++){
        if(obj[i].checked) {
            return true;
        }
    }
    return false;
}

//check number
function isNumeric(text){
    if(text == ""){
        return false;
    }
    
    var num = "0123456789";
    for(var i = 0; i < text.length; i++){
        var ch = text.charAt(i);
        if(num.indexOf(ch) < 0){
            return false;
        }
    }
    return true;
}

// check English and number
function checkEngNum(str) {
    var flag = false;
    if (str != null) {
        var length = str.length;
        if (length > 0) {
            // check valid char
            flag = true;
            for (var i = 0; i < length; i++) {
                var c = str.charAt(i);
                if(!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'))) {
                    flag = false;
                    break;
                }
            }
        }
    }
    return flag;
}

function checkEmail(email) {
    var flag = false;
    if (email != null) {
        var length = email.length;
        if (length > 0) {
            // check valid char
            flag = true;
            for (var i = 0; i < length; i++) {
                var c = email.charAt(i);
                if(!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || (c == '-') || (c == '_') || (c == '.') || (c == '@'))) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                // there is no @ || @ is head || @ is end
                if((email.indexOf("@") == -1) || (email.indexOf("@") == 0) || (email.indexOf("@") == (length-1))) {
                    flag = false;
                }
                // duplicate @
                if((email.indexOf("@") != -1) && (email.substring(email.indexOf("@")+1, length).indexOf("@") != -1)) {
                    flag = false;
                }
                // there is no . || . is head || . is end
                if((email.indexOf(".") == -1) || (email.indexOf(".") == 0) || (email.lastIndexOf(".") == (length-1))) {
                    flag = false;
                }
            }
        }
    }
    return flag;
}