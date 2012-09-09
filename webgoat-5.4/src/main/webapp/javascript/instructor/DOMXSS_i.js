function displayGreeting(name) {
	if (name != ''){
		document.getElementById("greeting").innerHTML="Hello, " + escapeHTML(name) + "!";
	}
}

function escapeHTML (str) {
   var div = document.createElement('div');
   var text = document.createTextNode(str);
   div.appendChild(text);
   return div.innerHTML;
}
	
