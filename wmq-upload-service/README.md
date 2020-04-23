WMQ Upload Service
========================================================

The Camel routes consumes an XML file from `/mnt/mft/out_wmq`, splits the file into individual records, then forwards each record to a WMQ broker.


Prerequisites
==============================

- Install JDK 1.8+
- Install Apache Maven 3.3+ [http://maven.apache.org]
- Ensure the http-upload-service is installed and running

Build, Deploy and Run on OpenShift
==============================

Let's try deploying our SpringBoot container to OpenShift.

1. Login via the CLI using `oc login -u YOURUSERID`.
2. Go to your Fuse Online project `oc project fuse`
3. Via the CLI, cd to the `wmq-upload-service` directory and execute `mvn -Popenshift`.
4. Once deployment and running on OpenShift, test uploading an XML file like so via HTTP:

```
curl --request PUT  --data-binary @../test/orders.xml --header "filename: order.xml" http://YOURROUTEURL  -v
```

Alternatively, you can place the `../test/orders.xml` file in the remote SFTP directory and route it through WMQ that way.