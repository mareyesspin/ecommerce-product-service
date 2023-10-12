#!/usr/bin/env bash
set -e

./gradlew --parallel build -x test

REGISTRY=local REPOSITORY=pagopopdev IMAGE_TAG=latest docker-compose up --build -d
