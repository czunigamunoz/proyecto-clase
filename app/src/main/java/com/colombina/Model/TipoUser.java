package com.colombina.Model;

public class TipoUser {
    private String uid_tipo;
    private String tipo;

    public TipoUser(String uid_tipo, String tipo) {
        this.uid_tipo = uid_tipo;
        this.tipo = tipo;
    }

    public TipoUser() {
    }

    public String getUid_tipo() {
        return uid_tipo;
    }
    public void setUid_tipo(String uid_tipo) {
        this.uid_tipo = uid_tipo;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return tipo;
    }
}
