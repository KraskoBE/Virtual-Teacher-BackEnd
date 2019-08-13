# Virtual-Teacher-BackEnd
Virtual Teacher Back-end

**Virtual Teacher**

**Java Team Project**

This document describes a **Team project** for the  **Java cohort** at Telerik Academy.

**Project Description**

Your task is to develop **Virtual Teacher** web application, an online platform for tutoring. Users will be able to register as either a teacher or a student.

**Students** should be able to enroll for video courses, watch the available the video lectures for them and after successful completion rate them. After finishing each video within a course, students will be asked to submit an assignment, which will be graded by the teacher.

**Teachers** should be able to create courses and upload video lectures with assignments. Each assignment will be graded individually after submission from the students. Users can become teachers only after approval from administrator.

**Functional Requirements**

**Web application**

**Public Section**

The public part of your projects should be **visible without authentication**. This includes the application landing page, the user login, a student and a teacher registration forms, as well as a catalog with the available courses. The catalogs you can filter by name, topic and teacher and order by name and rating. Users who are not logged in, cannot enroll for a course.

**Private Part (All users)**

**Registered users**  should have private section (profile) in the web application accessible after  **successful login**.

Users should be able to:

- view and edit their profile (name, email and picture)
- change their password
- enroll for courses
- see their enrolled and completed courses

Courses:

- The videos in a course will be accessible only after enrollment.
- The application will keep track of the student&#39;s progress through the course.
- The assignments will unlock after a student finishes a lecture.
- Students are required to submit their work as a file (txt, doc, java, etc.).
- Students will see their grades for the assignment and also their average grade for the course.
- After receiving a grade for their last assignment for the course, if their average grade is above the passing grade, they can leave a rating for the course.


**Private Part (Teachers only)**

Teacher users have access to all the functionality that students do. However, when they are approved by an administrator, a course administration page is accessible to them. On it they can create, edit and delete **courses**.

 Each **course** should have:

- title
- topic (writing, history, programming etc.)
- description
- list of **lectures**.

Each **lecture** should have:

- title
- description and/or notes
- video
- assignment

Once a course is set up, there should be a button to submit it. After the submission, the course become available for enrollment and the teachers now have the option to download and grade the assignments.

(Optional) A comment page for each course, where students can comment on the lectures and ask questions.

**Administration Part**

Administrator users can&#39;t be registered though the ordinary process.
They can delete courses and users, create other administrators and approve teachers.

**Web Application**

Behind every great **web application** is a great **backend**. And the first impression your application creates is via the **frontend**.

It is your choice whether to use **Spring REST API + JavaScript UI (single page app)** or **Spring MVC + Thymeleaf** to create it.

Don&#39;t forget to follow good coding practices when implementing the REST API.

**Database**

All **Virtual Teacher** data should be stored in **MySQL database (or MariaDB).**

**Technical Requirements**

Your project should meet the following requirements (these requirements will be used by TA trainers during project defense):

- Use  **IntelliJ IDEA**
- For the web application (UI) you can choose from two approaches
  - Use **Spring MVC** Framework with **Thymeleaf** template engine for generating the UI
  - Research and build Single Page Application to serve the UI with JavaScript library of your choice. Please, keep in mind that we have not covered this in our sessions, so it is totally up to you how to implement the authentication and other SPA specifics that depend on the library you choose.
**(Preferred choice)**
- Use **AJAX** form communication in some parts of your application
- Use **Dependency**** Inversion **principle and** Dependency ****Injection** technique following the best practices
- Use **MySQL (MariaDB)** as database back-end
  - Use  **Hibernate**  to access your database
    - If you do the research, you may use JPA or some other Hibernate-based framework.
  - Using **repositories and service layer** is a must
- Create **tables with data** with **server-side paging and sorting** for every model entity
  - You can use JavaScript library or generate your own HTML tables
- Create beautiful and interactive web UI

-
  - You may use **Bootstrap** or **Materialize**
  - You may change the standard theme and modify it to apply own web design and visual styles.
  - You may use a bootstrap/materialize downloaded theme.
- Apply  **error handling**  and  **data validation**  to avoid crashes when invalid data is entered (both client-side and server-side)
- Use **Spring**** Security** for managing users and roles
  - Your registered users should have at least one of the three roles: **student, teacher** and **administrator**
  - If you&#39;ve chosen to develop SPA, you&#39;d need to implement **JWT Authentication**.
- Create  **unit tests**  for your &quot;business&quot; functionality following the best practices for writing unit tests ( **at least 80% code coverage** )
- Follow  **OOP**  best practices and **SOLID** principles.
- Use **Git** &amp; **GitLab** for source control management – be careful with commit messages
- Use **Trello** for project management
- Create **user documentation / manual** of the application

**Optional Requirements (bonus points)**

- Integrate your app with a  **Continuous Integration server**  (e.g. Jenkins or other). Configure your unit tests **to run on each commit** to your master/develop branch or at every pull request
- Host your application&#39;s backend in a public hosting provider of your choice (e.g. AWS, Azure, GCP, Heroku)



# Database import script
```
INSERT INTO `topics` (`name`)
VALUES ('Business'),
       ('Design'),
       ('Development'),
       ('Finance&Accounting'),
       ('Health&Fitness'),
       ('IT&Software')
       
INSERT INTO `roles` (`name`)
VALUES ('Student'),
       ('Teacher'),
       ('Admin');
```
