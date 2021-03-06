package com.syscompraventa.web.controller;

import auxiliar.validations;
import com.syscompraventa.business.facade.UsuariosFacade;
import com.syscompraventa.data.entities.Usuarios;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.*;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named(value = "loginController")
@SessionScoped
public class loginController implements Serializable {

    @EJB
    private UsuariosFacade usuariosFacade;
    private static final Logger LOG = Logger.getLogger(loginController.class.getName());

    private Usuarios usuarioLogeado, usActualizando;
    private String redireccion;
    private String username, contrasenia, contrasenia2;

    public loginController() {
    }

    @PostConstruct
    public void inicializar() {
        try {
            usActualizando = new Usuarios();
            username = new String();
            contrasenia = new String();
            usuarioLogeado = new Usuarios();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error", ex);
        }
    }

    public void limpiarObjeto() {
        try {
            username = new String();
            contrasenia = new String();
            usuarioLogeado = new Usuarios();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }

    }

    public void actualizarUserPass() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (!usActualizando.getPaswword().equals(null)) {
                if (usuariosFacade.CambiarUsPass(usActualizando) != null) {
                    if (contrasenia.equals(contrasenia2)) {
                        usActualizando.setPaswword(validations.sha512(contrasenia));
                        usuariosFacade.edit(usActualizando);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Usuario y contraseña actualizado correctamente"));
                        usuarioLogeado = usActualizando;
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Las contraseñas no coinciden"));
                        usActualizando = usuarioLogeado;
                    }

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Contraseña incorrecta"));
                    usActualizando = usuarioLogeado;
                }
            } else {
                usuariosFacade.edit(usActualizando);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Usuario actualizado correctamente"));
                usuarioLogeado = usuarioLogeado;
            }

        } catch (Exception ex) {
            Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

//    public String autenticarUsuario() {
//
//        try {
//           // FacesMessage message = null;
//            usuarioLogeado = usuariosFacade.Autenticar(username, validations.sha512(contrasenia));
//            
//            System.out.println("Usuario en usos "+usuarioLogeado.getApellidos());
//
//            if (usuarioLogeado != null) {
//
//                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usFiltro", usuarioLogeado);
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Bienvenido: "+usuarioLogeado.getApellidos()));
//                usActualizando = usuarioLogeado;
//                redireccion = "/views/menuPrincipal?faces-redirect=true";
//            } else {
//                limpiarObjeto();
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Credenciales incorrectos"));
//            }
//
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Error", ex);
//        }
//        return redireccion;
//
//    }
    
    public String autenticarUsuario() {

        try {
            // FacesMessage message = null;
            usuarioLogeado = usuariosFacade.Autenticar(username, validations.sha512(contrasenia));

            if (usuarioLogeado != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usFiltro", usuarioLogeado);
                if (usuarioLogeado.getIdpermisos().getNombre().equalsIgnoreCase("Administrador")) {
                    redireccion = "/views/menuPrincipal?faces-redirect=true";
                } else if (usuarioLogeado.getIdpermisos().getNombre().equalsIgnoreCase("Secretario")) {
                    redireccion = "/views/menuSecretario?faces-redirect=true";
                } else if (usuarioLogeado.getIdpermisos().getNombre().equalsIgnoreCase("Tecnico")) {
                    redireccion = "/views/menuTecnico?faces-redirect=true";
                } else if (usuarioLogeado.getIdpermisos().getNombre().equalsIgnoreCase("Bodeguero")) {
                    redireccion = "/views/menuBodeguero?faces-redirect=true";
                } else if (usuarioLogeado.getIdpermisos().getNombre().equalsIgnoreCase("Cliente")) {
                    redireccion = "/views/menuCliente?faces-redirect=true";
                }

            } else {
                limpiarObjeto();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Credenciales incorrectos"));
            }

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error", ex);
        }
        return redireccion;

    }
    
    

    public String cerrarSesion() {
        limpiarObjeto();
        redireccion = new String();
        try {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            redireccion = "/index.xhtml";

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error", ex);
        }
        return redireccion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getContrasenia2() {
        return contrasenia2;
    }

    public void setContrasenia2(String contrasenia2) {
        this.contrasenia2 = contrasenia2;
    }

    public Usuarios getUsuarioLogeado() {
        return usuarioLogeado;
    }

    public void setUsuarioLogeado(Usuarios usuarioLogeado) {

        this.usuarioLogeado = usuarioLogeado;
    }

    public Usuarios getUsActualizando() {
        return usActualizando;
    }

    public void setUsActualizando(Usuarios usActualizando) {
        this.usActualizando = usActualizando;
    }

}
