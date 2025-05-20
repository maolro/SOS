/**
 * ETSIINFLibrarySkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
package com.practica;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import es.upm.etsiinf.sos.AddBookResponse;
import es.upm.etsiinf.sos.BorrowBookResponse;
import es.upm.etsiinf.sos.GetBookResponse;
import es.upm.etsiinf.sos.GetBooksFromAuthorResponse;
import es.upm.etsiinf.sos.ListBooksResponse;
import es.upm.etsiinf.sos.ListBorrowedBooksResponse;
import es.upm.etsiinf.sos.LoginResponse;
import es.upm.etsiinf.sos.LogoutResponse;
import es.upm.etsiinf.sos.RemoveBookResponse;
import es.upm.etsiinf.sos.ReturnBookResponse;
import es.upm.etsiinf.sos.model.xsd.Author;
import es.upm.etsiinf.sos.model.xsd.Book;
import es.upm.etsiinf.sos.model.xsd.BookList;
import es.upm.etsiinf.sos.model.xsd.Response;
import es.upm.etsiinf.sos.model.xsd.User;

/**
 * ETSIINFLibrarySkeleton java skeleton for the axisService
 */
public class ETSIINFLibrarySkeleton {
        // Mapa que guarda libros usando ISSN como clave. Emplea un mapa auxiliar de disponibilidad
        private static Map<String,Book> libros = new LinkedHashMap<>();
        private static Map<String,Integer> inventario = new ConcurrentHashMap<>();
        // Mapa que guarda préstamos de usuarios usando nombre usuario como clave
        private static Map<String,List<Book>> prestamos = new ConcurrentHashMap<>();
        // Mapa que guarda usuarios locales (solo admin)
        private static Map<String,String> localUsers = new HashMap<>();
        // Inicializa admin al arrancar
        static {
                localUsers.put("admin", "admin");
        }
        // MÉTODOS AUXILIARES

        // Clase auxiliar para obtener usuario actual
        private String validarSesion() throws Exception{
                MessageContext msgCtx = MessageContext.getCurrentMessageContext();
                Object sessionHeader = msgCtx.getHeader("sessionId");
                if(sessionHeader == null){
                        throw new Exception("Sesión inválida");
                }
                String sessionId = sessionHeader.toString();
                if(SessionManager.getUsername(sessionId) == null){
                        throw new Exception("Sesión caducada");
                }
                return SessionManager.getUsername(sessionId);
        }
        // Método auxiliar para buscar un libro en una lista en base al ISSN
        private Book buscarLibroISSN(String issn, List<Book> libros){
                for(Book l : libros)
                        if(l.getISSN().equals(issn)) return l;
                return null;
        }

        // MÉTODOS PRINCIPALES

        /**
         * Auto generated method signature
         * 
         * @param borrowBook
         * @return borrowBookResponse
         */

        public es.upm.etsiinf.sos.BorrowBookResponse borrowBook(
                        es.upm.etsiinf.sos.BorrowBook borrowBook) {
                BorrowBookResponse bresp = new BorrowBookResponse();
                Response r = new Response();
                try{
                        String usuario = validarSesion();
                        String issn = borrowBook.getArgs0();
                        if(libros.containsKey(issn) && inventario.get(issn) > 0){
                                inventario.put(issn, inventario.get(issn) - 1);
                                List<Book> listaLibros;
                                if(prestamos.containsKey(usuario)){
                                        listaLibros = prestamos.get(usuario);
                                }
                                else{
                                        listaLibros = new ArrayList<Book>();
                                }
                                listaLibros.add(libros.get(issn));
                                prestamos.put(usuario, listaLibros);
                                r.setResponse(true);
                        }
                        else{
                                throw new Exception("El libro escogido no existe o no está disponible");
                        }
                }
                catch(Exception e){
                        r.setResponse(false);
                }
                bresp.set_return(r);
                return bresp; 
        }

        /**
         * Auto generated method signature
         * 
         * @param returnBook
         * @return returnBookResponse
         */

        public es.upm.etsiinf.sos.ReturnBookResponse returnBook(
                        es.upm.etsiinf.sos.ReturnBook returnBook) {
                ReturnBookResponse rbresp = new ReturnBookResponse();
                Response r = new Response();
                try{
                        String usuario = validarSesion();
                        String issn = returnBook.getArgs0();
                        // Comprueba si el ISSN pertenece a un libro en la biblioteca
                        if(!libros.containsKey(issn)) 
                                throw new Exception("El ISSN indicado no se corresponde a ningún libro existente");
                        if(!prestamos.containsKey(usuario))
                                throw new Exception("El usuario indicado no tiene préstamos activos");
                        // Busca para comprobar si se ha prestado el libro
                        List<Book> listaPrestamos = prestamos.get(usuario);
                        Book b = buscarLibroISSN(issn, listaPrestamos);
                        if(b != null){
                                // Elimina el préstamo e inserta la nueva lista de préstamos
                                listaPrestamos.remove(b);
                                prestamos.put(usuario, listaPrestamos);
                                // Actualiza el inventario
                                inventario.put(issn, inventario.get(issn) + 1);
                                // Indica que la operación ha sido exitosa
                                r.setResponse(true);
                        }
                        else{
                                throw new Exception("El usuario indicado no ha cogido prestado ese libro");
                        }
                }
                catch(Exception e){
                        r.setResponse(false);
                }
                rbresp.set_return(r);
                return rbresp;
        }

        /**
         * Auto generated method signature
         * 
         * @param logout
         * @return logoutResponse
         */

        public es.upm.etsiinf.sos.LogoutResponse logout(
                        es.upm.etsiinf.sos.Logout logout) {
                // Añade el ID de la sesión a MessageContext de Axis2 para guardar estado
                MessageContext msgCtx = MessageContext.getCurrentMessageContext();
                OMFactory fac = OMAbstractFactory.getOMFactory();
                OMElement sessionHeader = fac.createOMElement("sessionId", null);
                sessionHeader.set(null); 
                msgCtx.addHeader(sessionHeader);

                return new LogoutResponse();
        }

        /**
         * Auto generated method signature
         * 
         * @param removeBook
         * @return removeBookResponse
         */

        public es.upm.etsiinf.sos.RemoveBookResponse removeBook(
                        es.upm.etsiinf.sos.RemoveBook removeBook) {
                RemoveBookResponse rbresp = new RemoveBookResponse();
                Response resp = new Response();
                try{
                        // Obtiene el usuario actual
                        String username = validarSesion();
                        // Determina si el usuario tiene los privilegios adecuados
                        if(username != "admin"){
                                throw new Exception("El usuario no tiene los permisos suficientes");
                        }
                        // Obtiene el ISSN del libro a eliminar
                        String issn = removeBook.getArgs0();
                        // Comprueba si quedan copias del libro en el inventario
                        if(inventario.containsKey(issn) && inventario.get(issn)>0){
                                inventario.put(issn, inventario.get(issn) - 1);
                                resp.setResponse(true); 
                        }
                        else{
                                throw new Exception("No existen copias del libro indicado");
                        }
                }
                catch(Exception e){
                        resp.setResponse(false);
                }
                rbresp.set_return(resp);
                return rbresp;
        }

        /**
         * Auto generated method signature
         * 
         * @param deleteUser
         * @return deleteUserResponse
         */

        public es.upm.etsiinf.sos.DeleteUserResponse deleteUser(
                        es.upm.etsiinf.sos.DeleteUser deleteUser) {
                // TODO : fill this with the necessary business logic
                throw new java.lang.UnsupportedOperationException(
                                "Please implement " + this.getClass().getName() + "#deleteUser");
        }

        /**
         * Auto generated method signature
         * 
         * @param addUser
         * @return addUserResponse
         */

        public es.upm.etsiinf.sos.AddUserResponse addUser(
                        es.upm.etsiinf.sos.AddUser addUser) {
                
                // TODO : fill this with the necessary business logic
                throw new java.lang.UnsupportedOperationException(
                                "Please implement " + this.getClass().getName() + "#addUser");
        }

        /**
         * Auto generated method signature
         * 
         * @param getBook
         * @return getBookResponse
         */

        public es.upm.etsiinf.sos.GetBookResponse getBook(
                        es.upm.etsiinf.sos.GetBook getBook) {
                GetBookResponse gbresp = new GetBookResponse();
                try{
                        validarSesion();
                        String issn = getBook.getArgs0();
                        if(libros.containsKey(issn)){
                                gbresp.set_return(libros.get(issn));
                        }
                        else{
                                throw new Exception("El libro indicado no existe");
                        }
                }
                catch(Exception e){
                        gbresp.set_return(null);
                }
                return gbresp;
        }

        /**
         * Auto generated method signature
         * 
         * @param listBooks
         * @return listBooksResponse
         */

        public es.upm.etsiinf.sos.ListBooksResponse listBooks(
                        es.upm.etsiinf.sos.ListBooks listBooks) {
                BookList bl = new BookList();
                ListBooksResponse lbr = new ListBooksResponse();
                try{
                        validarSesion();
                        for(Map.Entry<String, Book> l : libros.entrySet()){
                                bl.addBookNames(l.getValue().getName());
                                bl.addIssns(l.getKey());
                        }
                        bl.setResult(true);
                }
                catch(Exception e){
                        bl.setResult(false);
                }
                lbr.set_return(bl);
                return lbr;
        }

        /**
         * Auto generated method signature
         * 
         * @param changePassword
         * @return changePasswordResponse
         */

        public es.upm.etsiinf.sos.ChangePasswordResponse changePassword(
                        es.upm.etsiinf.sos.ChangePassword changePassword) {
                // TODO : fill this with the necessary business logic
                throw new java.lang.UnsupportedOperationException(
                                "Please implement " + this.getClass().getName() + "#changePassword");
        }

        /**
         * Auto generated method signature
         * 
         * @param login
         * @return loginResponse
         */

        public es.upm.etsiinf.sos.LoginResponse login(
                        es.upm.etsiinf.sos.Login login) {
                User user = login.getArgs0();
                LoginResponse lresp = new LoginResponse();
                Response res = new Response();
                if(user.getName() == "admin"){ 
                        res.setResponse(user.getPwd().equals(localUsers.get("admin")));
                        lresp.set_return(res);
                        SessionManager.createSession("admin");
                }
                String sessionId = SessionManager.createSession(user.getName());
                // Añade el ID de la sesión a MessageContext de Axis2 para guardar estado
                MessageContext msgCtx = MessageContext.getCurrentMessageContext();
                OMFactory fac = OMAbstractFactory.getOMFactory();
                OMElement sessionHeader = fac.createOMElement("sessionId", null);
                sessionHeader.setText(sessionId); 
                msgCtx.addHeader(sessionHeader);
                // Retorna la respuesta
                return lresp;
        }

        /**
         * Auto generated method signature
         * 
         * @param addBook
         * @return addBookResponse
         */

        public es.upm.etsiinf.sos.AddBookResponse addBook(
                        es.upm.etsiinf.sos.AddBook addBook) {
                AddBookResponse abr = new AddBookResponse();
                Response r = new Response();
                try{
                        // Comprueba si el usuario es admin
                        if(SessionManager.getRole(validarSesion()) != "admin"){
                                throw new Exception("No tienes los privilegios suficientes para ejecutar esta operación");
                        }
                        Book book = addBook.getArgs0();
                        String issn = book.getISSN();
                        // Comprueba si el libro existe ya o no
                        if(libros.containsKey(issn) && inventario.containsKey(issn)){
                                // Si el libro ya existe añade copia al inventario
                                inventario.put(issn, inventario.get(issn) + 1);
                        }
                        else{
                                // Si el libro no está crea nuevas entradas
                                libros.put(issn, book);
                                inventario.put(issn, 1);
                        }
                        r.setResponse(true);
                }
                catch(Exception e){
                        r.setResponse(false);
                }
                abr.set_return(r);
                return abr;
        }

        /**
         * Auto generated method signature
         * 
         * @param getBooksFromAuthor
         * @return getBooksFromAuthorResponse
         */

        public es.upm.etsiinf.sos.GetBooksFromAuthorResponse getBooksFromAuthor(
                        es.upm.etsiinf.sos.GetBooksFromAuthor getBooksFromAuthor) {
                GetBooksFromAuthorResponse gbfaresp = new GetBooksFromAuthorResponse();
                BookList bl = new BookList();
                try{
                        validarSesion();
                        Author author = getBooksFromAuthor.getArgs0();
                        for(Map.Entry<String, Book> entry : libros.entrySet()){
                                Book b = entry.getValue();
                                if(Arrays.asList(b.getAuthors()).contains(author.getName())){
                                        bl.addBookNames(b.getName());
                                        bl.addBookNames(b.getISSN());
                                }
                        }
                        bl.setResult(true);
                }
                catch(Exception e){
                        bl.setResult(false);
                }
                gbfaresp.set_return(bl);
                return gbfaresp;
        }

        /**
         * Auto generated method signature
         * 
         * @param listBorrowedBooks
         * @return listBorrowedBooksResponse
         */

        public es.upm.etsiinf.sos.ListBorrowedBooksResponse listBorrowedBooks(
                        es.upm.etsiinf.sos.ListBorrowedBooks listBorrowedBooks) {
                ListBorrowedBooksResponse lbbresp = new ListBorrowedBooksResponse();
                BookList bl = new BookList();
                try{
                        String usuario = validarSesion();
                        if(!prestamos.containsKey(usuario))
                                throw new Exception("El usuario indicado no tiene préstamos");
                        // Obtiene la lista de préstamos e itera sobre ella
                        for(Book b : prestamos.get(usuario)){
                                bl.addBookNames(b.getName());
                                bl.addIssns(b.getISSN());
                        }
                        bl.setResult(true);

                }
                catch(Exception e){
                        bl.setResult(false);
                }      
                lbbresp.set_return(bl);
                return lbbresp;
        }

}
