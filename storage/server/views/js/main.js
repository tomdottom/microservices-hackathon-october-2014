$(document).ready(function () {
	refreshCounters();
	$('#planetsDiv').hide();

	var menus = ['home', 'planets'];
	$(menus).each(function (i, el) {
		$('#' + el + 'Button').click(function () {
			$('div.active').fadeOut({
				complete: function () {
					$(this).removeClass('active');
					$('li.active').removeClass('active');
					$('#' + el + 'Div').fadeIn();
					$('#' + el + 'Div').addClass('active');
					$('#' + el + 'Button').addClass('active');
				}
			});
		});
	});
	setInterval(refreshCounters, 1000);
});

function refreshCounters () {
	$.get('http://127.0.0.1:8042/counters', function (data) {
		$('#planetNumber').html(data.planets);
		$('#timerNumber').html(data.timeCount);
		$('#messagesNumber').html(data.messages);
		$('#destroyedNumber').html(data.destroyed);
	});
}

function getPlanetList () {
	$.get('http://127.0.0.1:8042/planets', function (data) {
		$(data).each(function (i, el) {

		});
	});
}