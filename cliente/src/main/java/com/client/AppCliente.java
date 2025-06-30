package com.client;

import com.client.model.*;

import java.util.ArrayList;
import java.util.List;

public class AppCliente {
    private static void pruebasUsuario(ClienteBiblioteca client) {
        System.out.println("\n===== PRUEBAS DE USUARIO =====");

        // Prueba 1: Crear un usuario válido
        System.out.println("\n> Prueba 1: Crear un usuario válido (esperado: éxito)");
        client.postUsuario("juanperez", "1", "1995-04-15", "juan.perez@example.com");

        // Prueba 2: Crear un usuario con matrícula repetida
        System.out.println("\n> Prueba 2: Crear un usuario con matrícula repetida (esperado: error por duplicado)");
        client.postUsuario("analopez", "1", "2001-09-30", "ana.lopez@example.com");

        // Prueba 3: Crear un usuario con fecha en formato inválido
        System.out
                .println("\n> Prueba 3: Crear un usuario con fecha en formato inválido (esperado: error por formato)");
        client.postUsuario("analopez", "2", "09/30/2025", "ana.lopez@example.com");

        // Prueba 4: Crear un usuario con correo inválido
        System.out.println("\n> Prueba 4: Crear un usuario con correo inválido (esperado: error por formato)");
        client.postUsuario("analopez", "2", "2001-09-30", "ana.lopezexample.com");

        // Prueba 5: GET de un usuario existente
        System.out.println("\n> Prueba 5: Obtener usuario con matrícula existente (esperado: éxito)");
        client.getUsuario("12345");

        // Prueba 6: GET de un usuario inexistente
        System.out
                .println("\n> Prueba 6: Obtener usuario inexistente (esperado: error 404 o mensaje de no encontrado)");
        client.getUsuario("99999");

        // Prueba 7: Actualizar usuario existente con datos válidos
        System.out.println("\n> Prueba 7: Actualizar datos de usuario existente (esperado: éxito)");
        client.putUsuario("1", "juan_perez", "1", "1995-03-01", "juan.perez@gmail.com");

        // Prueba 8: Actualizar usuario inexistente
        System.out.println("\n> Prueba 8: Intentar actualizar usuario inexistente (esperado: error)");
        client.putUsuario("99999", "juan_perez", "1", "1995-03-01", "juan.perez@gmail.com");

        // Prueba 9: Cambiar matrícula del usuario
        System.out.println("\n> Prueba 9: Cambiar matrícula del usuario (esperado: éxito si permitido, o error)");
        client.putUsuario("1", "juan_perez", "2", "1995-03-01", "juan.perez@gmail.com");

        // Prueba 10: Enviar fecha inválida en PUT
        System.out.println("\n> Prueba 10: Actualizar usuario con fecha inválida (esperado: error de validación)");
        client.putUsuario("1", "juan_perez", "1", "1995/03/01", "juan.perez@gmail.com");

        // Prueba 11: Enviar correo inválido en PUT
        System.out.println("\n> Prueba 11: Actualizar usuario con correo inválido (esperado: error de validación)");
        client.putUsuario("1", "juan_perez", "1", "1995-03-01", "juan.perezgmail.com");

        // Prueba 12: Eliminar usuario existente
        System.out.println("\n> Prueba 12: Eliminar usuario existente (esperado: éxito)");
        client.deleteUsuario("1");

        // Prueba 13: Eliminar usuario inexistente
        System.out.println("\n> Prueba 13: Eliminar usuario inexistente (esperado: error 404 o sin efecto)");
        client.deleteUsuario("99999");

        // Prueba 14: Obtiene todos los usuarios (crea dos adicionales)
        client.postUsuario("analopez", "2", "2001-09-30", "ana.lopez@example.com");
        client.postUsuario("pepegarcia", "3", "2000-02-16", "pepe.garcia@example.com");
        System.out.println("\n> Prueba 14: Obtiene todos los usuarios (crea dos adicionales)");
        client.getUsuarioList(null, null, null);

        // Prueba 15: Obtiene todos los usuarios que empiecen por la j
        System.out.println("\n> Prueba 15: Obtiene todos los usuarios que empiecen por la j");
        client.getUsuarioList("a", null, null);

        // Prueba 16: Obtiene todos los usuarios con paginacion especifica
        System.out.println("\n> Prueba 16: Obtiene todos los usuarios con paginacion especifica");
        client.getUsuarioList(null, 0, 10);

        // Prueba 17: Obtene todos los usuarios con paginacion especifica y filtros
        System.out.println("\n> Prueba 17: Obtene todos los usuarios con paginacion especifica y filtros");
        client.getUsuarioList("a", 0, 10);

        // Prueba 18: Obtene todos los usuarios con filtro (no aparece ninguno)
        System.out.println("\n> Obtene todos los usuarios con filtro (no aparece ninguno)");
        client.getUsuarioList("7", null, null);

        // Limpieza de recursos
        client.deleteUsuario("2");
        client.deleteUsuario("3");
    }

    private static void pruebasLibro(ClienteBiblioteca client) {
        System.out.println("\n===== PRUEBAS DE LIBRO =====");

        // Prueba 1: Crear un libro válido
        System.out.println("\n> Prueba 1: Crear un libro válido (esperado: éxito)");
        client.postLibro("Cien años de soledad", "Gabriel García Márquez", "1ª edición", "1", "Editorial Sudamericana",
                5);

        // Prueba 2: Crear libro con disponibilidad inválida
        System.out.println("\n> Prueba 2: Crear libro con disponibilidad negativa (esperado: error)");
        client.postLibro("Libro Inválido", "Autor", "1ª ed", "2", "Editorial", -1);

        // Prueba 3: Crear libro con ISBN repetido
        System.out.println("\n> Prueba 3: Crear libro con ISBN repetido (esperado: error de conflicto)");
        client.postLibro("Libro Duplicado", "Autor", "1ª ed", "1", "Editorial", 5);

        // Prueba 4: GET de un libro existente
        System.out.println("\n> Prueba 4: Obtener libro con ISBN existente (esperado: éxito)");
        client.getLibro("1");

        // Prueba 5: GET de un libro inexistente
        System.out.println("\n> Prueba 5: Obtener libro inexistente (esperado: error 404)");
        client.getLibro("99999");

        // Crear 5 libros de prueba
        System.out.println("\n> Creando 5 libros de prueba...");
        client.postLibro("El Principito", "Antoine de Saint-Exupéry", "1ª ed", "2", "Editorial Francesa", 3);
        client.postLibro("1984", "George Orwell", "1ª ed", "3", "Editorial Inglesa", 2);
        client.postLibro("Don Quijote", "Miguel de Cervantes", "1ª ed", "4", "Editorial Española", 4);
        client.postLibro("Crimen y Castigo", "Fiodor Dostoievski", "1ª ed", "5", "Editorial Rusa", 1);
        client.postLibro("Orgullo y Prejuicio", "Jane Austen", "1ª ed", "6", "Editorial Inglesa", 2);

        // Prueba 6: Actualizar libro existente
        System.out.println("\n> Prueba 6: Actualizar datos de libro existente (esperado: éxito)");
        client.putLibro("1", "Cien años de soledad (Edición Especial)", "Gabriel García Márquez",
                "2ª edición", "1", "Editorial Sudamericana", 0);

        // Prueba 7: Actualizar libro inexistente
        System.out.println("\n> Prueba 7: Intentar actualizar libro inexistente (esperado: error)");
        client.putLibro("99999", "Libro Fantasma", "Autor", "1ª ed", "99999", "Editorial", 1);

        // Prueba 8: Cambiar ISBN
        System.out.println("\n> Prueba 8: Cambiar ISBN (esperado: error)");
        client.putLibro("1", "Cien años de soledad", "Gabriel García Márquez",
                "2ª edición", "A", "Editorial Sudamericana", 4);

        // Prueba 9: GET de todos los libros
        System.out.println("\n> Prueba 9: Obtener todos los libros (esperado: éxito)");
        client.getLibroList(null, null, null, null);

        // Prueba 10: GET de libros filtrados por patrón
        System.out.println("\n> Prueba 10: Obtener libros que contengan 'Cien' (esperado: éxito)");
        client.getLibroList("Cien", null, null, null);

        // Prueba 11: GET de libros filtrados por disponibilidad
        System.out.println("\n> Prueba 11: Obtener libros disponibles (esperado: éxito)");
        client.getLibroList(null, true, null, null);

        // Prueba 12: GET de libros con filtro combinado
        System.out.println("\n> Prueba 12: Obtener libros con el titulo Cien que esten disponibles (esperado: éxito)");
        client.getLibroList("Inglesa", true, null, null);

        // Prueba 13: GET de libros con paginación
        System.out.println("\n> Prueba 13: Obtener libros con paginación (esperado: éxito)");
        client.getLibroList(null, null, 0, 3);

        // Prueba 14: Eliminar libro existente
        System.out.println("\n> Prueba 14: Eliminar libro existente (esperado: éxito)");
        client.deleteLibro("6");

        // Prueba 15: Eliminar libro inexistente
        System.out.println("\n> Prueba 15: Eliminar libro inexistente (esperado: error 404)");
        client.deleteLibro("99999");

        // Limpieza de recursos
        System.out.println("\n> Limpiando libros de prueba...");
        client.deleteLibro("1");
        client.deleteLibro("2");
        client.deleteLibro("3");
        client.deleteLibro("4");
        client.deleteLibro("5");
    }

    private static void pruebasPrestamo(ClienteBiblioteca client) {
        System.out.println("\n===== PRUEBAS DE PRÉSTAMO =====");

        // Crear usuario y libro de prueba
        System.out.println("\n> Preparando datos de prueba...");
        client.postUsuario("usuarioprestamo", "PREST01", "1990-01-01", "prestamo@test.com");
        client.postLibro("Libro para Préstamo", "Autor Préstamo", "1ª ed", "PREST01", "Editorial", 1);
        client.postLibro("Libro Prestado", "Autor", "1ª ed", "PREST02", "Editorial", 1);
        client.postLibro("Libro Ampliado", "Autor", "1ª ed", "PREST03", "Editorial", 1);
        client.postLibro("Libro Devuelto", "Autor", "1ª ed", "PREST04", "Editorial", 1);

        // Prueba 1: Crear préstamo válido
        System.out.println("\n> Prueba 1: Crear préstamo válido (esperado: éxito)");
        client.postPrestamo("PREST01", "PREST01", "2025-06-27");

        // Prueba 2: Usuario inexistente intenta crear préstamo
        System.out.println("\n> Prueba 2: Usuario inexistente intenta préstamo (esperado: error 404)");
        client.postPrestamo("INEXIST", "PREST01", "2025-06-27");

        // Prueba 3: Crear préstamo con fecha inválida
        System.out.println("\n> Prueba 3: Crear préstamo con fecha inválida (esperado: error)");
        client.postPrestamo("PREST01", "PREST01", "2025/06/27");

        // Prueba 4: Crear préstamo de libro inexistente
        System.out.println("\n> Prueba 4: Crear préstamo de libro inexistente (esperado: error 404)");
        client.postPrestamo("PREST01", "INEXIST", "2025-06-27");

        // Prueba 5: Crear préstamo de libro no disponible
        System.out.println("\n> Prueba 5: Crear préstamo de libro no disponible (esperado: error 409)");
        client.postPrestamo("PREST01", "PREST02", "2025-06-27");

        // Prueba 6: Crear préstamo de libro ya prestado
        System.out.println("\n> Prueba 6: Crear préstamo de libro ya prestado (esperado: error 409)");
        client.postPrestamo("PREST01", "PREST01", "2025-06-28");

        // Prueba 7: GET de préstamo existente
        System.out.println("\n> Prueba 7: Obtener préstamo existente (esperado: éxito)");
        client.getPrestamo("PREST01", 1L);

        // Prueba 8: GET de préstamo inexistente
        System.out.println("\n> Prueba 8: Obtener préstamo inexistente (esperado: error 404)");
        client.getPrestamo("PREST01", 999L);

        // Prueba 9: GET de préstamo para usuario inexistente
        System.out.println("\n> Prueba 9: Obtener préstamo de usuario inexistente (esperado: error 404)");
        client.getPrestamo("INEXIST", 1L);

        // Crear más préstamos de prueba
        System.out.println("\n> Creando préstamos adicionales...");
        client.postPrestamo("PREST01", "PREST03", "2025-06-20"); // Para ampliación
        client.postPrestamo("PREST01", "PREST04", "2025-06-15"); // Para devolución
        client.returnPrestamo("PREST01", 3L, "2025-06-16"); // Marcar como devuelto

        // Prueba 10: GET todos los préstamos actuales
        System.out.println("\n> Prueba 10: Obtener préstamos actuales (esperado: éxito)");
        client.getPrestamoList("PREST01", null, null, null, null, null);

        // Prueba 11: GET préstamos actuales filtrados por periodo
        System.out.println("\n> Prueba 11: Obtener préstamos actuales por periodo (esperado: éxito)");
        client.getPrestamoList("PREST01", null, "2025-06-01", "2025-06-30", null, null);

        // Prueba 12: GET préstamos actuales de usuario inexistente
        System.out.println("\n> Prueba 12: Obtener préstamos de usuario inexistente (esperado: error 404)");
        client.getPrestamoList("INEXIST", null, null,
                null, null, null);

        // Prueba 13: GET préstamos actuales con fechas inválidas
        System.out.println("\n> Prueba 13: Obtener préstamos con fechas inválidas (esperado: error)");
        client.getPrestamoList("PREST01", null, "2025/06/01", "2025-06-30", null, null);

        // Prueba 14: GET préstamos históricos
        System.out.println("\n> Prueba 14: Obtener préstamos históricos (esperado: éxito)");
        client.getPrestamoList("PREST01", false, null, null, null, null);

        // Prueba 15: GET préstamos históricos filtrados
        System.out.println("\n> Prueba 15: Obtener préstamos históricos por periodo (esperado: éxito)");
        client.getPrestamoList("PREST01", false, "2025-06-01", "2025-06-30", null, null);

        // Prueba 16: Ampliar préstamo
        System.out.println("\n> Prueba 16: Ampliar préstamo (esperado: éxito)");
        client.extendPrestamo("PREST01", 2L);

        // Prueba 17: Ampliar préstamo inexistente
        System.out.println("\n> Prueba 17: Ampliar préstamo inexistente (esperado: error 404)");
        client.extendPrestamo("PREST01", 999L);

        // Prueba 18: Ampliar préstamo ya devuelto
        System.out.println("\n> Prueba 18: Ampliar préstamo ya devuelto (esperado: error 409)");
        client.extendPrestamo("PREST01", 3L);

        // Prueba 19: Ampliar préstamo ya ampliado
        System.out.println("\n> Prueba 19: Ampliar préstamo ya ampliado (esperado: error 409)");
        client.extendPrestamo("PREST01", 2L);

        // Prueba 20: Devolver préstamo
        System.out.println("\n> Prueba 20: Devolver préstamo (esperado: éxito)");
        client.returnPrestamo("PREST01", 1L, "2025-06-29");

        // Prueba 21: Devolver préstamo inexistente
        System.out.println("\n> Prueba 21: Devolver préstamo inexistente (esperado: error 404)");
        client.returnPrestamo("PREST01", 999L, "2025-06-29");

        // Prueba 22: Devolver préstamo ya devuelto
        System.out.println("\n> Prueba 22: Devolver préstamo ya devuelto (esperado: error 409)");
        client.returnPrestamo("PREST01", 3L, "2025-06-29");

        // Prueba 23: GET actividad de usuario
        System.out.println("\n> Prueba 23: Obtener actividad de usuario (esperado: éxito)");
        client.getActividadUsuario("PREST01");

        // Prueba 24: GET actividad de usuario inexistente
        System.out.println("\n> Prueba 24: Obtener actividad de usuario inexistente (esperado: error 404)");
        client.getActividadUsuario("INEXIST");

        // Prueba 25: Eliminar préstamo existente
        System.out.println("\n> Prueba 25: Eliminar préstamo existente (esperado: éxito)");
        client.deletePrestamo("PREST01", 3L); // Eliminar préstamo ya devuelto

        // Prueba 26: Eliminar préstamo inexistente
        System.out.println("\n> Prueba 26: Eliminar préstamo inexistente (esperado: error 404)");
        client.deletePrestamo("PREST01", 999L);

        // Prueba 27: Eliminar usuario con préstamos activos
        System.out.println("\n> Prueba 27: Eliminar usuario con préstamos activos (esperado: error 409)");
        client.deleteUsuario("PREST01");

        // Prueba 28: Eliminar libro prestado
        System.out.println("\n> Prueba 28: Eliminar libro prestado (esperado: error 409)");
        client.deleteLibro("PREST01");

        // Limpieza de recursos
        System.out.println("\n> Limpiando datos de prueba...");
        client.deletePrestamo("PREST01", 1L);
        client.deletePrestamo("PREST01", 2L);
        client.deletePrestamo("PREST01", 4L);
        client.deleteUsuario("PREST01");
        client.deleteLibro("PREST01");
        client.deleteLibro("PREST02");
        client.deleteLibro("PREST03");
        client.deleteLibro("PREST04");
    }

    public static void main(String[] args) {
        ClienteBiblioteca client = new ClienteBiblioteca();
        System.out.println("\n===== PRUEBAS DEL SERVICIO DE BIBLIOTECA =====");
        try {
            pruebasUsuario(client);
            pruebasLibro(client);
            pruebasPrestamo(client);
        } catch (Exception e) {
            System.err.println("Ocurrió una excepción durante las pruebas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}