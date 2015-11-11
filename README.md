# onemdm-client
[![wercker status](https://app.wercker.com/status/23dbc989b138aca8323b3c2d91fbd245/s/master "wercker status")](https://app.wercker.com/project/bykey/23dbc989b138aca8323b3c2d91fbd245)

## Setting up OneMDM client

Clone the repo:

``` bash
git clone https://github.com/multunus/onemdm-client.git
cd one-mdmclient
cp src/main/java/com/multunus/onemdm/config/Config.java.example src/main/java/com/multunus/one_mdm_client/Config.java
```

Set the following constants in `Config.java`:

* `ACCESS_TOKEN`
* `HOST_URL`

Build the project from the command line:

``` bash
./gradlew clean build
```
