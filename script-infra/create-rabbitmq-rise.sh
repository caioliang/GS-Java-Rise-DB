#!/bin/bash
az container create \
    --resource-group rg-rise \
    --name rabbitmq-rise \
    --image rabbitmq:3-management \
	--os-type Linux \
    --cpu 1 \
    --memory 1.5 \
    --ports 5672 15672 \
    --environment-variables RABBITMQ_DEFAULT_USER=YOUR-USER RABBITMQ_DEFAULT_PASS=YOUR-PASSWORD \
    --dns-name-label rabbitmq-rise-unique \
    --restart-policy Always
	
