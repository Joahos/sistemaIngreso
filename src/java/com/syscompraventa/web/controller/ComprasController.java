package com.syscompraventa.web.controller;

import com.syscompraventa.data.entities.Compras;
import com.syscompraventa.web.controller.util.JsfUtil;
import com.syscompraventa.web.controller.util.JsfUtil.PersistAction;
import com.syscompraventa.business.facade.ComprasFacade;
import com.syscompraventa.business.facade.DetalleCompraFacade;
import com.syscompraventa.business.facade.EmpresaFacade;
import com.syscompraventa.business.facade.ProductoFacade;
import com.syscompraventa.data.entities.DetalleCompra;
import com.syscompraventa.data.entities.Empresa;
import com.syscompraventa.data.entities.Producto;
import com.syscompraventa.data.entities.Proveedor;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.primefaces.event.RowEditEvent;

@Named("comprasController")
@ViewScoped
public class ComprasController implements Serializable {

    @EJB
    private com.syscompraventa.business.facade.ComprasFacade comprasFacade;
    @EJB
    private EmpresaFacade empresaFacade;
//    @EJB
//    private UsuariosFacade usuariosFacade;
    // @EJB
    //  private ProveedorFacade proveedorFacade;
    @EJB
    private ProductoFacade productoFacad;
    @EJB
    private DetalleCompraFacade detalleCompraFacad;
    private static final Logger LOG = Logger.getLogger(ComprasController.class.getName());
    //  FacesContext context = FacesContext.getCurrentInstance();
    DecimalFormat des = new DecimalFormat("#0.00");

    private List<Compras> items = null;
    private Compras compraActual, compraNumero;
    private boolean enabled;
    private Proveedor proveedorActual;
    private Producto productoActual, prodSelec;
    private Empresa empresaActual;
//    private Usuarios userLogeado;

    private long numeroCompra;
    private Float totalCompra;
    private String fechaSistema, totalCompraIMP, IVAIMP, desceuntoIMP, totalGenIMP;
    private Integer cantProducto;

    private List<DetalleCompra> listDetCompra;
    //   private List<Empresa> emplis;

    public ComprasController() {
        this.listDetCompra = new ArrayList<>();
    }

    public Empresa getEmpresaActual() {
        return empresaActual;
    }

    public void setEmpresaActual(Empresa empresaActual) {
        this.empresaActual = empresaActual;
    }

    @PostConstruct
    public void inicializar() {

        try {
            cantProducto = null;
            compraActual = new Compras();
            proveedorActual = new Proveedor();

            productoActual = new Producto();
            prodSelec = new Producto();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public void limpiarObj() {
        productoActual = null;
        fechaSistema = "";
        empresaActual = null;
        numeroCompra = 0;
        listDetCompra = null;
        proveedorActual = null;
        fechaSistema = totalCompraIMP = IVAIMP = desceuntoIMP = totalGenIMP = null;
        disableButton();
    }

    public void numeracionFactura() {

        try {
            empresaActual = empresaFacade.sacarEmpresa();
            compraNumero = comprasFacade.obtenerTotalRegistrosEnCompra();
            if (compraNumero == null) {
                numeroCompra = Long.valueOf("1");
            } else {
                compraNumero = comprasFacade.obtenerUltimoRegistro();
                numeroCompra = Long.valueOf(compraNumero.getIdcompras() + 1);
                totalCompra = new Float(0);// BigDecimal("0");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void agregarDatosProveed(Proveedor idProveed) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            proveedorActual = idProveed;
            compraActual.setIdproveedor(idProveed);//-
            // = proveedorFacade.obtenerProveedor(idProveed); para buscar
            System.out.println("..........." + compraActual.getIdproveedor().getRazonzocial());
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Proveedor agregado"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getFechaSistema() {
        Calendar fecha = new GregorianCalendar();

        int anio = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH);
        int dia = fecha.get(Calendar.DAY_OF_MONTH);

        this.fechaSistema = (dia + "/" + (mes + 1) + "/" + anio);

        return fechaSistema;
    }

    public void pedirCantidadProd(Producto idProveed) {

        prodSelec = idProveed;
    }

    public void agregarDatosProducto() {
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("Verificando si es string .............." + des.format(Float.valueOf(prodSelec.getPrecioventa()) * Float.valueOf(String.valueOf(cantProducto))));

        try {
            if (this.cantProducto == 0 || this.cantProducto == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Producto no agregado"));
            } else {
                System.out.println("viendo ..........." + prodSelec.getProducto());
                productoActual = productoFacad.obtenerProducto(prodSelec);////////////
                this.listDetCompra.add(new DetalleCompra(null, productoActual.getProducto(), productoActual.getPresentacion(), productoActual.getUnidad(), cantProducto,
                        productoActual.getPrecioventa(), des.format(Float.valueOf(prodSelec.getPrecioventa()) * Float.valueOf(String.valueOf(cantProducto))), null, productoActual));

                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Producto agregado correctamente"));
                cantProducto = null;
                calcularTotalFactura();
            }

        } catch (Exception e) {
            System.out.println(e);

//            System.out.println("Mensaje 1 "+e.getMessage());
//            System.out.println("Mensaje 1 "+e.getClass().getName());
//            
//             System.out.println("Mensaje 1 "+e.getCause().getMessage());
//             System.out.println("Mensaje 1 "+e.getCause().getClass().getName());
//             
//              System.out.println("Mensaje 1 "+e.getCause().getCause().getClass().getName());
        }
    }

    public void onRowEdit(RowEditEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Cantidad actualizada correctamente"));
        System.out.println("LLega hasta edicion de fila");

        calcularTotalFactura();
    }

    public void onRowCancel(RowEditEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelar", "No se realizo modificaciones"));
    }

    Float IVA, descuento = null, totalGeneral, totalCompraXProducto = new Float(0);

    public void calcularTotalFactura() {
        //   Float IVA, descuento = null, totalGeneral, totalCompraXProducto = new Float(0);
        try {

            for (DetalleCompra item : listDetCompra) {

                totalCompraXProducto = Float.valueOf(item.getPreciocompra()) * item.getCantidadcompra();// getPrecioVenta().multiply(new BigDecimal(item.getCantidad()));
                item.setTotal(String.valueOf(des.format(totalCompraXProducto)));

            }
            totalCompra = totalCompra + totalCompraXProducto;
            totalCompraIMP = des.format(totalCompra);
            ////

            compraActual.setTotal(des.format(totalCompra));
            IVA = (float) (totalCompra * 0.12);
            IVAIMP = des.format(IVA);

            if (totalCompra >= 0 && totalCompra < 60) {
                descuento = (totalCompra + IVA) * (float) 0.04;

            } else if (totalCompra >= 60 && totalCompra < 150) {
                descuento = (totalCompra + IVA) * (float) 0.06;

            } else if (totalCompra >= 150) {
                descuento = (totalCompra + IVA) * (float) 0.08;
            }

            desceuntoIMP = des.format(descuento);
            totalGeneral = (totalCompra + IVA) - descuento;
            totalGenIMP = des.format(totalGeneral);

        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage()));
        }
    }

    public void quitarProductoDetalleFactura(String codBarra, Integer filaSeleccionada) {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            int i = 0;
            for (DetalleCompra item : this.listDetCompra) {
                if (item.getProducto().equals(codBarra) && filaSeleccionada.equals(i)) {
                    this.listDetCompra.remove(i);
                    break;
                }
                i++;
            }
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Se retiro el producto de la factura"));

            this.calcularTotalFactura2();

        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage()));
            //  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage()));
        }
    }

    public void calcularTotalFactura2() {

        //   Float IVA, descuento = null, totalGeneral, totalCompraXProducto = new Float(0);
        try {

            for (DetalleCompra item : listDetCompra) {

                System.out.println(" viene ..............." + totalCompraXProducto);
                System.out.println("Imprimiendo total de compras desde los subidos ..................." + totalCompra);
                System.out.println("....................." + item.getTotal());
                totalCompraXProducto = Float.valueOf(item.getPreciocompra()) * item.getCantidadcompra();// getPrecioVenta().multiply(new BigDecimal(item.getCantidad()));
                item.setTotal(String.valueOf(des.format(totalCompraXProducto)));
                System.out.println(" viene .......totCampra........" + totalCompraXProducto);
                System.out.println(" viene ..............." + totalCompra);

            }
            totalCompra = totalCompra + totalCompraXProducto;

            totalCompraIMP = des.format(totalCompra);
            ////

            compraActual.setTotal(des.format(totalCompra));
            IVA = (float) (totalCompra * 0.12);
            IVAIMP = des.format(IVA);

            if (totalCompra >= 0 && totalCompra < 60) {
                descuento = (totalCompra + IVA) * (float) 0.04;

            } else if (totalCompra >= 60 && totalCompra < 150) {
                descuento = (totalCompra + IVA) * (float) 0.06;

            } else if (totalCompra >= 150) {
                descuento = (totalCompra + IVA) * (float) 0.08;
            }

            desceuntoIMP = des.format(descuento);
            totalGeneral = (totalCompra + IVA) - descuento;
            System.out.println("imprimeindo el total de descuento ................" + totalGeneral);
            totalGenIMP = des.format(totalGeneral);

        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage()));
        }
    }

    public void guardarCompra() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {

            compraActual.setNumerocompra(String.valueOf(numeroCompra));
            compraActual.setEstado(true);
            compraActual.setFechacompra(new Date());
            compraActual.setProveedor(proveedorActual.getRazonzocial());
            compraActual.setCedulaproveedor(proveedorActual.getCedula());
            compraActual.setComprador(empresaActual.getNombreempresa());
            compraActual.setIdempresa(empresaActual);//-
            compraActual.setMoneda("Dolar");
            //     compraActual.setIdusuarios(usuariosFacade.find(userLogeado.getIdusuarios()));//-
            compraActual.setSubtotal(totalCompraIMP);
            compraActual.setTotaliva(IVAIMP);
            compraActual.setDescuento(desceuntoIMP);
            compraActual.setTotal(totalGenIMP);

            persist(PersistAction.CREATE, ResourceBundle.getBundle("/mensajes").getString("ComprasCreated"));

            compraActual = comprasFacade.obtenerUltimoRegistro();

            for (DetalleCompra item : listDetCompra) {
                item.setIdcompras(compraActual);
                detalleCompraFacad.guardarVentaDetalleCompra(item);
            }
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Producto agregado correctamente"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void enableButton() {
        enabled = true;
    }

    public void disableButton() {
        enabled = false;
    }

    public Proveedor getProveedorActual() {
        return proveedorActual;
    }

    public void setProveedorActual(Proveedor proveedorActual) {
        this.proveedorActual = proveedorActual;
    }

    public long getNumeroCompra() {
        return numeroCompra;
    }

    public void setNumeroCompra(long numeroCompra) {
        this.numeroCompra = numeroCompra;
    }

    public List<DetalleCompra> getListDetCompra() {
        return listDetCompra;
    }

    public void setListDetCompra(List<DetalleCompra> listDetCompra) {
        this.listDetCompra = listDetCompra;
    }

    public String getTotalCompraIMP() {
        return totalCompraIMP;
    }

    public void setTotalCompraIMP(String totalCompraIMP) {
        this.totalCompraIMP = totalCompraIMP;
    }

    public String getIVAIMP() {
        return IVAIMP;
    }

    public void setIVAIMP(String IVAIMP) {
        this.IVAIMP = IVAIMP;
    }

    public String getTotalGenIMP() {
        return totalGenIMP;
    }

    public void setTotalGenIMP(String totalGenIMP) {
        this.totalGenIMP = totalGenIMP;
    }

    public String getDesceuntoIMP() {
        return desceuntoIMP;
    }

    public void setDesceuntoIMP(String desceuntoIMP) {
        this.desceuntoIMP = desceuntoIMP;
    }

    public Integer getCantProducto() {
        return cantProducto;
    }

    public void setCantProducto(Integer cantProducto) {
        this.cantProducto = cantProducto;
    }

    public Producto getProdSelec() {
        return prodSelec;
    }

    public void setProdSelec(Producto prodSelec) {
        this.prodSelec = prodSelec;
    }

    //////////////////
    public Compras getCompraActual() {
        return compraActual;
    }

    public void setCompraActual(Compras compraActual) {
        this.compraActual = compraActual;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ComprasFacade getFacade() {
        return comprasFacade;
    }

    public Compras prepareCreate() {
        compraActual = new Compras();
        initializeEmbeddableKey();
        return compraActual;
    }

    public void create() {

        persist(PersistAction.CREATE, ResourceBundle.getBundle("/mensajes").getString("ComprasCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }

    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("ComprasUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/mensajes").getString("ComprasDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            compraActual = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Compras> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (compraActual != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(compraActual);
                } else {
                    getFacade().remove(compraActual);
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

    public Compras getCompras(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Compras> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Compras> getItemsAvailableSelectOne() {
        return getFacade().findAll();

    }

    @FacesConverter(forClass = Compras.class)
    public static class ComprasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ComprasController controller = (ComprasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "comprasController");
            return controller.getCompras(getKey(value));
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
            if (object instanceof Compras) {
                Compras o = (Compras) object;
                return getStringKey(o.getIdcompras());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Compras.class.getName()});
                return null;
            }
        }

    }

}
