# Online bookstore - REST API built with Spring Boot
## The application has the basic functionalities of an online bookstore - listing books and authors for all users - authenticated and unauthenticated, adding to cart and buying books for authenticated regular users (customers) and adding new books and authors as well as processing orders and deleting customers for admins.

## I. General information
### 1. Initianlization of initial data - via data.sql file.

### 2. Logging into the app as regular user - perform POST request to https://powerful-beach-00895.herokuapp.com/api/auth/login with either of these:
* {"username": "user@example.com", "password": "12345"}
* {"username": "user2@example.com", "password": "12345"}
* the response body contains the JWT, needed for authentication of all request which need authentication as regular user
* the JWT is valid for 1 hour

### 3. Logging into the app as admin - perform POST request to https://powerful-beach-00895.herokuapp.com/api/auth/login with:
* {"username": "admin@example.com", "password": "12345"}
* the response body contains the JWT, needed for authentication of all requests which need authentication as admin
* the JWT is valid for 1 hour

## II. Functionalities available to all users
### 1. Listing all books
* GET request to https://powerful-beach-00895.herokuapp.com/api/books/all - returns the first 5 books ordered by title
* GET request to https://powerful-beach-00895.herokuapp.com/api/books/all?page={page}&size={size} for custom pagination and sizing

### 2. Listing books by genre
* GET request to https://powerful-beach-00895.herokuapp.com/api/books/{genre} - returns the first 5 books of the genre ordered by title
* GET request to https://powerful-beach-00895.herokuapp.com/api/books/{genre}?page={page}&size={size} for custom pagination and sizing

### 3. Viewing book details
* GET request to https://powerful-beach-00895.herokuapp.com/api/books/{id}/details where id is the id of the book.

### 4. Listing all authors
* GET request to https://powerful-beach-00895.herokuapp.com/api/authors - returns the first 3 authors sorted by last name.
* GET request to https://powerful-beach-00895.herokuapp.com/api/authors?page={page}&size={size} for custom pagination and sizing

### 5. Viewing book details
* GET request to https://powerful-beach-00895.herokuapp.com/api/authors/{id} where id is the id of the author

### 6. Requesting password reset
* POST request to https://powerful-beach-00895.herokuapp.com/api/password/reset
* request body should contain an email, which is registered in the app, and a base URL which is used for building the URL of the frontend app, where the password change is being shown the user
* request body example: {"email": "user@example.com", "baseUrl": "https://online-bookstore-dbdef.web.app/books"}

## III. Functionalities available to authenticated regular users (customers) - all request should be performed with valid JWT
### 1. Viewing own cart
* GET request to https://powerful-beach-00895.herokuapp.com/api/cart

### 2. Adding books to cart
* POST request to https://powerful-beach-00895.herokuapp.com/api/cart
* request body should contain the id and the quantity of the book which is being added to the cart
* request body example {"bookId": 1, "quantity": 1}

### 3. Removing a specific item from the cart
* DELETE request to https://powerful-beach-00895.herokuapp.com/api/cart/{id} where id is the id of the book which is being removed from the cart

### 4. Removing all items from the cart
* DELETE request to https://powerful-beach-00895.herokuapp.com/api/cart

### 5. Creating new order
* POST request to https://powerful-beach-00895.herokuapp.com/api/cart/confirm

### 6. Viewing own orders
* GET request to https://powerful-beach-00895.herokuapp.com/api/orders

### 7. Viewing details of an own order
* GET request to https://powerful-beach-00895.herokuapp.com/api/orders/{id}/details where id is the UUID of the order
