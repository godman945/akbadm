//0
window.onload=function(){
	var i = 0;
	var coor = [0,0,0,0,0];
	var pn=0;
	var adDiv = document.getElementById('pc8044_adv');
	
	adDiv.onmousemove = function move(e){
							var x = e.clientX;
							var y = e.clientY;
							
							i ++;

							if(i <= 10){
								if(i%2 == 0){
									if(pn<=5){
										coor[pn]=x+y;
										 //console.log('pn='+pn+'xy='+x+','+y+','+coor[pn]);
										pn++;
									}
								}
							} 
						}

	adDiv.onmouseleave = function mouseleave(){
							i = 0;
							pn = 0;
							coor = [0,0,0,0,0];
							//console.log("clear");
						}

	adDiv.onclick = function mouseclick(e){
							var x = e.clientX;
							var y = e.clientY;
							var ph = window.innerHeight;
						    var pw = window.innerWidth;
						    //console.log('pageHW='+pw+','+ph);
							//setCookie('mousemove',coor);
							//setCookie('mouseclick','(' + x + ',' + y+ ')');
							var veydata=0;
							var chflag='T';
							var chcount=0;
							
							for(pa=0;pa<5;pa++){
								
								veydata=coor[pa];
								
								for(pv=0;pv<5;pv++){
									//console.log('veydata='+veydata+',xy='+coor[pv]);
									if(veydata==coor[pv]){
										chcount++;
									}
								}
								
								//console.log('chflag='+chflag);
								//console.log('chcount='+chcount);
								
								if(chcount>5){
									chflag='F';
									break;
								}
							
							}
							
							var mp=chflag+","+pw+','+ph+','+x+","+y;
							
							//console.log('chcount='+chcount);
							//console.log('chflag='+chflag);
							//console.log('mp='+mp);
							
							var mpr="";
							
							for(ct=0;ct<mp.length;ct++){
								//console.log('ch='+mp.charCodeAt(ct));
								mpr+=String.fromCharCode(mp.charCodeAt(ct)+3+ct);
														
							}
							//console.log('mpr='+mpr);
							
							mpr=encodeURIComponent(mpr);
							
							//console.log('mpr='+mpr);
							
							
							setCookie('mpv',mpr);
							
							
                           // mpr=decodeURIComponent(mpr);
							
							//console.log('mpr='+mpr);
							
							
							/*
							console.log('mpr='+mpr);
							
							var mpd="";
							mpr=decodeURI(mpr);
							console.log('mpr='+mpr);
							for(ct=0;ct<mpr.length;ct++){
								//console.log('chd='+mpr.charCodeAt(ct));
								mpd+=String.fromCharCode(mpr.charCodeAt(ct)-3-ct);
														
							}
							
							console.log('mpd='+mpd);
							
							*/
							
							
						}
}

function setCookie(name,value){ 
	var Days = 1; 
	var exp = new Date(); 
	exp.setTime(exp.getTime() + Days*24*60*60*1000); 
	document.cookie = name + '='+ value + ';expires=' + exp.toGMTString(); 
	
}
