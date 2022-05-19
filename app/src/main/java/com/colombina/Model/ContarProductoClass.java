package com.colombina.Model;

public class ContarProductoClass {

    private String uid_contar;
    private String producto;
    private String categoria;
    private String cantidad;
    private String conteo;
    private String pedido;
    private String aprobo;
    private String fecha;
    private String user;


    public ContarProductoClass() {
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public String getUid_contar() {
        return uid_contar;
    }

    public String getProducto() {
        return producto;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getCantidad() {
        return cantidad;
    }

    public String getConteo() {
        return conteo;
    }

    public String getAprobo() {
        return aprobo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getUser() {
        return user;
    }

    public void setUid_contar(String uid_contar) {
        this.uid_contar = uid_contar;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public void setConteo(String conteo) {
        this.conteo = conteo;
    }

    public void setAprobo(String aprobo) {
        this.aprobo = aprobo;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        return builder.append("Categoria:  ").append(categoria).
                append("\n Producto:  ").append(producto).
                append("\n Cantidad: ").append(cantidad).
                append("\n  Aprobo: ").append(aprobo).toString();

    }
}
