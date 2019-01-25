function previewVideo(akbPfpServer, width, height, adPreviewVideoURL, adPreviewVideoBgImg, realUrl) {
	var previewUrl = "adVideoModel.html?adPreviewVideoURL=" + adPreviewVideoURL + "&adPreviewVideoBgImg=" + adPreviewVideoBgImg + "&realUrl=" + realUrl;
	var myWindow = window.open("", "", "width=" + width + ",height=" + height);
	myWindow.document.write("<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><body><iframe class='akb_iframe' scrolling='no' frameborder='0' marginwidth='0' marginheight='0' vspace='0' hspace='0' id='pchome8044_ad_frame1' width='" + width + "' height='" + height + "' allowtransparency='true' allowfullscreen='true' src='" + previewUrl + "'></iframe></body></html>");
}
