#!/bin/sh -e

cd  app/build/outputs/apk
aws s3 cp onemdm-v*.apk s3://onemdm/onemdm.apk --grants "read=uri=http://acs.amazonaws.com/groups/global/AllUsers"
aws s3 cp onemdm-debug-v*.apk s3://onemdm/onemdm-demo.apk --grants "read=uri=http://acs.amazonaws.com/groups/global/AllUsers"
