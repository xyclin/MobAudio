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
	io = require('socket.io').listen(server),
	fs = require('fs'),
	bodyParser = require('body-parser'),
	multiparty = require('multiparty'),
	request = require('request'),
	uuid = require('uuid');
app.use(bodyParser());
server.listen(54321);

app.get('/', function(req, res) {
	res.sendfile(__dirname+'/index.html');
});

app.post('/upload', function(req, res) {
	var rand = uuid.v4();
	if (req.body.file !== undefined)
		handle(req.body.file);
	else {
		var form = new multiparty.Form({ encoding: 'binary' });
		form.parse(req, function(err, fields, files) {
			try {
				handle(fields.file[0]);
			} catch(err) {
				res.send(500);
			}
		});
	}
	function handle(data) {
		fs.writeFile('static/'+rand, data, function(err) {
			if (err) {
				res.send(500)
			} else {
				res.send(200);
				res.send('http://'+req.host+':54322/'+rand);
			}
		});
	}
});

app.get('/youtube/dl', function(req, res) {
	var rand = uuid.v4();
	exec('youtube-dl -x --audio-format mp3 -q -o \''+rand+'.%(ext)s\' "$url"', {
		env: { url: req.params.url },
		cwd: process.cwd+'/static/',
	}, function(err, stdout, stderr) {
		if(err) {
			res.send(500);
		} else {
			res.send('http://'+req.host+':54322/'+rand+'.mp3');
		}
	});
});

app.get('/youtube/list', function(req, res) {
	var url = 'https://www.googleapis.com/youtube/v3/search?key=AIzaSyCaiXT6QtzUBK0Lt5CvwAgLTnsYiHGgIeU';
	for(var key in req.query)
		if(req.query.hasOwnProperty(key))
			url += '&'+key+'='+encodeURIComponent(req.query[key]);
	request.get(url, function(err, result, body) {
		if(err) {
			res.send(500);
		} else {
			res.send(JSON.parse(body));
		}
	});
});

var _nextClientId = 0;
var _nextMobId = 0;
var _subscribers = {};
var _data = {};
var _clients = {};

var api = {
	list: function(data) {
		return {
			id: data.id,
			mobs: Object.keys(_data).map(function(mobId) {
				var mob = _data[mobId];
				var r = 6378100, RATIO = Math.PI/180;
				mob.dist = 2*r*Math.asin(Math.sqrt(Math.pow(Math.sin((mob.lat-data.lat)/2*RATIO), 2)+Math.cos(data.lat*RATIO)*Math.cos(mob.lat*RATIO)*Math.pow((mob.lon-data.lon)/2*RATIO, 2)));
				return mob;
			}).filter(function(mob) {
				return !mob.done && mob.dist <= data.radius;
			}).sort(function(a, b) {
				return a.dist-b.dist;
			}),
		};
	},
	subscribe: function(data) {
		console.assert(typeof data.mobId == 'number');
		_subscribers[data.mobId][data._clientId] = true;
		if(_data[data.mobId].done) {
			data.mobId = -1;
		}
		++_data[data.mobId].count;
		broadcast('count', data);
	},
	unsubscribe: function(data) {
		console.assert(typeof data.mobId == 'number');
		delete _subscribers[data.mobId][data._clientId];
		--_data[data.mobId].count;
		broadcast('count', data);
	},
	create: function(data) {
		var mobId = _nextMobId++;
		_subscribers[mobId] = {};
		_subscribers[mobId][data._clientId] = true;
		_data[mobId] = data;
		data.count = 1;
		data.mobId = mobId;
	},
	play: function(data) {
		_data[data.mobId].done = true;
		broadcast('play', {
			id: data.id,
			mobId: data.mobId,
		});
		return null;
	},
	destroy: function(data) {
		var subs = _subscribers[data.mobId];
		delete _subscribers[data.mobId];
		broadcast('unsubscribe', {
			id: -1,
			mobId: data.mobId,
		});
		delete _data[data.mobId];
	},
};

var _requests = 0;
Object.keys(api).map(function(func) {
	app.post('/'+func, function(req, res) {
		req._clientId = -1-_requests++;
		for(var p in req.body)
			if(req.body.hasOwnProperty(p) && typeof req.body[p] == 'string' && !isNaN(+req.body[p]))
				req.body[p] = +req.body[p];
console.log('REST', req.body);
		var msg = api[func](req.body);
		if(msg === undefined)
			msg = req.body;
console.log('RETURN', msg);
		if (msg === null)
			req.send(200);
		else
			res.send(msg);
	});
});

function broadcast(evt, data) {
	var subs = _subscribers[data.mobId];
	for(var cid in subs)
		if(subs.hasOwnProperty(cid))
			_clients[cid].emit(evt, data, handler);
}

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
	Object.keys(api).map(function(func) {
		socket.on(func, function(data) {
			data._clientId = _clientId;
			var msg = api[func](data);
			if (msg !== null)
				socket.emit(func, msg === undefined ? data : msg, handler);
		}.protectWith(handler));
	});
});
