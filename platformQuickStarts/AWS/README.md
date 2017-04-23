# AWS

- This contains the various platform Quick Starts for Getting WebGoat Deployed into AWS.
- This IaaS quickstart uses AWS CloudFormation to perform most of the provisioning
- This IaaS quickstart is composed of three independent bundles
   - Code pipeline and Build
   - Deploying to EC2
   - Deploying to ECS


It is Assumed:
- You have an AWS Account 
- You know what an S3 bucket is
- You have seen the IAM console and have permissions to create IAM Roles




## Code Pipeline and Build

This Quickstart is for those that just want to perform builds with AWS. It Triggers off of Github to perform builds of `webgoat-server`



## EC2

(WIP) This uses AWS CodePipeline, CodeBuild, and CodeDeploy to land WebGoat to Running EC2 instances

## ECS

(WIP) This uses AWS CodePipeline, CodeBuild, ECR, to land a container onto  an ECS cluster