package com.colombina.Model;

public class CategoriaClass {

    private String uid_categoria;
    private String nombre_categoria;

    public CategoriaClass(String uid_categoria, String nombre_categoria) {
        this.uid_categoria = uid_categoria;
        this.nombre_categoria = nombre_categoria;
    }

    public String getUid_categoria() {
        return uid_categoria;
    }

    public String getNombre_categoria() {
        return nombre_categoria;
    }


    public void setUid_categoria(String uid_categoria) {
        this.uid_categoria = uid_categoria;
    }

    public void setNombre_categoria(String nombre_categoria) {
        this.nombre_categoria = nombre_categoria;
    }


    @Override
    public String toString() {
        return nombre_categoria;
    }
}
