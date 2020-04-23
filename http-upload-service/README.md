HTTP Upload Service
========================================================

The Camel routes stands up an HTTP Server that accepts large files and streams them to a file directory.  Filename-based routing occurs, whereby large files are sent to `/mnt/mft/out_sftp` and XML files are sent to `/mnt/mft/out_wmq`.


Setup
==============================

- Install JDK 1.8+
- Install Apache Maven 3.3+ [http://maven.apache.org]

Build & Run Locally
==============================

1. Build this project so bundles are deployed into your local maven repo

```
<project home> $ mvn clean install
```
2. Run locally via SpringBoot

```
<project home>  $ mvn spring-boot:run -Dspring.cloud.kubernetes.enabled=false
```
3. Test sending a large file over HTTP

```
curl --request PUT  --data-binary @/path/to/your/file.txt --header "filename: file.txt" http://localhost:8123  -v
```

Build, Deploy and Run on OpenShift
==============================

Now that everything is running perfectly in your local environment, let's try deploying to our SpringBoot container to OpenShift.

1. Login via the CLI using `oc login -u YOURUSERID`.
2. Go to your Fuse Online project `oc project fuse-XXX`
3. Via the CLI, cd to your mvn project and execute `oc create -f src/main/fabric8/sa.yml` to create the service account.
4. Using the same CLI, execute `oc create -f src/main/fabric8/secrets.yml` to create the secret.
5. Using the same CLI, execute `oc create -f src/main/fabric8/configmap.yaml` to create the configmap.
6. Using the same CLI, execute `oc secrets add sa/qs-camel-config secret/camel-config` to add the secret to the service account.
7. Using the same CLI, execute `oc policy add-role-to-user view system:serviceaccount:YOURPROJECT:qs-camel-config` to give the 'view' permission to the service account.
8. Create the persistent volume claim via the CLI, by executing `oc create -f support/nfs-pvc-01.yaml`
9. Via the CLI, cd to the `http-upload-service` directory and execute `mvn clean -DskipTests fabric8:deploy -Popenshift`.
11. Once deployment and running on OpenShift, test uploading a large file like so:

```
curl --request PUT  --data-binary @/path/to/file/YOURFILENAME --header "filename: YOURFILENAME" http://YOURROUTEURL  -v
```
