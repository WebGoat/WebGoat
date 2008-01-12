var dataFetched = false;


function selectUser(){

	var newEmployeeID = document.getElementById("UserSelect").options[document.getElementById("UserSelect").selectedIndex].value;
	
	document.getElementById("employeeRecord").innerHTML = document.getElementById(newEmployeeID).innerHTML;
	
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
        	document.getElementById("hiddenEmployeeRecords").innerHTML=result; 
        	
        }
      }
    xmlHttp.open("GET","lessons/Ajax/clientSideFiltering.jsp?userId=" + userId,true);
    xmlHttp.send(null);
  }