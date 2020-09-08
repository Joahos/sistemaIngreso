package com.syscompraventa.web.controller;

import com.syscompraventa.data.entities.UsuarioPermiso;
import com.syscompraventa.web.controller.util.JsfUtil;
import com.syscompraventa.web.controller.util.JsfUtil.PersistAction;
import com.syscompraventa.business.facade.UsuarioPermisoFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;

@Named("usuarioPermisoController")
@ViewScoped
public class UsuarioPermisoController implements Serializable {

    @EJB
    private com.syscompraventa.business.facade.UsuarioPermisoFacade ejbFacade;
    private static final Logger LOG = Logger.getLogger(UsuarioPermisoController.class.getName());

    private List<UsuarioPermiso> items = null;
    private UsuarioPermiso selected;

    public UsuarioPermisoController() {
    }

    public void limpiarObjeto() {
        try {
            selected = new UsuarioPermiso();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public void eraseLog() {
        
      //  persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("UsuarioPermisoUpdated"));
        limpiarObjeto();
    }

    public UsuarioPermiso getSelected() {
        return selected;
    }

    public void setSelected(UsuarioPermiso selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UsuarioPermisoFacade getFacade() {
        return ejbFacade;
    }

    public UsuarioPermiso prepareCreate() {
        selected = new UsuarioPermiso();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/mensajes").getString("UsuarioPermisoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("UsuarioPermisoUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/mensajes").getString("UsuarioPermisoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<UsuarioPermiso> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
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

    public UsuarioPermiso getUsuarioPermiso(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<UsuarioPermiso> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<UsuarioPermiso> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = UsuarioPermiso.class)
    public static class UsuarioPermisoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsuarioPermisoController controller = (UsuarioPermisoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usuarioPermisoController");
            return controller.getUsuarioPermiso(getKey(value));
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
            if (object instanceof UsuarioPermiso) {
                UsuarioPermiso o = (UsuarioPermiso) object;
                return getStringKey(o.getIdusuariopermiso());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), UsuarioPermiso.class.getName()});
                return null;
            }
        }

    }

}
