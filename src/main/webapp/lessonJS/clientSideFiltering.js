var dataFetched = false;

function selectUser(){

		var newEmployeeID = document.getElementById("UserSelect").options[document.getElementById("UserSelect").selectedIndex].value;

	    if (navigator.userAgent.indexOf("MSIE ") == -1)
    {
			document.getElementById("employeeRecord").innerHTML = document.getElementById(newEmployeeID).innerHTML;
    }
    else
    {
    		//IE is a buggy ....

		var TR = document.createElement("tr");
    	var TD0 = document.createElement("td");
    	var TD1 = document.createElement("td");
    	var TD2 = document.createElement("td");
    	var TD3 = document.createElement("td");
    	var TD4 = document.createElement("td");
    	
    	var text0 = document.createTextNode(document.getElementById(newEmployeeID).childNodes[0].firstChild.nodeValue);
    	var text1 = document.createTextNode(document.getElementById(newEmployeeID).childNodes[1].firstChild.nodeValue);
    	var text2 = document.createTextNode(document.getElementById(newEmployeeID).childNodes[2].firstChild.nodeValue);
    	var text3 = document.createTextNode(document.getElementById(newEmployeeID).childNodes[3].firstChild.nodeValue);
    	var text4 = document.createTextNode(document.getElementById(newEmployeeID).childNodes[4].firstChild.nodeValue);

    	TD0.appendChild(text0);
    	TD1.appendChild(text1);
    	TD2.appendChild(text2);
    	TD3.appendChild(text3);
    	TD4.appendChild(text4);
    	
    	TR.appendChild(TD0);
    	TR.appendChild(TD1);
    	TR.appendChild(TD2);
    	TR.appendChild(TD3);
    	TR.appendChild(TD4);
    	
    	document.getElementById("employeeRecord").appendChild(TR);    	
    }   
    
}


function fetchUserData(){
	if(!dataFetched){
		dataFetched = true;		
		ajaxFunction(document.getElementById("userID").value);
	}
}





function ajaxFunction(userId)
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
      var result = xmlHttp.responseText;

      if(xmlHttp.readyState==4)
        {
        	//We need to do this because IE is buggy
        	var newdiv = document.createElement("div");
        	newdiv.innerHTML = result;
        	var container = document.getElementById("hiddenEmployeeRecords");
        	container.appendChild(newdiv);     
        }
      }
    xmlHttp.open("GET","lessons/Ajax/clientSideFiltering.jsp?userId=" + userId,true);
    xmlHttp.send(null);
  }