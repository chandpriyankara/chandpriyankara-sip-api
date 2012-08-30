#!/bin/sh
export pid=$1
export killed="no"

echo "Trying Killing script $2 child process with parent pid = $pid"

for child in $(ps -o pid,ppid ax | \
   awk "{ if ( \$2 == $pid ) { print \$1 }}")
do
  echo "Soft Killing script $2 child process $child because parent pid = $pid"
  kill $child # clean kill with shutdown instruction
  sleep 1 # give some time for the clean shutdown to reach out
  echo "Hard Killing script $2 child process $child because parent pid = $pid"
  kill -9 $child # kill it immediately without wasting more time
  killed="yes"
done

if [ "no" = "$killed" ]; then
  echo "The app server is not dead? We will sleep. We must raise error here, because this server should have been dead."
  sleep 10
fi

