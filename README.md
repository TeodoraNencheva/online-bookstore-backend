# Online bookstore - REST API built with Spring Boot
## The application has the basic functionalities of an online bookstore - listing books and authors for all users - authenticated and unauthenticated, adding to cart and buying books for authenticated regular users (customers) and adding new books and authors as well as processing orders and deleting customers for admins.

## I. General information
### 1. Database model
![database model](https://user-images.githubusercontent.com/79698998/233033429-70881cb9-f6fd-4145-ac94-f3f8e5d4d8f1.PNG)

### 2. Maintenance interceptor
* the bookstore should be closed due to maintenance every Sunday between midnight and 5am
* the interceptor checks the current time and if it is maintenance time, redirects to the "/api/maintenance" endpoint

### 3. Scheduler
* the application keeps track of the count of orders made on the current date
* in order to start counting the orders from 0 every day, the scheduler sets the counter to 0 every day at midnight

### 4. Cloudinary
* the application uses Cloudinary for image upload

## II. Authentication

### 1. Logging into the app as regular user 
* POST request to https://powerful-beach-00895.herokuapp.com/api/auth/login with either of these:
* {"username": "user@example.com", "password": "12345"}
* {"username": "user2@example.com", "password": "12345"}
* the response body contains the JWT, needed for authentication of all request which need authentication as regular user
* the JWT is valid for 1 hour

### 2. Logging into the app as admin 
* POST request to https://powerful-beach-00895.herokuapp.com/api/auth/login with:
* {"username": "admin@example.com", "password": "12345"}
* the response body contains the JWT, needed for authentication of all requests which need authentication as admin
* the JWT is valid for 1 hour

### 3. Registering a new user
* POST request to https://powerful-beach-00895.herokuapp.com/api/auth/register
* request body should contain valid email, first name, last name, password, confirmPassword and baseUrl where baseUrl is used for building a registration confirmation URL which the user receives per email to verify the registration
* request body example: {"email": "user3@example.com", "firstName": "John". "lastName": "Doe", "password": "12345", "confirmPassword": "12345", "baseUrl" : "https://online-bookstore-dbdef.web.app/books"}

### 4. Confirming a registration
* POST request to https://powerful-beach-00895.herokuapp.com/api/auth/register/verify?token={token} where token is the secure token generated for the given registration confirmation

## III. Functionalities available to all users
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

### 6. Viewing all books of an author
* GET request to https://powerful-beach-00895.herokuapp.com/api/books?authorId={authorId}

### 7. Searching books by a search text
* GET request to https://powerful-beach-00895.herokuapp.com/api/books/search
* the request accepts a DTO with search text
* request body example: {"searchText": "some text"}

### 8. Searching authors by a search text
* GET request to https://powerful-beach-00895.herokuapp.com/api/authors/search
* the request accepts a DTO with search text
* request body example: {"searchText": "some text"}

### 9. Requesting password reset
* POST request to https://powerful-beach-00895.herokuapp.com/api/password/reset
* request body should contain an email, which is registered in the app, and a base URL which is used for building the URL of the frontend app, where the password change is being shown the user
* request body example: {"email": "user@example.com", "baseUrl": "https://online-bookstore-dbdef.web.app/books"}

### 10. Changing password
* POST request to https://powerful-beach-00895.herokuapp.com/api/password/change
* the request body should contain the secure token for the password change, new password and password confirmation
* request body example: {"token": "randomToken". "password": "new password", "confirmPassword": "new password"}

## IV. Functionalities available to authenticated regular users (customers) - all request should be performed with a valid JWT
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


## V. Functionalities available to admins - all requests should be performed with a valid JWT
### 1. Adding a new author
* POST request to https://powerful-beach-00895.herokuapp.com/api/authors/add
* the request accepts two request parts - 1) a DTO with valid first name, last name and biography and 2) a picture (optional)

### 2. Editing an existing author
* PUT request to https://powerful-beach-00895.herokuapp.com/api/authors/{id} where id is the id of the author whose details are being updated
* the request accepts two request parts - 1) a DTO with valid first name, last name and biography and 2) a picture (optional)

### 3. Deleting an existing author
* DELETE request to https://powerful-beach-00895.herokuapp.com/api/authors/{id} where id is the id of the author who is being deleted

### 4. Adding a new book
* POST request to https://powerful-beach-00895.herokuapp.com/api/books/add
* the request accepts two request parts - 1) a DTO with valid title, authorId, genreId, yearOfPublication, summary and price and 2) picture

### 5. Editing an existing book
* PUT request to https://powerful-beach-00895.herokuapp.com/api/books/{id} where id is the id of the book which is being updated
* the request accepts two request parts - 1) a DTO with valid title, authorId, genreId, yearOfPublication, summary and price and 2) picture

### 6. Deleting an existing book
* DELETE request to https://powerful-beach-00895.herokuapp.com/api/books/{id} where id is the id of the book which is being deleted

### 7. Viewing all processed orders
* GET request to https://powerful-beach-00895.herokuapp.com/api/orders/processed

### 8. Viewing all unprocessed orders
* GET request to https://powerful-beach-00895.herokuapp.com/api/orders/unprocessed

### 9. Viewing order's details
* GET request to https://powerful-beach-00895.herokuapp.com/api/orders/{id}/details where id is the UUID of the order

### 10. Processing orders
* POST request to https://powerful-beach-00895.herokuapp.com/api/orders/{id}/confirm where id is the UUID of the order

### 11. Viewing the number of orders that are made on the same day
* GET request to https://powerful-beach-00895.herokuapp.com/api/orders/statistics

### 12. Viewing all customers' profile details
* GET request to https://powerful-beach-00895.herokuapp.com/api/auth/all

### 13. Adding a regular user as an admin
* POST request to https://powerful-beach-00895.herokuapp.com/api/auth/addAdmin/{username} where {username} is the email of a registered customer

### 14. Deleting a user
* DELETE request to https://powerful-beach-00895.herokuapp.com/api/auth/{username} where {username} is the email of the customer to be deleted
