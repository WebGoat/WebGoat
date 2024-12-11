# WEBGOAT Application - Project

## Configure K8s cluster on Kubernetes

### Requirements:

- Before configuring the cluster, you need to register an application on Azure and obtain a secret so that Terraform can automate the creation of the cluster on your behalf.
- Azure CLI version 2.30.0 or higher

Then, in your terminal, go to the `terraform` directory

```
cd ./terraform
```

**Optional** In `variables.tfvars`, set:

```
appId = "<YOUR APP ID>"
password = "<YOUR APP SECRET>"
```

If you are running Terraform for the first time, intialize Terraform:

```
terraform init
```

If you updated the `variables.tfvars` file, apply the tf files to configure the cluster :

```
terraform apply -var-file="variables.tfvars"
```

Or if you want to enter the app ID and secret from the command line:

```
terraform apply
```

