package com.cliente;

import com.cliente.ETSIINFLibraryStub.*;
import org.apache.axis2.context.*;
import java.util.*;

public class TestETSIINFLibraryClient {
    public static void main(String[] args) {
        try {
            System.out.println("=== Iniciando pruebas ETSIINFLibrary ===");
            ETSIINFLibraryStub stub = new ETSIINFLibraryStub("http://localhost:8080/axis2/services/ETSIINFLibrary");
            stub._getServiceClient().getOptions().setManageSession(true);

            //Login como admin

			Login Admin = new Login();
			User admin = new User();
			admin.setName("admin");
			admin.setPwd("admin");
			Admin.setArgs0(admin);
			Response loginAdmin = new Response();
			loginAdmin = stub.login(Admin).get_return();
			System.out.println("Login response: " + loginAdmin.getResponse());

			//Añadiendo usuario nuevo

			Username nuevoUsuario = new Username();
			AddUser nuevoUser = new AddUser();
			nuevoUsuario.setUsername("juan");
			nuevoUser.setArgs0(nuevoUsuario);
			AddUserResponse addUserRes = stub.addUser(nuevoUser).get_return();
			System.out.println("Usuario creado: " + addUserRes.getResponse() + ", Contraseña: " + addUserRes.getPwd());

			//Login como Juan (fallo porque ya hay sesión)

			User juan = new User();
			Login Juan = new Login();
			juan.setName("juan");
			juan.setPwd(addUserRes.getPwd());
			Juan.setArgs0(juan);
			Response loginJuan = stub.login(Juan).get_return();
			System.out.println("Login inválido Juan: " + loginJuan.getResponse());

			// Logout 
			Logout logout = new Logout();
			Response logoutResp = stub.logout(logout).get_return();
			System.out.println("Logout Admin: " + logoutResp.getResponse());

			// Login correcto de Juan
			loginJuan = stub.login(Juan).get_return();
			System.out.println("Login Juan: " + loginJuan.getResponse());

			//Cambio de contraseña

			PasswordPair pwdPair = new PasswordPair();
			ChangePassword PWDPair = new ChangePassword();
			pwdPair.setOldpwd(addUserRes.getPwd());
			pwdPair.setNewpwd("nuevaClave");
			PWDPair.setArgs0(pwdPair);
			Response pwdChanged = stub.changePassword(PWDPair).get_return();
			System.out.println("Contraseña cambiada: " + pwdChanged.getResponse());			

			//Logout

			stub.logout(logout).get_return();
			System.out.println("Logout Juan: " + logoutResp.getResponse());

			//Logout inválido (no hay usuarios dentro)

			logoutResp = stub.logout(logout).get_return();
			System.out.println("Logout inválido Juan: " + logoutResp.getResponse());

			//Añadiendo libros como admin

			stub.login(Admin);

			Book book = new Book();
			AddBook BOOK = new AddBook();
			book.setISSN("1234-5678");
			book.setName("Prueba");
			book.setAuthors(new String[] {"Juan Perez", "Ana Garcia"});
			BOOK.setArgs0(book);
			Response bookAdded = stub.addBook(BOOK).get_return();
			System.out.println("Libro añadido: " + bookAdded.getResponse());

			//Listado completo de libros

			ListBooks listbooks = new ListBooks();
			BookList fullList = stub.listBooks(listbooks).get_return();
			System.out.println("Catalogo: " + Arrays.toString(fullList.getBookNames()));
			System.out.println("ISSNs: " + Arrays.toString(fullList.getIssns()));


			//Prestamo de libro por Juan

			juan.setPwd("nuevaClave");
			Juan.setArgs0(juan);
			stub.login(Juan);
			BorrowBook borrowbook = new BorrowBook();
		    borrowbook.setArgs0("1234-5678");
			Response borrowed = stub.borrowBook(borrowbook).get_return();
			System.out.println("Libro prestado: " + borrowed.getResponse());

			//Lista de libros prestados por Juan

			ListBorrowedBooks listbrwdbooks = new ListBorrowedBooks();
     		BookList borrowedList = stub.listBorrowedBooks(listbrwdbooks).get_return();
			System.out.println("Libros prestados: " + Arrays.toString(borrowedList.getBookNames()));
			System.out.println("ISSNs prestados: " + Arrays.toString(borrowedList.getIssns()));

			// Logout Juan, login Admin y pide préstamo libro que ya tiene Juan
			stub.logout(logout);
			stub.login(Admin);
			borrowed = stub.borrowBook(borrowbook).get_return();
			System.out.println("Préstamo inválido: " + borrowed.getResponse());	
			stub.logout(logout);		

			//Login Juan y devloución libro

			stub.login(Juan);
			ReturnBook returnbook = new ReturnBook();
			returnbook.setArgs0("1234-5678");
			Response returned = stub.returnBook(returnbook).get_return();
			System.out.println("Libro devuelto: " + returned.getResponse());

			//Listado completo de libros (post-devolución)

			fullList = stub.listBooks(listbooks).get_return();
			System.out.println("Catalogo: " + Arrays.toString(fullList.getBookNames()));

			//Libros del autor Juan Perez

			Author autor = new Author();
			autor.setName("Juan Perez");
			GetBooksFromAuthor getauthor = new GetBooksFromAuthor();
			getauthor.setArgs0(autor);
			BookList booksAuthor = stub.getBooksFromAuthor(getauthor).get_return();
			System.out.println("Libros del autor Juan Perez: " + Arrays.toString(booksAuthor.getBookNames()));
			System.out.println("ISSNs del autor Juan Perez: " + Arrays.toString(booksAuthor.getIssns()));


			// Login Juan y login de admin

			stub.logout(logout);
			stub.login(Admin);

			//Caso error: eliminar un libro inexistente

			RemoveBook removebook = new RemoveBook();
			removebook.setArgs0("0000-0000");
			Response deleted = stub.removeBook(removebook).get_return();
			System.out.println("Intento de borrar libro inexistente: " + deleted.getResponse());		

			// Eliminar usuario
            DeleteUser del = new DeleteUser();
            Username delU = new Username();
            delU.setUsername("Juan");
            del.setArgs0(delU);
            System.out.println("Eliminar Juan: " + stub.deleteUser(del).get_return().getResponse());

			// Eliminar libro existente
     		removebook.setArgs0("1234-5678");
			deleted = stub.removeBook(removebook).get_return();
			System.out.println("Eliminar libro: " + deleted.getResponse());		

			//Logout final

			stub.logout(logout);

            System.out.println("=== Fin de las pruebas ===");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
