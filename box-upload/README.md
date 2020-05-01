Box Upload
=============

This flow consumes files from `/mnt/mft/out_box` directory, then pushes the files to a remote Box directory.

<p align="center">
  <img src="images/box-upload-flow.png"/>
</p>

### Configuration

1. If you haven't already done so, build the `syndesis-connector-file2` step extension, and import it into Fuse Online.

2. If you haven't already done so, create a Connector called `File Connector` using the imported step extension.

3. Create a new Integration and add the following steps, as depicted in the image above.  For the *Read File* configuration, be sure to use the following configuration.  You can use the following text for *Directory Name*: `/mnt/mft/out_box?antExclude=*/.inprogress&moveFailed=.error&move=.done`.

<p align="center">
  <img src="images/box-upload-read-file.png"/>
</p>

4. For the *Box Upload* step, configure it as below.  For the *Parent Folder Id* field, you'll need to lookup the ID of the Box folder you want to upload to.

<p align="center">
  <img src="images/box-upload-config.png"/>
</p>

5. Publish the flow as `Box Upload` and navigate to the `Syndesis` project in the OCP Web UI.

6. Once the *Box Upload* integration is deployed, update the deployment config to attach the `nfs-pvc-01` storage.  Re-deploy the integration.

### Testing the flow

1.  Drop a *.cobol* file in the SFTP `/download` directory.  Alternatively, you can use the HTTP upload service to upload a *COBOL* file.
2. Once the file is consumed and deleted, navigate to the running Pod terminal (via OCP UI) and navigate to `/mnt/mft/` directory.  
3. Check the sub-directories to ensure the file has be routed correctly.