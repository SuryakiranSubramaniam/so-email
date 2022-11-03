#! /bin/bash

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
#title           :so-env.sh
#description     :This script sets env variable
#author          :SIFT SO Team
#version         :0.0.1   
#usage           :bash so-env.sh
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

export SO_HOME=$(dirname `pwd`)
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

export SIFT_QUEUES=localhost:9091,localhost:9092
export SO_LOG_LEVEL=debug
export LOG_TARGET=file
export AUTO_OFFSET_RESET=latest
export MAX_POLL_RECORDS_CONFIG=200
export MAX_POLL_INTERVAL_MS_CONFIG=60000


export AUTH_REQUEST_URL=mc63kdf7d4l9r0c3-0njhd1ss851.auth.marketingcloudapis.com/v2/token
export EMAIL_REGISTER_CONTACT_REQUEST_URL=gateway-runtime:10060/runtime/api/v2/registercontact
export EMAIL_REQUEST_URL_PATH=/messaging/v1/email/messages/

export GROUP_ID=emailhandler

export SEDA_EMAILAPI_SIZE=100
export SEDA_EMAILAPI_CONCURRENT_CONSUMERS=10
export SEDA_CONTACT_POLICY_SIZE=100
export SEDA_CONTACT_POLICY_CONCURRENT_CONSUMERS=10
export SEDA_REGISTER_CONTACT_SIZE=100
export SEDA_REGISTER_CONTACT_CONCURRENT_CONSUMERS=10
export SO_WHITE_LISTED_EMAILS=ajay@knowesis.com,amit.baid@knowesis.com
export SO_WHITELISTING_ENABLE=true
export TRIGGER_SOURCE_CORE=CORE
export AUTH_TOKEN_REQUEST=KOPutInJrhR7EnuvmEvg1KUUImCx2wRPVrDlBrlewQsyNlRQBybpBSviSBNVrPyY1hcJ41/2Mdgy9av45Dvhd8ok2wu26pYOZzJpmbI6uvN9K8ex1vHB2s7rlSsmOB9+PbOTpHC0SDrA9vrQGwCjLa9bjaCVdl6BRdYe/nYQe+SycPcvyRaxGoj0wQDbY7YJMJUWfhqMGa7XPFZFsmmcLg==
export OPOLO_REGISTER_CONTACT_ENABLE=false
export ENABLE_DE_INGESTION=true
export INGESTION_REQUEST_URL_PATH=/data/v1/async/dataextensions/key:AE0A3772-BAB8-4F0B-863D-7F4D193448A3/rows

export ELASTICSEARCH_URL=http://localhost:9200/_bulk
export ELASTICSEARCH_SIFT_LOGS_INDEX='siftlogs-%date{yyyy.MM.dd}'

export SO_CONTACTWINDOW_TOPIC=so.contactwindow.topic

export REDIS_POOL_SIZE=20
export SO_CACHE_HOSTS=localhost:6401,localhost:6402,localhost:6403
export DUPLICATION_KEY_TTL=600
export SO_CACHE_KEY_DEFAULT_TTL=28800
#configMode either clustered or native
export REDIS_CONFIG_MODE=clustered
export REDIS_HOST=127.0.0.1
export REDIS_PORT=6379
export MAXCONNECTIONS=128

export DUPLICATE_CHECK=true
