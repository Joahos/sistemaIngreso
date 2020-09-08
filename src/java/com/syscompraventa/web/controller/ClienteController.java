package com.syscompraventa.web.controller;

import com.syscompraventa.data.entities.Cliente;
import com.syscompraventa.web.controller.util.JsfUtil;
import com.syscompraventa.web.controller.util.JsfUtil.PersistAction;
import com.syscompraventa.business.facade.ClienteFacade;

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

@Named("clienteController")
@ViewScoped
public class ClienteController implements Serializable {

    @EJB
    private com.syscompraventa.business.facade.ClienteFacade ejbFacade;
    private static final Logger LOG = Logger.getLogger(ClienteController.class.getName());

    private List<Cliente> items = null;
    private List<Cliente> listaClientes;
    private List<Cliente> listaCliInactivos;
    private Cliente selected;

    public ClienteController() {
    }

    @PostConstruct
    public void inicializar() {
        try {
            listaClientes = ejbFacade.listarClientes();
            listaCliInactivos = ejbFacade.listarCliInactivo();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public void limpiarObjeto() {
        try {
            selected = new Cliente();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public void activarCliente(Cliente clie) {
        try {
            selected = clie;
            selected.setEstado(true);
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("ActivarClie"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error inicializar", ex);
        }
    }

    public Cliente getSelected() {
        return selected;
    }

    public void setSelected(Cliente selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ClienteFacade getFacade() {
        return ejbFacade;
    }

    public Cliente prepareCreate() {
        selected = new Cliente();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setEstado(true);
        selected.setFechaingreso(new Date());
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/mensajes").getString("ClienteCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        selected.setFechaingreso(new Date());
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("ClienteUpdated"));
    }

    public void eraseLog() {
        selected.setEstado(false);
        selected.setFechaingreso(new Date());
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("ClienteErased"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/mensajes").getString("ClienteDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Cliente> getItems() {
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

    public Cliente getCliente(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Cliente> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Cliente> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public List<Cliente> getListaCliInactivos() {
        return listaCliInactivos;
    }

    public void setListaCliInactivos(List<Cliente> listaCliInactivos) {
        this.listaCliInactivos = listaCliInactivos;
    }

    @FacesConverter(forClass = Cliente.class)
    public static class ClienteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClienteController controller = (ClienteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "clienteController");
            return controller.getCliente(getKey(value));
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
            if (object instanceof Cliente) {
                Cliente o = (Cliente) object;
                return getStringKey(o.getIdcliente());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Cliente.class.getName()});
                return null;
            }
        }

    }

}