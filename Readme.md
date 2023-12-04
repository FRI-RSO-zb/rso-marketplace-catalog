# Marketplace

[![Run tests with Maven](https://github.com/FRI-RSO-zb/marketplace/actions/workflows/maven-run-tests.yml/badge.svg?branch=main)](https://github.com/FRI-RSO-zb/marketplace/actions/workflows/maven-run-tests.yml)

[![Build with Maven and package 'catalogue' as Docker image](https://github.com/FRI-RSO-zb/marketplace/actions/workflows/build-and-publish-docker-image-catalogue.yml/badge.svg)](https://github.com/FRI-RSO-zb/marketplace/actions/workflows/build-and-publish-docker-image-catalogue.yml)

[![Build with Maven and package 'loader-agent' as Docker image](https://github.com/FRI-RSO-zb/marketplace/actions/workflows/build-and-publish-docker-image-loader-agent.yml/badge.svg)](https://github.com/FRI-RSO-zb/marketplace/actions/workflows/build-and-publish-docker-image-loader-agent.yml)

## Build
`mvn clean package`

To run:
- `cd catalogue/target`
- `java -jar catalogue-1.0.0-SNAPSHOT.jar`



## Useful commands


### K3D:
Create cluster: `k3d cluster create marketplace-cluster \
    --servers 1 \
    --agents 3 \
    --api-port 0.0.0.0:6550 \
    -p "8888:80@loadbalancer" \
    --registry-use k3d-test-registry:5050 \
    --registry-config registries.yaml`

~Create cluster: `k3d cluster create testcluster \
    --servers 2 \
    --agents 3 \
    -p "9900:80@loadbalancer" \
    --registry-use k3d-test-registry:5050 \
    --registry-config registries.yaml`~

List clusters: `k3d cluster list`
Stop (pause) cluster: `k3d cluster stop [cl_name]`
Restart clusters: `k3d cluster start [cl_name]`

Create internal registry: `k3d registry create [reg_name] --port [5050]`

### Kubectl:
Get info about cluster: `kubectl cluster-info`
Show all items inside cluster: `kubectl get all --all-namespaces -o wide`


// Use `--dry-run=client -oyaml` flag to produce yaml file of command that is to be executed

`# kubectl create deployment marketplace-catalogue --image=k3d-test-registry:5050/marketplace-catalogue:debug`
`# kubectl create service clusterip marketplace-catalogue --tcp=8001:8001`

### Docker:
Build image: `docker build -f Dockerfile.catalogue -t marketplace-catalogue .`
Tag for upload to local registry: `docker tag marketplace-catalogue localhost:5050/marketplace-catalogue:debug`
Upload to registry: `docker push localhost:5050/marketplace-catalogue:debug`



