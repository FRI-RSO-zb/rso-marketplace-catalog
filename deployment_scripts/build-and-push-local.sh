docker build -f Dockerfile.catalogue -t marketplace-catalogue .
docker tag marketplace-catalogue localhost:5050/marketplace-catalogue:v1.0.0-1
docker push localhost:5050/marketplace-catalogue:v1.0.0-1


docker build -f Dockerfile.loader-agent -t loader-agent .
docker tag loader-agent localhost:5050/marketplace-loader-agent:v1.0.0-3
docker push localhost:5050/marketplace-loader-agent:v1.0.0-3
