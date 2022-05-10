package net.iessochoa.neighborconect.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Incidencias {

    private String usuario;
    private String descripcion;
    //@ServerTimestamp permite que sea el servidor el que asigne la hora al crear el documento
    @ServerTimestamp
    private Date fechaCreacion;

    public Incidencias() {
    }

    public Incidencias(String usuario, String descripcion) {
        this.usuario = usuario;
        this.descripcion = descripcion;
    }

    public Incidencias(String descripcion) {
        this.descripcion = descripcion;
    }

    public Incidencias(String usuario, String descripcion, Date fechaCreacion) {
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
