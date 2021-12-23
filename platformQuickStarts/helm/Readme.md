# Helm chart deployment on OpenShift K8S clusters

This helm chart can be used on a OpenShift Code Ready Container environment or an OpenShift Cloud Container environment.

With the OpenShift CRC (Code Ready Container) cluster you run an entire environment on your local machine. (> 4 vCPU, >8GB mem)

See the Red Hat documentation for general understanding of OpenShift. Make sure helm is installed as well.

https://developers.redhat.com/developer-sandbox

## CRC commands

    crc config set cpus 6
    crc config set memory 12288
    crc setup
    crc start
    eval $(crc oc-env)
    oc login -u developer https://api.crc.testing:6443
    oc new-project demo-project

The example without modification uses *demo-project* as the project/namespace for installing WebGoat and WebWolf.


## Helm install this example on your local Code Ready Container environment

    helm install goat1 ./webgoat

## Helm install on single node Developer Sandbox (cloud)

    oc login --token=sha256~phDWy6Wm_oJQW6kmOHEbLkRdDIXU6b70hRVmdSYWolM --server=https://api.sandbox-m2.rz9k.p1.openshiftapps.com:6443 
    helm install --set namespace=renezubcevic-dev --set accessMode=ReadWriteOnce --set urlpostfix=.apps.sandbox-m2.rz9k.p1.openshiftapps.com goat1 ./webgoat

A code ready container looks the same for all developers on their local machine, but a developer sandbox requires other credentials from your account in the cloud and different namespace and urlpostfix and also a different access mode for the persistent storage.
Of course the token here is a fake.

## uninstall 

    helm uninstall goat1

The URL on a Code Ready Container is build from router name + namespace + default extension .apps-crc.testing:

+ [https://webgoat-1-goat-demo-project.apps-crc.testing/WebGoat](https://webgoat-1-goat-demo-project.apps-crc.testing/WebGoat)
+ [http://webwolf-1-wolf-demo-project.apps-crc.testing/WebWolf](http://webwolf-1-wolf-demo-project.apps-crc.testing/WebWolf)

## Explanation

deployment.yaml contains two K8S deployment elements. Both use the same Persistent Volume Claim and use the same Volume mapping. 
They both use the same image but with other entrypoint and command arguments. The java.io.dir is also mapped to this persistent volume mapping. The number of pods is 1 for both WebGoat and WebWolf. WebGoat uses the WEBWOLF_HOST parameter to know where the external address of WebWolf is defined. WebWolf uses WEBGOAT_HOST to define the internal service address to WebGoat for connecting to the HSQL database

persistent-storage-claim.yaml contains the OpenShift K8S extension for requestig a volume with Read-Write access that will survive any pod replacements.

service.yaml defines the service ports for both WebGoat and WebWolf

route-goat defines an https endpoint toward the 8080 port. route-wolf defines an http port towards the 9090 port.
