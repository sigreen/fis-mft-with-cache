SFTP Download
=============

This flow polls a remote SFTP server and downloads files to the centralized `/mnt/mft/in` directory.

<p align="center">
  <img src="images/sftp-flow.png"/>
</p>

### Configuration

1. In Fuse Online, create a new Connection to your SFTP server.  It should look similar to below, and be sure to validate the connection

<p align="center">
  <img src="images/sftp-config.png"/>
</p>

2. Build the `syndesis-connector-file2` step extension, and import it into Fuse Online.

3.  Create a Connector called `File Connector` using the imported step extension.

4. Create a new Integration and add the following steps, as depicted in the image above.  For the remote SFTP server download configuration, be sure to use the following configuration

<p align="center">
  <img src="images/sftp-download.png"/>
</p>

5. For the *Write File* step, set the *Directory Name* `/mnt/mft/in?fileName=${header.CamelFileNameConsumed}-${date:now:yyyyMMddHHmmssSSS}.ready&tempFileName=.inprogress`.

6. Publish the flow and navigate to the `Shared Fuse` project in the OCP Web UI.

7. Once the SFTP integration is deployed, update the deployment config to attach the `nfs-pvc-01` storage.  Re-deploy the integration.

### Testing the flow

1.  Drop a file in the `/download` directory.
2. Once the file is consumed and deleted, navigate to the running Pod terminal (via OCP UI) and navigate to `/mnt/mft/` directory.  
3. Check the sub-directories to ensure the file has be routed correctly.