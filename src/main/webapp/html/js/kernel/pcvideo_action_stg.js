	var urlInfo = location.href;
	var paramaterArray = location.href.split("&");
	
	var padWidth = 0;
	var padHeight = 0;
	paramaterArray.forEach(function(element) {
		if(element.indexOf('padHeight') >= 0){
			padHeight = element.split("=")[1];
		}
		if(element.indexOf('padWidth') >= 0){
			padWidth = element.split("=")[1];
		}
		
	});

	var timeVideo;
	var iframeInfoMap = new Object();
	var video = null;
	document.addEventListener('DOMContentLoaded', function () {
		video = document.getElementsByTagName("video")[0];		
		iframeInfoMap["iframe"] = {adView:false,vpv:false,timer:null,percent25:false,percent50:false,percent75:false,percent100:false,clickPause:false};
		var adSize = document.querySelector("#adSize");
		if(video != null && video != undefined){
			var playbtn = video.parentElement.querySelector('#video-playbtn');
			var pausebtn = video.parentElement.querySelector('#video-pausebtn');
			var replaybtn = video.parentElement.querySelector('#video-replaybtn');
			var videoSoundoff = video.parentElement.parentElement.parentElement.querySelector('#video-soundoff');
			var videoSoundOn = video.parentElement.parentElement.parentElement.querySelector('#video-soundon');
			var adlinkbtn1 = video.parentElement.parentElement.parentElement.parentElement.querySelector('#ad-linkbtn');
			var adlinkbtn2 = video.parentElement.querySelector('#video-linkbtn');
			var kwstg1Url = adlinkbtn2.href;
			var videoSoundoffObj = video.parentElement.parentElement.parentElement.querySelector('#video-soundoff');
			var videoSoundOnObj = video.parentElement.parentElement.parentElement.querySelector('#video-soundon');
			
			adlinkbtn1.style.opacity=1;
			adlinkbtn2.style.display='block';
			
			playbtn.addEventListener('click', function() {
				PlayHandler(video);
			});
			replaybtn.addEventListener('click', function() {
				ReplayHandler(video);
			});
			video.addEventListener('click', function() {
				PauseHandler(video,true);
			});
			pausebtn.addEventListener('click', function() {
				PlayHandler(video);
			});
			videoSoundoff.addEventListener('click', function() {
				VideoSound(false,videoSoundoffObj,videoSoundOnObj,video);
			});
			videoSoundOn.addEventListener('click', function() {
				VideoSound(true,videoSoundoffObj,videoSoundOnObj,video);
			});
			
				
			var css = document.createElement("style");
			css.type = "text/css";				
			var adratio = padHeight / padWidth;
			var adw = padWidth;
			var adh = adw * adratio;
			var barh = 30;
			var vdow;
			var vdoh = adh - barh;
			var ratio = 0.5625;		
			var xcenter=false;
			var ycenter=true;
			var xpos;
			var ypos;
			var adbg = "";
			var poster = "img/video/missdior.gif";
					
			if (vdoh/adw<ratio){
				vdow=vdoh/ratio;
				xpos=(!xcenter)?0:(adw-vdow)/2;
				ypos=0;
				vdow+="px";
			}else{
				vdow=100;
				xpos=0;
				ypos=(!ycenter)?0:(adh-adw*0.5625 - 30)/2;
				vdow+="%";
			}
			if(document.getElementById("adSize").value == '970_250'){
				adw = 391;
			}
			if(document.getElementById("adSize").value == '950_390'){
				adw = 640;
			}
			//verticalAd:Y為直立廣告
			if (document.body.querySelector("#verticalAd") != undefined && document.body.querySelector("#verticalAd") != null) {
				if(document.body.querySelector("#verticalAd").value == 'Y'){
	        		css.innerHTML = ".adw{width:" + adw + "px}.adh{height:" + adh + "px}";
	        	}else{
	        		css.innerHTML = ".adw{width:"+adw+"px,}.adh{height:"+vdoh+"px}.vdow{width:"+vdow+"}.xpos{left:"+xpos+"px}.ypos{top:"+ypos+"px}}";
	        	}
			}
			document.getElementsByTagName("head")[0].appendChild(css);
			var adbg = document.querySelector('.adbg');
			
			if(document.getElementById("adSize").value == '970_250'){
				adbg.setAttribute("style", "height:250px");
			}else if(document.getElementById("adSize").value == '950_390'){
				adbg.setAttribute("style", "height:390px");
			}else{
				adbg.setAttribute("style", "height:"+padHeight+"px");
			}
		}
	
		function ReplayHandler(video){
			if(video == null || video == undefined){
				return false;
			}
			timeVideo = setInterval('alex()',"50");
			iframeInfoMap["iframe"].timer = timeVideo;
			iframeInfoMap["iframe"].percent25 = false;
			iframeInfoMap["iframe"].percent50 = false;
			iframeInfoMap["iframe"].percent75 = false;
			iframeInfoMap["iframe"].percent100 = false;
			
			var playbtn = video.parentElement.querySelector('#video-playbtn');
			var pausebtn = video.parentElement.querySelector('#video-pausebtn');
			var replaybtn = video.parentElement.querySelector('#video-replaybtn');
			var adlinkbtn1 = video.parentElement.parentElement.parentElement.parentElement.querySelector('#ad-linkbtn');
			var adlinkbtn2 = video.parentElement.querySelector('#video-linkbtn');
			var kwstg1Url = adlinkbtn2.href;
			var soundoff = video.parentElement.parentElement.parentElement.querySelector('#video-soundoff');
			playbtn.style.display='none';
			pausebtn.style.display='none';
			replaybtn.style.display='none';
			adlinkbtn1.style.opacity=0;
			adlinkbtn2.style.display='none';
			video.play();
			
			if(soundoff.display == 'none'){
				video.muted = false;
				video.volume = 0.1;
			}		
			/*呼叫重新播放log API*/
			callKwstgLog("adVideoReplay","1",kwstg1Url);
			//3秒觀看控制恢復原狀
			iframeInfoMap["iframe"].clickPause = false;
		}

		/*開始播放*/
		function PlayHandler(video){
			if(video == null || video == undefined){
				return false;
			}
			var playbtn = video.parentElement.querySelector('#video-playbtn');
			var pausebtn = video.parentElement.querySelector('#video-pausebtn');
			var replaybtn = video.parentElement.querySelector('#video-replaybtn');
			var adlinkbtn1 = video.parentElement.parentElement.parentElement.parentElement.querySelector('#ad-linkbtn');
			var adlinkbtn2 = video.parentElement.querySelector('#video-linkbtn');
			var kwstg1Url = video.parentElement.querySelector('#video-linkbtn').href;
			playbtn.style.display='none';
			pausebtn.style.display='none';
			replaybtn.style.display='none';
			adlinkbtn1.style.opacity=0;
			adlinkbtn2.style.display='none';
			video.play();
			
			//取得監聽播放時間物件
			timeVideo = setInterval('alex()',"50");
			iframeInfoMap["iframe"].clickPause = false;			
			iframeInfoMap["iframe"].timer = timeVideo;
			/*呼叫播放log API*/
			callKwstgLog("adVideoPlay","1",kwstg1Url);
			
		}
		
		/*暫停播放*/
		function PauseHandler(video,isClick){
			if(video == null || video == undefined){
				return false;
			}

			//停止監聽播放時間物件
			if(!isClick){
				return false;
			}
			var adlinkbtn1 = video.parentElement.parentElement.parentElement.parentElement.querySelector('#ad-linkbtn');
			var adlinkbtn2 = video.parentElement.querySelector('#video-linkbtn');
			var playbtn = video.parentElement.querySelector('#video-playbtn');
			var pausebtn = video.parentElement.querySelector('#video-pausebtn');
			var adcountdown = video.parentElement.parentElement.parentElement.querySelector('#video-countdown');
			
			if(isClick){
				iframeInfoMap["iframe"].clickPause = true;
			}
			
			clearInterval(iframeInfoMap["iframe"].timer);
			pausebtn.style.display = 'block';
			adcountdown.style.display = 'block';
			video.pause();
			adlinkbtn1.style.opacity=1;
			adlinkbtn2.style.display='block';
		}
		
		/*點擊聲音，true為關閉聲音*/
		function VideoSound(flag,videoSoundoffObj,videoSoundOnObj,video){
			if(flag){
				video.muted = true;
				videoSoundOnObj.style.display='none';
				videoSoundoffObj.style.display='block';
			}else {
				video.muted = false;
				video.volume = 0.1;
				videoSoundOnObj.style.display='block';
				videoSoundoffObj.style.display='none';
				/*呼叫開啟聲音log API*/
				var kwstg1Url = video.parentElement.querySelector('#video-linkbtn').href;
				callKwstgLog("adVideoMuted","1",kwstg1Url);
			}
		}
		
		/*切換頁簽或遮屏則停止*/
		function playVisibilitychangeController(changeFlag,video){
			var browser = navigator.userAgent.toLowerCase();
			if(browser.indexOf('firefox') >= 0){
				changeFlag = !changeFlag;
			}
			
			if(video == null || video == undefined){
				return false;
			}
			
			if(!changeFlag){
				if(!video.paused){
					//console.log('因為切換我停止影片了 '+"iframe:"+i+" 是否播放:"+false);
					scrollPauseHandler(video);
					//停止監聽播放時間物件
					clearInterval(iframeInfoMap["iframe"].timer);
				}
			}else{
				//console.log('頁簽切換回來，重新監控');
				playController();
			}
		}
		playController();
	}, false);	
	
	/*影片時間格式*/
	function formatSecond(secs) {
		if(isNaN(secs)){
			return "";
		}
		
        var min = Math.floor(secs / 60);
        var sec = parseInt(secs-(min * 60));
        if (sec < 10) {
        	sec= '0' + sec;
        }
        return min+' : '+sec;
    }

	function alex(){
		if(video == null || video == undefined){
			clearInterval(timeVideo);
			return false;
		}
		var replaybtn = video.parentElement.querySelector('#video-replaybtn');
		var pausebtn = video.parentElement.querySelector('#video-pausebtn');
		var adcountdown = video.parentElement.parentElement.parentElement.querySelector('#video-countdown');
		var ttime = Math.ceil(video.duration - video.currentTime);
		var percent = Math.ceil((video.currentTime / video.duration) * 100);
		var adlinkbtn1 = video.parentElement.parentElement.parentElement.parentElement.querySelector('#ad-linkbtn');
		var adlinkbtn2 = video.parentElement.querySelector('#video-linkbtn');
		var kwstg1Url = adlinkbtn2.href;
		if(!video.paused){
			adcountdown.style.display='block';
			var time = "影片倒數 "+ formatSecond(ttime);			
			if(adcountdown.innerHTML != time){
				adcountdown.innerHTML= time;
			}
			/*3秒呼叫影音廣告觀看數log*/
			if(iframeInfoMap["iframe"].adView == false && Math.ceil(video.currentTime) == 3){
				iframeInfoMap["iframe"].adView = true;
				callKwstgLog("adView","1",kwstg1Url);
			}
			/*呼叫影音廣告播放百分比log*/
			if(percent == 25 && iframeInfoMap["iframe"].percent25 == false){
				iframeInfoMap["iframe"].percent25 = true;
				callKwstgLog("adVideoProcess","25",kwstg1Url);
			}else if(percent == 50 && iframeInfoMap["iframe"].percent50 == false){
				iframeInfoMap["iframe"].percent50 = true;
				callKwstgLog("adVideoProcess","50",kwstg1Url);
			}else if(percent == 75 && iframeInfoMap["iframe"].percent75 == false){
				iframeInfoMap["iframe"].percent75 = true;
				callKwstgLog("adVideoProcess","75",kwstg1Url);
			}else if(percent == 100 && iframeInfoMap["iframe"].percent100 == false){
				iframeInfoMap["iframe"].percent100 = true;
				callKwstgLog("adVideoProcess","100",kwstg1Url);
			}
		}
		
		if(ttime == 0){
			clearInterval(timeVideo);
			adcountdown.style.display='none';
				replaybtn.style.display='block';
				pausebtn.style.display='none';
				adcountdown.innerHTML= "";
				adlinkbtn1.style.opacity=1;
				adlinkbtn2.style.display='block';
				video.load();
		}
	}

	/*呼叫api*/
	function callKwstgLog(keyType,value,kwstg1Url){
		var xmlHttp = new XMLHttpRequest();
		var kwstg1LogApi = kwstg1Url + "&" + keyType + "=" + value;
		console.log('kwstg1LogApi:'+kwstg1LogApi);
		xmlHttp.open("GET", kwstg1LogApi, true);
		xmlHttp.send(null);
	}
	
	/*滾輪觸發播放*/
	function scrollPlayHandler(video){
		console.log(video);
		
		var playbtn = video.parentElement.querySelector('#video-playbtn');
		var pausebtn = video.parentElement.querySelector('#video-pausebtn');
		var replaybtn = video.parentElement.querySelector('#video-replaybtn');
		var kwstg1Url = video.parentElement.querySelector('#video-linkbtn').href;
		var adlinkbtn1 = video.parentElement.parentElement.parentElement.parentElement.querySelector('#ad-linkbtn');
		var adlinkbtn2 = video.parentElement.querySelector('#video-linkbtn');
		playbtn.style.display='none';
		pausebtn.style.display='none';
		replaybtn.style.display='none';
		adlinkbtn1.style.opacity=0;
		adlinkbtn2.style.display='none';
		
		//呼叫廣告可視曝光log API
		if(!iframeInfoMap["iframe"].vpv){
			callKwstgLog("vpv","1",kwstg1Url);
			iframeInfoMap["iframe"].vpv = true;
		}
		video.play();
		var timeVideo = setInterval('alex()',"50");
		iframeInfoMap["iframe"].timer = timeVideo;
	}
	
	
	/*滾輪觸發停止*/
	function scrollPauseHandler(video){
		if(video == null || video == undefined){
			return false;
		}
		clearInterval(iframeInfoMap["iframe"].timer);
		var playbtn = video.parentElement.querySelector('#video-playbtn');
		var pausebtn = video.parentElement.querySelector('#video-pausebtn');
		var adcountdown = video.parentElement.parentElement.parentElement.querySelector('#video-countdown');
		var adlinkbtn1 = video.parentElement.parentElement.parentElement.parentElement.querySelector('#ad-linkbtn');
		var adlinkbtn2 = video.parentElement.querySelector('#video-linkbtn');
		pausebtn.style.display = 'block';
		adcountdown.style.display = 'block';
		adlinkbtn1.style.opacity=1;
		adlinkbtn2.style.display='block';
		video.pause();
	}

	/*提供父階層呼叫傳遞參數*/
	window.addEventListener("message", playController, false);
	function playController(event){
		try{
			if(event != undefined){
				video = document.getElementsByTagName("video")[0];
				var adInfo = (JSON.parse(event.data)).adInfo;
				/*滾輪移動距離*/
				var scrollTop = adInfo.scrollTop;
				/*視窗可視大小*/
				var viewHeight = adInfo.viewHeight;
				/*iframe頂部距離最上方絕對位置*/
				var iframeOffSetTop = adInfo.iframeOffSetTop;
				/*iframe底部距離最上方距離*/
				var iframeBottom = adInfo.iframeBottom;
				/*iframe頂部距離最上方距離*/
				var iframeTop = adInfo.iframeTop;
				/*iframe高度一半*/
				var iframeHalf = adInfo.iframeHalf; 
				/*控制高度可視範圍高度扣除iframe高度*/
				var controllerHeight = adInfo.controllerHeight;
				/*是否切分頁*/
				var visibilitychange = adInfo.visibilitychange;
				/*重播鈕*/
				var replaybtn = video.parentElement.querySelector('#video-replaybtn');
				
				if(iframeInfoMap["iframe"].clickPause || replaybtn.style.display == 'block'){
					return false;
				}
				
				if(visibilitychange){
					scrollPauseHandler(video);
					return false;
				}
				
				if(iframeOffSetTop >= scrollTop){
					if(iframeBottom > 0){
						if(iframeTop <= controllerHeight){
							if(video.paused){
								scrollPlayHandler(video);
							}
						}else{
							if(!video.paused){
								scrollPauseHandler(video);
							}
						}
					}
				}else{
					if((iframeBottom - viewHeight)> iframeHalf ){
						if(!video.paused){
							scrollPauseHandler(video);
						}
					}else if(iframeBottom < iframeHalf){
						if(!video.paused){
							scrollPauseHandler(video);
						}
					}else{
						if(video.paused){
							scrollPlayHandler(video);
						}
					}
				}
			}
		}catch(err){
			console.log(err);
		}
	}