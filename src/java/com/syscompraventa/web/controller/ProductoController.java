package com.syscompraventa.web.controller;

import com.syscompraventa.data.entities.Producto;
import com.syscompraventa.web.controller.util.JsfUtil;
import com.syscompraventa.web.controller.util.JsfUtil.PersistAction;
import com.syscompraventa.business.facade.ProductoFacade;
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
import org.primefaces.model.UploadedFile;

@Named("productoController")
@ViewScoped
public class ProductoController implements Serializable {

    @EJB
    private com.syscompraventa.business.facade.ProductoFacade ejbFacade;
    private static final Logger LOG = Logger.getLogger(ProductoController.class.getName());

    private List<Producto> items = null;
    private List<Producto> listarProducto;
    private List<Producto> listarProdInactivo;
    private Producto selected;
    private UploadedFile file;
    private int stockAct;

    public ProductoController() {
    }

    @PostConstruct
    public void inicializar() {
        try {
            selected = new Producto();
            listarProducto = ejbFacade.listarProductos();
            listarProdInactivo = ejbFacade.listarProdInactivo();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public void limpiarObjeto() {
        try {
            selected = new Producto();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public void activarProducto(Producto prod) {
        try {
            selected = prod;
            selected.setEstado(true);
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("ActivarProducto"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error inicializar", ex);
        }
    }

    public Producto getSelected() {
        return selected;
    }

    public void setSelected(Producto selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ProductoFacade getFacade() {
        return ejbFacade;
    }

    public Producto prepareCreate() {
        selected = new Producto();
        initializeEmbeddableKey();
        return selected;
    }
    Producto prod;
    private String nombreProd;

    public String getNombreProd() {
        return nombreProd;
    }

    public void setNombreProd(String nombreProd) {
        this.nombreProd = nombreProd;
    }

    public void create() {
        int nuevStock;
        Float totalt;
        selected.setProducto(nombreProd);

        try {
            prod = ejbFacade.obtenerProducto(selected);

            System.out.println("..................." + prod);

            if (prod.equals(ejbFacade.obtenerProducto(selected))) {

                nuevStock = prod.getStock() + selected.getStock();

                totalt = ((prod.getStock() * Float.valueOf(prod.getPrecioventa())) + (selected.getStock() * Float.valueOf(selected.getPreciocompra()))) / nuevStock;

                selected.setProducto(nombreProd);
                selected.setStock(nuevStock);
                selected.setPrecioventa(String.valueOf(totalt));
                ejbFacade.actualizarStock(selected);

            } else {
                selected.setEstado(true);
                persist(PersistAction.CREATE, ResourceBundle.getBundle("/mensajes").getString("ProductoCreated"));
                System.out.println("Que haces aqui.............");
            }

        } catch (Exception e) {

        }
        listarProducto = ejbFacade.listarProductos();
        limpiarObjeto();

    }

    public void update() {
        try {
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("ProductoUpdated"));
        } catch (Exception ex) {
            Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        listarProducto = ejbFacade.listarProductos();
    }

    public void eraseLog() {
        selected.setEstado(false);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("ProductoErased"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/mensajes").getString("ProductoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Producto> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        FacesContext context = FacesContext.getCurrentInstance();
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
                    //   msg = cause.getLocalizedMessage();

                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Duplicado", "El producto ya existe debe actualizar"));
                }
                if (msg.length() > 0) {
                    //  JsfUtil.addErrorMessage(msg);

                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Duplicado", "El producto ya existe debe actualizar"));

                } //else {
//                 //   JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/mensajes").getString("PersistenceErrorOccured"));
//                    System.out.println("clave duplicada n1...............");
//                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Duplicado", "El producto ya existe debe actualizar"));
//                }
//            } catch (Exception ex) {

//            System.out.println("Mensaje 1" + e.getMessage());
//            System.out.println("Clase 1" + e.getClass().getName());
//
//            System.out.println("Mensaje 2 " + e.getCause().getMessage());
//            System.out.println("Clase 2 " + e.getCause().getClass().getName());
//
//            System.out.println("Mensaje 3 " + e.getCause().getCause().getMessage());
//            System.out.println("Clase 3 " + e.getCause().getCause().getClass().getName());
//             //   Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
//              //  JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/mensajes").getString("PersistenceErrorOccured"));
//                System.out.println("Clave Duplicada...................");
            }
        }
    }

    public Producto getProducto(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Producto> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Producto> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Producto> getListarProducto() {
        return listarProducto;
    }

    public void setListarProducto(List<Producto> listarProducto) {
        this.listarProducto = listarProducto;
    }

    public List<Producto> getListarProdInactivo() {
        return listarProdInactivo;
    }

    public void setListarProdInactivo(List<Producto> listarProdInactivo) {
        this.listarProdInactivo = listarProdInactivo;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    @FacesConverter(forClass = Producto.class)
    public static class ProductoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductoController controller = (ProductoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productoController");
            return controller.getProducto(getKey(value));
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
            if (object instanceof Producto) {
                Producto o = (Producto) object;
                return getStringKey(o.getIdproducto());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Producto.class.getName()});
                return null;
            }
        }

    }

}
