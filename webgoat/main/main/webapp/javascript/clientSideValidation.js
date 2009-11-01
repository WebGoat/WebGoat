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
        document.form.GRANDTOT.value = calcTot(document.form.SUBTOT.value , xmlHttp.responseText);
        }
      }
    xmlHttp.open("GET","lessons/Ajax/clientSideValidation.jsp?coupon=" + coupon,true);
    xmlHttp.send(null);
  }
  
  
  function updateTotals(){

	f = document.form;
	
	f.TOT1.value = calcTot(f.PRC1.value , f.QTY1.value);
	f.TOT2.value = calcTot(f.PRC2.value , f.QTY2.value);
	f.TOT3.value = calcTot(f.PRC3.value , f.QTY3.value);
	f.TOT4.value = calcTot(f.PRC4.value , f.QTY4.value);	
	
	f.SUBTOT.value = formatCurrency(unFormat(f.TOT1.value) 
							+ unFormat(f.TOT2.value) 
							+ unFormat(f.TOT3.value) 
							+ unFormat(f.TOT4.value));
	
	f.GRANDTOT.value = f.SUBTOT.value;	
	
	isValidCoupon(f.field1.value);
	
	
}

function unFormat(price){
	
	price = parseFloat(unFormatCurrency(price));

	if(isNaN(price))
		price = 0;
	
	return price;

}

function calcTot( price,  qty){
	
	price = unFormatCurrency(price);
	
	return formatCurrency(price*qty);
}


function unFormatCurrency(price){
	price = price.toString().replace(/\$|\,/g,'');
	return price;
}

function formatCurrency(num) {
	num = num.toString().replace(/\$|\,/g,'');
	if(isNaN(num))
		num = "0";
	sign = (num == (num = Math.abs(num)));
	num = Math.floor(num*100+0.50000000001);
	cents = num%100;
	num = Math.floor(num/100).toString();
	if(cents<10)
		cents = "0" + cents;
	for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
		num = num.substring(0,num.length-(4*i+3))+','+
	num.substring(num.length-(4*i+3));
	return (((sign)?'':'-') + '$' + num + '.' + cents);
}
