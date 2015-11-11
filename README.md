# onemdm-client
[![wercker status](https://app.wercker.com/status/23dbc989b138aca8323b3c2d91fbd245/s/master "wercker status")](https://app.wercker.com/project/bykey/23dbc989b138aca8323b3c2d91fbd245)

## Setting up OneMDM client

Clone the repo:

``` bash
git clone https://github.com/multunus/onemdm-client.git
cd onemdm-client
cp src/main/java/com/multunus/onemdm/config/Config.java.example src/main/java/com/multunus/one_mdm_client/Config.java
```

Change the following constant(s) in `Config.java`:

* `HOST_URL`

Build the debug version of the APK from the command line:

``` bash
./gradlew clean installDebug
```

### TODO

- How to build the release version
- How to change the version number

