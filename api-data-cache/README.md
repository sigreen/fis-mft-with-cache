API Data Cache Service
========================================================

This Camel routes demonstrated connectivity with Red Hat Data Grid.


Setup
==============================

- Install JDK 1.8+
- Install Apache Maven 3.3+ [http://maven.apache.org]

Build, Deploy and Run on OpenShift
==============================

1. Login via the CLI using `oc login -u YOURUSERID`.
2. Create a new project project by running: `oc new-project api-data-cache`.
3. Via the Administrator web console, provision a new Data Grid v8.0 operator.
4. Once the Operator is deployed and running, navigate to Secrets and copy the developer username / password from `example-infinispan-generated-secret`.  Paste the credentials in `/src/main/resources/application.properties`.
5. Via the CLI, cd to the `api-data-cache` directory and execute `mvn -Popenshift`.
6. Once deployment and running on OpenShift, you should see the following log output (or similar):

```
18:12:40.893 [Camel (camel) thread #1 - timer://messages] INFO  test-message-producer-route - GENERATED message: Sample message with ID message-key-8
18:12:45.893 [Camel (camel) thread #1 - timer://messages] INFO  test-message-producer-route - GENERATED message: Sample message with ID message-key-4
18:12:45.895 [Camel (camel) thread #1 - timer://messages] INFO  test-message-consumer-route - PROCESSING new message: Sample message with ID message-key-4
18:12:47.907 [Camel (camel) thread #2 - timer://check] INFO  test-using-cache-directly-route - CACHE LOOKUP: no entry for key message-key-2
18:12:50.893 [Camel (camel) thread #1 - timer://messages] INFO  test-message-producer-route - GENERATED message: Sample message with ID message-key-1
18:12:50.895 [Camel (camel) thread #1 - timer://messages] INFO  test-message-consumer-route - PROCESSING new message: Sample message with ID message-key-1
18:12:55.894 [Camel (camel) thread #1 - timer://messages] INFO  test-message-producer-route - GENERATED message: Sample message with ID message-key-4
18:13:00.893 [Camel (camel) thread #1 - timer://messages] INFO  test-message-producer-route - GENERATED message: Sample message with ID message-key-2
18:13:00.896 [Camel (camel) thread #2 - timer://check] INFO  test-using-cache-directly-route - CACHE LOOKUP: entry for key message-key-4 is: true
```
