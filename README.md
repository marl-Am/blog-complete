## [Blog](https://blog-complete-wogw.onrender.com/)
A place to document what I am learning/interested in, and to show my portfolio.
Originally deployed on Azure, I later redeployed it on Render with a Supabase PostgreSQL database.

JAVA VERSION
* Java 17
  Dependencies (SpringBoot):
* Data JPA
* JDBC
* Spring Security
* Thymeleaf
* Spring Web
  hibernate-validator
* Mysql Connector J
* MssSQL JDBC
* Bootstrap
* webjars-locator
* webjars-locator-core
* lombok
* Spring-boot-starter-test
* Spring-security-test
* annotations
* Spring Actuator

Front page for Admin and Guest
![home](src/main/resources/static/images/adminfront.png)
![home](src/main/resources/static/images/frontpage.png)
The Admin user has create/edit/delete post privileges that a Guest does not have
![home](src/main/resources/static/images/adminpost.png)
![home](src/main/resources/static/images/adminpostedit.png)
The user can now create an account
![home](src/main/resources/static/images/signuppage.png)
The user can then log in to their account (As of now there are no special privileges for having an account)
![home](src/main/resources/static/images/login.png)
Your account page is displayed, showing the logged-in user's email and their roles.
![home](src/main/resources/static/images/accountpage.png)
Projects
![home](src/main/resources/static/images/projectspage.png)
Contact
![home](src/main/resources/static/images/contactpage.png)