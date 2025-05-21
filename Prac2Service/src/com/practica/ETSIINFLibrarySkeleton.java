/**
 * ETSIINFLibrarySkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
package com.practica;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;

import com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub;
import com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.AddUser;
import com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.AddUserResponse;
import com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.AddUserResponseBackEnd;
import com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.ChangePassword;
import com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordBackEnd;
import com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordResponseE;
import com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.Login;
import com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.LoginBackEnd;
import com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUser;
import com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUserE;
import com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUserResponse;
import com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUserResponseE;
import com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.UserBackEnd;
import com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.Username;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import es.upm.etsiinf.sos.AddBookResponse;
import es.upm.etsiinf.sos.BorrowBookResponse;
import es.upm.etsiinf.sos.ChangePasswordResponse;
import es.upm.etsiinf.sos.DeleteUser;
import es.upm.etsiinf.sos.DeleteUserResponse;
import es.upm.etsiinf.sos.GetBookResponse;
import es.upm.etsiinf.sos.GetBooksFromAuthorResponse;
import es.upm.etsiinf.sos.ListBooksResponse;
import es.upm.etsiinf.sos.ListBorrowedBooksResponse;
import es.upm.etsiinf.sos.LoginResponse;
import es.upm.etsiinf.sos.LogoutResponse;
import es.upm.etsiinf.sos.RemoveBookResponse;
import es.upm.etsiinf.sos.ReturnBookResponse;
import es.upm.etsiinf.sos.model.xsd.*;

/**
 * ETSIINFLibrarySkeleton java skeleton for the axisService
 */
public class ETSIINFLibrarySkeleton {
        // Cliente para UPMAuthorization
        private UPMAuthenticationAuthorizationWSSkeletonStub upmAuth =               new UPMAuthenticationAuthorizationWSSkeletonStub();
        // Mapa que guarda libros usando ISSN como clave. Emplea un mapa auxiliar de disponibilidad
        private static Map<String,Book> libros = new LinkedHashMap<>();
        private static Map<String,Integer> inventario = new ConcurrentHashMap<>();
        // Mapa que guarda préstamos de usuarios usando nombre usuario como clave
        private static Map<String,List<Book>> prestamos = new ConcurrentHashMap<>();
        // Mapa que guarda usuarios locales (solo admin)
        private static Map<String,String> localUsers = new HashMap<>();
        // Inicializa admin y crea cliente stub al arrancar
        static {
                localUsers.put("admin", "admin");
        }
        // MÉTODOS AUXILIARES
        // Clase auxiliar para el id de la sesión actual
        private String sesionActual() throws Exception{
                MessageContext msgCtx = MessageContext.getCurrentMessageContext();
                Object sessionHeader = msgCtx.getHeader("sessionId");
                if(sessionHeader == null){
                        throw new Exception("Sesión inválida");
                }
                return sessionHeader.toString();
        }
        // Método auxiliar para buscar un libro en una lista en base al ISSN
        private Book buscarLibroISSN(String issn, List<Book> libros){
                for(Book l : libros)
                        if(l.getISSN().equals(issn)) return l;
                return null;
        }
        // Método auxiliar para eliminar todos los préstamos de un usuario y devolveros al inventario
        private void eliminarPrestamosUsuario(String usuario){
                for(Entry<String, List<Book>> pr : prestamos.entrySet()){
                        if(pr.getKey().equals(usuario)){
                                // Itera sobre libros prestados y los devuelve
                                for(Book b : pr.getValue()){
                                        inventario.put(b.getISSN(), inventario.get(b.getISSN()) + 1);
                                        pr.getValue().remove(b);
                                }
                        }
                }
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
                        String usuario = SessionManager.getUsername(sesionActual());
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
                        String usuario = SessionManager.getUsername(sesionActual());
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
                LogoutResponse lgeresp = new LogoutResponse();
                Response r = new Response();
                try{
                                        
                        // Elimina el ID de todas las sesiones del usuario
                        String usuario = SessionManager.getUsername(sesionActual());
                        SessionManager.invalidateUserSessions(usuario);
                        r.setResponse(true);
                }
                catch(Exception e){
                        r.setResponse(false);
                }
                lgeresp.set_return(r);
                return lgeresp;
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
                        String username = SessionManager.getUsername(sesionActual());
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
                DeleteUserResponse dluresp = new DeleteUserResponse();
                Response r = new Response();
                try{
                        // Comprueba si es admin
                        String usuario = SessionManager.getUsername(sesionActual());
                        if(SessionManager.getRole(usuario) != "admin"){
                                throw new Exception("No tienes los privilegios suficientes para ejecutar esta operación");
                        }
                        // Elimina los préstamos del usuario antes de eliminarlo
                        eliminarPrestamosUsuario(usuario);
                        prestamos.remove(usuario);
                        // Llama a UPMAuth para eliminar el usuario
                        RemoveUser rmu = new RemoveUser();
                        RemoveUserE rmue = new RemoveUserE();
                        rmu.setName(usuario);
                        rmue.setRemoveUser(rmu);
                        if(!upmAuth.removeUser(rmue).get_return().getResult()){
                                throw new Exception("La operación ha fallado");
                        }
                        // Si realiza bien esta operación todo OK
                        r.setResponse(true);
                }
                catch(Exception e)
                {
                        r.setResponse(false);
                }
                dluresp.set_return(r);
                return dluresp;
        }

        /**
         * Auto generated method signature
         * 
         * @param addUser
         * @return addUserResponse
         */

        public es.upm.etsiinf.sos.AddUserResponse addUser(
                        es.upm.etsiinf.sos.AddUser addUser) {
                es.upm.etsiinf.sos.AddUserResponse adduresp = new es.upm.etsiinf.sos.AddUserResponse();
                es.upm.etsiinf.sos.model.xsd.AddUserResponse addURespObj = new  es.upm.etsiinf.sos.model.xsd.AddUserResponse();
                AddUserResponse extAddU = new AddUserResponse();
                AddUserResponseBackEnd addUserResponseBackEnd = new AddUserResponseBackEnd();
                try{
                        String usuario = SessionManager.getUsername(sesionActual());
                        if(!SessionManager.getRole(usuario).equals("admin")){
                                throw new Exception("No tienes los privilegios suficientes para ejecutar esa operación");
                        }
                        // Obtiene el nombre del usuario
                        es.upm.etsiinf.sos.model.xsd.Username u = addUser.getArgs0();
                        // Llama al servicio externo
                        AddUser addUser2 = new AddUser();
                        UserBackEnd ube = new UserBackEnd();
                        ube.setName(u.getUsername());
                        addUser2.setUser(ube);
                        // Obtiene la respuesta
                        AddUserResponse addUResp = upmAuth.addUser(addUser2);
                        AddUserResponseBackEnd addURespBE = new AddUserResponseBackEnd();
                        addURespObj.setResponse(addURespBE.getResult());
                        addURespObj.setPwd(addURespBE.getPassword());
                }
                catch(Exception e){
                        addURespObj.setResponse(false);
                }
                adduresp.set_return(addURespObj);
                return adduresp;
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
                        sesionActual();
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
                        sesionActual();
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
                ChangePasswordResponse chpr = new ChangePasswordResponse();
                Response r = new Response();
                try{
                        String usuario = SessionManager.getUsername(sesionActual());
                        PasswordPair pp = changePassword.getArgs0();
                        // Si se trata del admin cambia en local
                        if(usuario == "admin") 
                                localUsers.put("admin", pp.getNewpwd());
                        else{
                                                        // Ejecuta la operación externa
                        ChangePassword extChpw = new ChangePassword();
                        ChangePasswordBackEnd extChpwBE = new ChangePasswordBackEnd();
                        extChpwBE.setName(usuario);
                        extChpwBE.setOldpwd(pp.getOldpwd());
                        extChpwBE.setNewpwd(pp.getNewpwd());
                        extChpw.setChangePassword(extChpwBE);
                        ChangePasswordResponseE chpwE = upmAuth.changePassword(extChpw);
                        // comprueba si la operación ha sido exitosa
                        r.setResponse(chpwE.get_return().getResult());
                        }
                }
                catch(Exception e){
                        r.setResponse(false);
                }
                chpr.set_return(r);
                return chpr;
        }

        /**
         * Auto generated method signature
         * 
         * @param login
         * @return loginResponse
         */

        public es.upm.etsiinf.sos.LoginResponse login(
                        es.upm.etsiinf.sos.Login login) {
                LoginResponse lresp = new LoginResponse();
                Response res = new Response();
                try{
                        User user = login.getArgs0();
                        // Si es admin autentica en local
                        if(user.getName() == "admin" && !user.getPwd().equals(localUsers.get("admin"))){
                                throw new Exception("Contraseña incorrecta");
                        }
                        // Si no es admin autentica usando el servicio externo
                        else{
                                // Prepara el servicio externo
                                Login extLogin = new Login();
                                LoginBackEnd loginBE = new LoginBackEnd();
                                loginBE.setName(user.getName());
                                loginBE.setPassword(user.getPwd());
                                extLogin.setLogin(loginBE);
                                // Determina resultado de operación
                                if(!upmAuth.login(extLogin).get_return().getResult())
                                        throw new Exception("Operación externa fallida");
                        }
                        // Crea una nueva sesión
                        String sessionId = SessionManager.createSession(user.getName());
                        // Añade el ID de la sesión a MessageContext de Axis2 para guardar estado
                        MessageContext msgCtx = MessageContext.getCurrentMessageContext();
                        OMFactory fac = OMAbstractFactory.getOMFactory();
                        OMElement sessionHeader = fac.createOMElement("sessionId", null);
                        sessionHeader.setText(sessionId); 
                        msgCtx.addHeader(sessionHeader); 
                        res.setResponse(true);      
                }
                catch(Exception e){
                        res.setResponse(false);
                }
                // Retorna la respuesta
                lresp.set_return(res);
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
                        if(SessionManager.getRole(SessionManager.getUsername(sesionActual())) != "admin"){
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
                        sesionActual();
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
                        String usuario = SessionManager.getUsername(sesionActual());
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
