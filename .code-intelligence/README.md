# Web App fuzzing

This is a document on how to use the Web App fuzzer without using the CI Fuzz local installation

## Prerequisites

This repository contains a template and documentation to set up fuzzing for your own Web App/Web API. You need to have your own CI Fuzz server installed. In the near future, an account in CI Fuzz SaaS platform will also be supported.

This template will work with few modifications for Java HTTP APIs (this use case is also described in this readme).

 With some modifications, it can also be used to fuzz Java GRPC APIs and Go APIs (both HTTP and GRPC).

## Repository setup

Create an online Git repository based on this one and modify the following files:

### .code-intelligence/web_services.yaml

If your application consists of several microservices, add one web service entry for each microservice and name them appropriately. Otherwise, the default name can be used.

If your application does not have web controllers written in the Springboot framework, or if you already configured fuzzing but you have problems with the endpoint analysis step when starting fuzzing, add a file with OpenAPI specifications. The path is relative to the root directory of this git repository.

### .code-intelligence/fuzz_targets/fuzzallendpoints.yaml

Modify base_url to the URL where your application under test will be accessible. Make sure that networ traffic from the CI Fuzz server to the application is allowed on the port where the application is running.

If you changed or added web service name(s) in the previous file, change/add them here to match.

### .code-intelligence/fuzz-targets

Use one or more of the files here to add the steps needed to authenticate to your application or set it's initial state. More info [here](https://help.code-intelligence.com/configure-http-headers)

### .code-intelligence/fuzzallendpoints_seed_corpus

By default, CI Fuzz will generate HTTP requests based on the results of automatic endpoint analysis of your application. If you have low code coverage, you can improve it by putting valid raw HTTP requests in this directory, in text files ending with .http extension. 

You can also add multiple requests in one file, if you want to fuzz more complex scenarios in your application (for example create something and then delete it). In order to do this, separate the requests with a newline and set correct Content-Length header for all requests that have a body, except the last request, where it is not needed.

This is further explained later on in this Readme.

## Configure project in CI Fuzz Web Interface

Push the changes to your online repository.

Go to the CI Fuzz web interface and create a project from this repository. Instructions can be found [here](https://help.code-intelligence.com/using-the-web-app)

## Generate CICD script or prepare environment to start your web application

CICD script is documented [here](https://help.code-intelligence.com/continuous-fuzzing-setup)

You can also run your web application on any machine, to test fuzzing. 

In any case, you will need to download the Java Agent which your application will need to start with, in order to be fuzzed:

${CICTL} --server="${FUZZING_SERVER_URL}" get javaagent

The CICD script documentation page shows how to use the cictl tool and how to obtain the CI Fuzz API token needed for it to work.

## Run and connect your web application

Explained [here](https://help.code-intelligence.com/configure-your-web-app-for-fuzzing-with-ci-fuzz-server). 

At this point, everything listed in the prerequisites section of that page is covered in your Git repository.

Run your application with the Java agent and correct options, or set up your CICD script to run it.

The java agent command you are using will in most cases look like this:

java -javaagent:$PWD/fuzzing_agent.jar=fuzzing_server_host=my_fuzzing_server.com,api_token=NzM4NDgxOTZAZ2l0aHViOjlmNGE4ODkwZDE5MGFhYjVhY2I1YzFlMjEzNTgxYmFlYmUwNmIwYjk0YzM4OTZjMTU2NDg2ZDE0MGQ5ODI3M2Q,instrumentation_includes="org.some_package.**",service_name=projects/my_project/web_services/mywebservice -jar my_application.jar

Make sure that the names of web services match the entries in web_services.yaml and fuzzallendpoints.yaml. If you did not change those, use the default name: mywebservice. If you give the java agent aa different service_name, the fuzz test will not work correctly!

Full list of java agent options can be found [here](https://help.code-intelligence.com/set-java-agent-options).
  
When running, your application's output should say that it is instrumenting Java classes and that it is connecting to the CI Fuzz server:

INFO: Got status 'OK' from fuzzing server

In your CI Fuzz web interface, in the Web Services view, your web service should be green.

## Start fuzzing

If you have started your application, you can start the fuzz test from CI Fuzz web interface. If you have configured CICD integration, you can trigger your CICD pipeline. Then your web service will turn green and a fuzzing run will shortly appear in CI Fuzz web interface.

## Additional arguments for configuring the fuzzing agent:

- `instrumentation_includes` - A ":" separated list of package glob patterns to include in the instrumentation
- `instrumentation_excludes` - A ":" separarted list of package glob patterns to exclude from instrumentation

## Working with seeds

For the out of process fuzzer to reach any meaningful code coverage good seeds should be provided.
When running a fuzz target seeds are automatically generated using the OpenAPI spec or Spring endpoint
analysis. In some cases these automatically generated seeds will require some user modification.

The automatic seeds can only be found on the CI Fuzz server's file system, in the directory  `/root/.local/share/code-intelligence/<project_dir>/corpora` and the format
is plain `.http` files. However, users should add valid http requests as new files in the relevant directory in the fuzzing project's git repository, not to this corpora directory.

Note: The http request parsing is fairly strict, for example the `Content-Length` header has to be set correctly for all requests except the last and an
empty line has to be entered before the request body.

## Encrypted communication

Add the option `tls=true` to the fuzzing agent, as the CI Fuzz server must have TLS enabled. This option only applies to the
communication between the fuzzing agent and CI Fuzz server. The gRPC communication between fuzzing agent and fuzzer is
unencrypted!

A default SSL configuration is used for the communication which first looks for an OpenSSL provider and if not found
falls back on the JDK SSL provider (see [GrpcSslContexts.java#L229](https://github.com/grpc/grpc-java/blob/814655cdde5797854289ce5c2ec3e7b80ce0cf44/netty/src/main/java/io/grpc/netty/GrpcSslContexts.java#L229)).

## Troubleshooting

- Check that the fuzzing server can be reached from the web application. This could require editing the firewall settings,
docker networks, cluster settings ...

## Tips and Tricks

- The "REST Client" VS Code extension is great for sending .http files as requests from the IDE.
https://marketplace.visualstudio.com/items?itemName=humao.rest-client
- The `instrumentation_includes` option to the fuzzing agent is an important setting that can be tweaked depending on
the project. If this option is not set the agent will default to instrumenting all packages. This might cause errors
for certain large projects that include many dependencies. If that is the case a simple heuristic is to instrument the
user code + some dependencies that are especially interesting (the user should know).

