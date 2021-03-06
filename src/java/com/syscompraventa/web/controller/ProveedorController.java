package com.syscompraventa.web.controller;

import com.syscompraventa.data.entities.Proveedor;
import com.syscompraventa.web.controller.util.JsfUtil;
import com.syscompraventa.web.controller.util.JsfUtil.PersistAction;
import com.syscompraventa.business.facade.ProveedorFacade;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;

@Named("proveedorController")
@ViewScoped
public class ProveedorController implements Serializable {

    @EJB
    private com.syscompraventa.business.facade.ProveedorFacade ejbFacade;
    private static final Logger LOG = Logger.getLogger(ProveedorController.class.getName());

    private List<Proveedor> items = null;
    private Proveedor selected;
    private List<Proveedor> listProveedor;
    private List<Proveedor> listProveedorInact;

    public ProveedorController() {
    }

   @PostConstruct
    public void inicializar() {
        try {
            selected = new Proveedor();
            listProveedor = ejbFacade.listarProveedor();
            listProveedorInact = ejbFacade.listarProvInactiva();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public void limpiarObjeto() {
        try {
            selected = new Proveedor();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public Proveedor getSelected() {
        return selected;
    }

    public void setSelected(Proveedor selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ProveedorFacade getFacade() {
        return ejbFacade;
    }

    public Proveedor prepareCreate() {
        selected = new Proveedor();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setFecha(new Date());
        selected.setEstado(true);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/mensajes").getString("ProveedorCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        selected.setFecha(new Date());
        selected.setEstado(true);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("ProveedorUpdated"));
    }

    public void eraseLog() {
        selected.setEstado(false);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("UsuariosErased"));
        limpiarObjeto();
        inicializar();
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/mensajes").getString("ProveedorDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Proveedor> getItems() {
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

    public Proveedor getProveedor(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Proveedor> getListProveedor() {
        return listProveedor;
    }

    public void setListProveedor(List<Proveedor> listProveedor) {
        this.listProveedor = listProveedor;
    }

    public List<Proveedor> getListProveedorInact() {
        return listProveedorInact;
    }

    public void setListProveedorInact(List<Proveedor> listProveedorInact) {
        this.listProveedorInact = listProveedorInact;
    }

    public List<Proveedor> getItemsAvailableSelectMany() {
        return getFacade().listarProveedor();//.findAll();
    }

    public List<Proveedor> getItemsAvailableSelectOne() {
        return getFacade().listarProveedor();//findAll();
    }

    @FacesConverter(forClass = Proveedor.class)
    public static class ProveedorControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProveedorController controller = (ProveedorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "proveedorController");
            return controller.getProveedor(getKey(value));
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
            if (object instanceof Proveedor) {
                Proveedor o = (Proveedor) object;
                return getStringKey(o.getIdproveedor());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Proveedor.class.getName()});
                return null;
            }
        }

    }

}
