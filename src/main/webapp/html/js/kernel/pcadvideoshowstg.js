	document.domain="pchome.com.tw";
	var iframeArray = window.parent.parent.document.getElementsByTagName("iframe");
	var scrollTop = window.parent.parent.document.body.scrollTop || window.parent.parent.document.documentElement.scrollTop;

	document.addEventListener('DOMContentLoaded', function () {
		
			
		window.parent.parent.document.addEventListener('scroll', function() {
			playController();
		});
			

		window.parent.parent.addEventListener('resize', function() {
			playController();
		});

		document.addEventListener('visibilitychange', function() {
			if(document.webkitHidden){
				console.log('我重整了');
			}else if(document.hidden){
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
								console.log("iframe:"+i+" 是否播放:"+true);
								video.play();
							}else{
								console.log("iframe:"+i+" 是否播放:"+false);
								video.pause();
							}
						}
					}else{
						if(iframeBottom < iframeHalf){
							console.log("iframe:"+i+" 是否播放:"+false);
							video.pause();
						}else{
							console.log("iframe:"+i+" 是否播放:"+true);
							video.play();
						}
					}
				}
			}catch(err){
				console.log('');
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
				if(!changeFlag){
					if(!video.paused){
						console.log("iframe:"+i+" 是否播放:"+false);
						video.pause();
					}
				}else{
					playController();
				}
			}
		}
		playController();
	}, false);