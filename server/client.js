//usr/bin/env node "$0" "$@"; exit $?
var io = require('socket.io-client');
var socket = io.connect('http://127.0.0.1:54321');
socket.on('connect', function() {
	console.log('connected!');
	socket.emit('list', { lat: 37.78, lon: -122.39, radius: 5000 }, console.error);
	socket.emit('create', {
		lat: 37.7798940,
		lon: -122.3948790,
		url: 'http://charleslin.ca/res/my_nigga.mp3',
		name: 'my nigga',
		time: 'soon',
	}, console.error);
});
'heartbeat list create'.split(' ').map(function(evt) {
	socket.on(evt, function(data) {
		console.log(evt, data);
	});
});
setTimeout(function() {
	console.log('time is up');
	process.exit(0);
}, 150);
