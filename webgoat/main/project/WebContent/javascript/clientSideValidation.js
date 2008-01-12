var coupons = ["nvojubmq",
"emph",
"sfwmjt",
"faopsc",
"fopttfsq",
"pxuttfsq"];


function isValidCoupon(coupon) {
	coupon = coupon.toUpperCase();
	for(var i=0; i<coupons.length; i++) {
		decrypted = decrypt(coupons[i]);
		if(coupon == decrypted){
			ajaxFunction(coupon);
			return true;
		}
	}
	return false;	
}




function decrypt(code){

	code = code.toUpperCase();

	alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	caesar = '';

	for (i = code.length ;i >= 0;i--){	
	
		for (j = 0;j<alpha.length;j++){		
			
			if(code.charAt(i) == alpha.charAt(j)){
			
				caesar = caesar + alpha.charAt((j+(alpha.length-1))%alpha.length);
			}		
		}
	}	
	return caesar;
}

function ajaxFunction(coupon)
  {
  
  var xmlHttp;
  try
    {
    // Firefox, Opera 8.0+, Safari
    xmlHttp=new XMLHttpRequest();
    }
  catch (e)
    {
    // Internet Explorer
    try
      {
      xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
      }
    catch (e)
      {
      try
        {
        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
      catch (e)
        {
        alert("Your browser does not support AJAX!");
        return false;
        }
      }
    }
    xmlHttp.onreadystatechange=function()
      {
      if(xmlHttp.readyState==4)
        {
        document.form.GRANDTOT.value = document.form.SUBTOT.value * xmlHttp.responseText;
        document.form.GRANDTOT.value = dollarRound(document.form.GRANDTOT.value);
        }
      }
    xmlHttp.open("GET","lessons/Ajax/clientSideValidation.jsp?coupon=" + coupon,true);
    xmlHttp.send(null);
  }
  
  
  function updateTotals(){

	f = document.form;
	
	f.TOT1.value = dollarRound(f.QTY1.value * f.PRC1.value);
	f.TOT2.value = dollarRound(f.QTY2.value * f.PRC2.value);
	f.TOT3.value = dollarRound(f.QTY3.value * f.PRC3.value);
	f.TOT4.value = dollarRound(f.QTY4.value * f.PRC4.value);
	
	f.SUBTOT.value = dollarRound(parseFloat(f.TOT1.value) + parseFloat(f.TOT2.value) + parseFloat(f.TOT3.value) + parseFloat(f.TOT4.value));

	
	f.GRANDTOT.value = f.SUBTOT.value;
	
	isValidCoupon(f.field1.value);

}

function calcTot( price,  qty){
	
	return parseInt(qty * price *100)/100;

}

function dollarRound(price){
	return parseInt(price *100)/100;
}
