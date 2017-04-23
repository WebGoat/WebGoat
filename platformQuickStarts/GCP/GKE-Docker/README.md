= GKE - DockerHub

This Quickstart shows how to create a Kubernettes Cluster using Google Cloud Platform's [GKE](https://cloud.google.com/container-engine/) and WebGoat Docker [Image](https://hub.docker.com/r/webgoat/webgoat-8.0/). 

To be Successfull with this Quickstart

1. You have a Google Cloud PlatForm account (trial works too) and have enough priveleges to create Computer Engine and Container Engine Resources
2. You know how to `git clone`
3. You have access to the gcloud SDK

== Create Kubernettes Cluster

Using the cloud console the default settings will suffice. Just provide a cluster name that makes sense to you. Otherwise you can use the [Google Cloud Shell](https://cloud.google.com/shell/docs/) and the followihg command: 


```
gcloud container --project "PROJECTNAME" clusters create "owaspbasiccluster" --zone "us-central1-b" --machine-type "n1-standard-1" --image-type "COS" --disk-size "100" --scopes "https://www.googleapis.com/auth/compute","https://www.googleapis.com/auth/devstorage.read_only","https://www.googleapis.com/auth/logging.write","https://www.googleapis.com/auth/cloud-platform","https://www.googleapis.com/auth/servicecontrol","https://www.googleapis.com/auth/service.management.readonly","https://www.googleapis.com/auth/trace.append","https://www.googleapis.com/auth/source.read_only" --num-nodes "3" --network "default" --enable-cloud-logging --no-enable-cloud-monitoring


```

Notice that Google Source is `readonly` and Cloud Platform as `Enabled`


== Set up Kubectl

Using either Google Cloud Shell or other commandline gcloud SDK environment you need to set-up 'kubectl'

If you have not already installed 'Kubectl' you can do so with the following command using `gcloud`
- `gcloud components install kubectl` 

Then you just run:
`gcloud container clusters get-credentials owaspbasiccluster --zone us-central1-b --project PROJECTNAME`


== Deploy WebGoat Deployment

Time to deploy the latest DockerImage for WebGoat


Let's First Make a namespace for this: 
- `kubectl create namespace webgoat`

Now it is time to make the magic happen!

- `kubectl create -f /where_you_git_cloned_webgoat/platformQuickStart/GCP/GKE-Docker/webgoat_noDNSnoTLS.yml`

This should complete with no errors.

Use the following command to see information about the deployment
- `kubectl describe deployment  webgoat-dpl --namespace=webgoat`

After a few minutes the service endpoint should be ready. You can check the status with
- `kubectl describe service  webgoatsvc --namespace=webgoat`

In the output you should see a message like "Created Load..."  after a "Creating load..." which means that the public facing loadbalancer (even thou there is just one container running!) is ready.


== Test Deployment

From the previous `describe service` command the `LoadBalancer Ingress:` line should have the external IP. The line below should give the port.

So.....

[IP]:[PORT]/WebGoat in your browser!

DONE


