# GKE - DockerHub

This Quickstart shows how to create a Kubernettes Cluster using Google Cloud Platform's [GKE](https://cloud.google.com/container-engine/) and WebGoat's Docker [Image](https://hub.docker.com/r/webgoat/webgoat-8.0/). 

To be Successfull with this Quickstart

1. You have a Google Cloud Platform account and have enough access rights to create Compute Engine and Container Engine Resources
2. You know how to `git clone`
3. You have the gcloud SDK install and initialized somewhere ( Or just use the Google Cloud Shell) 

## Create Kubernettes Cluster

Using the cloud console the default settings will suffice. The following is the commandline you would use to create the cluster using the gcloud command.  For this QuickStart the cluster name used is `owaspbasiccluster`. The `PROJECTNAME` is whatever your project is. The `REGION` is a region near you. 


```
gcloud container --project "PROJECTNAME" clusters create "owaspbasiccluster" --zone "REGION" --machine-type "n1-standard-1" --image-type "COS" --disk-size "100" --scopes "https://www.googleapis.com/auth/compute","https://www.googleapis.com/auth/devstorage.read_only","https://www.googleapis.com/auth/logging.write","https://www.googleapis.com/auth/cloud-platform","https://www.googleapis.com/auth/servicecontrol","https://www.googleapis.com/auth/service.management.readonly","https://www.googleapis.com/auth/trace.append","https://www.googleapis.com/auth/source.read_only" --num-nodes "3" --network "default" --enable-cloud-logging --no-enable-cloud-monitoring


```



## Set up Kubectl

Using either Google Cloud Shell or other commandline gcloud SDK environment you need to set-up 'kubectl'

If you have not already installed 'Kubectl' you can do so with the following command using `gcloud`
- `gcloud components install kubectl` 

Then you just run:
- `gcloud container clusters get-credentials owaspbasiccluster --zone REGION --project PROJECTNAME`


## Deploy WebGoat Deployment

Time to deploy the latest DockerImage for WebGoat!


Let's First Make a namespace for this: 
- `kubectl create namespace webgoat`

Now it is time to make the magic happen!

- `kubectl create -f /where_you_git_cloned_webgoat/platformQuickStart/GCP/GKE-Docker/webgoat_noDNSnoTLS.yml`

This should complete with no errors.

Use the following command to see information/status about the deployment
- `kubectl describe deployment  webgoat-dpl --namespace=webgoat`

After a few minutes the service endpoint should be ready. You can check the status with
- `kubectl describe service  webgoatsvc --namespace=webgoat`

In the output you should see a message like "Created load..."  after a "Creating load..." which means that the public facing loadbalancer (even thou there is just one container running!) is ready.


## Test Deployment

From the previous `describe service` command the `LoadBalancer Ingress:` line should have the external IP. The line below should give the port.

So.....

[IP]:[PORT]/WebGoat in your browser!

DONE


