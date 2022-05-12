package net.iessochoa.neighborconect.model;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class Incidencias {

    private String usuario;
    private String descripcion;
    private String fotoUri;
    //@ServerTimestamp permite que sea el servidor el que asigne la hora al crear el documento
    @ServerTimestamp
    private Date fechaCreacion;

    public Incidencias() {
    }



    public Incidencias(String usuario, String descripcion, String fotoUri) {
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.fotoUri = fotoUri;
    }

    public Incidencias(String usuario, String descripcion, String fotoUri, Date fechaCreacion) {
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.fotoUri = fotoUri;
        this.fechaCreacion = fechaCreacion;
    }

    public Incidencias(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFotoUri() {
        return fotoUri;
    }

    public void setFotoUri(String fotoUri) {
        this.fotoUri = fotoUri;
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
