# Helm chart deployment on K8s clusters

## install on local Docker Desktop with Kubernetes

### ClusterIP

    helm install "mytest" ./webgoat
    helm install "mytest" --debug ./webgoat 
    export POD_NAME=$(kubectl get pods --namespace default -l "app.kubernetes.io/name=webgoat,app.kubernetes.io/instance=mytest" -o jsonpath="{.items[0].metadata.name}")
    export CONTAINER_PORT=$(kubectl get pod --namespace default $POD_NAME -o jsonpath="{.spec.containers[0].ports[0].containerPort}")
    echo $CONTAINER_PORT
    kubectl --namespace default port-forward $POD_NAME 8080:$CONTAINER_PORT
    echo $POD_NAME

### uninstall 

    helm uninstall "mytest"

## install NodePort

    helm install "mytest" --debug ./webgoat --set service.type=NodePort
