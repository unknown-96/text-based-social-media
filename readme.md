# Text based social media
This project is the result of the assignment that I received a week ago. It contains a Spring Boot project that exposes an API meant to be theoretically used by a Front End social media application. It allows users to register, login, post text, comment on posts, follow other users and view different types of posts and comments while ensuring security and preventing unathorized access.

# Implemented features
* **Account registration and authentication**: A user can register with their email as their username and choose a plan type.
* **Posts and Comments**: Post and comment handling is done to ensure the proper length of text and the repetitiveness (spam). Everything is then stored to the database.
* **Following and Follower relations**: The relations between followers and the accounts they are following is managed through the API. Just like posts and comments, the relations are stored to the database as well.
* **Views**: The API supports a variety of views meant to be utilized by the Front End application. Viewing posts of people they follow, posts of their own, and their latest comments are all supported. A view of their followers is supported as well.

# Tech stack
The project was developed and tested in Debian 12 Bookworm with IntelliJ IDEA 2023.2.5. The technologies used were the following:

* PostgreSQL 15.5
* Java 17
* Spring 3.1.5
    * Spring Security
    * Spring Data JPA
    * Spring Web
    * Hibernate Validator
    * Lombok

# Usage instructions
The project contains two application.properties files:
* /src/main/resources/application.properties
* /src/test/resources/application.properties

Both expect the database to be:
* Named as: db
* Available at port: 5432
* With the password of user postgres as: postgres

Either the database will be set as expected above, or the files will have to be modified accordingly. Sample values for the database are provided in the sample-values.txt file. The passwords provided for the three sample accounts are bcrypt encoded and their decoded values are first-password, second-password and third-password respectively. The schema is auto-generated on the first run of the project.

Loading the project with Intellij IDEA on a machine with Java 17 or higher, allows easily running the project as well as the tests included.

# Communication
The project makes communication effective by providing four DTOs.

* The Post/Comment DTOs which internally have the same structure
```
    long accountId
    String text
    String timestamp
```
* The Account DTO
```
    String plan
    String email
    String password
```
* The Follower DTO
```
    long accountId
    String email
```
Communication is done using JSON exclusively. JSON received from the Front End is checked for validity and if it doesn't match the corresponding DTO fields, it gets rejected.

# Endpoints
* **Registration**: POST - /register - Account DTO
* **New post**: POST - /posts - Post DTO
* **New comment**: POST - /posts/{postId}/comments - Comment DTO
* **Add follower**: POST - /follow - Follower DTO
* **Remove follower**: DELETE - /unfollow - Follower DTO
* **View posts of people the follow**: GET - /relevant-posts/{accountId}
* **View their own posts**: GET - /own-posts/{accountId}
* **View the latest 100 comments of a post**: GET - /posts/{postId}/comments
* **View latest comments on their posts or of the people they follow**: GET -- /relevant-comments/{accountId}
* **View followers of a user**: GET - /followers/{accountId}
