mob-audio
=========

A repo for AngelHack S14 #uWaterlooMasterRace

socket.io api
============
## s→c `heartbeat`
Every 5 seconds:
`{ timestamp: Date.now() }` (milliseconds)

## c→s `list`
`{ id: (anything, even undefined), lat: (latitude float), lon: (latitude float), radius: (search radius in meters) }`
Requests a s→c `list` reply

## s→c `list`

	{
		id: (same as in c→s),
		mobs: [
			{
				name: (name string),
				time: (time string),
				mobId: (mobId integer),
				url: (media url),
				dist: (distance in m),
				lat: (gps latitude),
				lon: (gps longitude),
				count: (number of subscribers),
			},
			...
		],
	}

## c→s `subscribe`
Subscribe and trigger a s→c reply.
You will get s→c `play` events.
`{ mobId: (integer mob id), id: (anything) }`

## s→c `subscribe`
`{ mobId: (integer mob id), id: (same as in c→s) }`
If mobId is `-1`, subscribe failed.

## c→s `unsubscribe`
`{ mobId: (integer mob id), id: (anything) }`

## s→c `unsubscribe`
`{ mobId: (integer mob id), id: (same as in c→s) }`

## c→s `create`
Create a new mob.
Trigger a s→c `create`.
`{ url: (media url), name: (name string), time: (time string), lat: (latitude float), lon: (longitude float) }`

## s→c `create`
`{ url: (media url), name: (name string), time: (time string), lat: (latitude float), lon: (longitude float), mobId: (mob id integer) }`

## c→s `destroy`
Destroy a mob.
Trigger a s→c `destroy`.
`{ mobId: (mob id integer) }`

## s→c `destroy`
`{ mobId: (mob id integer) }`

## s→c `count`
Someone joined/left a subscribed mob.
`{ mobId: (mob id integer), count: (new count integer) }`

## c→s `play`
Trigger a s→c `play` for all subscribers.
`{ mobId: (mob id integer) }`
Mobs stay in existence.
