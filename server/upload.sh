#!/bin/sh
# usage: ./upload.sh myfile
curl localhost:54321/upload --data-urlencode file@"$1"
