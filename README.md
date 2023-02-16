# online-bookstore-backend
Spring Boot backend application exposing REST API for online bookstore. The frontend application, which consumes it, is available at the online-bookstore-frontend repository.

To login into user's account perform POST request to https://powerful-beach-00895.herokuapp.com/api/auth/login (or http://localhost:8080/api/auth/login if running the app locally) with either of these:
- {"username": "user@example.com", "password": "12345"}
- {"username": "user2@example.com", "password": "12345"}

The response body contains the JWT to use to authenticate the requests, which need authentication as user.

To login into admin's account perform POST request to https://powerful-beach-00895.herokuapp.com/api/auth/login (or http://localhost:8080/api/auth/login if running the app locally) with {"username": "admin@example.com", "password": "12345"}.

The response body contains the JWT to use to authenticate the requests, which need authentication as admin.

JWT is valid for 1 hour.
