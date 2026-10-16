function getCookieValue() {
	var cookie = document.cookie.match(new RegExp('(^| )spoof_auth=([^;]+)'));
	if (cookie != null)
		return [2];
	return null;
}

function cleanup() {
	document.cookie = 'spoof_auth=;Max-Age=0;secure=true';
	$('#spoof_username').removeAttr('disabled');
	$('#spoof_password').removeAttr('disabled');
	$('#spoof_submit').removeAttr('disabled');
	$('#spoof_attack_feedback').html('');
	$('#spoof_attack_output').html('');
}

var target = document.getElementById('spoof_attack_feedback');

var obs = new MutationObserver(function(mutations) {
	mutations.forEach(function() {
		var cookie = getCookieValue();
		if (cookie) {
			$('#spoof_username').prop('disabled', true);
			$('#spoof_password').prop('disabled', true);
			$('#spoof_submit').prop('disabled', true);
		}
	});
});

obs.observe(target, { characterData: false, attributes: false, childList: true, subtree: false });
