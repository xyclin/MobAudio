//usr/bin/env node "$0" "$@"; exit $?
var io = require('socket.io-client');
var socket = io.connect('http://127.0.0.1:54321');
socket.on('connect', function() {
	console.log('connected!');
});
socket.on('heartbeat', function(data) {
	console.log('heartbeat', data);
});
setTimeout(function() {
	console.log('time is up');
	process.exit(0);
}, 15000);
