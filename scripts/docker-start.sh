#!/bin/bash

# Define variables
CONTAINER_NAME_PROD="REST_BLOG_DB_PROD"
POSTGRES_USER_PROD="myusername"
POSTGRES_PASSWORD_PROD="mypassword"
PORT_PROD="3000:5432"

CONTAINER_NAME_TEST="REST_BLOG_DB_TEST"
POSTGRES_USER_TEST="myusernametest"
POSTGRES_PASSWORD_TEST="mypasswordtest"
PORT_TEST="3001:5432"

# Run the production container
echo -e "\nRunning production container..."

docker run --name "$CONTAINER_NAME_PROD" -e POSTGRES_USER="$POSTGRES_USER_PROD" -e POSTGRES_PASSWORD="$POSTGRES_PASSWORD_PROD" -p $PORT_PROD -d postgres

# Check for errors and print container ID
if [ $? -eq 0 ]; then
    echo -e "Production container ID: $(docker ps -aqf "name=$CONTAINER_NAME_PROD") \n"
else
    echo -e "Error running production container \n"
fi

# Run the test container
echo -e "Running test container..."
docker run --name "$CONTAINER_NAME_TEST" -e POSTGRES_USER="$POSTGRES_USER_TEST" -e POSTGRES_PASSWORD="$POSTGRES_PASSWORD_TEST" -p $PORT_TEST -d postgres

# Check for errors and print container ID
if [ $? -eq 0 ]; then
    echo -e "Test container ID: $(docker ps -aqf "name=$CONTAINER_NAME_TEST") \n"
else
    echo -e "Error running test container \n"
fi
