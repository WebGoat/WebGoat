function changeLanguage(){
	var select=MM_findObj("language",null);
	
	document.location="attack?language="+select.value;
}	


function MM_findObj(n, d) {
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function getHW(t,a) {
  var r,p,h=0,w=0;if((p=MM_findObj(a)) !=null){
  if(document.all || document.getElementById){h=parseInt(p.offsetHeight);w=parseInt(p.offsetWidth);
  if(!h){h=parseInt(p.style.pixelHeight);w=parseInt(p.style.pixelWidth);}
  }else if(document.layers){h=parseInt(p.clip.height);w=parseInt(p.clip.width);}}
  if(t=="width"){r=w;}else{r=h;}return r; 
}

function MM1dwt() {
  var g,lh,sw,fr = false;
  if(!document.mc)return;
  for(var x=0;x<m1.length;x++){tl=m1[x].id;lh="P7CM1DWT0"+tl;
	if((g=MM_findObj(lh)) !=null){fr=true;sw=0;break;}
	lh="P7CM1DWT1"+tl;if((g=MM_findObj(lh)) !=null){fr=true;sw=1;break;}}
  if(fr){eval("trigMenuMagic1('"+tl+"',"+sw+")");}
}

function setMenuMagic1() {
  var s,d,g,g2,gg,ww,kx,th,tu,ts,nu,xx,k=0,pa=0;args=setMenuMagic1.arguments;
  if((parseInt(navigator.appVersion)>4 || navigator.userAgent.indexOf("MSIE")>-1)&& navigator.userAgent.indexOf("Opera")==-1){pa="px";}
  if(navigator.userAgent.indexOf("Opera")>-1){P7OperaW=window.innerWidth;P7OperaH=window.innerHeight;}
  if(!document.mc) { m3=new Array();
   m=new Array();document.mc=true;ms=new Array();document.imswap=new Array();document.imswapo=new Array();
   m1=new Array();m2=new Array();mprop=new Object();mprop.offset=args[0];mprop.rate=args[1];
   mprop.delay=args[2];mprop.bottom=args[3];
   if(document.layers){mprop.pageh = document.height;}}
  for(var x=4;x<args.length;x+=3){if((g=MM_findObj(args[x])) !=null){
    m[k]=args[x];g.imname=args[x+2];g.sub=args[x+1];m3[k]=0;
    g2=MM_findObj(args[x+2]);tu=g2.src;ts=tu.lastIndexOf(".");
    nu=tu.substring(0,ts)+"_open"+tu.substring(ts,tu.length);
	nu2=tu.substring(0,ts)+"_over"+tu.substring(ts,tu.length);
    document.imswap[k]=new Image();document.imswap[k].src=tu;
	document.imswapo[k]=new Image();document.imswapo[k].src=tu;k++;}}
  var lf=0;for (var j=0;j<m.length;j++){
   if((g=MM_findObj(m[j])) !=null){d=(document.layers)?g:g.style;m1[j]=g;g.waiting=false;
    if(j==0){lf=parseInt(d.left);th=parseInt(d.top);}
    if(j>0){d.left=(lf+pa);th+=getHW('height',m[j-1]);d.top=(th+pa);}
    if((s=MM_findObj(g.sub)) !=null){m2[j]=s;ww=getHW('width',g.sub);
     kx=lf-ww-30;dd=(document.layers)?s:s.style;
     dd.left=(kx+pa);dd.top=(th+pa);ms[j]=th;dd.visibility="visible";s.open=false;s.waiting=false;}}}
  if((g=MM_findObj(mprop.bottom)) !=null){d=(document.layers)?g:g.style;
   d.left=(lf+parseInt(args[0])+pa);th+=getHW('height',m[m.length-1]);d.top=(th+pa);}
}

function BM1(el,x,y,a,b,c,s) {
 var g,elo=el,f="",m=false,d="";x=parseInt(x);y=parseInt(y);
 var t = 'g.BM = setTimeout("BM1(\''+elo+'\','; 
 if ((g=MM_findObj(el))!=null) {d=(document.layers)?g:g.style;}else{return;}
 var xx=(parseInt(d.left))?parseInt(d.left):0;
 var yy=(parseInt(d.top))?parseInt(d.top):0;
 var i=parseInt(a);
  if (eval(g.moved)){clearTimeout(g.BM);}
  if (xx<x){xx+=i;m=true;if(xx>x){xx=x;}}
  if (xx>x){xx-=i;m=true;if(xx<x){xx=x;}}
  if (yy<y){yy+=i;m=true;if(yy>y){yy=y;}}
  if (yy>y){yy-=i;m=true;if(yy<y){yy=y;}}
 if (m) {
  if((parseInt(navigator.appVersion)>4 || navigator.userAgent.indexOf("MSIE")>-1)&& navigator.userAgent.indexOf("Opera")==-1){   
   xx+="px";yy+="px";}d.left=xx;d.top=yy;g.moved=true;eval(t+x+','+y+','+a+','+b+','+c+',0)",'+b+')');
  }else {g.moved=false;wait(elo);}
}

function wait(a) {
  var ma,mb;if((mb=MM_findObj(a)) !=null){
   if(!mb.waiting || mb.waiting=="none"){return;}
    ma=mb.waiting;mb.waiting=false;eval(ma);}
}

function trigMenuMagic1(a,sw) {
  var x,g,gg,d,dd,w,lp,tp,im,im2,ts,nu,e,pa=0;if(!document.mc)return;
  if((parseInt(navigator.appVersion)>4 || navigator.userAgent.indexOf("MSIE")>-1)&& navigator.userAgent.indexOf("Opera")==-1){pa="px";}
  if(navigator.userAgent.indexOf("Opera")>-1){if( P7OperaW!=window.innerWidth || P7OperaH!=window.innerHeight)setMenuMagic1();}
  var ofs=parseInt(mprop.offset),trt = parseInt(mprop.rate);
  var tdy=parseInt(mprop.delay),tsb,tlf,tst;for(x=0;x<m.length;x++){
  if(m[x]==a){d=m1[x];dd=(document.layers)?d:d.style;g=m2[x];gg=(document.layers)?g:g.style;
   e=MM_findObj(d.imname);im=e.src;ts=im.replace("_open","");ts=ts.replace("_over","");
   if(!g.open){tst="closed";im2=ts.lastIndexOf(".");
   nu=ts.substring(0,im2)+"_open"+ts.substring(im2,ts.length);ts = nu;}else{tst="open"}break;}}
   if(document.mm1Q){trt=20000;document.mm1Q=false;}
  for(j=0;j<m.length;j++){
   d=m1[j];dd=(document.layers)?d:d.style;g=m2[j];gg=(document.layers)?g:g.style;
   if(j==0){tlf=parseInt(dd.left);}if(g.open){
   w=getHW('width',d.sub)+30;w-=parseInt(dd.left);w*=-1;d.waiting=false;
   eval("BM1('"+d.sub+"',"+w+","+parseInt(gg.top)+","+20000+","+tdy+",0,0)");}
   d.waiting=false;g.open=false;
   if(parseInt(sw)==1){e=MM_findObj(d.imname);im=e.src;im2=im.replace("_open","");e.src=im2;}}
  var tnt=new Array();var df=0,tcd=0,tdl=m[0];for(j=0;j<m.length;j++){
  d=m1[j];dd=(document.layers)?d:d.style;g=m2[j];gg=(document.layers)?g:g.style;
  if(j==0){th=parseInt(dd.top);}tnt[j]=th;df=Math.abs(parseInt(dd.top)-th);
  if(df>tcd){tdl=m[j];tcd=df;}th+=getHW('height',m[j]);
  if(x==j && tst=="closed"){tsb=th;if(m3[j]!=1){th+=getHW('height',d.sub);}}ms[j]=th;}
  if(tst=="closed"){d=m1[x];dd=(document.layers)?d:d.style;
   g=m2[x];gg=(document.layers)?g:g.style;lp=tlf+ofs;
   gg.top=(tsb+pa);ms[x]=tsb;e=MM_findObj(d.imname);if(parseInt(sw)==1){e.src=ts;}
   g.open=true;if(m3[x]!=1){gg.visibility="visible";var r;r=MM_findObj(tdl);		
   r.waiting="BM1('"+d.sub+"',"+lp+","+tsb+","+20000+","+tdy+",0,0)" ;}
   }else{d=m1[m1.length-1];d.waiting="none";}
  for(j=0;j<m.length;j++ ){eval("BM1('"+m[j]+"',"+tlf+","+tnt[j]+","+trt+","+tdy+",0,0)");}
  if((g=MM_findObj(mprop.bottom)) !=null){d=(document.layers)?g:g.style;g.waiting=false;
   eval("BM1('"+mprop.bottom+"',"+(tlf+ofs)+","+th+","+trt+","+tdy+",0,0)");
   th+=(document.layers)?getHW('height',mprop.bottom):0;}
  if(document.layers){var tw2=document.width;
    if(document.height<th) {document.height=th;document.width=tw2;}}
}

function rollCMenu1(ev,a,b) {
 var e,im,ts,j,nu,g,x,tev=ev.type;
 if(!document.mc)return;
 if(tev=="mouseover"){for(x=0;x<m.length;x++){
 if(m[x]==a){g=m2[x];if(parseInt(b)==0 && g.open) {break;return;}
 e=MM_findObj(m1[x].imname);im=e.src;ts=im.replace("_open","");
 ts=ts.replace("_over","");j=ts.lastIndexOf(".");
 e.src=ts.substring(0,j)+"_over"+ts.substring(j,ts.length);break;}}
 }else if(tev=="mouseout"){for(x=0;x<m.length;x++){
 if(m[x]==a){e=MM_findObj(d=m1[x].imname);im=e.src;
 g=m2[x];ts=im.replace("_open","");ts=ts.replace("_over","");
 if(g.open){j=ts.lastIndexOf(".");
 nu=ts.substring(0,j)+"_open"+ts.substring(j,ts.length);
 }else{nu=ts;}e.src=nu;break;}}}
}

function trigMM1url(param,opt){
  var ur,x,i,nv,mn,pr=new Array();
  ur=document.URL;x=ur.indexOf("?");
  if(x>1){pr=ur.substring(x+1,ur.length).split("&");
  for(i=0;i<pr.length;i++){nv=pr[i].split("=");
  if(nv.length>0){if(unescape(nv[0])==param){
  mn="menu"+unescape(nv[1]);
  eval("trigMenuMagic1('"+mn+"',"+opt+")");}}}}
  }
  
  document.mm1Q=true;