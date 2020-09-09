package com.syscompraventa.web.controller;

import com.syscompraventa.business.facade.DetalleVentaFacade;
import com.syscompraventa.business.facade.EmpresaFacade;
import com.syscompraventa.business.facade.ProductoFacade;
import com.syscompraventa.data.entities.Ventas;
import com.syscompraventa.web.controller.util.JsfUtil;
import com.syscompraventa.web.controller.util.JsfUtil.PersistAction;
import com.syscompraventa.business.facade.VentasFacade;
import com.syscompraventa.data.entities.Cliente;
import com.syscompraventa.data.entities.DetalleVenta;
import com.syscompraventa.data.entities.Empresa;
import com.syscompraventa.data.entities.Producto;

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

@Named("ventasController")
@ViewScoped
public class VentasController implements Serializable {

    @EJB
    private com.syscompraventa.business.facade.VentasFacade ventasFacade;
    @EJB
    private ProductoFacade productoFacad;
    @EJB
    private DetalleVentaFacade detalleVentaFacade;
    @EJB
    private EmpresaFacade empresaFacade;
    private static final Logger LOG = Logger.getLogger(VentasController.class.getName());
    DecimalFormat des = new DecimalFormat("#0.00");

    private List<Ventas> items;
    private List<Ventas> listVentas;
    private List<DetalleVenta> listDetVenta;

    private boolean enabled;
    private Ventas selected, compraNumero, compraActual;
    private Producto productoActual, prodSelec;
    private Cliente proveedorActual;
    private Empresa empresaActual;

    private Integer cantProducto;
    Date fechaUno, fechaDos;
    private long numeroCompra;
    private Float totalCompra;
    private String fechaSistema, totalCompraIMP, IVAIMP, desceuntoIMP, totalGenIMP, mes;

    public VentasController() {
    }

    //////////////{
    @PostConstruct
    public void inicializar() {

        try {
            totalCompra = new Float(0);
            numeroCompra = 0;
            compraNumero = new Ventas();
            compraActual = new Ventas();
            cantProducto = null;
            selected = new Ventas();
            proveedorActual = new Cliente();
            listDetVenta = new ArrayList<>();
            listVentas = new ArrayList<>();
            productoActual = new Producto();
            prodSelec = new Producto();
            empresaActual = new Empresa();
            fechaSistema = totalCompraIMP = IVAIMP = desceuntoIMP = totalGenIMP = mes = new String();

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public void limpiarObj() {
        productoActual = null;
        fechaSistema = "";
        empresaActual = null;
        numeroCompra = 0;
        listDetVenta = new ArrayList<>();
        listVentas = new ArrayList<>();
        proveedorActual = null;
        fechaSistema = totalCompraIMP = IVAIMP = desceuntoIMP = totalGenIMP = null;
        disableButton();
    }

    public void numeracionFactura() {

        try {
            empresaActual = empresaFacade.sacarEmpresa();
            compraNumero = ventasFacade.obtenerTotalRegistrosEnCompra();
            if (compraNumero == null) {
                numeroCompra = Long.valueOf("1");
            } else {
                compraNumero = ventasFacade.obtenerUltimoRegistro();
                numeroCompra = Long.valueOf(compraNumero.getIdventas() + 1);
                totalCompra = new Float(0);// BigDecimal("0");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void agregarDatosProveed(Cliente idProveed) {
        System.out.println(" ,,,,,,,,,,,,,,,,,,,,,,," + idProveed);
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            proveedorActual = idProveed;
            compraActual.setIdcliente(idProveed);//-
            // = proveedorFacade.obtenerProveedor(idProveed); para buscar
            System.out.println("..........." + compraActual.getIdcliente().getApellidocliente());
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
                productoActual = productoFacad.obtenerProducto(prodSelec);

                Float descuentoL = (Float.valueOf(productoActual.getPrecioventa()) * cantProducto) - ((Float.valueOf(productoActual.getPrecioventa()) * cantProducto) * Float.valueOf("0.01"));

                this.listDetVenta.add(new DetalleVenta(null, productoActual.getUnidad(), productoActual.getProducto(), cantProducto, new Date(), productoActual.getPrecioventa(),
                        des.format(descuentoL), des.format(Float.valueOf(productoActual.getPrecioventa()) * cantProducto), productoActual, null));

                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Producto agregado correctamente"));
                cantProducto = null;
                calcularTotalFactura();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Cantidad actualizada correctamente"));
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

            listDetVenta.stream().map((item) -> {
                totalCompraXProducto = Float.valueOf(item.getPrecioventa()) * item.getCantidadventa();// getPrecioVenta().multiply(new BigDecimal(item.getCantidad()));
                return item;
            }).forEachOrdered((item) -> {
                item.setTotal(String.valueOf(des.format(totalCompraXProducto)));
            });
            totalCompra = totalCompra + totalCompraXProducto;
            totalCompraIMP = des.format(totalCompra);
            ////

            compraActual.setTotal(des.format(totalCompra));
            IVA = (float) (totalCompra * 0.12);
            IVAIMP = des.format(IVA);

            if (totalCompra >= 0 && totalCompra < 100) {
                descuento = (totalCompra + IVA) * (float) 0.05;

            } else if (totalCompra >= 100 && totalCompra < 200) {
                descuento = (totalCompra + IVA) * (float) 0.08;

            } else if (totalCompra >= 200) {
                descuento = (totalCompra + IVA) * (float) 0.11;
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
            for (DetalleVenta item : this.listDetVenta) {
                if (item.getProducto().equals(codBarra) && filaSeleccionada.equals(i)) {
                    this.listDetVenta.remove(i);
                    break;
                }
                i++;
            }
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Se retiro el producto de la factura"));

            this.calcularTotalFactura();

        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage()));
            //  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage()));
        }
    }

    public void guardarCompra() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {

            compraActual.setNumeroventa(String.valueOf(numeroCompra));
            //    compraActual.setNumerocompra(String.valueOf(numeroCompra));
            compraActual.setEstado(true);
            compraActual.setFechaventa(new Date());
            compraActual.setCliente(proveedorActual.getApellidocliente() + " " + proveedorActual.getNombrecliente());
            compraActual.setCedulacliente(proveedorActual.getCedulacliente());
            compraActual.setVendedor(empresaActual.getNombreempresa());
            compraActual.setMoneda("Dolar");
//            compraActual.setIdempresa(empresaActual);//-
//            System.out.println(".............."+proveedorActual);
//            System.out.println(".............."+proveedorActual.getApellidocliente());
//            compraActual.setIdcliente(proveedorActual);
            //     compraActual.setIdusuarios(usuariosFacade.find(userLogeado.getIdusuarios()));//-
            compraActual.setSubtotal(totalCompraIMP);
            compraActual.setTotaliva(IVAIMP);
            compraActual.setDescuento(desceuntoIMP);
            compraActual.setTotal(totalGenIMP);

            persist(PersistAction.CREATE, ResourceBundle.getBundle("/mensajes").getString("VentasCreated"));

            compraActual = ventasFacade.obtenerUltimoRegistro();

            for (DetalleVenta item : listDetVenta) {
                item.setIdventas(compraActual);
                detalleVentaFacade.guardarVentaDetalleCompra(item);
            }
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Producto agregado correctamente"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
      public void enableButton() {
        enabled = true;
    }

    public void disableButton() {
        enabled = false;
    }

    //{
    public void obtenerProductos() {
        try {
            listDetVenta = detalleVentaFacade.listarCompraXID(compraActual.getIdventas());
        } catch (Exception ex) {
            Logger.getLogger(ComprasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void obtenerProductosXFecha() {
        listVentas = ventasFacade.listarProductosXFecha(fechaUno, fechaDos);

    }

    public void obtenerProductosXMes() {
        listVentas = ventasFacade.listarProductosXMes(mes);

    }
    //}

    public boolean isEnabled() {
        return enabled;
    }


    public Cliente getProveedorActual() {
        return proveedorActual;
    }

    public void setProveedorActual(Cliente proveedorActual) {
        this.proveedorActual = proveedorActual;
    }

    public Empresa getEmpresaActual() {
        return empresaActual;
    }

    public void setEmpresaActual(Empresa empresaActual) {
        this.empresaActual = empresaActual;
    }

    public Ventas getCompraActual() {
        return compraActual;
    }

    public void setCompraActual(Ventas compraActual) {
        this.compraActual = compraActual;
    }

    public long getNumeroCompra() {
        return numeroCompra;
    }

    public void setNumeroCompra(long numeroCompra) {
        this.numeroCompra = numeroCompra;
    }

    public List<DetalleVenta> getListDetVenta() {
        return listDetVenta;
    }

    public void setListDetVenta(List<DetalleVenta> listDetVenta) {
        this.listDetVenta = listDetVenta;
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

    public String getDesceuntoIMP() {
        return desceuntoIMP;
    }

    public void setDesceuntoIMP(String desceuntoIMP) {
        this.desceuntoIMP = desceuntoIMP;
    }

    public String getTotalGenIMP() {
        return totalGenIMP;
    }

    public void setTotalGenIMP(String totalGenIMP) {
        this.totalGenIMP = totalGenIMP;
    }

    public Integer getCantProducto() {
        return cantProducto;
    }

    public void setCantProducto(Integer cantProducto) {
        this.cantProducto = cantProducto;
    }

    public Date getFechaUno() {
        return fechaUno;
    }

    public void setFechaUno(Date fechaUno) {
        this.fechaUno = fechaUno;
    }

    public Date getFechaDos() {
        return fechaDos;
    }

    public void setFechaDos(Date fechaDos) {
        this.fechaDos = fechaDos;
    }

    public List<Ventas> getListVentas() {
        return listVentas;
    }

    public void setListVentas(List<Ventas> listVentas) {
        this.listVentas = listVentas;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
    
    

    /////////////////}
    public Ventas getSelected() {
        return selected;
    }

    public void setSelected(Ventas selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private VentasFacade getFacade() {
        return ventasFacade;
    }

    public Ventas prepareCreate() {
        selected = new Ventas();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/mensajes").getString("VentasCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("VentasUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/mensajes").getString("VentasDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Ventas> getItems() {
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
                if (cause != null || msg.length() > 0) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Error de registro"));
                    //   msg = cause.getLocalizedMessage();
                } //                if (msg.length() > 0) {
                //                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Error de registro"));
                //                    //  JsfUtil.addErrorMessage(msg);
                //                }
                else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Error de registro"));
                    //  JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/mensajes").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Error de registro"));
                //  Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                //  JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/mensajes").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Ventas getVentas(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Ventas> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Ventas> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Ventas.class)
    public static class VentasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VentasController controller = (VentasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ventasController");
            return controller.getVentas(getKey(value));
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
            if (object instanceof Ventas) {
                Ventas o = (Ventas) object;
                return getStringKey(o.getIdventas());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Ventas.class.getName()});
                return null;
            }
        }

    }

}
