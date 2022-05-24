package com.colombina.Model;

public class Usuario {
    private String uid_user;
    private String correo2;
    private String rol;


    public Usuario(String uid_user, String correo2, String rol) {
        this.uid_user = uid_user;
        this.correo2 = correo2;
        this.rol = rol;
    }

    public Usuario() {
    }

    public String getUid_user() {
        return uid_user;
    }

    public void setUid_user(String uid_user) {
        this.uid_user = uid_user;
    }

    public String getCorreo2() {
        return correo2;
    }

    public void setCorreo2(String correo2) {
        this.correo2 = correo2;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
