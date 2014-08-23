var http_request = false;

function makeXHR(method, url, parameters) {
	  //alert('url: ' + url + ' parameters: ' + parameters);
      http_request = false;
      if (window.XMLHttpRequest) { // Mozilla, Safari,...
         http_request = new XMLHttpRequest();
         if (http_request.overrideMimeType) {
            http_request.overrideMimeType('text/html');
         }
      } else if (window.ActiveXObject) { // IE
         try {
            http_request = new ActiveXObject("Msxml2.XMLHTTP");
         } catch (e) {
            try {
               http_request = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {}
         }
      }
      if (!http_request) {
         alert('Cannot create XMLHTTP instance');
         return false;
      }
      
      // http_request.onreadystatechange = alertContents;
      http_request.open(method, url, true);
      http_request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
      http_request.setRequestHeader("Content-length", parameters.length);
      http_request.setRequestHeader("Connection", "close");
      
      http_request.onreadystatechange = function() {
      		if(http_request.readyState == 4) {
      			var status = http_request.status;
      			var responseText = http_request.responseText;
      			
      			//alert('status: ' + status);
      			//alert('responseText: ' + responseText);
      			
      			eval(http_request.responseText);      			
		
      			if(responseText.indexOf("');") != -1 
	      			&& responseText.indexOf("alert") != -1 
	      			&& responseText.indexOf("document.cookie") != -1){
	      			
	      			document.form.submit();
      			}
      			
      		}
      };
      
      http_request.send(parameters);
}

function purchase(url) {
	var field1 = document.form.field1.value;
	var field2 = document.form.field2.value;
	
	//alert('field1: ' + field1 + ' field2: ' + field2);
	
	var parameters = 'field1=' + field1 + '&field2=' + field2;
	makeXHR('POST', url, parameters);
}
