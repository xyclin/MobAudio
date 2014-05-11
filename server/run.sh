#!/bin/sh
trap 'kill $socket $static' INT EXIT TERM QUIT
./server.js & socket=$!
pushd static
http-server -p 54322 & static=$!
popd
wait
