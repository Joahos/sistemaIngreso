package com.syscompraventa.web.controller;

import com.syscompraventa.data.entities.Categoria;
import com.syscompraventa.web.controller.util.*;
import com.syscompraventa.business.facade.CategoriaFacade;
import com.syscompraventa.business.facade.ProductoFacade;
import com.syscompraventa.data.entities.Producto;
import com.syscompraventa.web.controller.util.JsfUtil.PersistAction;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;

@Named("categoriaController")
@ViewScoped
//@SessionScoped
public class CategoriaController implements Serializable {

    @EJB
    private ProductoFacade productoFacade;
    @EJB
    private com.syscompraventa.business.facade.CategoriaFacade ejbFacade;

    private static final Logger LOG = Logger.getLogger(CategoriaController.class.getName());
    FacesContext context = FacesContext.getCurrentInstance();

    private List<Categoria> listarCategTotal = null;
    private List<Categoria> listarCategorias;
    private List<Categoria> listarCatInactivas;
    private Categoria categoriaActual;
    private List<Producto> prodAfectado;

    public CategoriaController() {
    }

    @PostConstruct
    public void inicializar() {
        try {
            listarCategorias = ejbFacade.listarCategoria();
            listarCatInactivas = ejbFacade.listarCatInactiva();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public void limpiarObjeto() {
        try {
            categoriaActual = new Categoria();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public Categoria getCategoriaActual() {
        return categoriaActual;
    }

    public void setCategoriaActual(Categoria categoriaActual) {
        this.categoriaActual = categoriaActual;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CategoriaFacade getFacade() {
        return ejbFacade;
    }

    public Categoria prepareCreate() {
        categoriaActual = new Categoria();
        initializeEmbeddableKey();
        return categoriaActual;
    }

    public void activarCateg(Categoria cat) {
        try {
            categoriaActual = cat;
            categoriaActual.setEstado(true);
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("ActivarCateg"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error inicializar", ex);
        }
    }

    public void create() {
        categoriaActual.setEstado(true);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/mensajes").getString("CategoriaCreated"));
        if (!JsfUtil.isValidationFailed()) {
            listarCategTotal = null;    // Invalidate list of listarCategTotal to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("CategoriaUpdated"));
    }

    private Boolean enabled = false;
    

    public boolean isEnabled() {
        return enabled;
    }

    public void enableButton() {
        enabled = true;
    }

    public void disableButton() {
        enabled = false;
    }

    public void eraseLog() {
        prodAfectado = productoFacade.produdctoAfectado(categoriaActual);

        categoriaActual.setEstado(false);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("CategoriaErased"));
        try {
            for (Producto prod : prodAfectado) {
                productoFacade.desactivarProdXXGateg(prod.getIdproducto());
            }
        } catch (Exception ex) {
            Logger.getLogger(CategoriaController.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/mensajes").getString("CategoriaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            categoriaActual = null; // Remove selection
            listarCategTotal = null;    // Invalidate list of listarCategTotal to trigger re-query.
        }
    }

    public List<Categoria> getListarCategTotal() {
        if (listarCategTotal == null) {
            listarCategTotal = getFacade().findAll();
        }
        return listarCategTotal;
    }

    private void persist(PersistAction persistAction, String successMessage) {

        if (categoriaActual != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(categoriaActual);
                } else {
                    getFacade().remove(categoriaActual);
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

    public Categoria getCategoria(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Categoria> getItemsAvailableSelectMany() {
        return getFacade().listarCategoria();//findAll();
    }

    public List<Categoria> getItemsAvailableSelectOne() {
        return getFacade().listarCategoria();//.findAll();
    }

    public List<Categoria> getListarCategorias() {
        return listarCategorias;
    }

    public void setListarCategorias(List<Categoria> listarCategorias) {
        this.listarCategorias = listarCategorias;
    }

    public List<Categoria> getListarCatInactivas() {
        return listarCatInactivas;
    }

    public void setListarCatInactivas(List<Categoria> listarCatInactivas) {
        this.listarCatInactivas = listarCatInactivas;
    }

    public List<Producto> getProdAfectado() {
        return prodAfectado;
    }

    public void setProdAfectado(List<Producto> prodAfectado) {
        this.prodAfectado = prodAfectado;
    }


    @FacesConverter(forClass = Categoria.class)
    public static class CategoriaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CategoriaController controller = (CategoriaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "categoriaController");
            return controller.getCategoria(getKey(value));
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
            if (object instanceof Categoria) {
                Categoria o = (Categoria) object;
                return getStringKey(o.getIdcategoria());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Categoria.class.getName()});
                return null;
            }
        }

    }

}
