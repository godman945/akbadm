function ad_getCookie(a){for(var b=a+"=",c=document.cookie.split(";"),d=0;d<c.length;d++){for(var e=c[d];" "==e.charAt(0);)e=e.substring(1,e.length);if(0==e.indexOf(b))return e.substring(b.length,e.length)}return null}
function ad_setCookieToMidnight(a,b){var c=new Date;c.setHours(23),c.setMinutes(59),c.setSeconds(59);var d="; expires="+c.toGMTString();document.cookie=a+"="+b+d+"; path=/"}
var _timeout;
function fixedbottombar(o){
    var _o = o;
    _o.vid = _o.adc + _o.adn;
    _o.count = ad_getCookie(_o.vid);
    null == _o.count && (_o.count = 0), _o.count++;
    ad_setCookieToMidnight(_o.vid, _o.count);

	$(document).ready(function() {
		$('.fixedbottombar-openbtn2').click(function(){playfixedbottombar();});
		$('.fixedbottombar-closebtn').click(function(){closefixedbottombar();clearTimeout(_timeout);});
		if(_o.count<3){
			$('.fixedbottombar').imagesLoaded().done(function(instance){
				playfixedbottombar();
				_timeout=setTimeout(function(){closefixedbottombar();},7000);
			}).fail(function(){
				playfixedbottombar();
				_timeout=setTimeout(function(){closefixedbottombar();},7000);
			});
		}else{
			showbtmbar();
		}
	});
}

function playfixedbottombar(){
	hidebtmbar();
	TweenMax.set('.fixedbottombar',{display:'none',bottom:0,width:0});
	TweenMax.set('.fixedbottombar-car-bg.g',{scale:0,bottom:15,left:276});
	TweenMax.set('.fixedbottombar-car-bg.b',{scale:0,bottom:27,left:0});
	TweenMax.set('.fixedbottombar-car-bg.r',{scale:0,bottom:0,left:119});
	TweenMax.set('.fixedbottombar-logo',{bottom:'-100%'});
	TweenMax.set('.fixedbottombar-slogan',{bottom:'-100%'});
	TweenMax.set('.fixedbottombar-closebtn',{display:'none',opacity:0});
	TweenMax.set('.fixedbottombar-floatad',{left:'-100%',display:'block'});
	scene1();
}

function scene1(){
	TweenMax.set('.fixedbottombar',{display:'block'});
	TweenMax.fromTo('.fixedbottombar' ,0.5,{width:0},{width:'100%',ease:Power3.easeIn});
	TweenMax.fromTo('.fixedbottombar-car-bg.g' ,1,{scale:0,bottom:15+50,left:276+550},{delay:1.15,scale:1,bottom:15,left:276,ease:Power4.easeOut});
	TweenMax.fromTo('.fixedbottombar-car-bg.b' ,1,{scale:0,bottom:27+50,left:0+550},{delay:1.07,scale:1,bottom:27,left:0,ease:Power4.easeOut});
	TweenMax.fromTo('.fixedbottombar-car-bg.r' ,1,{scale:0,bottom:0+50,left:119+550},{delay:1,scale:1,bottom:0,left:119,ease:Power4.easeOut});
	TweenMax.fromTo('.fixedbottombar-car-wheel', 1.5,{rotation:360*3},{delay:0.6,rotation:0, ease:Power4.easeOut})
	TweenMax.fromTo('.fixedbottombar-logo' ,0.75,{bottom:'-100%'},{delay:1.2,bottom:33,ease:Power4.easeOut});
	TweenMax.fromTo('.fixedbottombar-slogan' ,0.75,{bottom:'-100%'},{delay:1.2,bottom:33,ease:Power4.easeOut,onComplete:function(){
		TweenMax.set('.fixedbottombar-closebtn',{display:'block'});
		TweenMax.fromTo('.fixedbottombar-closebtn' ,0.75,{opacity:0},{opacity:1,ease:Power4.easeOut});
	}});
}


function closefixedbottombar(){
	TweenMax.fromTo('.fixedbottombar',0.5,{bottom:0},{bottom:'-100%',ease:Linear.easeNone,onComplete:function(){
		TweenMax.set('.fixedbottombar',{display:'none',bottom:0,width:0});
	}});
	showbtmbar();
}

function showbtmbar(){
	TweenMax.set('.fixedbottombar-floatad',{left:'-100%',display:'block'});
	TweenMax.to('.fixedbottombar-floatad',0.3,{left:0,ease:Power4.easeOut});
}

function hidebtmbar(){
	TweenMax.to('.fixedbottombar-floatad',0.3,{left:'-100%',ease:Power4.easeIn});
}
