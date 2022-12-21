# Kumo
## Table of Content
1. [Setup](#setup)

## Setup
- database & docker or w/o docker

### Generating public and private keys for signing the JWTs
Change directory into: `backend/src/main/resources/` and create a folder called `keystore`.  

Then run the command: `openssl ecparam -name prime256v1 -genkey -noout -out private-auth-key.pem` to generate the private key.

After that run: `openssl ec -in private-auth-key.pem -pubout -out public-auth-key.pem`