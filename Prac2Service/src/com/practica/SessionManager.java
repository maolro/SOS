package com.practica;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private Map<String, String> sesionesActivas = new ConcurrentHashMap<>();
    private Map<String, String> rolesUsuario = new HashMap<>();
    // Otorga privilegios de admin al usuario admin al arrancar
    public SessionManager(){
        rolesUsuario.put("admin", "admin");
    }
    // Métodos para gestionar el funcionamiento de sesiones
    public String createSession(String usuario){
        String sessionId = UUID.randomUUID().toString();
        sesionesActivas.put(sessionId, usuario);
        return sessionId;
    }
    public String getUsername(String sessionId) throws Exception{
        if(!sesionesActivas.containsKey(sessionId)){
            throw new Exception("Sesión caducada o inválida");
        }
        return sesionesActivas.get(sessionId);
    }
    public String getRole(String usuario){
        return rolesUsuario.getOrDefault(usuario, "usuario");
    }
    public void invalidateSession(String sessionId) throws Exception{
        if(!rolesUsuario.containsKey(sessionId)){
            throw new Exception("Sesión inválida");
        }
        sesionesActivas.remove(sessionId);
    }
    public void invalidateUserSessions(String usuario) throws Exception{
        for(Entry<String,String> e : sesionesActivas.entrySet()){
            if(e.getValue().equals("usuario")) invalidateSession(e.getKey());
        }
    }
}  
