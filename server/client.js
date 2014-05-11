//usr/bin/env node "$0" "$@"; exit $?
var io = require('socket.io-client');
var socket = io.connect('http://127.0.0.1:54321');
socket.on('connect', function() {
	console.log('connected!');
	socket.emit('list', { lat: 37.8, lon: -122, radius: 5000 }, console.error);
});
'heartbeat list'.split(' ').map(function(evt) {
	socket.on(evt, function(data) {
		console.log(evt, data);
	});
});
setTimeout(function() {
	console.log('time is up');
	process.exit(0);
}, 15000);
