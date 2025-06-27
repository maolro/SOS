package com.client;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate.Param;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.client.wrappers.*;

public class ClienteLibreria {

    private static final String BASE_URL = "http://localhost:8080/api/v1/"; 
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private RestTemplate restTemplate;

    public ClienteLibreria() {
        restTemplate = new RestTemplate();
    }

    // Función para imprimir la respuesta con mensajes en español
    private void imprimirRespuesta(HttpStatusCode status, String msg) {
        System.out.println("\n=== RESULTADO DE LA OPERACIÓN ===");
        System.out.println("Estado HTTP: " + status);
        System.out.println("Mensaje: " + msg);
        System.out.println("===============================\n");
    }

    // Crear usuario (POST /usuarios)
    public Usuario crearUsuario(String nombreUsuario, String matricula, String fechaNacimiento, String correoElectronico) {
        String url = BASE_URL + "usuarios";
        Usuario usuario = new Usuario(nombreUsuario, matricula, fechaNacimiento, correoElectronico);

        try {
            ResponseEntity<Usuario> resp = restTemplate.postForEntity(url, usuario, Usuario.class);
            imprimirRespuesta(resp.getStatusCode(), "Usuario creado exitosamente: " + resp.getBody().toString());
            return resp.getBody();
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al crear usuario: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Obtener usuario por ID (GET /usuarios/{id})
    public Usuario obtenerUsuario(Long id) {
        String url = BASE_URL + "usuarios/" + id;
        
        try {
            ResponseEntity<Usuario> resp = restTemplate.getForEntity(url, Usuario.class);
            imprimirRespuesta(resp.getStatusCode(), "Usuario obtenido exitosamente: " + resp.getBody().toString());
            return resp.getBody();
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al obtener usuario: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Actualizar usuario (PUT /usuarios/{id})
    public Usuario actualizarUsuario(Long id, String nombreUsuario, String matricula, String fechaNacimiento, String correoElectronico) {
        String url = BASE_URL + "usuarios/" + id;
        Usuario usuario = new Usuario(nombreUsuario, matricula, fechaNacimiento, correoElectronico);
        usuario.setId(id);

        try {
            restTemplate.put(url, usuario);
            imprimirRespuesta(HttpStatus.OK, "Usuario actualizado exitosamente");
            return obtenerUsuario(id); // Volver a obtener para ver cambios
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al actualizar usuario: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Eliminar usuario (DELETE /usuarios/{id})
    public void eliminarUsuario(Long id) {
        String url = BASE_URL + "usuarios/" + id;
        
        try {
            restTemplate.delete(url);
            imprimirRespuesta(HttpStatus.NO_CONTENT, "Usuario eliminado exitosamente");
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al eliminar usuario: " + e.getResponseBodyAsString());
        }
    }

    // Crear Libro (POST /libros)
    public Libro crearLibro(String titulo, String autor, String edicion, 
    String isbn, String editorial, boolean disponible) {
        String url = BASE_URL + "libros";
        Libro libro = new Libro(titulo, autor, edicion, isbn, editorial, disponible);

        try {
            ResponseEntity<Libro> resp = restTemplate.postForEntity(url, libro, Libro.class);
            imprimirRespuesta(resp.getStatusCode(), "Libro creado exitosamente: " + resp.getBody().toString());
            return resp.getBody();
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al crear libro: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Obtener libro por ID (GET /libros/{id})
    public Libro obtenerLibro(Long id) {
        String url = BASE_URL + "libros/" + id;
        
        try {
            ResponseEntity<Libro> resp = restTemplate.getForEntity(url, Libro.class);
            imprimirRespuesta(resp.getStatusCode(), "Libro obtenido exitosamente: " + resp.getBody().toString());
            return resp.getBody();
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al obtener libro: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Actualizar libro (PUT /libros/{id})
    public Libro actualizarLibro(Long id, String titulo, String autor, String edicion, 
    String isbn, String editorial, boolean disponible) {
        String url = BASE_URL + "libros/" + id;
        Libro libro = new Libro(titulo, autor, edicion, isbn, editorial, disponible);
        libro.setId(id);

        try {
            restTemplate.put(url, libro);
            imprimirRespuesta(HttpStatus.OK, "Libro actualizado exitosamente");
            return obtenerLibro(id); // Volver a obtener para ver cambios
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al actualizar libro: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Eliminar libro (DELETE /libros/{id})
    public void eliminarLibro(Long id) {
        String url = BASE_URL + "libros/" + id;
        
        try {
            restTemplate.delete(url);
            imprimirRespuesta(HttpStatus.NO_CONTENT, "Libro eliminado exitosamente");
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al eliminar libro: " + e.getResponseBodyAsString());
        }
    }

    // Crear Prestamo (POST /prestamos)
    public Prestamo crearPrestamo(Long usuario_id, Long libro_id) {
        String url = BASE_URL + "prestamos";
        String fechaPrestamo = LocalDate.now().format(DATE_FORMATTER);
        CrearPrestamoDTO prestamoDTO = new CrearPrestamoDTO(usuario_id, libro_id, fechaPrestamo);

        try {
            ResponseEntity<Prestamo> resp = restTemplate.postForEntity(url, prestamoDTO, Prestamo.class);
            imprimirRespuesta(resp.getStatusCode(), "Préstamo creado exitosamente: " + resp.getBody().toString());
            return resp.getBody();
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al crear préstamo: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Obtener préstamo por ID (GET /prestamos/{id})
    public Prestamo obtenerPrestamo(Long id) {
        String url = BASE_URL + "prestamos/" + id;
        
        try {
            ResponseEntity<Prestamo> resp = restTemplate.getForEntity(url, Prestamo.class);
            imprimirRespuesta(resp.getStatusCode(), "Préstamo obtenido exitosamente: " + resp.getBody().toString());
            return resp.getBody();
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al obtener préstamo: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Listar todos los usuarios con paginación
    public List<Usuario> listarUsuarios(String startsWith, int page, int size) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "usuarios")
            .queryParam("starts_with", startsWith)
            .queryParam("page", page)
            .queryParam("size", size)
            .toUriString();
        try {
            ResponseEntity<UsuarioPagedResponse> resp = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<UsuarioPagedResponse>() {}
            );

            UsuarioPagedResponse usuarios = resp.getBody();
            if (usuarios != null && usuarios.get_embedded() != null) {
                imprimirRespuesta(resp.getStatusCode(), "Lista de usuarios obtenida exitosamente: " 
                + usuarios.get_embedded().getUsuarioList().toString());
                return usuarios.get_embedded().getUsuarioList();
            } 
            else {
                imprimirRespuesta(resp.getStatusCode(), "No se encontraron usuarios");
                return Collections.emptyList();
            }
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            imprimirRespuesta(ex.getStatusCode(), "Error al listar usuarios: " + ex.getResponseBodyAsString());
            return Collections.emptyList();
        }
    }

    // Listar todos los libros con filtros
    public List<Libro> listarLibros(int page, int size, String titulo, Boolean disponible) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(BASE_URL + "libros")
            .queryParam("page", page)
            .queryParam("size", size);
        if (titulo != null)     uri.queryParam("titulo", titulo);
        if (disponible != null) uri.queryParam("disponible", disponible);

        String url = uri.toUriString();
        try{
            ResponseEntity<LibroPagedResponse> resp = restTemplate.exchange(url, 
            HttpMethod.GET,null,
            new ParameterizedTypeReference<LibroPagedResponse>() {});       
            LibroPagedResponse libros = resp.getBody();
            if (libros != null && libros.get_embedded() != null) {
                imprimirRespuesta(resp.getStatusCode(), "Lista de libros obtenida exitosamente: " 
                + libros.get_embedded().getLibroList().toString());
                return libros.get_embedded().getLibroList();
            } 
            else {
                imprimirRespuesta(resp.getStatusCode(), "No se encontraron libros");
                return Collections.emptyList();
            }
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            imprimirRespuesta(ex.getStatusCode(), "Error al listar libros: " + ex.getResponseBodyAsString());
            return Collections.emptyList();
        }
    }

    // Listar préstamos de un usuario
    public List<Prestamo> listarPrestamosUsuario(Long usuarioId) {
        String url = BASE_URL + "prestamos?usuarioId=" + usuarioId;
        try{
            ResponseEntity<PrestamoPagedResponse> resp = restTemplate.exchange(url, 
            HttpMethod.GET,null,
            new ParameterizedTypeReference<PrestamoPagedResponse>() {});       
            PrestamoPagedResponse prestamos = resp.getBody();
            if (prestamos != null && prestamos.get_embedded() != null) {
                imprimirRespuesta(resp.getStatusCode(), "Préstamos del usuario obtenidos exitosamente: " 
                + prestamos.get_embedded().getPrestamoList().toString());
                return prestamos.get_embedded().getPrestamoList();
            } 
            else {
                imprimirRespuesta(resp.getStatusCode(), "El usuario no tiene préstamos");
                return Collections.emptyList();
            }
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            imprimirRespuesta(ex.getStatusCode(), "Error al listar préstamos del usuario: " + ex.getResponseBodyAsString());
            return Collections.emptyList();
        }
    }

    // Listar histórico de préstamos de un usuario
    public List<Prestamo> listarHistoricoPrestamosUsuario(Long usuarioId) {
        String url = BASE_URL + "prestamos/historico?usuarioId=" + usuarioId;
        try{
            ResponseEntity<PrestamoPagedResponse> resp = restTemplate.exchange(url, 
            HttpMethod.GET,null,
            new ParameterizedTypeReference<PrestamoPagedResponse>() {});       
            PrestamoPagedResponse prestamos = resp.getBody();
            if (prestamos != null && prestamos.get_embedded() != null) {
                imprimirRespuesta(resp.getStatusCode(), "Histórico de préstamos del usuario obtenido exitosamente: " 
                + prestamos.get_embedded().getPrestamoList().toString());
                return prestamos.get_embedded().getPrestamoList();
            } 
            else {
                imprimirRespuesta(resp.getStatusCode(), "El usuario no tiene histórico de préstamos");
                return Collections.emptyList();
            }
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            imprimirRespuesta(ex.getStatusCode(), "Error al listar histórico de préstamos: " + ex.getResponseBodyAsString());
            return Collections.emptyList();
        }
    }

    // Obtener actividad completa de un usuario
    public void obtenerActividadUsuario(Long usuarioId) {
        String url = BASE_URL + "usuarios/" + usuarioId + "/actividad";
        
        try {
            ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
            imprimirRespuesta(resp.getStatusCode(), "Actividad del usuario obtenida exitosamente: " + resp.getBody());
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al obtener actividad del usuario: " + e.getResponseBodyAsString());
        }
    }

    // Ampliar préstamo
    public void ampliarPrestamo(Long prestamoId, int dias) {
        String url = BASE_URL + "prestamos/" + prestamoId + "/ampliar?dias=" + dias;
        try {
            ResponseEntity<Void> resp = restTemplate.postForEntity(url, null, Void.class);
            imprimirRespuesta(resp.getStatusCode(), "Préstamo ampliado exitosamente por " + dias + " días");
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al ampliar préstamo: " + e.getResponseBodyAsString());
        }
    }

    // Devolver préstamo
    public Prestamo devolverPrestamo(Long prestamoId) {
        String url = BASE_URL + "prestamos/" + prestamoId + "/devolucion";
        try {
            ResponseEntity<Prestamo> resp = restTemplate.postForEntity(url, null, Prestamo.class);
            imprimirRespuesta(resp.getStatusCode(), "Préstamo devuelto exitosamente: " + resp.getBody().toString());
            return resp.getBody();
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al devolver préstamo: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Probar devolución tardía
    public void probarDevolucionTardia(Long usuarioId, Long libroId) {
        // Crear un préstamo con fecha antigua (simulando retraso)
        String fechaAntigua = LocalDate.now().minusWeeks(3).format(DATE_FORMATTER);
        String url = BASE_URL + "prestamos";
        CrearPrestamoDTO prestamoDTO = new CrearPrestamoDTO(usuarioId, libroId, fechaAntigua);

        try {
            // Crear préstamo atrasado (esto requeriría que el backend permita fechas pasadas para pruebas)
            ResponseEntity<Prestamo> respCrear = restTemplate.postForEntity(url, prestamoDTO, Prestamo.class);
            Prestamo prestamo = respCrear.getBody();
            
            if (prestamo != null) {
                // Intentar devolver
                Prestamo prestamoDevuelto = devolverPrestamo(prestamo.getId());
                
                if (prestamoDevuelto != null && prestamoDevuelto.getSancion() != null) {
                    imprimirRespuesta(HttpStatus.OK, "SANCION APLICADA CORRECTAMENTE: " + prestamoDevuelto.getSancion());
                    
                    // Intentar crear nuevo préstamo (debería fallar por sanción)
                    try {
                        crearPrestamo(usuarioId, libroId);
                        imprimirRespuesta(HttpStatus.BAD_REQUEST, "ERROR: Se permitió préstamo durante sanción");
                    } catch (Exception e) {
                        imprimirRespuesta(HttpStatus.FORBIDDEN, "CORRECTO: No se permitió préstamo durante sanción");
                    }
                }
            }
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error en prueba de devolución tardía: " + e.getResponseBodyAsString());
        }
    }

    public static void main(String[] args) {
        ClienteLibreria client = new ClienteLibreria();

        System.out.println("\n===== PRUEBAS DEL SERVICIO DE BIBLIOTECA =====");

        // ===== PRUEBAS DE USUARIOS =====
        System.out.println("\n--- Pruebas de Usuarios ---");
        
        // Crear usuarios
        System.out.println("\nCreando usuarios...");
        Usuario usuario1 = client.crearUsuario("Ana Pérez", "A123456", 
                "2000-04-20", "ana.perez@ejemplo.com");
        Usuario usuario2 = client.crearUsuario("Juan López", "B789012", 
                "1999-07-03", "juan.lopez@ejemplo.com");
        
        // Obtener usuario
        System.out.println("\nObteniendo usuario 1...");
        Usuario usuarioObtenido = client.obtenerUsuario(usuario1 != null ? usuario1.getId() : 1L);
        
        // Actualizar usuario
        System.out.println("\nActualizando usuario 1...");
        if (usuarioObtenido != null) {
            client.actualizarUsuario(usuarioObtenido.getId(), "Ana Pérez Martínez", 
                    usuarioObtenido.getMatricula(), usuarioObtenido.getFechaNacimiento(), 
                    "ana.martinez@ejemplo.com");
        }
        
        // Listar usuarios
        System.out.println("\nListando todos los usuarios...");
        client.listarUsuarios("", 0, 10);
        
        // Listar usuarios que empiezan por "J"
        System.out.println("\nListando usuarios que empiezan por 'J'...");
        client.listarUsuarios("J", 0, 10);

        // ===== PRUEBAS DE LIBROS =====
        System.out.println("\n--- Pruebas de Libros ---");
        
        // Crear libros
        System.out.println("\nCreando libros...");
        Libro libro1 = client.crearLibro("Programación en Java", "Carlos López", "Tercera", 
                "9780307474723", "Pearson", true);
        Libro libro2 = client.crearLibro("Cien Años de Soledad", "Gabriel García Márquez", 
                "Primera", "123483249545", "Editorial Sudamericana", true);
        Libro libro3 = client.crearLibro("Diseño de APIs RESTful", "Laura Fernández", "Segunda",
                "9780565432109", "Editorial Tecnológica", true);
        
        // Obtener libro
        System.out.println("\nObteniendo libro 1...");
        Libro libroObtenido = client.obtenerLibro(libro1 != null ? libro1.getId() : 1L);
        
        // Actualizar libro
        System.out.println("\nActualizando libro 1...");
        if (libroObtenido != null) {
            client.actualizarLibro(libroObtenido.getId(), "Programación en Java - Edición Especial", 
                    libroObtenido.getAutor(), "Cuarta", libroObtenido.getIsbn(), 
                    libroObtenido.getEditorial(), false);
        }
        
        // Listar todos los libros
        System.out.println("\nListando todos los libros...");
        client.listarLibros(0, 10, null, null);
        
        // Listar libros disponibles
        System.out.println("\nListando libros disponibles...");
        client.listarLibros(0, 10, null, true);
        
        // Buscar libros por título
        System.out.println("\nBuscando libros con 'Java' en el título...");
        client.listarLibros(0, 10, "Java", null);

        // ===== PRUEBAS DE PRÉSTAMOS =====
        System.out.println("\n--- Pruebas de Préstamos ---");
        
        // Crear préstamos
        System.out.println("\nCreando préstamos...");
        Prestamo prestamo1 = client.crearPrestamo(
                usuario1 != null ? usuario1.getId() : 1L, 
                libro1 != null ? libro1.getId() : 1L);
        
        Prestamo prestamo2 = client.crearPrestamo(
                usuario2 != null ? usuario2.getId() : 2L, 
                libro2 != null ? libro2.getId() : 2L);
        
        // Obtener préstamo
        System.out.println("\nObteniendo préstamo 1...");
        Prestamo prestamoObtenido = client.obtenerPrestamo(prestamo1 != null ? prestamo1.getId() : 1L);
        
        // Listar préstamos del usuario 1
        System.out.println("\nListando préstamos del usuario 1...");
        client.listarPrestamosUsuario(usuario1 != null ? usuario1.getId() : 1L);
        
        // Ampliar préstamo
        System.out.println("\nAmpliando préstamo 1 por 7 días...");
        if (prestamoObtenido != null) {
            client.ampliarPrestamo(prestamoObtenido.getId(), 7);
        }
        
        // Devolver préstamo
        System.out.println("\nDevolviendo préstamo 1...");
        if (prestamoObtenido != null) {
            client.devolverPrestamo(prestamoObtenido.getId());
        }
        
        // Listar histórico de préstamos
        System.out.println("\nListando histórico de préstamos del usuario 1...");
        client.listarHistoricoPrestamosUsuario(usuario1 != null ? usuario1.getId() : 1L);

        // ===== PRUEBAS ESPECIALES =====
        System.out.println("\n--- Pruebas Especiales ---");
        
        // Probar devolución tardía
        System.out.println("\nProbando devolución tardía...");
        client.probarDevolucionTardia(
                usuario1 != null ? usuario1.getId() : 1L, 
                libro3 != null ? libro3.getId() : 3L);
        
        // Obtener actividad completa del usuario
        System.out.println("\nObteniendo actividad completa del usuario 1...");
        client.obtenerActividadUsuario(usuario1 != null ? usuario1.getId() : 1L);

        // ===== LIMPIEZA (opcional) =====
        System.out.println("\nEliminando recursos de prueba...");
        if (prestamo2 != null) client.devolverPrestamo(prestamo2.getId());
        if (libro1 != null) client.eliminarLibro(libro1.getId());
        if (libro2 != null) client.eliminarLibro(libro2.getId());
        if (libro3 != null) client.eliminarLibro(libro3.getId());
        if (usuario1 != null) client.eliminarUsuario(usuario1.getId());
        if (usuario2 != null) client.eliminarUsuario(usuario2.getId());

        System.out.println("\n===== PRUEBAS COMPLETADAS =====");
    }
}