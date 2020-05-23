#!/usr/bin/env bash

docker login -u $DOCKER_USER -p $DOCKER_PASS

export REPO=webgoat/goatandwolf
cd ..
cd docker
ls target/

if [ ! -z "${TRAVIS_TAG}" ]; then
  # If we push a tag to master this will update the LATEST Docker image and tag with the version number
  docker build --build-arg webgoat_version=${TRAVIS_TAG:1} -f Dockerfile -t $REPO:latest -t $REPO:${TRAVIS_TAG} .
  docker push $REPO
else
  echo "Skipping releasing to DockerHub because it is a build of branch ${BRANCH}"
fi
