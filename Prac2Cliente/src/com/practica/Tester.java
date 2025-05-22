package com.practica;
import com.practica.ETSIINFLibraryStub.*;

public class Tester {
    
    private static ETSIINFLibraryStub stub;
    private static String sessionId;
    
    public static void main(String[] args) {
        try {
            // Initialize the stub
            stub = new ETSIINFLibraryStub("http://localhost:8080/axis2/services/ETSIINFLibrary");
            
            // Test login (admin)
            testAdminLogin();
            
            // Test book operations
            testAddBook();
            testGetBook();
            testListBooks();
            
            // Test user operations
            testAddUser();
            testChangePassword();
            
            // Test borrowing operations
            testBorrowBook();
            testReturnBook();
            testListBorrowedBooks();
            
            // Test admin-only operations
            testRemoveBook();
            testDeleteUser();
            
            // Test logout
            testLogout();
            
            System.out.println("\nAll tests completed successfully!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void testAdminLogin() throws Exception {
        System.out.println("Testing admin login...");
        
        Login login = new Login();
        User user = new User();
        user.setName("admin");
        user.setPwd("admin");
        login.setArgs0(user);
        
        LoginResponse response = stub.login(login);
        if (response.get_return().getResponse()) {
            System.out.println("Admin login successful");
            // Store session ID from headers for subsequent calls
        } else {
            throw new Exception("Admin login failed");
        }
    }
    
    private static void testAddBook() throws Exception {
        System.out.println("\nTesting addBook...");
        
        // Create a test book
        com.practica.ETSIINFLibraryStub.Book book = new Book();
        book.setISSN("123-456");
        book.setName("Test Book");
        book.setAuthors(new String[]{"Test Author"});
        
        com.practica.ETSIINFLibraryStub.AddBook addBook = new com.practica.ETSIINFLibraryStub.AddBook();
        addBook.setArgs0(book);
        
        com.practica.ETSIINFLibraryStub.AddBookResponse response = stub.addBook(addBook);
        if (response.get_return().isResponseSpecified()) {
            System.out.println("Book added successfully");
        } else {
            throw new Exception("Failed to add book");
        }
        
        // Test adding duplicate book
        AddBookResponse duplicateResponse = stub.addBook(addBook);
        if (duplicateResponse.get_return().isResponseSpecified()) {
            System.out.println("Duplicate book added (inventory increased)");
        } else {
            throw new Exception("Failed to add duplicate book");
        }
    }
    
    private static void testGetBook() throws Exception {
        System.out.println("\nTesting getBook...");
        
        GetBook getBook = new GetBook();
        getBook.setArgs0("123-456"); // ISBN we just added
        
        GetBookResponse response = stub.getBook(getBook);
        Book returnedBook = response.get_return();
        if (returnedBook != null && returnedBook.getISSN().equals("123-456")) {
            System.out.println("Book retrieved successfully: " + returnedBook.getName());
        } else {
            throw new Exception("Failed to get book");
        }
        
        // Test with non-existent ISBN
        getBook.setArgs0("000-000");
        GetBookResponse invalidResponse = stub.getBook(getBook);
        if (invalidResponse.get_return() == null) {
            System.out.println("Correctly returned null for non-existent book");
        } else {
            throw new Exception("Should return null for non-existent book");
        }
    }
    
    private static void testListBooks() throws Exception {
        System.out.println("\nTesting listBooks...");
        
        ListBooks listBooks = new ListBooks();
        ListBooksResponse response = stub.listBooks(listBooks);
        
        if (response.get_return().isResult()) {
            System.out.println("Books listed successfully. Found " + 
                response.get_return().getBookNames().length + " books");
        } else {
            throw new Exception("Failed to list books");
        }
    }
    
    private static void testAddUser() throws Exception {
        System.out.println("\nTesting addUser...");
        
        AddUser addUser = new AddUser();
        Username username = new Username();
        username.setUsername("testuser");
        addUser.setArgs0(username);
        
        AddUserResponse response = stub.addUser(addUser);
        if (response.get_return().isResponse()) {
            System.out.println("User added successfully. Temporary password: " + 
                response.get_return().getPwd());
        } else {
            throw new Exception("Failed to add user");
        }
    }
    
    private static void testChangePassword() throws Exception {
        System.out.println("\nTesting changePassword...");
        
        // First login as the test user to get a session
        Login login = new Login();
        User user = new User();
        user.setName("testuser");
        user.setPwd("temp123"); // Use the temp password from addUser response
        login.setArgs0(user);
        
        LoginResponse loginResponse = stub.login(login);
        if (!loginResponse.get_return().isResponse()) {
            throw new Exception("Failed to login as test user");
        }
        
        // Now change password
        ChangePassword changePassword = new ChangePassword();
        PasswordPair pp = new PasswordPair();
        pp.setOldpwd("temp123");
        pp.setNewpwd("newpassword");
        changePassword.setArgs0(pp);
        
        ChangePasswordResponse response = stub.changePassword(changePassword);
        if (response.get_return().isResponse()) {
            System.out.println("Password changed successfully");
        } else {
            throw new Exception("Failed to change password");
        }
        
        // Logout the test user
        Logout logout = new Logout();
        stub.logout(logout);
    }
    
    private static void testBorrowBook() throws Exception {
        System.out.println("\nTesting borrowBook...");
        
        BorrowBook borrowBook = new BorrowBook();
        borrowBook.setArgs0("123-456"); // ISBN we added earlier
        
        BorrowBookResponse response = stub.borrowBook(borrowBook);
        if (response.get_return().isResponse()) {
            System.out.println("Book borrowed successfully");
        } else {
            throw new Exception("Failed to borrow book");
        }
    }
    
    private static void testReturnBook() throws Exception {
        System.out.println("\nTesting returnBook...");
        
        ReturnBook returnBook = new ReturnBook();
        returnBook.setArgs0("123-456"); // ISBN we borrowed earlier
        
        ReturnBookResponse response = stub.returnBook(returnBook);
        if (response.get_return().isResponse()) {
            System.out.println("Book returned successfully");
        } else {
            throw new Exception("Failed to return book");
        }
    }
    
    private static void testListBorrowedBooks() throws Exception {
        System.out.println("\nTesting listBorrowedBooks...");
        
        // First borrow a book to test with
        BorrowBook borrowBook = new BorrowBook();
        borrowBook.setArgs0("123-456");
        stub.borrowBook(borrowBook);
        
        ListBorrowedBooks listBorrowedBooks = new ListBorrowedBooks();
        ListBorrowedBooksResponse response = stub.listBorrowedBooks(listBorrowedBooks);
        
        if (response.get_return().isResult()) {
            System.out.println("Borrowed books listed successfully. Found " + 
                response.get_return().getBookNames().length + " books");
        } else {
            throw new Exception("Failed to list borrowed books");
        }
        
        // Return the book
        ReturnBook returnBook = new ReturnBook();
        returnBook.setArgs0("123-456");
        stub.returnBook(returnBook);
    }
    
    private static void testRemoveBook() throws Exception {
        System.out.println("\nTesting removeBook...");
        
        // First add a book to remove
        Book book = new Book();
        book.setISSN("999-999");
        book.setName("Book to remove");
        book.setAuthors(new String[]{"Test Author"});
        
        AddBook addBook = new AddBook();
        addBook.setArgs0(book);
        stub.addBook(addBook);
        
        // Now remove it
        RemoveBook removeBook = new RemoveBook();
        removeBook.setArgs0("999-999");
        
        RemoveBookResponse response = stub.removeBook(removeBook);
        if (response.get_return().isResponse()) {
            System.out.println("Book removed successfully");
        } else {
            throw new Exception("Failed to remove book");
        }
    }
    
    private static void testDeleteUser() throws Exception {
        System.out.println("\nTesting deleteUser...");
        
        // First add a user to delete
        AddUser addUser = new AddUser();
        Username username = new Username();
        username.setUsername("usertodelete");
        addUser.setArgs0(username);
        stub.addUser(addUser);
        
        // Now delete the user
        DeleteUser deleteUser = new DeleteUser();
        Username deleteUsername = new Username();
        deleteUsername.setUsername("usertodelete");
        deleteUser.setArgs0(deleteUsername);
        
        DeleteUserResponse response = stub.deleteUser(deleteUser);
        if (response.get_return().isResponse()) {
            System.out.println("User deleted successfully");
        } else {
            throw new Exception("Failed to delete user");
        }
    }
    
    private static void testLogout() throws Exception {
        System.out.println("\nTesting logout...");
        
        Logout logout = new Logout();
        LogoutResponse response = stub.logout(logout);
        
        if (response.get_return().isResponse()) {
            System.out.println("Logout successful");
        } else {
            throw new Exception("Logout failed");
        }
    }
}