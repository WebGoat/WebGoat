
function makeWindow(url, windowName) 
{
	day = new Date();
	id = day.getTime();
	eval("page" + id + " = window.open(url, '" + id + "', 'toolbar=0,location=0,scrollbars=1,statusbar=0,menubar=0,resizable=1,width=600,height=500');");
}