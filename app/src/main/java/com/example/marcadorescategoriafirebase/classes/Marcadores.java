package com.example.marcadorescategoriafirebase.classes;

public class Marcadores {
    private long id;
    private String categoria;
    private String url;
    private String descripcion;

    public Marcadores(){

    }

    public Marcadores(long id, String categoria, String url, String descripcion) {
        this.id = id;
        this.categoria = categoria;
        this.url = url;
        this.descripcion = descripcion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Marcadores{" +
                "id=" + id +
                ", categoria='" + categoria + '\'' +
                ", url='" + url + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
