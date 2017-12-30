# Serverless MVN builds Featuring AWS

This Quick Start forms the basis for the other AWS  quickstarts. This only BUILDS the `webgoat-server` spring boot jar. If you want to also run it on AWS skip to the other AWS quickstarts

Before you Begin
1. Do you have an AWS Account?
2. Can you create an S3 Bucket?
3. Can you create a KMS Key?
4. Do you know what Cloud Formation is?
5. Do you have enough permissions to do any real work in said AWS Account? 

If you said no to any of those...hop over to [docs](https://aws.amazon.com/documentation/) and learn (but don't do) how to create those.


You will also need:
1. A GitHub Account
2. Fork of WebGoat
3. Personal access Token with `Admin:repo_hook` and `repo`



## Create Pre-requisites 

First pick an AWS region and stick with it for ALL the quickstarts. This one was mostly executed on US-east-1/2 but any region with KMS, CodePipeline, and CodeBuild will work. eu-Central-1, ap-southeast-1 and sa-east-1 have reported success also.


1. Create an S3 bucket and call it something meaningfull like `webgoat-stash-username` or something or use an existing bucket you have access to.
2. Create a KMS Key. Make sure you are a key administrator so you can add key users later.

## Deploy IAM role Cloud Formation Stacks

In this folder there are two json cloudformation templates:
-`01_IAM_codebuild.json`
-`01_IAM_codepipeline.json`

You will use the CloudFormation templates to create two roles. One for CodePipeline and the Other for CodeBuild. You will use the name of the bucket you just created as a parameter. 

## Update KMS Key

Access the KMS key you created earlier...add the two IAM roles you just created and Key Users

## Finally the Pipeline

You will use the yaml cloudformation template `01_codepiplinebuild.yml` to create the code building pipeline. 

Some of the parameters you will need to pass:
1. The S3 bucket (twice)
2. The Github Branch name (master? develop? yourbranchname?)
3. The Github user (if you forked it would be your username)
4. You personal access token for GitHub
5. The name or the repo (WebGoat! ...unless you  renamed and did a whole bunch of fancy git magic)
6. The ARN of the KMS key
7. The ARN of the role for the codebuild for parameter qsCodeRoleArn
8. The ARN for codepipeline

If this Stack successfully deploys a build will begin based on the latest commit automatically. You will have a funky named zip file (without the .zip ending) in a folder in the S3 bucket in a few minutes. 



Congratulations. You just Deployed a two step AWS Codepipeline that looks for codechanges and then performs a build. 

... ON to the next AWS Quickstart


