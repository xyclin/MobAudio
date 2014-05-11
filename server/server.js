//usr/bin/env node "$0" "$@"; exit $?
Function.prototype.protectWith = function(handler) {
	var func = this;
	return function() {
		try {
			return func.apply(this, arguments);
		} catch(err) {
			return handler(err);
		}
	};
}

var app = require('express')(),
	server = require('http').createServer(app),
	io = require('socket.io').listen(server);
app.listen(54321);

app.get('/', function (req, res) {
	res.sendfile(__dirname + '/index.html');
});

var _nextClientId = 0;
var _nextMobId = 0;
var _subscribers = {};
var _data = {};
var _clients = {};
var _mobs = [];
io.sockets.on('connection', function(socket) {
	var _clientId = _nextClientId++;
	_clients[_clientId] = socket;
	function handler(err) {
		console.error('socket error in', _clientId, err);
		delete _clients[_clientId];
		socket.close();
	}
	(function heartbeat() {
		socket.emit('heartbeat', { timestamp: Date.now() }, handler);
		setTimeout(heartbeat, 5000);
	})();
	function broadcast(evt, data) {
		var subs = _subscribers[data.mobId];
		for(var cid in subs)
			if(subs.hasOwnProperty(cid))
				_clients[cid].emit(evt, data, handler);
	}
	socket.on('list', function(data) {
		socket.emit('list', {
			id: data.id,
			mobs: _mobs.map(function(mob) {
				var r = 6378100, RATIO = Math.PI/180;
				return 2*r*Math.asin(Math.sqrt(Math.pow(Math.sin((mob.lon-data.lat)/2*RATIO), 2)+Math.cos(data.lat*RATIO)*Math.cos(mob.lat*RATIO)*Math.pow((mob.lon-data.lon)/2*RATIO, 2)));
			}).filter(function(mob) {
				return !mob.done && mob.dist <= data.radius;
			}).sort(function(a, b) {
				return a.dist-b.dist;
			}),
		}, handler);
	});
	socket.on('subscribe', function(data) {
		console.assert(typeof data.mobId == 'number');
		_subscribers[data.mobId][_clientId] = true;
		if(_data[data.mobId].done) {
			data.mobId = -1;
		}
		++_data[data.mobId].count;
		broadcast('count', data);
		socket.emit('subscribe', data, handler);
	}.protectWith(handler));
	socket.on('unsubscribe', function(data) {
		console.assert(typeof data.mobId == 'number');
		delete _subscribers[data.mobId][_clientId];
		--_data[data.mobId].count;
		broadcast('count', data);
		socket.emit('unsubscribe', data, handler);
	}.protectWith(handler));
	socket.on('create', function(data) {
		var mobId = _nextMobId++;
		_subscribers[mobId] = {};
		_subscribers[mobId][_clientId] = true;
		_data[mobId] = data;
		data.count = 1;
		data.mobId = mobId;
		data._clientId = _clientId;
		socket.emit('create', data, handler);
	}.protectWith(handler));
	socket.on('destroy', function(data) {
		var subs = _subscribers[data.mobId];
		delete _subscribers[data.mobId];
		broadcast('unsubscribe', {
			id: -1,
			mobId: data.mobId,
		});
		delete _data[data.mobId];
		socket.emit('destroy', data, handler);
	}.protectWith(handler));
	socket.on('play', function(data) {
		console.assert(_clientId == data._clientId);
		_data[data.mobId].done = true;
		broadcast('play', {
			id: data.id,
			mobId: data.mobId,
		});
	}.protectWith(handler));
});
