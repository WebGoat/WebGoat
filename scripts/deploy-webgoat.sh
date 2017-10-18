#!/usr/bin/env bash

docker login -u $DOCKER_USER -p $DOCKER_PASS
export REPO=webgoat/webgoat-8.0

cd webgoat-server

if [ "${BRANCH}" == "master" ] && [ ! -z "${TRAVIS_TAG}" ]; then
  # If we push a tag to master this will update the LATEST Docker image and tag with the version number
  docker build -f Dockerfile -t $REPO:latest .
  docker tag $REPO:${TRAVIS_TAG}
  docker push $REPO
elif [ ! -z "${TRAVIS_TAG}" ]; then
  # Creating a tag build we push it to Docker with that tag
  docker build -f Dockerfile -t $REPO:${TRAVIS_TAG} .
  docker tag $REPO:${TRAVIS_TAG}
  docker push $REPO
elif [ "${BRANCH}" == "develop" ]; then
  docker build -f Dockerfile -t $REPO:snapshot .
  docker push $REPO
else
  echo "Skipping releasing to DockerHub because it is a build of branch ${BRANCH}"
fi