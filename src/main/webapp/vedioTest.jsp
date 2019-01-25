<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>

<script >
getBlobURL("http://vstreamdev.mypchome.com.tw/Sony_4K_Demo_Another_World_360P_60S.mp4", "video/mp4", function(url, blob) {
    var source = $("<source>");
    source[0].src = url;

    $("#video-bg").append(source);
});


function getBlobURL(url, mime, callback) {
  var xhr = new XMLHttpRequest();
  xhr.open("get", url);
  xhr.responseType = "arraybuffer";

  xhr.addEventListener("load", function() {

    var arrayBufferView = new Uint8Array( this.response );
    var blob = new Blob( [ arrayBufferView ], { type: mime } );
    var url = null;

    if ( window.webkitURL ) {
        url = window.webkitURL.createObjectURL(blob);
    } else if ( window.URL && window.URL.createObjectURL ) {
        url = window.URL.createObjectURL(blob);
    }

    callback(url, blob);
  });
  xhr.send();
}

</script>
</head>
<body>
nico

<video controls id="video-bg"></video>

</body>
</html>