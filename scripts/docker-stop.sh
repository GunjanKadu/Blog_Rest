#!/bin/bash

# Function to stop and remove a Docker container
stop_container() {
    local name="$1"

    docker stop "$name" &> /dev/null
    docker rm "$name" &> /dev/null

    echo "Container '$name' stopped and removed."
}

echo "Stopping Docker containers..."
stop_container "REST_BLOG_DB_PROD"
stop_container "REST_BLOG_DB_TEST"