$(document).ready(function() {
	$(".remove-user-btn").click(function(e) {
		var re = confirm("Are you sure");
		if (re != true) {
			e.preventDefault();
		}
	});
});