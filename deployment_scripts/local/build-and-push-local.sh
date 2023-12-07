docker build -f Dockerfile -t fri-rso-marketplace-catalog .
docker tag fri-rso-marketplace-catalog localhost:5050/fri-rso-marketplace-catalog:v1.1.4
docker push localhost:5050/fri-rso-marketplace-catalog:v1.1.4

# TODO: Move to other repo.
#docker build -f Dockerfile.loader-agent -t loader-agent .
#docker tag loader-agent localhost:5050/marketplace-loader-agent:v1.0.0-3
#docker push localhost:5050/marketplace-loader-agent:v1.0.0-3
