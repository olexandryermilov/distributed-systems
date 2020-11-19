FROM hseeberger/scala-sbt:8u181_2.12.7_1.2.6 as builder
COPY . /opt/distributed-systems/
WORKDIR /opt/distributed-systems/

ENTRYPOINT [ "/bin/bash", "run.sh" ]