# Keycloak Retailsuite Event Listener

## Purpose

Create an custom event listener for Keycloak

## Prerequisites

- jdk-21.0.2.13-hotspot
- apache-maven-3.9.6
- Keycloak (latest)
- PubSub service account or emulator

## USAGE

- build from source: make package
- copy jar into your Keycloak /opt/keycloak/providers/
- Restart the Keycloak server

### Enable listener in Keycloak UI

Manage > Events > Config > Events Config > Event Listeners
Configuration

## Configure ENVIRONMENT VARIABLES

GCP_CREDENTIALS -> your GCP service-account.json in base64

GCP_PROJECT_ID

GCP_TOPIC_ID

GCP_ADMIN_TOPIC_ID
