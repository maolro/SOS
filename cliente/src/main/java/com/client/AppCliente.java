package com.client;
import com.client.model.*;

public class AppCliente {
    public static void main(String[] args) {
        BibliotecaService client = new BibliotecaService();

        System.out.println("\n===== PRUEBAS DEL SERVICIO DE BIBLIOTECA =====");

        // ===== PRUEBAS DE USUARIOS =====
        System.out.println("\n--- PRUEBAS DE USUARIOS ---");
        
        // PRUEBAS DE CREACIÓN
        System.out.println("\n1. Crear usuario válido");
        UsuarioResp usuario1 = client.crearUsuario("Ana Pérez", "A123456", 
                "2000-04-20", "ana.perez@ejemplo.com");
        
        System.out.println("\n2. Crear usuario con matrícula repetida");
        client.crearUsuario("Ana Pérez", "A123456", "2000-04-20", "ana2@ejemplo.com");
        
        System.out.println("\n3. Crear usuario con fecha inválida");
        client.crearUsuario("Juan López", "B789012", "1999/07/03", "juan@ejemplo.com");
        
        System.out.println("\n4. Crear usuario con correo inválido");
        client.crearUsuario("Juan López", "B789012", "1999-07-03", "juan.ejemplo.com");
        
        System.out.println("\n5. Crear usuario con correo repetido");
        client.crearUsuario("Juan López", "B789012", "1999-07-03", "ana.perez@ejemplo.com");
        
        // Crear más usuarios para pruebas
        UsuarioResp usuario2 = client.crearUsuario("Juan López", "B789012", 
                "1999-07-03", "juan.lopez@ejemplo.com");
        UsuarioResp usuario3 = client.crearUsuario("Carlos Ruiz", "C456789", 
                "1998-05-15", "carlos.ruiz@ejemplo.com");
        UsuarioResp usuario4 = client.crearUsuario("María García", "D012345", 
                "2001-02-28", "maria.garcia@ejemplo.com");
        UsuarioResp usuario5 = client.crearUsuario("Luisa Martínez", "E678901", 
                "2002-11-10", "luisa.martinez@ejemplo.com");
        
        // PRUEBAS DE GET
        System.out.println("\n6. Obtener usuario existente");
        client.obtenerUsuario(usuario1 != null ? usuario1.getId() : 1L);
        
        System.out.println("\n7. Obtener usuario inexistente");
        client.obtenerUsuario(999L);
        
        // PRUEBAS DE LISTADO
        System.out.println("\n8. Listar todos los usuarios");
        client.listarUsuarios("", 0, 10);
        
        System.out.println("\n9. Listar usuarios que empiezan por 'J'");
        client.listarUsuarios("J", 0, 10);
        
        System.out.println("\n10. Listar usuarios con paginación específica");
        client.listarUsuarios("", 1, 2);
        
        System.out.println("\n11. Listar usuarios con filtro y paginación");
        client.listarUsuarios("M", 0, 10);
        
        System.out.println("\n12. Listar usuarios con filtro inválido");
        client.listarUsuarios("123", 0, 10);
        
        // PRUEBAS DE ACTUALIZACIÓN
        System.out.println("\n13. Actualizar usuario válido");
        if (usuario1 != null) {
            client.actualizarUsuario(usuario1.getId(), "Ana Pérez Martínez", 
                    usuario1.getMatricula(), usuario1.getFechaNacimiento(), 
                    "ana.martinez@ejemplo.com");
        }
        
        System.out.println("\n14. Actualizar usuario inexistente");
        client.actualizarUsuario(999L, "No Existe", "X999999", "2000-01-01", "no@existe.com");
        
        System.out.println("\n15. Actualizar matrícula de usuario");
        if (usuario1 != null) {
            client.actualizarUsuario(usuario1.getId(), "Ana Pérez Martínez", 
                    "NUEVA123", usuario1.getFechaNacimiento(), 
                    "ana.martinez@ejemplo.com");
        }
        
        System.out.println("\n16. Actualizar con fecha inválida");
        if (usuario1 != null) {
            client.actualizarUsuario(usuario1.getId(), "Ana Pérez Martínez", 
                    usuario1.getMatricula(), "2020/01/01", 
                    "ana.martinez@ejemplo.com");
        }
        
        System.out.println("\n17. Actualizar con correo inválido");
        if (usuario1 != null) {
            client.actualizarUsuario(usuario1.getId(), "Ana Pérez Martínez", 
                    usuario1.getMatricula(), usuario1.getFechaNacimiento(), 
                    "ana.martinez.ejemplo.com");
        }
        
        System.out.println("\n18. Actualizar con correo repetido");
        if (usuario1 != null && usuario2 != null) {
            client.actualizarUsuario(usuario1.getId(), "Ana Pérez Martínez", 
                    usuario1.getMatricula(), usuario1.getFechaNacimiento(), 
                    usuario2.getCorreoElectronico());
        }
        
        // PRUEBAS DE ELIMINACIÓN
        System.out.println("\n19. Eliminar usuario válido");
        if (usuario5 != null) {
            client.eliminarUsuario(usuario5.getId());
        }
        
        System.out.println("\n20. Eliminar usuario inexistente");
        client.eliminarUsuario(999L);
        
        // ===== PRUEBAS DE LIBROS =====
        System.out.println("\n--- PRUEBAS DE LIBROS ---");
        
        // PRUEBAS DE CREACIÓN
        System.out.println("\n1. Crear libro válido");
        Libro libro1 = client.crearLibro("Programación en Java", "Carlos López", "Tercera", "9780307474723", "Pearson", 1);
        
        System.out.println("\n2. Crear libro con ISBN inválido");
        client.crearLibro("Libro Inválido", "Autor", "Edición", "123", "Editorial", 1);
        
        System.out.println("\n3. Crear libro con disponibilidad inválida");
        client.crearLibro("Libro Inválido", "Autor", "Edición", "9780000000000", "Editorial", -1);
        
        System.out.println("\n4. Crear libro con ISBN repetido");
        client.crearLibro("Otro Libro", "Otro Autor", "Otra Edición", 
                "9780307474723", "Otra Editorial", 1);
        
        // Crear más libros para pruebas
        Libro libro2 = client.crearLibro("Cien Años de Soledad", "Gabriel García Márquez", "Primera", "9788437604947", "Editorial Sudamericana", 5);
        Libro libro3 = client.crearLibro("Diseño de APIs RESTful", "Laura Fernández", "Segunda","9780565432109", "Editorial Tecnológica", 2);
        Libro libro4 = client.crearLibro("Clean Code", "Robert C. Martin", "Primera", "9780132350884", "Prentice Hall", 4);
        Libro libro5 = client.crearLibro("Patrones de Diseño", "Erich Gamma", "Segunda","9780201633610", "Addison-Wesley", 2);
        
        // PRUEBAS DE GET
        System.out.println("\n5. Obtener libro existente");
        client.obtenerLibro(libro1.getId());
        
        System.out.println("\n6. Obtener libro inexistente");
        client.obtenerLibro(999L);
        
        // PRUEBAS DE LISTADO
        System.out.println("\n7. Listar todos los libros");
        client.listarLibros(0, 2, null, null);
        
        System.out.println("\n8. Listar libros filtrados por patrón");
        client.listarLibros(0, 2, "Java", null);
        
        System.out.println("\n9. Listar libros filtrados por disponibilidad");
        client.listarLibros(0, 2, null, true);
        
        System.out.println("\n10. Listar libros con múltiples filtros");
        client.listarLibros(0, 2, "a", true);
        
        System.out.println("\n11. Listar libros con paginación específica");
        client.listarLibros(1, 2, null, null);
        
        // PRUEBAS DE ACTUALIZACIÓN
        System.out.println("\n12. Actualizar libro válido");
        if (libro1 != null) {
            client.actualizarLibro(libro1.getId(), "Programación en Java - Edición Especial", libro1.getAutor(), "Cuarta", libro1.getIsbn(), libro1.getEditorial(), 3);
        }
        
        System.out.println("\n13. Actualizar libro inexistente");
        client.actualizarLibro(999L, "No Existe", "Autor", "Edición", "0000000000000", "Editorial", 1);
        
        System.out.println("\n14. Actualizar con ISBN inválido");
        if (libro1 != null) {
            client.actualizarLibro(libro1.getId(), libro1.getTitulo(), libro1.getAutor(), libro1.getEdicion(), "123", libro1.getEditorial(), libro1.getDisponibles());
        }
        
        System.out.println("\n15. Actualizar con disponibilidad inválida");
        if (libro1 != null) {
            client.actualizarLibro(libro1.getId(), libro1.getTitulo(), 
            libro1.getAutor(), libro1.getEdicion(), libro1.getIsbn(), 
            libro1.getEditorial(), -1);
        }
        
        System.out.println("\n16. Actualizar con ISBN repetido");
        if (libro1 != null && libro2 != null) {
            client.actualizarLibro(libro1.getId(), libro1.getTitulo(), 
                    libro1.getAutor(), libro1.getEdicion(), libro2.getIsbn(), 
                    libro1.getEditorial(), libro1.getDisponibles());
        }
        
        // PRUEBAS DE ELIMINACIÓN
        System.out.println("\n17. Eliminar libro válido");
        if (libro5 != null) {
            client.eliminarLibro(libro5.getId());
        }
        
        System.out.println("\n18. Eliminar libro inexistente");
        client.eliminarLibro(999L);
        
        // ===== PRUEBAS DE PRÉSTAMOS =====
        System.out.println("\n--- PRUEBAS DE PRÉSTAMOS ---");
        
        // PRUEBAS DE CREACIÓN
        System.out.println("\n1. Crear préstamo válido");
        Prestamo prestamo1 = client.crearPrestamo(usuario1.getId(), libro1.getId());
        
        System.out.println("\n2. Crear préstamo con usuario inexistente");
        client.crearPrestamo(999L, libro1.getId());
        
        System.out.println("\n3. Crear préstamo con libro inexistente");
        client.crearPrestamo(usuario2.getId(), 999L);
        
        System.out.println("\n4. Crear préstamo con libro no disponible");
        client.crearPrestamo(usuario2.getId(), libro4.getId());
        
        System.out.println("\n5. Crear préstamo con fecha específica");
        Prestamo prestamo2 = client.crearPrestamoConFecha(usuario2.getId(), 
            libro2.getId(),"2023-01-01");
        
        System.out.println("\n6. Crear préstamo con fecha inválida");
        client.crearPrestamoConFecha(usuario2.getId(), libro2.getId(),
"2023/01/01");
        
        System.out.println("\n7. Crear préstamo de libro ya prestado");
        client.crearPrestamo(usuario1.getId(), libro1.getId());
        
        // PRUEBAS DE GET
        System.out.println("\n8. Obtener préstamo existente");
        client.obtenerPrestamo(usuario1.getId(), prestamo1.getId());
        
        System.out.println("\n9. Obtener préstamo inexistente");
        client.obtenerPrestamo(usuario1.getId(), 999L);
        
        System.out.println("\n10. Obtener préstamo para usuario inexistente");
        client.obtenerPrestamo(999L, prestamo1.getId());
        
        System.out.println("\n11. Obtener préstamo de usuario equivocado");
        client.obtenerPrestamo(usuario2.getId(), prestamo1.getId());
        
        // PRUEBAS DE LISTADO
        // Crear más préstamos para pruebas
        Prestamo prestamo3 = client.crearPrestamo(usuario3.getId(), libro3.getId());
        Prestamo prestamo4 = client.crearPrestamo(usuario1.getId(), libro2.getId());
        
        // Devolver algunos préstamos para tener histórico
        client.devolverPrestamo(prestamo3.getUsuario().getId(), prestamo3.getId());
        client.devolverPrestamo(prestamo4.getUsuario().getId(), prestamo4.getId());
        
        System.out.println("\n12. Listar préstamos actuales de usuario");
        client.listarPrestamosUsuario(usuario1.getId(), null, null, null, null);
        
        System.out.println("\n13. Listar préstamos actuales filtrados por periodo");
        client.listarPrestamosUsuario(usuario1.getId(), null, null,
                "2023-01-01", "2023-12-31");
        
        System.out.println("\n14. Listar préstamos actuales de usuario inexistente");
        client.listarPrestamosUsuario(999L, null, null, null, null);
        
        System.out.println("\n15. Listar préstamos actuales con fechas inválidas");
        client.listarPrestamosUsuario(usuario1.getId(), 0, 2,
                "2023/01/01", "2023-12-31");
        
        System.out.println("\n16. Listar histórico de préstamos");
        client.listarHistoricoPrestamosUsuario(usuario1.getId(), 0, 2, null, null);
        
        System.out.println("\n17. Listar histórico filtrado por periodo");
        client.listarHistoricoPrestamosUsuario(usuario1.getId(), 0, 2, 
                "2023-01-01", "2023-12-31");
        
        System.out.println("\n18. Listar histórico de usuario inexistente");
        client.listarHistoricoPrestamosUsuario(999L, 0, 2, null, null);
        
        System.out.println("\n19. Listar histórico con fechas inválidas");
        client.listarHistoricoPrestamosUsuario(usuario1.getId(), 0, 2,
                "2023/01/01", "2023-12-31");
        
        // PRUEBAS DE AMPLIACIÓN
        System.out.println("\n20. Ampliar préstamo válido");
        client.ampliarPrestamo(prestamo1.getUsuario().getId(), prestamo1.getId());
        
        System.out.println("\n21. Ampliar préstamo inexistente");
        client.ampliarPrestamo(usuario1.getId(), 999L);
        
        System.out.println("\n22. Ampliar préstamo de usuario equivocado");
        client.ampliarPrestamo(usuario1.getId(), prestamo2.getId());
        
        System.out.println("\n23. Ampliar préstamo ya devuelto");
        client.ampliarPrestamo(prestamo1.getUsuario().getId(), prestamo1.getId());
        
        System.out.println("\n24. Ampliar préstamo ya ampliado");
        client.ampliarPrestamo(prestamo1.getUsuario().getId(), prestamo1.getId());
        
        // PRUEBAS DE DEVOLUCIÓN
        System.out.println("\n25. Devolver préstamo válido");
        client.devolverPrestamo(prestamo1.getUsuario().getId(), prestamo1.getId());
        
        System.out.println("\n26. Devolver préstamo inexistente");
        client.devolverPrestamo(usuario1.getId(), 999L);
        
        System.out.println("\n27. Devolver préstamo de usuario equivocado");
        client.devolverPrestamo(usuario1.getId(), prestamo2.getId());
        
        System.out.println("\n28. Devolver préstamo ya devuelto");
        client.devolverPrestamo(prestamo1.getUsuario().getId(), prestamo1.getId());
        
        // ===== PRUEBAS FINALES =====
        System.out.println("\n--- PRUEBAS FINALES ---");
        
        System.out.println("\n1. Obtener actividad de usuario");
        client.obtenerActividadUsuario(usuario1.getId(), null, null);
        
        System.out.println("\n2. Obtener actividad de usuario inexistente");
        client.obtenerActividadUsuario(999L, null, null);

        // ===== LIMPIEZA =====
        System.out.println("\nEliminando recursos de prueba...");
        if (libro1 != null) client.eliminarLibro(libro1.getId());
        if (libro2 != null) client.eliminarLibro(libro2.getId());
        if (libro3 != null) client.eliminarLibro(libro3.getId());
        if (libro4 != null) client.eliminarLibro(libro4.getId());
        if (libro5 != null) client.eliminarLibro(libro5.getId());
        if (usuario1 != null) client.eliminarUsuario(usuario1.getId());
        if (usuario2 != null) client.eliminarUsuario(usuario2.getId());
        if (usuario3 != null) client.eliminarUsuario(usuario3.getId());
        if (usuario4 != null) client.eliminarUsuario(usuario4.getId());
        if (usuario5 != null) client.eliminarUsuario(usuario5.getId());

        System.out.println("\n===== PRUEBAS COMPLETADAS =====");
    }
}