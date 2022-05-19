package com.colombina.Model;

public class ProductoClass {

    private String uid;
    private String nombre_producto;
    private String categoria;
    private String precio;
    private String cantidad;
    private String fecha;
    private String user;

    public ProductoClass() {

    }

    public String getUid() {
        return uid;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getPrecio() {
        return precio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public String getUser() {
        return user;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return nombre_producto;
    }
}
