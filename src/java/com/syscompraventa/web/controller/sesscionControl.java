/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.syscompraventa.web.controller;

import com.syscompraventa.data.entities.Usuarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author DELL
 */
@Named(value = "sesscionControl")
@ViewScoped
public class sesscionControl implements Serializable {

    private static final Logger LOG = Logger.getLogger(sesscionControl.class.getName());

    private Usuarios usuarioLogeado;

    public sesscionControl() {
    }

    @PostConstruct
    public void inicializar() {
        try {
            usuarioLogeado = new Usuarios();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error", ex);
        }
    }

    public Usuarios getUsuarioLogeado() {
        return usuarioLogeado;
    }

    public void setUsuarioLogeado(Usuarios usuarioLogeado) {
        this.usuarioLogeado = usuarioLogeado;
    }

    public void verificarSession() {

        if (usuarioLogeado == null) {
            try {

                FacesContext cont = FacesContext.getCurrentInstance();
                usuarioLogeado = (Usuarios) cont.getExternalContext().getSessionMap().get("usFiltro");
                cont.getExternalContext().redirect("/sysCompraVenta1.0.0/views/plantillas/index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(sesscionControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    public void serrarSecion(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

}
