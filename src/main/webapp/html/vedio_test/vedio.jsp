<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>
<canvas id="videoCanvas" width="640" height="480">
		<p>
			Please use a browser that supports the Canvas Element, like
			<a href="http://www.google.com/chrome">Chrome</a>,
			<a href="http://www.mozilla.com/firefox/">Firefox</a>,
			<a href="http://www.apple.com/safari/">Safari</a> or Internet Explorer 10
		</p>
	</canvas>
	<script type="text/javascript" src="jsmpg.js"></script>
	<script type="text/javascript">
		// Setup the WebSocket connection and start the player
		//var client = new WebSocket( 'ws://example.com:8084/' );

		var canvas = document.getElementById('videoCanvas');
		//var player = new jsmpeg(client, {canvas:canvas});
		
		var player = new jsmpeg('hst_1.mpg', {canvas:canvas, autoplay:true, loop: true});
		canvas.addEventListener('click', function(){
			if(player.playing) { player.pause(); } else { player.play(); } 
		}, false);
	</script>
	
	
	
	
</body>
</html>