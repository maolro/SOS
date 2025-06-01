package com.cliente;

import com.cliente.ETSIINFLibraryStub.*;
import java.util.*;
import java.rmi.RemoteException;

public class TestETSIINFLibraryClient {

    public static void main(String[] args) {
        try {
            System.out.println("=== Iniciando pruebas ETSIINFLibrary ===");
            ETSIINFLibraryStub stub = new ETSIINFLibraryStub("http://localhost:8080/axis2/services/ETSIINFLibrary");
            stub._getServiceClient().getOptions().setManageSession(true);

			// Login como admin
			System.out.println("Login inicial: " + loginTest("admin", "admin", stub));	

			// Añadiendo usuario nuevo
			AddUserResponse addUserRes = addUserTest("Juan", stub);
			System.out.println("Usuario creado: " + addUserRes.getResponse() + ", Contraseña: " + addUserRes.getPwd());
			String nombreClave = addUserRes.getPwd();

			// Añadiendo usuario ya existente
			AddUserResponse addUserRes2 = addUserTest("admin", stub);
			System.out.println("Creación inválida de usuario: " + addUserRes2.getResponse() + ", Contraseña: " + addUserRes2.getPwd());
				
			// Login como Juan (fallo porque ya hay sesión)
			System.out.println("Login inválido Juan (ya hay sesión): " + loginTest("Juan", nombreClave, stub));	


			// Logout admin
			System.out.println("Logout Admin: " + logoutTest(stub));

			// Ejecución de operaciones sin autenticación
			System.out.println("Contraseña cambiada (sin autenticación): " + changePasswordTest("admin", "claveAdmin", stub));			

			// Login como Juan (fallo por contraseña invalida)
			System.out.println("Login inválido Juan (contraseña incorrecta): " + loginTest("Juan", "claveIncorrecta", stub));
			
			// Login correcto de Juan y cambio de contraseña
			System.out.println("Login Juan: " + loginTest("Juan", nombreClave, stub));
			System.out.println("Contraseña cambiada: " + changePasswordTest(addUserRes.getPwd(), "claveJuan", stub));			
			System.out.println("Logout Juan: " + logoutTest(stub));

			//Añadiendo libros como admin
			System.out.println("Login Admin: " + loginTest("admin", "admin", stub));
			String[] autores1 = {"Paco Perez", "Ana Maria"};
			String[] autores2 = {"Miguel de Cervantes"};
			String[] autores3 = {"Paco Perez"};
			System.out.println("Libro añadido: " + addBookTest("1234-5678", "Test",
			autores1, stub));
			System.out.println("Libro añadido: " + addBookTest("5555-6666", "Don Quijote de la Mancha",
			autores2, stub));
			System.out.println("Libro añadido: " + addBookTest("5555-6666", "Don Quijote de la Mancha",
			autores2, stub));
			System.out.println("Libro añadido: " + addBookTest("7777-8888", "SOS",
			autores3, stub));

			//Listado completo de libros
			bookListTest(stub);

			//Logout de Admin, Login de Juan y Prestamo de libros por Juan
			System.out.println("Logout Admin: " + logoutTest(stub));
			System.out.println("Login Juan: " + loginTest("Juan", "claveJuan", stub));
			System.out.println("Libro prestado: " + borrowBookTest("1234-5678", stub));
			System.out.println("Libro prestado: " + borrowBookTest("5555-6666", stub));

			//Lista de libros prestados por Juan
			borrowedListTest(stub);

			// Juan intenta realizar una operación por la que no tiene permisos
			System.out.println("Eliminar libro (no hay permisos): " + removeBookTest("1234-5678", stub));		

			// Logout Juan, login Admin y pide préstamo libro que ya tiene Juan
			System.out.println("Logout Juan: " + logoutTest(stub));
			System.out.println("Login Admin: " + loginTest("admin", "admin", stub));
			System.out.println("Préstamo inválido: " + borrowBookTest("1234-5678", stub));	

			// Admin pide préstamo de un libro del que quedan copias
			System.out.println("Libro prestado: " + borrowBookTest("5555-6666", stub));
			borrowedListTest(stub);

			// Ejecución de operación sin autenticación
			System.out.println("Logout Admin: " + logoutTest(stub));
			borrowedListTest(stub);

			//Login Juan y devloución libro

			System.out.println("Login Juan: " + loginTest("Juan", "claveJuan", stub));
			System.out.println("Libro devuelto: " + returnBookTest("1234-5678", stub));
			borrowedListTest(stub);

			//Libros del autor Juan Perez
			listarLibrosAutorTest("Paco Perez", stub);

			// Logout Juan, login de admin y eliminación libro inexistente
			System.out.println("Logout Juan: "+logoutTest(stub));
			System.out.println("Login Admin: " + loginTest("admin", "admin", stub));
			System.out.println("Eliminar libro inexistente: " + removeBookTest("0000-0000", stub));		

			// Eliminar usuario
			System.out.println("Eliminar usuario Juan: "+deleteUserTest("Juan", stub));

			// Eliminar libro existente
			System.out.println("Eliminar libro existente: " + removeBookTest("1234-5678", stub));		

			//Logout final

			System.out.println("Logout admin: "+logoutTest(stub));

            System.out.println("=== Fin de las pruebas ===");
        } 
		catch (Exception e) {
            e.printStackTrace();
        }
    }
	private static boolean removeBookTest(String issn, ETSIINFLibraryStub stub) throws RemoteException{
		RemoveBook removebook = new RemoveBook();
		removebook.setArgs0(issn);
		return stub.removeBook(removebook).get_return().getResponse();
	}
	private static boolean deleteUserTest(String username, ETSIINFLibraryStub stub) throws RemoteException{
		DeleteUser del = new DeleteUser();
        Username delU = new Username();
        delU.setUsername(username);
        del.setArgs0(delU);
		return stub.deleteUser(del).get_return().getResponse();
	}
	private static void listarLibrosAutorTest(String authorName, ETSIINFLibraryStub stub) throws RemoteException{
			Author autor = new Author();
			autor.setName(authorName);
			GetBooksFromAuthor getauthor = new GetBooksFromAuthor();
			getauthor.setArgs0(autor);
			BookList booksAuthor = stub.getBooksFromAuthor(getauthor).get_return();
			System.out.println("Libros del autor "+authorName+":" + Arrays.toString(booksAuthor.getBookNames()));
			System.out.println("ISSNs del autor "+authorName+":" + Arrays.toString(booksAuthor.getIssns()));
	}

	private static boolean addBookTest(String issn, String nombre, 
	String[] authors, ETSIINFLibraryStub stub) throws RemoteException{
		Book book = new Book();
		AddBook BOOK = new AddBook();
		book.setISSN(issn);
		book.setName(nombre);
		book.setAuthors(authors);
		BOOK.setArgs0(book);
		Response bookAdded = stub.addBook(BOOK).get_return();
		return bookAdded.getResponse();
	}
	private static boolean logoutTest(ETSIINFLibraryStub stub) throws RemoteException{
		Logout logout = new Logout();
		Response logoutResp = stub.logout(logout).get_return();
		return logoutResp.getResponse();
	}
	private static boolean changePasswordTest(String viejaClave, String nuevaClave,
	ETSIINFLibraryStub stub) throws RemoteException{
		PasswordPair pwdPair = new PasswordPair();
		ChangePassword PWDPair = new ChangePassword();
		pwdPair.setOldpwd(viejaClave);
		pwdPair.setNewpwd(nuevaClave);
		PWDPair.setArgs0(pwdPair);
		Response pwdChanged = stub.changePassword(PWDPair).get_return();
		return pwdChanged.getResponse();
	}
	private static void bookListTest(ETSIINFLibraryStub stub) throws RemoteException{
		ListBooks listbooks = new ListBooks();
		BookList fullList = stub.listBooks(listbooks).get_return();
		System.out.println("Catalogo: " + Arrays.toString(fullList.getBookNames()));
		System.out.println("ISSNs: " + Arrays.toString(fullList.getIssns()));
	}
	private static boolean returnBookTest(String issn, ETSIINFLibraryStub stub) throws RemoteException{
		ReturnBook returnbook = new ReturnBook();
		returnbook.setArgs0(issn);
		Response returned = stub.returnBook(returnbook).get_return();
		return returned.getResponse();
	}
	private static boolean borrowBookTest(String issn, ETSIINFLibraryStub stub) throws RemoteException{
		BorrowBook borrowbook = new BorrowBook();
		borrowbook.setArgs0(issn);
		Response borrowed = stub.borrowBook(borrowbook).get_return();
		return borrowed.getResponse();
	}
	private static void borrowedListTest(ETSIINFLibraryStub stub) throws RemoteException{
		ListBorrowedBooks listbrwdbooks = new ListBorrowedBooks();
     	BookList borrowedList = stub.listBorrowedBooks(listbrwdbooks).get_return();
		System.out.println("Libros prestados: " + Arrays.toString(borrowedList.getBookNames()));
		System.out.println("ISSNs prestados: " + Arrays.toString(borrowedList.getIssns()));
	}
	private static boolean loginTest(String username, String password, ETSIINFLibraryStub stub)
	throws RemoteException{
		User usr = new User();
		Login login = new Login();
		usr.setName(username);
		usr.setPwd(password);
		login.setArgs0(usr);
		Response loginResp = stub.login(login).get_return();
		return loginResp.getResponse();
	}
	private static AddUserResponse addUserTest(String username, ETSIINFLibraryStub stub) throws RemoteException {
		Username nuevoUsuario = new Username();
		AddUser nuevoUser = new AddUser();
		nuevoUsuario.setUsername(username);
		nuevoUser.setArgs0(nuevoUsuario);
		return stub.addUser(nuevoUser).get_return();
	}
}
