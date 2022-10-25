var iframe;

function initIframe() {
		var body;
		var element;
		
		body = document.getElementsByTagName('body')[0];
		element = document.getElementById('lessonPlans');
		
      	iframe = document.createElement('iframe');
      	iframe.style.position = "absolute";
      	iframe.style.visibility = "hidden";
      	body.appendChild(iframe);
        
        // Configure the iFrame to border the lessonPlan
        document.getElementsByTagName('body')[0].appendChild(element);
        iframe.style.height = element.offsetHeight;
        iframe.style.left = '275px';
        iframe.style.top = '145px';
        iframe.style.width = '474px';
}
        
        
function toggle(id) {
		element = document.getElementById(id);

        if (!element) return;

        if (element.style.visibility=='visible' || element.style.visibility=='') {
            iframe.style.visibility = 'hidden';
            element.style.visibility = 'hidden';
            element.style.overflow = 'hidden';
            element.style.height='1';
        } else {
            iframe.style.visibility= 'visible';
            element.style.visibility = 'visible';
            element.style.overflow = 'visible';
            element.style.height='';
        }
     }