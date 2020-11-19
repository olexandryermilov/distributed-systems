#!/bin/bash

set -x

if [ "$MODE" == "MASTER" ]; then
  sbt "runMain edu.ucu.yermilov.master.Master --server.port=$PORT"
elif [ "$MODE" == "SECONDARY" ]; then
  sbt "runMain edu.ucu.yermilov.secondary.Secondary --server.port=$PORT"
fi