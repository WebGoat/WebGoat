# AWS

- This contains the various platform Quick Starts for Getting WebGoat Deployed into AWS.
- This IaaS quickstart uses AWS CloudFormation to perform most of the provisioning
- This IaaS quickstart is composed of three independent bundles
   - Code pipeline and Build
   - Deploying to EC2
   - Deploying to ECS



## Code Pipeline and Build

This Quickstart is for those that just want to perform builds with AWS. It uses CodeCommit but can be modified to use GitHub



## EC2

This uses AWS CodePipeline, CodeBuild, and CodeDeploy to land WebGoat to Running EC2 instances

## ECS

This uses AWS CodePipeline, CodeBuild, CodeDeploy, ECR, to update an ECS cluster