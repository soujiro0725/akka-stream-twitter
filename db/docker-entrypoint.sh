#!/bin/bash
set -e

if [ "$1" = 'dynamodb' ]; then
  exec gosu dynamodb java -Djava.library.path=/opt/dynamodb/DynamoDBLocal_lib -jar /opt/dynamodb/DynamoDBLocal.jar "${@:2}"
fi

exec "$@"
