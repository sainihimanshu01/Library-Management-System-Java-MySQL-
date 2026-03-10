Library Management System (Java + MySQL):-
    A Library Management System desktop application built using Java Swing and MySQL (JDBC).
    The system helps manage books, issue books to students, return books, and track issued books through a graphical interface.

Features:-
    Login authentication system
    Add new books to the library
    Prevent duplicate book entries
    View all books in a table
    Delete books from the system
    Issue books to students
    Return issued books
    Track issued books
    Continuous display numbering for books
    MySQL database integration using JDBC



Technologies Used:-
    Java (Swing) – GUI
    MySQL / MariaDB (XAMPP) – Database
    JDBC – Database connectivity
    Git & GitHub – Version control

Project Structure
    Library-Management-System
    │
    ├── LibraryManagement.java
    ├── mysql-connector-j-9.6.0.jar
    ├── README.md

Database Setup

Start XAMPP and run MySQL.

Open:
http://localhost/phpmyadmin

Create a database:
library_db

Run the following SQL:

    CREATE TABLE books  (
        id INT PRIMARY KEY AUTO_INCREMENT,
        title VARCHAR(100),
        author VARCHAR(100),
        quantity INT
    );

    CREATE TABLE issued_books (
        issue_id INT PRIMARY KEY AUTO_INCREMENT,
        book_id INT,
        student_name VARCHAR(100)
    );


Default Login Credentials:-
    Username: admin
    Password: 1234

You can change these credentials inside the source code:

    if(user.equals("admin") && pass.equals("1234"))



How to Run the Project
    1. Start MySQL
        Open XAMPP Control Panel and start MySQL.

    2. Compile the Program
        Open terminal inside the project folder and run:
            javac -cp ".;mysql-connector-j-9.6.0.jar" LibraryManagement.java

    3. Run the Program
            java -cp ".;mysql-connector-j-9.6.0.jar" LibraryManagement


Application Workflow:-
    User logs into the system.
    Books can be added to the database.
    Books are displayed in the View Books table.
    Books can be issued to students.
    Quantity decreases automatically when issued.
    Returning a book restores the quantity.



Future Improvements:-
    Search books by title
    Update book information
    Student management system
    Fine calculation for late returns
    Role-based authentication
    Store login credentials in database



Author:-
    Himanshu Malakar
    B.Sc IT Student


<img width="325" height="208" alt="login" src="https://github.com/user-attachments/assets/778b1ae3-f4d9-4fe8-b74d-a0cf4e8fc71c" />

<img width="1110" height="742" alt="add_book" src="https://github.com/user-attachments/assets/145344b9-9547-474a-abbf-9466f9f4bf3c" />

<img width="1102" height="732" alt="issue_book" src="https://github.com/user-attachments/assets/d8304449-1d84-408c-a420-1912bd2ad5e0" />


    
