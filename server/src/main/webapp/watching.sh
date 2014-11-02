#!/bin/bash
BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd $BASE_DIR

coffee -w -b -c -o ./js/ ./coffeescript &
coffee -w -b --join ./js/full.js --compile ./coffeescript/*.coffee &
compass watch &