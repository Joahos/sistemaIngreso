package com.syscompraventa.web.controller;

import com.syscompraventa.business.facade.ClienteFacade;
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
    private ClienteFacade clienteFacade;
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
    private List<Cliente> listCliente;

    private boolean enabled;
    private Ventas ventaNumero, ventaActual;
    private Producto productoActual, prodSelec;
    private Cliente clienteActual;
    private Empresa empresaActual;

    private Integer cantProducto;
    Date fechaUno, fechaDos;
    private long numeroVenta;
    private Float totalVenta;
    private String fechaSistema, totalVentaIMP, IVAIMP, desceuntoIMP, totalFacturaIMP, mes;

    public VentasController() {
    }

    //////////////{
    @PostConstruct
    public void inicializar() {

        try {
            items = ventasFacade.listarVentasActivas();
            totalVenta = new Float(0);
            numeroVenta = 0;
            ventaNumero = new Ventas();
            cantProducto = null;
            ventaActual = new Ventas();
            listDetVenta = new ArrayList<>();
            listVentas = new ArrayList<>();
            productoActual = new Producto();
            prodSelec = new Producto();
            empresaActual = new Empresa();
            listCliente = clienteFacade.listarClientes();
            fechaSistema = totalVentaIMP = IVAIMP = desceuntoIMP = totalFacturaIMP = mes = new String();

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public void limpiarObj() {
        productoActual = null;
        fechaSistema = "";
        empresaActual = null;
        numeroVenta = 0;
        listDetVenta = new ArrayList<>();
        listVentas = new ArrayList<>();
        clienteActual = null;
        fechaSistema = totalVentaIMP = IVAIMP = desceuntoIMP = totalFacturaIMP = null;
        disableButton();
    }

    public void numeracionFactura() {

        try {
            empresaActual = empresaFacade.sacarEmpresa();
            ventaActual.setIdempresa(empresaActual);
            ventaNumero = ventasFacade.obtenerTotalRegistrosEnCompra();
            if (ventaNumero == null) {
                numeroVenta = Long.valueOf("1");
            } else {
                ventaNumero = ventasFacade.obtenerUltimoRegistro();
                numeroVenta = Long.valueOf(ventaNumero.getIdventas() + 1);
                totalVenta = new Float(0);// BigDecimal("0");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addDatosCliente(Cliente addClient) {
        clienteActual = clienteFacade.obtenerCliente(addClient);
        ventaActual.setIdcliente(clienteActual);
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

        try {
            if (this.cantProducto == 0 || this.cantProducto == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Producto no agregado"));
            } else {
                productoActual = productoFacad.obtenerProducto(prodSelec);

                Float descuentoL = (Float.valueOf(productoActual.getPrecioventa()) * cantProducto) - ((Float.valueOf(productoActual.getPrecioventa()) * cantProducto) * Float.valueOf("0.01"));

                this.listDetVenta.add(new DetalleVenta(null, productoActual.getUnidad(), productoActual.getProducto(), cantProducto, productoActual.getPrecioventa(),
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
            totalVenta = totalVenta + totalCompraXProducto;
            totalVentaIMP = des.format(totalVenta);
            ////

            ventaActual.setTotal(des.format(totalVenta));
            IVA = (float) (totalVenta * 0.12);
            IVAIMP = des.format(IVA);

            if (totalVenta >= 0 && totalVenta < 100) {
                descuento = (totalVenta + IVA) * (float) 0.05;

            } else if (totalVenta >= 100 && totalVenta < 200) {
                descuento = (totalVenta + IVA) * (float) 0.08;

            } else if (totalVenta >= 200) {
                descuento = (totalVenta + IVA) * (float) 0.11;
            }

            desceuntoIMP = des.format(descuento);
            totalGeneral = (totalVenta + IVA) - descuento;
            totalFacturaIMP = des.format(totalGeneral);

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

    public void guardarVentas() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {

            ventaActual.setNumeroventa(String.valueOf(numeroVenta));
            ventaActual.setEstado(true);
            ventaActual.setFechaventa(new Date());
            ventaActual.setCliente(clienteActual.getApellidocliente());
            ventaActual.setCedulacliente(clienteActual.getCedulacliente());
            ventaActual.setVendedor(empresaActual.getNombreempresa());
            ventaActual.setMoneda("Dolar");
            ventaActual.setSubtotal(totalVentaIMP);
            ventaActual.setTotaliva(IVAIMP);
            ventaActual.setDescuento(desceuntoIMP);
            ventaActual.setTotal(totalFacturaIMP);
            persist(PersistAction.CREATE, ResourceBundle.getBundle("/mensajes").getString("VentasCreated"));
            ventaActual = ventasFacade.obtenerUltimoRegistro();

            for (DetalleVenta item : listDetVenta) {
                item.setIdventas(ventaActual);
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

    public void obtenerProductos() {
        try {
            listDetVenta = detalleVentaFacade.listarCompraXID(ventaActual.getIdventas());
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

    //{
    public void eraseLog() {
        try {
            ventaActual.setEstado(false);
            detalleVentaFacade.borradoLogXVenta(ventaActual);
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/mensajes").getString("CompraErased"));
            limpiarObj();
            inicializar();
        } catch (Exception ex) {
            Logger.getLogger(ComprasController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //}
    public boolean isEnabled() {
        return enabled;
    }

    public Cliente getClienteActual() {
        return clienteActual;
    }

    public void setClienteActual(Cliente clienteActual) {
        this.clienteActual = clienteActual;
    }

    public Empresa getEmpresaActual() {
        return empresaActual;
    }

    public void setEmpresaActual(Empresa empresaActual) {
        this.empresaActual = empresaActual;
    }

    public Ventas getVentaActual() {
        return ventaActual;
    }

    public void setVentaActual(Ventas ventaActual) {
        this.ventaActual = ventaActual;
    }

    public long getNumeroVenta() {
        return numeroVenta;
    }

    public void setNumeroVenta(long numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public List<DetalleVenta> getListDetVenta() {
        return listDetVenta;
    }

    public void setListDetVenta(List<DetalleVenta> listDetVenta) {
        this.listDetVenta = listDetVenta;
    }

    public String getTotalVentaIMP() {
        return totalVentaIMP;
    }

    public void setTotalVentaIMP(String totalVentaIMP) {
        this.totalVentaIMP = totalVentaIMP;
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

    public String getTotalFacturaIMP() {
        return totalFacturaIMP;
    }

    public void setTotalFacturaIMP(String totalFacturaIMP) {
        this.totalFacturaIMP = totalFacturaIMP;
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

    public List<Cliente> getListCliente() {
        return listCliente;
    }

    public void setListCliente(List<Cliente> listCliente) {
        this.listCliente = listCliente;
    }

    /////////////////}
    public Ventas getSelected() {
        return ventaActual;
    }

    public void setSelected(Ventas ventaActual) {
        this.ventaActual = ventaActual;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private VentasFacade getFacade() {
        return ventasFacade;
    }

    public Ventas prepareCreate() {
        ventaActual = new Ventas();
        initializeEmbeddableKey();
        return ventaActual;
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
            ventaActual = null; // Remove selection
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
        if (ventaActual != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(ventaActual);
                } else {
                    getFacade().remove(ventaActual);
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
