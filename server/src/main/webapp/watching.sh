#!/bin/bash
BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd $BASE_DIR

coffee --watch --bare --compile --output ./js/ ./coffeescript &
compass watch &