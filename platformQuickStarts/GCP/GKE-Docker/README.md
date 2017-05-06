# GKE - DockerHub

This Quickstart shows how to create a Kubernettes Cluster using Google Cloud Platform's [GKE](https://cloud.google.com/container-engine/) and WebGoat's Docker [Image](https://hub.docker.com/r/webgoat/webgoat-8.0/). 

To be Successfull with this Quickstart

1. You have a Google Cloud Platform account and have enough access rights to create Compute Engine and Container Engine Resources
2. You know how to `git clone`
3. You have the gcloud SDK install and initialized somewhere ( do not use the google cloud shell) 


Remeber to perform a 'gcloud auth login' before using the gcloud commands below. 



## Create Kubernettes Cluster

You can create a cluster using the Google Cloud Console. The Default settings will suffice.  For this QuickStart the cluster name used is `owaspbasiccluster`. The `PROJECTNAME` is whatever your project is. The `REGION` is a region/zone near you. 

If you want to use the gcloud sdk from a properly initialized gcloud commandline environment use the following command


```
gcloud container --project "PROJECTNAME" clusters create "owaspbasiccluster" --zone "REGION" --machine-type "n1-standard-1" --image-type "COS" --disk-size "100" --scopes "https://www.googleapis.com/auth/compute","https://www.googleapis.com/auth/devstorage.read_only","https://www.googleapis.com/auth/logging.write","https://www.googleapis.com/auth/cloud-platform","https://www.googleapis.com/auth/servicecontrol","https://www.googleapis.com/auth/service.management.readonly","https://www.googleapis.com/auth/trace.append","https://www.googleapis.com/auth/source.read_only" --num-nodes "3" --network "default" --enable-cloud-logging --no-enable-cloud-monitoring


```

The command creates a  similar cluster with more of the options set explicitly. 

## Set up Kubectl

Using the commandline gcloud SDK environment you need to set-up 'kubectl'

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


If you want to see the Kubernetes dashboard you can run `kubectl proxy` (in a new terminal window) and then navigate to http://localhost:8001/ui .



## Test Deployment

From the previous `describe service` command the `LoadBalancer Ingress:` line should have the external IP. The line below should give the port.

So.....

[IP]:[PORT]/WebGoat in your browser!

DONE



