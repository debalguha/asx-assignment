# Read Me First

The user-management-service has been implemented in the child module called user-service-api. The module co-exists
with another auxiliary module user-service-pact-generation, that helps generates pact contract testing file.

There is a CommandLineRunner called DataSetupService which sets up random 100 users when the service boots up. However
during contract testing stage, it is suppressed.

## How to run
    - Required minimum Jdk 8 to be installed and JAVA_HOME set
    - From the root directory of parent please execute
        ./mvnw clean package -pl user-service-api spring-boot:run
    - The above command will build the service module, run all the tests and bring up the spring boot service 
      with 100 random users created.

## Sample requests
    - Postman collection directory at project root contains a json request collections file, that can validate
      all the APIs and associated validations.

## Request/Response tracing
    - Logbook has been added to the dependencies to provide a trace level logging of incoming and outgoing requests.
