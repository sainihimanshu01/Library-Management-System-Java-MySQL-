import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class LibraryManagement extends JFrame {

    JTextField titleField, authorField, quantityField;
    JTextField issueBookIdField, issueStudentField;
    JTextField returnBookIdField;

    DefaultTableModel bookModel;
    DefaultTableModel issuedModel;

    final String URL = "jdbc:mysql://localhost:3306/library_db";
    final String USER = "root";
    final String PASSWORD = "";

    public LibraryManagement() {

        if(!showLogin()){
            System.exit(0);
        }

        setTitle("Library Management System");
        setSize(900,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Add Book", createAddBookPanel());
        tabs.add("View Books", createViewBookPanel());
        tabs.add("Issue Book", createIssuePanel());
        tabs.add("Return Book", createReturnPanel());
        tabs.add("Issued Books", createIssuedViewPanel());

        add(tabs);
        setVisible(true);
    }

    boolean showLogin(){

        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();

        Object[] message = {
                "Username:", username,
                "Password:", password
        };

        int option = JOptionPane.showConfirmDialog(
                null,
                message,
                "Login",
                JOptionPane.OK_CANCEL_OPTION
        );

        if(option == JOptionPane.OK_OPTION){

            String user = username.getText();
            String pass = new String(password.getPassword());

            if(user.equals("admin") && pass.equals("1234")){
                return true;
            } else {
                JOptionPane.showMessageDialog(null,"Invalid Login");
                return false;
            }
        }

        return false;
    }

    Connection connect() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC",
                "root",
                ""
        );
    }

    JPanel createAddBookPanel(){

        JPanel panel = new JPanel(new GridLayout(5,2,10,10));

        titleField = new JTextField();
        authorField = new JTextField();
        quantityField = new JTextField();

        JButton addBtn = new JButton("Add Book");

        panel.add(new JLabel("Title:"));
        panel.add(titleField);

        panel.add(new JLabel("Author:"));
        panel.add(authorField);

        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        panel.add(new JLabel());
        panel.add(addBtn);

        addBtn.addActionListener(e -> addBook());

        return panel;
    }

    JPanel createViewBookPanel(){

        JPanel panel = new JPanel(new BorderLayout());

        bookModel = new DefaultTableModel();
        JTable table = new JTable(bookModel);

        bookModel.setColumnIdentifiers(
                new String[]{"No","ID","Title","Author","Quantity"}
        );

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadBooks());

        JButton deleteBtn = new JButton("Delete Book");
        deleteBtn.addActionListener(e -> deleteBook());

        JPanel topPanel = new JPanel();
        topPanel.add(refreshBtn);
        topPanel.add(deleteBtn);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    JPanel createIssuePanel(){

        JPanel panel = new JPanel(new GridLayout(5,2,10,10));

        issueBookIdField = new JTextField();
        issueStudentField = new JTextField();

        JButton issueBtn = new JButton("Issue Book");

        panel.add(new JLabel("Book ID:"));
        panel.add(issueBookIdField);

        panel.add(new JLabel("Student Name:"));
        panel.add(issueStudentField);

        panel.add(new JLabel());
        panel.add(issueBtn);

        issueBtn.addActionListener(e -> issueBook());

        return panel;
    }

    JPanel createReturnPanel(){

        JPanel panel = new JPanel(new GridLayout(4,2,10,10));

        returnBookIdField = new JTextField();

        JButton returnBtn = new JButton("Return Book");

        panel.add(new JLabel("Book ID:"));
        panel.add(returnBookIdField);

        panel.add(new JLabel());
        panel.add(returnBtn);

        returnBtn.addActionListener(e -> returnBook());

        return panel;
    }

    JPanel createIssuedViewPanel(){

        JPanel panel = new JPanel(new BorderLayout());

        issuedModel = new DefaultTableModel();
        JTable table = new JTable(issuedModel);

        issuedModel.setColumnIdentifiers(
                new String[]{"Issue ID","Book ID","Student Name"}
        );

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadIssuedBooks());

        panel.add(refreshBtn, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    void addBook(){

        try(Connection con = connect()){

            PreparedStatement check = con.prepareStatement(
                    "SELECT * FROM books WHERE title=? AND author=?");

            check.setString(1,titleField.getText());
            check.setString(2,authorField.getText());

            ResultSet rs = check.executeQuery();

            if(rs.next()){
                JOptionPane.showMessageDialog(this,"Book Already Exists");
                return;
            }

            PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO books(title,author,quantity) VALUES(?,?,?)"
            );

            pst.setString(1,titleField.getText());
            pst.setString(2,authorField.getText());
            pst.setInt(3,Integer.parseInt(quantityField.getText()));

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,"Book Added");

            titleField.setText("");
            authorField.setText("");
            quantityField.setText("");

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    void loadBooks()
    {

     try(Connection con = connect()){

        bookModel.setRowCount(0);

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM books");

        int serial = 1;

        while(rs.next()){

            bookModel.addRow(new Object[]{
                    serial++,
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("quantity")
            });
        }

     }
     catch(Exception e){
        JOptionPane.showMessageDialog(this,e.getMessage());
     }
    }
    void deleteBook(){

        String id = JOptionPane.showInputDialog(this,"Enter Book ID");

        if(id == null || id.isEmpty()) return;

        try(Connection con = connect()){

            PreparedStatement pst = con.prepareStatement(
                    "DELETE FROM books WHERE id=?");

            pst.setInt(1,Integer.parseInt(id));

            int rows = pst.executeUpdate();

            if(rows>0){
                JOptionPane.showMessageDialog(this,"Book Deleted");
                loadBooks();
            }else{
                JOptionPane.showMessageDialog(this,"Invalid Book ID");
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    void issueBook(){

        try(Connection con = connect()){

            int bookId = Integer.parseInt(issueBookIdField.getText());
            String student = issueStudentField.getText();

            PreparedStatement check = con.prepareStatement(
                    "SELECT quantity FROM books WHERE id=?");

            check.setInt(1,bookId);

            ResultSet rs = check.executeQuery();

            if(rs.next()){

                int qty = rs.getInt("quantity");

                if(qty>0){

                    PreparedStatement update = con.prepareStatement(
                            "UPDATE books SET quantity=quantity-1 WHERE id=?");

                    update.setInt(1,bookId);
                    update.executeUpdate();

                    PreparedStatement insert = con.prepareStatement(
                            "INSERT INTO issued_books(book_id,student_name) VALUES(?,?)");

                    insert.setInt(1,bookId);
                    insert.setString(2,student);

                    insert.executeUpdate();

                    JOptionPane.showMessageDialog(this,"Book Issued");

                }else{
                    JOptionPane.showMessageDialog(this,"Out of Stock");
                }

            }else{
                JOptionPane.showMessageDialog(this,"Invalid Book ID");
            }

            issueBookIdField.setText("");
            issueStudentField.setText("");

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    void returnBook(){

        try(Connection con = connect()){

            int bookId = Integer.parseInt(returnBookIdField.getText());

            PreparedStatement delete = con.prepareStatement(
                    "DELETE FROM issued_books WHERE book_id=? LIMIT 1");

            delete.setInt(1,bookId);

            int rows = delete.executeUpdate();

            if(rows>0){

                PreparedStatement update = con.prepareStatement(
                        "UPDATE books SET quantity=quantity+1 WHERE id=?");

                update.setInt(1,bookId);
                update.executeUpdate();

                JOptionPane.showMessageDialog(this,"Book Returned");

            }else{
                JOptionPane.showMessageDialog(this,"Book Not Issued");
            }

            returnBookIdField.setText("");

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    void loadIssuedBooks(){

        try(Connection con = connect()){

            issuedModel.setRowCount(0);

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM issued_books");

            while(rs.next()){

                issuedModel.addRow(new Object[]{
                        rs.getInt("issue_id"),
                        rs.getInt("book_id"),
                        rs.getString("student_name")
                });
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    public static void main(String[] args){
        new LibraryManagement();
    }
}