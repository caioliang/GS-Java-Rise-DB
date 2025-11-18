#!/bin/bash
export RG="rg-rise"
export LOCATION="brazilsouth"
export SERVER_NAME="msqlserver-rise"
export USERNAME="YOUR-USER"
export PASSWORD="YOUR-PASSWORD"
export DBNAME="rise-db"

az group create --name "$RG" --location "$LOCATION"

az sql server create \
  --location "$LOCATION" \
  --resource-group "$RG" \
  --name "$SERVER_NAME" \
  --admin-user "$USERNAME" \
  --admin-password "$PASSWORD" \
  --enable-public-network true

az sql db create \
  --resource-group "$RG" \
  --server "$SERVER_NAME" \
  --name "$DBNAME" \
  --service-objective Basic \
  --backup-storage-redundancy Local \
  --zone-redundant false

az sql server firewall-rule create \
  --resource-group "$RG" \
  --server "$SERVER_NAME" \
  --name AllowAllInternet \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 255.255.255.255