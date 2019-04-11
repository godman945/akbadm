var iframeArrayData = window.parent.document.getElementsByTagName("iframe");
var iframeArray = [];
for (var i = 0; i < iframeArrayData.length; i++) {
    if (iframeArrayData[i].id == "pchome8044_ad_frame1") {
        iframeArray.push(iframeArrayData[i]);
    }
}
var iframeInfoMap = new Object();
document.addEventListener('DOMContentLoaded',
function() {
    for (var i = 0; i < iframeArray.length; i++) {
        iframeArray[i].onload = function() {
            var video = this.contentDocument.body.querySelector(".home-banner");
            if (video != null) {
                var playbtn = video.parentElement.querySelector('#video-playbtn');
                var pausebtn = video.parentElement.querySelector('#video-pausebtn');
                var replaybtn = video.parentElement.querySelector('#video-replaybtn');
                var videoSoundoff = video.parentElement.parentElement.parentElement.querySelector('#video-soundoff');
                var videoSoundOn = video.parentElement.parentElement.parentElement.querySelector('#video-soundon');
                var videoCountdown = video.parentElement.parentElement.parentElement.querySelector('#video-countdown');
                var videoIconbox = video.parentElement.parentElement.parentElement.querySelector('.video-iconbox');
                var videoBtn = video.parentElement.querySelector('.video-btn');
                var adlinkbtn1 = video.parentElement.parentElement.parentElement.parentElement.querySelector('#ad-linkbtn');
                var adlinkbtn2 = video.parentElement.querySelector('#video-linkbtn');
                var videoControlbar = video.parentElement.parentElement.parentElement.querySelector('.video-controlbar');
                if (this.src.indexOf('na.gif') >= 0) {
                    adlinkbtn1.setAttribute("style", "opacity:1;position:absolute;background-color:black;");
                } else {
                    adlinkbtn1.setAttribute("style", "opacity:1;position:absolute;");
                }
                playbtn.addEventListener('click',
                function() {
                    PlayHandler(video);
                });
                replaybtn.addEventListener('click',
                function() {
                    ReplayHandler(video);
                });
                video.addEventListener('click',
                function() {
                    PauseHandler(video);
                });
                pausebtn.addEventListener('click',
                function() {
                    PlayHandler(video);
                });
                videoSoundoff.addEventListener('click',
                function() {
                    var videoSoundoffObj = this.parentElement.querySelector('#video-soundoff');
                    var videoSoundOnObj = this.parentElement.querySelector('#video-soundon');
                    VideoSound(false, videoSoundoffObj, videoSoundOnObj, video);
                });
                videoSoundOn.addEventListener('click',
                function() {
                    var videoSoundoffObj = this.parentElement.querySelector('#video-soundoff');
                    var videoSoundOnObj = this.parentElement.querySelector('#video-soundon');
                    VideoSound(true, videoSoundoffObj, videoSoundOnObj, video);
                });
                adlinkbtn1.style.opacity = 1;
                adlinkbtn2.style.display = 'block';
                video.load();
                var url = location.href;
                url = location.href.split('&');
                var resizeFlag = false;
                for (var i in url) {
                    if (url[i].indexOf('resize') >= 0) {
                        var resizeInfo = url[i].split('=');
                        resizeFlag = resizeInfo[1];
                    }
                }
                var css = document.createElement("style");
                
                css.type = "text/css";
                if((this.width == 1400)){
                	this.width = 970;
                	this.height = 250;
                }
                
                var adratio = this.height / this.width;
                var adw = null;
               
                if (resizeFlag == 'true') {
                    adw = 250;
                    var adh = adw * adratio;
                    var barh = 30;
                    var vdow;
                    var vdoh = adh - barh;
                    var ratio = 0.5625;
                    var xcenter = false;
                    var ycenter = true;
                    var xpos;
                    var ypos;
                    var imgWidth;
                    var imgHeight;
                    var specialTemplate = false;
                    if ((this.width == 970 && this.height == 250) || (this.width == 950 && this.height == 390)) {
                        specialTemplate = true;
                    }
                    
                    var previewRatio = adw / this.width;
                    var previewHeight = previewRatio * this.height;
                    var barhRatio = barh / this.height;
                    barh = previewHeight * barhRatio;
                    if (specialTemplate) {
                        //計算字幕高度
                        vdoh = previewHeight - barh;
                        vdow = vdoh / ratio;
                        imgWidth = adw - vdow;
                        imgHeight = previewHeight;
                        xpos = (!xcenter) ? 0 : (adw - vdow) / 2;
                        ypos = 0;
                        vdow += "px";
                        if (navigator.userAgent.match("Safari")) {
                            videoCountdown.setAttribute("style", "margin-top:-10px;-webkit-transform:scale(0.45);");
                        } else {
                            videoCountdown.setAttribute("style", "margin-top:-10px;font-size:6px;");
                        }
                        if (this.width == 970 && this.height == 250) {
                            videoIconbox.setAttribute("style", "margin-top:-30px;");
                            videoSoundoff.setAttribute("style", "margin-left:-125px;position:fixed;margin-top:25px;");
                        } else {
                            videoIconbox.setAttribute("style", "margin-top:-10px;");
                        }
                        videoBtn.setAttribute("style", "margin-top:-10px;");
                        pausebtn.setAttribute("style", "width:35px;margin-left:-35px;display:none");
                        replaybtn.setAttribute("style", "width:35px;margin-left:-35px;display:none");
                        playbtn.setAttribute("style", "width:35px;margin-left:-35px");
                        videoControlbar.setAttribute("style", "height:" + barh + "px;");
                    } else {
                        imgWidth = adw;
                        imgHeight = previewHeight;
                        vdow = 100;
                        xpos = 0;
                        ypos = (!ycenter) ? 0 : (adh - adw * 0.5625 - 30) / 2;
                        vdow += "%";
                    }
                    //verticalAd:Y為直立廣告
                	if(this.contentDocument.body.querySelector("#verticalAd").value == 'Y'){
                		var barh=30
                		var vdow=100;
                		var vdoh=adh-barh;
                		var ratio=1.7778;
                		var xcenter=true;
                		var ycenter=true;
                		var xpos;
                		var ypos;
                		if (vdoh/adw<ratio){
                			vdow=vdoh/ratio;
                			xpos=(!xcenter)?0:(adw-vdow)/2;
                			vdow+="px";
                		}else{
                			vdow=100;
                			xpos=0
                			ypos=(!ycenter)?0:(adh-adw*0.5625-30)/2;
                			vdow+="%";
                		}
                		css.innerHTML = ".adw{width:" + adw + "px}.adh{height:" + adh + "px}";
                	}else{
                		css.innerHTML = ".adw{width:" + adw + "px}.adh{height:" + adh + "px}.vdow{width:" + vdow + "}.xpos{left:" + xpos + "px}.ypos{top:" + ypos + "px}";
                	}
                    this.contentDocument.childNodes[0].getElementsByTagName("head")[0].appendChild(css);
                    var adbg = this.contentDocument.childNodes[0].querySelector('.adbg');
                    adbg.setAttribute("style", "background-size:" + imgWidth + "px " + imgHeight + "px;");
                    this.width = adw;
                    this.height = adh;
                } else {
                    adw = this.width;
                    var adh = adw * adratio;
                    var barh = 30;
                    var vdow;
                    var vdoh = adh - barh;
                    var ratio = 0.5625;
                    var xcenter = false;
                    var ycenter = true;
                    var xpos;
                    var ypos;
                    var adbg = "";
                    if (vdoh / adw < ratio) {
                        vdow = vdoh / ratio;
                        xpos = (!xcenter) ? 0 : (adw - vdow) / 2;
                        ypos = 0;
                        vdow += "px";
                    } else {
                        vdow = 100;
                        xpos = 0;
                        ypos = (!ycenter) ? 0 : (adh - adw * 0.5625 - 30) / 2;
                        vdow += "%";
                    }
                    
                    //verticalAd:Y為直立廣告
                	if(this.contentDocument.body.querySelector("#verticalAd").value == 'Y'){
                		css.innerHTML = ".adw{width:" + adw + "px}.adh{height:" + adh + "px}";
                	}else{
                		css.innerHTML = ".adw{width:" + adw + "px}.adh{height:" + adh + "px}.vdow{width:" + vdow + "}.xpos{left:" + xpos + "px}.ypos{top:" + ypos + "px}";
                	}
                    this.contentDocument.childNodes[0].getElementsByTagName("head")[0].appendChild(css);
                    var adbg = this.contentDocument.childNodes[0].querySelector('.adbg');
                    if (this.width == 970) {
                        adbg.setAttribute("style", "height:250px");
                    } else if (this.width == 950) {
                        adbg.setAttribute("style", "height:390px");
                    } else {
                        adbg.setAttribute("style", "height:" + this.height + "px");
                    }
                }
            }
        };
    }
    function ReplayHandler(video) {
        if (video == null || video == undefined) {
            return false;
        }
        var index = getIframeIndex(video);
        var timeVideo = setInterval('alex(' + index + ')', "300");
        iframeInfoMap["iframe" + index] = {
            timmer: timeVideo
        };
        var playbtn = video.parentElement.querySelector('#video-playbtn');
        var pausebtn = video.parentElement.querySelector('#video-pausebtn');
        var replaybtn = video.parentElement.querySelector('#video-replaybtn');
        var adlinkbtn1 = video.parentElement.parentElement.parentElement.parentElement.querySelector('#ad-linkbtn');
        var adlinkbtn2 = video.parentElement.querySelector('#video-linkbtn');
        var videoSoundOn = video.parentElement.parentElement.parentElement.querySelector('#video-soundon');
        if (videoSoundOn.style.display == 'block') {
            video.muted = false;
            video.volume = 0.1;
        }
        playbtn.style.display = 'none';
        pausebtn.style.display = 'none';
        replaybtn.style.display = 'none';
        adlinkbtn1.style.opacity = 0;
        adlinkbtn2.style.display = 'none';
        video.play();
    }
    function PlayHandler(video) {
        if (video == null || video == undefined) {
            return false;
        }
        var playbtn = video.parentElement.querySelector('#video-playbtn');
        var pausebtn = video.parentElement.querySelector('#video-pausebtn');
        var replaybtn = video.parentElement.querySelector('#video-replaybtn');
        var adlinkbtn1 = video.parentElement.parentElement.parentElement.parentElement.querySelector('#ad-linkbtn');
        var adlinkbtn2 = video.parentElement.querySelector('#video-linkbtn');
        playbtn.style.display = 'none';
        pausebtn.style.display = 'none';
        replaybtn.style.display = 'none';
        adlinkbtn1.style.opacity = 0;
        adlinkbtn2.style.display = 'none';
        var index = getIframeIndex(video);
        video.play();
        var timeVideo = setInterval('alex(' + index + ')', '300');
        iframeInfoMap["iframe" + index] = {
            timmer: timeVideo
        };
    }
    function PauseHandler(video) {
        if (video == null || video == undefined) {
            return false;
        }
        var index = getIframeIndex(video);
        clearInterval(iframeInfoMap["iframe" + index].timmer);
        var playbtn = video.parentElement.querySelector('#video-playbtn');
        var pausebtn = video.parentElement.querySelector('#video-pausebtn');
        var adcountdown = video.parentElement.parentElement.parentElement.querySelector('#video-countdown');
        var adlinkbtn1 = video.parentElement.parentElement.parentElement.parentElement.querySelector('#ad-linkbtn');
        var adlinkbtn2 = video.parentElement.querySelector('#video-linkbtn');
        pausebtn.style.display = 'block';
        adcountdown.style.display = 'block';
        adlinkbtn1.style.opacity = 1;
        adlinkbtn2.style.display = 'block';
        video.pause();
    }
    function VideoSound(flag, videoSoundoffObj, videoSoundOnObj, video) {
        if (flag) {
            video.muted = true;
            videoSoundOnObj.style.display = 'none';
            videoSoundoffObj.style.display = 'block';
        } else {
            video.muted = false;
            video.volume = 0.1;
            videoSoundOnObj.style.display = 'block';
            videoSoundoffObj.style.display = 'none';
            var index = getIframeIndex(video);
        }
    }
    function getIframeIndex(video) {
        for (var i = 0; i < iframeArray.length; i++) {
            if (iframeArray[i].contentDocument.body.querySelector(".home-banner") == video) {
                return i;
            }
        }
    }
},
false);
function alex(index) {
    var video = iframeArray[index].contentDocument.body.querySelector(".home-banner");
    var adcountdown = video.parentElement.parentElement.parentElement.querySelector('#video-countdown');
    var ttime = Math.ceil(video.duration - video.currentTime);
    var percent = Math.ceil((video.currentTime / video.duration) * 100);
    if (!video.paused) {
        adcountdown.style.display = 'block';
        var time = "影片倒數 " + formatSecond(ttime);
        if (adcountdown.innerHTML != time) {
            adcountdown.textContent = time.toString();
        }
    } else if (ttime == 0) {
        var replaybtn = video.parentElement.querySelector('#video-replaybtn');
        var pausebtn = video.parentElement.querySelector('#video-pausebtn');
        var adlinkbtn1 = video.parentElement.parentElement.parentElement.parentElement.querySelector('#ad-linkbtn');
        var adlinkbtn2 = video.parentElement.querySelector('#video-linkbtn');
        clearInterval(iframeInfoMap["iframe" + index].timmer);
        adcountdown.style.display = 'none';
        replaybtn.style.display = 'block';
        pausebtn.style.display = 'none';
        adcountdown.innerHTML = "";
        adlinkbtn1.style.opacity = 1;
        adlinkbtn2.style.display = 'block';
        video.load();
    } else if (video.paused) {
        clearInterval(iframeInfoMap["iframe" + index].timmer);
    }
}
function formatSecond(secs) {
    var min = Math.floor(secs / 60);
    var sec = parseInt(secs - (min * 60));
    if (sec < 10) {
        sec = '0' + sec;
    }
    return min + ' : ' + sec;
}