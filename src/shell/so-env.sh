#! /bin/bash

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
#title           :so-env.sh
#description     :This script sets env variable
#author          :SIFT SO Team
#version         :0.0.1   
#usage           :bash so-env.sh
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

export SO_HOME=$(dirname `pwd`)
export JAVA_HOME=/usr/lib/jvm/java-8-oracle
#sift queues
export SIFT_QUEUES=siftqueue1:9092,siftqueue2:9092,siftqueue3:9092
export SO_LOG_LEVEL=debug
export LOG_TARGET=file
export AUTO_OFFSET_RESET=latest
export MAX_POLL_RECORDS_CONFIG=20
export MAX_POLL_INTERVAL_MS_CONFIG=60000
#URLs
export AUTH_REQUEST_URL=mc63kdf7d4l9r0c3-0njhd1ss851.auth.marketingcloudapis.com/v2/token
export EMAIL_REGISTER_CONTACT_REQUEST_URL=gateway-runtime:10060/runtime/api/v2/registercontact
export EMAIL_REQUEST_URL_PATH=/messaging/v1/email/messages/
export SECRETS_PATH=/opt/knowesis/sift/orchestrator/conf/secret.properties	
#Group Id
export GROUP_ID=emailHandler
#Seda components
export SEDA_EMAILAPI_SIZE=100
export SEDA_EMAILAPI_CONCURRENT_CONSUMERS=10
export SEDA_CONTACT_POLICY_SIZE=100
export SEDA_CONTACT_POLICY_CONCURRENT_CONSUMERS=10
export SEDA_REGISTER_CONTACT_SIZE=100
export SEDA_REGISTER_CONTACT_CONCURRENT_CONSUMERS=10
export SO_WHITE_LISTED_NUMBERS=0477704537,0457412087
export SO_WHITELISTING_ENABLE=true
export TRIGGER_SOURCE_CORE=CORE
export AUTH_TOKEN_REQUEST=
export ENABLE_DE_INGESTION=true
export INGESTION_REQUEST_URL_PATH=/data/v1/async/dataextensions/key:{key}/rows
export SO_CONTACTWINDOW_TOPIC=so.contactwindow.topic
export CONTAINER_ID=so-emailHandler