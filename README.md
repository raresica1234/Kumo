# Kumo
## Table of Content
- [Kumo](#kumo)
  - [Table of Content](#table-of-content)
  - [Setup](#setup)
    - [Generating public and private keys for signing the JWTs](#generating-public-and-private-keys-for-signing-the-jwts)
  - [Deploy](#deploy)
    - [Building the frontend](#building-the-frontend)
    - [Building the backend](#building-the-backend)
    - [Running the project](#running-the-project)

## Setup
- database & docker or w/o docker

### Generating public and private keys for signing the JWTs
Change directory into: `backend/src/main/resources/` and create a folder called `keystore`.  

Then run the command: `openssl ecparam -name secp521r1 -genkey -noout -out private-auth-key.pem` to generate the private key.

After that run: `openssl ec -in private-auth-key.pem -pubout -out public-auth-key.pem`


## Deploy
### Building the frontend
Change `basePath` in `environment.ts` from `frontend/src/environments` to what the url of the host will be.

Compile the project with `ng build --configuration production`

Build the docker using Dockerfile-frontend and tag it
`docker buildx build -f Dockerfile-frontend . -t kumo_frontend`


### Building the backend
Change `kumo.cors.origin` in `backend/src/resources/application-deploy.yml` to what the url of the host will be.

Compile the project with `mvn package -Pdeploy`

Build the docker using Dockerfile-frontend and tag it
`docker buildx build -f Dockerfile-backend . -t kumo_backend`

### Running the project
Simply compose the whole created images alongside the dependencies using `docker-compose-deploy.yaml`