package com.practica;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static Map<String, String> sesionesActivas = new ConcurrentHashMap<>();
    private static Map<String, String> rolesUsuario = new HashMap<>();
    // Otorga privilegios de admin al usuario admin al arrancar
    static{
        rolesUsuario.put("admin", "admin");
    }
    // Métodos para gestionar el funcionamiento de sesiones
    public static String createSession(String usuario){
        String sessionId = UUID.randomUUID().toString();
        sesionesActivas.put(sessionId, usuario);
        return sessionId;
    }
    public static String getUsername(String sessionId) throws Exception{
        if(!sesionesActivas.containsKey(sessionId)){
            throw new Exception("Sesión caducada o inválida");
        }
        return sesionesActivas.get(sessionId);
    }
    public static String getRole(String usuario){
        return rolesUsuario.getOrDefault(usuario, "usuario");
    }
    public static void invalidateSession(String sessionId) throws Exception{
        if(!rolesUsuario.containsKey(sessionId)){
            throw new Exception("Sesión inválida");
        }
        sesionesActivas.remove(sessionId);
    }
    public static void invalidateUserSessions(String usuario) throws Exception{
        for(Entry<String,String> e : sesionesActivas.entrySet()){
            if(e.getValue().equals("usuario")) invalidateSession(e.getKey());
        }
    }
}  
