package com.syscompraventa.web.controller;

import auxiliar.validations;
import com.syscompraventa.business.facade.PermisosFacade;
import com.syscompraventa.data.entities.Usuarios;
import com.syscompraventa.web.controller.util.JsfUtil;
import com.syscompraventa.web.controller.util.JsfUtil.PersistAction;
import com.syscompraventa.business.facade.UsuariosFacade;
import com.syscompraventa.data.entities.Permisos;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;

@Named("usuariosController")
@ViewScoped
public class UsuariosController implements Serializable {

    @EJB
    private PermisosFacade permisosFacade;
    @EJB
    private UsuariosFacade usuariosFacade;
    private static final Logger LOG = Logger.getLogger(UsuariosController.class.getName());

    private List<Usuarios> listaUsuarios;
    private List<Usuarios> listaUsInactivos;
    private List<Permisos> listPerm;

    private Usuarios usuarioActual;
    private Permisos perm;  //-----------------

    private String password, password2;
    private Integer bandera = 0;
    //-----------------

    public Permisos getPerm() {
        return perm;
    }
/////////////{

    @PostConstruct
    public void inicializar() {
        try {
            listPerm = permisosFacade.findAll();
            listaUsuarios = usuariosFacade.listarUsuarios();
            listaUsInactivos = usuariosFacade.listarUsInactivos();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public void limpiarObjeto() {
        try {
            usuarioActual = new Usuarios();
            password2 = new String();
            password = new String();
            bandera = 0;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public void activarUser(Usuarios us) {
        try {
            usuarioActual = us;
            usuarioActual.setEstado(true);
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("ActivarUser"));
            limpiarObjeto();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error inicializar", ex);
        }
    }

    //metod ajax para ver que tenga los 10 digitos de la cedula
    public void validarCedula() {

        if (usuarioActual.getCedula().length() == 10) {
            bandera = validations.operacionCedula(usuarioActual.getCedula());
        }
    }

    public Usuarios getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuarios usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UsuariosFacade getFacade() {
        return usuariosFacade;
    }

    public Usuarios prepareCreate() {
        usuarioActual = new Usuarios();
        initializeEmbeddableKey();
        return usuarioActual;
    }

    public void create() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Usuarios userC = usuariosFacade.usuarioUnico(usuarioActual.getCedula());
            if (userC != null) {
                usuarioActual.setEstado(true);
                usuarioActual.setCargo(true);
                usuarioActual.setPaswword(validations.sha512(password));
                persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("UsuariosUpdated"));
                limpiarObjeto();
            } else {
                if ((password.equals(password2)) && (bandera == 1)) {
                    usuarioActual.setEstado(true);
                    usuarioActual.setCargo(true);
                    usuarioActual.setPaswword(validations.sha512(password));
                    persist(PersistAction.CREATE, ResourceBundle.getBundle("/mensajes").getString("UsuarioCreated"));
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cedula incorrecta"));
                }
                limpiarObjeto();
            }
        } catch (Exception ex) {
            Logger.getLogger(UsuariosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update() {
        usuarioActual.setIdpermisos(perm);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("UsuariosUpdated"));
    }

    public void resetPassword() {
        usuarioActual.setUsuario(usuarioActual.getCedula());
        usuarioActual.setPaswword(validations.sha512(usuarioActual.getCedula()));
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("PassordReset"));
        limpiarObjeto();
    }

    public void eraseLog() {
        usuarioActual.setEstado(false);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("UsuariosErased"));
        limpiarObjeto();
        inicializar();
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/mensajes").getString("UsuariosDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            limpiarObjeto();
        }
    }

    public List<Usuarios> getListUsTotal() {
        if (listaUsuarios == null) {
            listaUsuarios = getFacade().listarUsuarios();
        }
        return listaUsuarios;
    }

    public void setPerm(Permisos perm) {
        this.perm = perm;
    }

    public List<Permisos> getListPerm() {
        return listPerm;
    }

    public void setListPerm(List<Permisos> listPerm) {
        this.listPerm = listPerm;
    }

    public UsuariosController() {
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (usuarioActual != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(usuarioActual);
                } else {
                    getFacade().remove(usuarioActual);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/mensajes").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/mensajes").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Usuarios getUsuarios(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Usuarios> getItemsAvailableSelectMany() {
        return getFacade().listarUsuarios(); //findAll();
    }

    public List<Usuarios> getItemsAvailableSelectOne() {
        return getFacade().listarUsuarios();    //findAll();
    }
/////{

    public List<Permisos> getItemsAvailableSelectMany2() {
        return permisosFacade.findAll();//getFacade().findAll(); //findAll();
    }

    public List<Permisos> getItemsAvailableSelectOne2() {
        return permisosFacade.findAll();//getFacade().listarUsuarios();    //findAll();
    }

    public List<Usuarios> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuarios> Filtrar) {
        this.listaUsuarios = Filtrar;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Usuarios> getListaUsInactivos() {
        return listaUsInactivos;
    }

    public void setListaUsInactivos(List<Usuarios> listaUsInactivos) {
        this.listaUsInactivos = listaUsInactivos;
    }

    @FacesConverter(forClass = Usuarios.class)
    public static class UsuariosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsuariosController controller = (UsuariosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usuariosController");
            return controller.getUsuarios(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Usuarios) {
                Usuarios o = (Usuarios) object;
                return getStringKey(o.getIdusuarios());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Usuarios.class.getName()});
                return null;
            }
        }

    }

}
