# Nutrition-Api

**_IMPORTANT:_** The repo is currently extended from a pure REST API to a website! Keycloak is currently disabled!

A REST API to store and get food, meals and a food diary. 
This API is also connected to the external provider https://world.openfoodfacts.org/ to retrieve food information by barcodes.

## Start Application

### Dev 

Make sure ports 5342 and 8080 are not in use.
Start the test db and the spring application:
~~~
docker-compose -f ./dev/docker/docker-compose.yml up
mvn spring-boot:run '-Dspring.profiles.active=dev'
~~~

Make sure port 8081 is not in use.
Start the keycloak (edit PATH, full path required!):
~~~
docker run --name keycloak -p 8081:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=adminpw -v <PATH>/nutritionApi/dev/keycloak/imports/realm-export.json:/opt/keycloak/data/import/realm-export.json quay.io/keycloak/keycloak:19.0.0 start-dev --import-realm
~~~

You can send test requests by importing the postman collection in ```/dev/postman``` to your local postman. 
In postman, get the token by clicking on the folder ```nutritionApi``` and get the token under ```Authorization```.
Now, you are able to send the following requests.