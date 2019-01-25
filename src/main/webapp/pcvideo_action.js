window.onload = function() {
	// document.domain = "nicolee.pchome.com.tw";

	//document.domain="pchome.com.tw";
	var iframeArray = window.parent.parent.document.getElementsByTagName("iframe");
	var scrollTop = window.parent.parent.document.body.scrollTop || window.parent.parent.document.documentElement.scrollTop;
	alert(iframeArray.length)
	var timeVideo;
	document.addEventListener('DOMContentLoaded', function () {
		window.parent.parent.document.addEventListener('scroll', function() {
			playController();
		});
		
		window.parent.parent.addEventListener('resize', function() {
			playController();
		});

		window.parent.parent.document.addEventListener('visibilitychange', function() {			
			if(document.hidden){
				console.log('我切換了');
				playVisibilitychangeController(false);
			}else{
				console.log('我回來了');
				playVisibilitychangeController(true);
			}
		},false);	
			
		function playController(){
			try{
				/*滾輪移動距離*/
				var scrollTop = window.parent.parent.document.body.scrollTop || window.parent.parent.document.documentElement.scrollTop;
				/*視窗可視大小*/
				var viewHeight = window.parent.parent.innerHeight;
				for(var i = 0; i< iframeArray.length; i++){	
					var video = iframeArray[i].contentDocument.body.children[0].contentWindow.document.querySelector(".home-banner");
					if(iframeArray[i].contentDocument.body.children[0] == undefined){
						continue;
					}
						
					if(video == null){
						continue;
					}

					video.style.width = iframeArray[i].width+'px';
					video.style.height = iframeArray[i].height+'px';

					/*iframe頂部距離最上方絕對位置*/
					var iframeOffSetTop = iframeArray[i].offsetTop;
					/*iframe底部距離最上方距離*/
					var iframeBottom = iframeArray[i].getBoundingClientRect().bottom;
					/*iframe頂部距離最上方距離*/
					var iframeTop = iframeArray[i].getBoundingClientRect().top;
					/*iframe高度一半*/
					var iframeHalf = iframeArray[i].height / 2; 
					/*控制高度可視範圍高度扣除iframe高度*/
					var controllerHeight = (viewHeight - iframeArray[i].height) + iframeHalf;			
					if(iframeOffSetTop >= scrollTop){
						if(iframeBottom > 0){
							if(iframeTop <= controllerHeight){
								if(video.paused){
									console.log("iframe:"+i+" 是否播放:"+true);
									video.play();
									timeVideo = setInterval('alex('+i+')',"1000");
								}
							}else{
								if(!video.paused){
									console.log("iframe:"+i+" 是否播放:"+false);
									video.pause();
									clearInterval(timeVideo);
								}
							}
						}
					}else{
						if(iframeBottom < iframeHalf){
							if(!video.paused){
								console.log("iframe:"+i+" 是否播放:"+false);
								video.pause();
								clearInterval(timeVideo);
							}
						}else{
							if(video.paused){
								console.log("iframe:"+i+" 是否播放:"+true);
								video.play();
								timeVideo = setInterval('alex('+i+')',"1000");
							}
						}
					}
				}
			}catch(err){
				console.log(err);
			}
		}
		
		/*切換頁簽或遮屏則停止*/
		function playVisibilitychangeController(changeFlag){
			for(var i = 0; i< iframeArray.length; i++){
				if(iframeArray[i].contentDocument == null){
					continue;
				}
				
				if(iframeArray[i].contentDocument.body.children[0] == undefined){
					continue;
				}
				
				var video = iframeArray[i].contentDocument.body.children[0].contentWindow.document.querySelector(".home-banner");	
				if(video == null){
					continue;
				}
				
				/*切換進行停止影片動作*/			
				if(!changeFlag){
					if(!video.paused){
						console.log("iframe:"+i+" 是否播放:"+false);
						video.pause();
						clearInterval(timeVideo);
						console.log('因為切換我停止影片了');
					}
				}else{
					playController();
				}
			}
		}
	}, false);
	
	
	function alex(index){
		var video = iframeArray[index].contentDocument.body.children[0].contentWindow.document.querySelector(".home-banner");
		if(!video.paused){
			console.log('iframe:'+index +'目前:'+video.currentTime+'秒');
		}else{
			console.log('我停止影片了');
			clearInterval(timeVideo);
		}
	}
	
	/*影片屬性&&點擊*/
	var video=document.getElementsByTagName('video')[0];
	video.removeAttribute('controls');
	video.removeAttribute('autoplay');
	video.removeAttribute('loop');
	video.onclick=function(){video[video.paused?'play':'pause']();};

	/*播放&&重新播放按鈕*/
	var pauseTime=0;
	var playbtn=document.getElementById('video-playbtn');
	var pausebtn=document.getElementById('video-pausebtn');
	var replaybtn=document.getElementById('video-replaybtn');
	playbtn.onclick=function(){
		video.play();
	};
	pausebtn.onclick=function(){
		video.play();
	};
	replaybtn.onclick=function(){
		video.play();
	};

	/*聲音&&靜音按鈕*/
	var sndonbtn=document.getElementById('video-soundon');
	var sndoffbtn=document.getElementById('video-soundoff');
	sndoffbtn.onclick=function(){
		video.muted=false;
		this.style.display='none';
		sndonbtn.style.display='block';

	};
	sndonbtn.onclick=function(){
		video.muted=true;
		this.style.display='none';
		sndoffbtn.style.display='block';
	};

	/*廣告連結按鈕*/
	var adlinkbtn1=document.getElementById('adlinkbtn');
	var adlinkbtn2=document.getElementById('video-adlinkbtn');

	/*廣告秒數倒數*/
	var adcountdown=document.getElementById('video-countdown');

	video.addEventListener('progress',ProgresHandler,false);
	video.addEventListener('durationchange',DurationchangeHandler,false);
	video.addEventListener('loadedmetadata',MetadataHandler,false);
	video.addEventListener('canplay',CanplayHandler,false);
	video.addEventListener('play',PlayHandler,false);
	video.addEventListener('pause',PauseHandler,false);
	video.addEventListener('timeupdate',TimeupdateHandler,false);
	video.addEventListener('ended',EndedHandler,false);

	//document.addEventListener("fullscreenchange",FsHandler);
	//document.addEventListener("webkitfullscreenchange",FsHandler);
	//document.addEventListener("mozfullscreenchange",FsHandler);
	//document.addEventListener("MSFullscreenChange",FsHandler);
	
	function ProgresHandler(){
		/*影片下載處理中*/
	}
	function DurationchangeHandler(){

	}
	function MetadataHandler(){
		/*讀取影片資訊*/
		var metadatas=[this.duration,this.videoWidth,this.videoHeight];
		console.log('總秒數: '+metadatas[0]+'\n寬度: '+metadatas[1]+'\n高度: '+metadatas[2]);
	}
	function CanplayHandler(){
		/*可開始播放*/
		/*send pv*/
	}
	function PlayHandler(){
		/*播放影片*/
		/*send click*/
		this.currentTime=pauseTime;
		playbtn.style.display='none';
		pausebtn.style.display='none';
		replaybtn.style.display='none';
		adlinkbtn1.style.opacity=0;
		adlinkbtn2.style.display='none';
		adcountdown.style.display='block';
	}
	function PauseHandler(){
		/*暫停影片*/
		/*send click*/
		pauseTime=this.currentTime;
		pausebtn.style.display='block';
		//this.load();
	}
	function EndedHandler(){
		/*影片結束*/
		/*send complete*/
		pauseTime=0;
		video.load();
		adlinkbtn1.style.opacity=1;
		adlinkbtn2.style.display='block';
		adcountdown.style.display='none';
		replaybtn.style.display='block';
		pausebtn.style.display='none';
		//exitFS();
	}
	function TimeupdateHandler(){
		/*cue點更新*/
		/*send second record*/
		console.log(this.currentTime);
		var ttime=Math.ceil(this.duration-this.currentTime);
		adcountdown.innerHTML=formatSecond(ttime);
	}
	function formatSecond(secs) {          
         var min=Math.floor(secs/60);
         var sec=parseInt(secs-(min*60));
         if (sec<10) {sec='0'+sec;}
         return min+':'+sec;
     }

	function FsHandler(){
		if(document.fullscreenElement||document.webkitFullscreenElement||document.mozFullScreenElement||document.msFullscreenElement){
		}else{
		}
	}
	function exitFS(){
		if(document.exitFullscreen){
			document.exitFullscreen();
		}else if(document.webkitExitFullscreen){
			document.webkitExitFullscreen();
		}else if(
			document.mozCancelFullScreen){
			document.mozCancelFullScreen();
		}else if(
			document.msExitFullscreen){
			document.msExitFullscreen();
		}
	}
	
	
	
	
}
