#!/bin/bash

# Script to deploy webgoat docker (or I guess any docker for that matter) to GKE
# You will need the SDK installed and be authenticated and set up for the project you 
# plan to push the image into and run it on

# will pull in config vars
source deploy.cfg

echo "tagging $CURTAG to $DEST_TAG"
docker tag $CURTAG $DEST_TAG
# gcloud docker -- push $DEST_TAG
docker push $DEST_TAG
if [ $? -eq 0 ]; then
    echo "*** $DEST_TAG pushed ... "	
else
    echo "failed to push $DEST_TAG to GKE repo"
    exit 1
fi

echo "*** creating cluster $CLUSTER_NAME ... "
gcloud container clusters create $CLUSTER_NAME
if [ $? -eq 0 ]; then
	echo "*** $CLUSTER_NAME cluster created ... "
else
    echo "!!! failed to create cluster $CLUSTER_NAME"
    exit 1
fi


# DEST_TAG refers to the docker image we'll use
kubectl run $CLUSTER_NAME --image=$DEST_TAG --port=$PORT_NUM
if [ $? -eq 0 ]; then
	echo "*** cluster should be running now ... "
else
    echo "!!! failed to start service"
    echo "*** cleaning up, deleting cluster ... "
    gcloud container clusters delete $CLUSTER_NAME --quiet
    exit 1
fi

kubectl expose deployment $CLUSTER_NAME --type="LoadBalancer"
if [ $? -eq 0 ]; then
	echo "*** cluster exposed via load balancer ... "
	echo "*** TO GET YOUR SERVICE's IP, run ... "
	echo "$ kubectl get service $CLUSTER_NAME"
else
    echo "!!! failed to start service"
    echo "*** cleaning up, deleting cluster ... "
    gcloud container clusters delete $CLUSTER_NAME --quiet
    exit 1
fi
