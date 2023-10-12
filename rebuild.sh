#!/usr/bin/env bash
set -e

./gradlew --parallel clean build -x test

# removed old container, if this is not done it might cause conflicts with new images
docker-compose rm -s -f
REGISTRY=local REPOSITORY=pagopopdev IMAGE_TAG=latest docker-compose up --build -d
