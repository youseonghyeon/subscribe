#!/bin/bash

IMAGE_NAME="epfzja/subscribe"

docker build -t "$IMAGE_NAME" .

docker push "$IMAGE_NAME"
