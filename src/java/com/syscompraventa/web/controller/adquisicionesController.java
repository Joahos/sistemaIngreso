package com.syscompraventa.web.controller;

import com.syscompraventa.business.facade.ComprasFacade;
import com.syscompraventa.business.facade.DetalleCompraFacade;
import com.syscompraventa.business.facade.EmpresaFacade;
import com.syscompraventa.business.facade.ProductoFacade;
import com.syscompraventa.business.facade.ProveedorFacade;
import com.syscompraventa.business.facade.UsuariosFacade;
import com.syscompraventa.data.entities.Compras;
import com.syscompraventa.data.entities.DetalleCompra;
import com.syscompraventa.data.entities.Empresa;
import com.syscompraventa.data.entities.Producto;
import com.syscompraventa.data.entities.Proveedor;
import com.syscompraventa.data.entities.Usuarios;
import com.syscompraventa.web.controller.util.JsfUtil;
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
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.RowEditEvent;

@Named(value = "adqController")
@ViewScoped
public class adquisicionesController implements Serializable {

    @EJB
    private EmpresaFacade empresaFacade;
    @EJB
    private UsuariosFacade usuariosFacade;
    @EJB
    private ProveedorFacade proveedorFacade;
    @EJB
    private ComprasFacade comprasFacade;
    @EJB
    private ProductoFacade productoFacad;
    @EJB
    private DetalleCompraFacade detalleCompraFacad;

    private static final Logger LOG = Logger.getLogger(adquisicionesController.class.getName());

    private boolean enabled;
    private Compras compraActual;
    private Proveedor proveedorActual;
    private Producto productoActual, prodSelec;
    private Empresa empresaActual;
    private Usuarios userLogeado;

    private long numeroCompra;
    private Float totalCompra;
    private String fechaSistema, totalCompraIMP, IVAIMP, desceuntoIMP, totalGenIMP;
    private Integer cantProducto;

    private List<DetalleCompra> listDetCompra;
    private List<Empresa> emplis;

//        //Llamar al bean loginBean.
//    @ManagedProperty("#{loginController}")
//    private loginController lBean;
//
//    public loginController getlBean() {
//        return lBean;
//    }
//
//    public void setlBean(loginController lBean) {
//        this.lBean = lBean;
//    }

    public adquisicionesController() {
        //    this.compraActual = new Compras();
        this.listDetCompra = new ArrayList<>();
    }

    @PostConstruct
    public void inicializar() {
        cantProducto = null;
        emplis = empresaFacade.findAll();
        try {
            compraActual = new Compras();
            proveedorActual = new Proveedor();

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public void numeracionFactura() {
      //  System.out.println("imprimiendo listas " + lBean.getUsuarioLogeado());
        

        try {
            compraActual = comprasFacade.obtenerTotalRegistrosEnCompra();
            if (compraActual == null) {
                numeroCompra = Long.valueOf("1");
            } else {
                compraActual = comprasFacade.obtenerUltimoRegistro();
                numeroCompra = Long.valueOf(compraActual.getIdcompras() + 1);
                totalCompra = new Float(0);// BigDecimal("0");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void agregarDatosProveed(Proveedor idProveed) {
        try {

            proveedorActual = proveedorFacade.obtenerProveedor(idProveed);
            System.out.println(" ......." + proveedorActual.getCedula());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Proveedor agregado"));
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

    FacesContext context = FacesContext.getCurrentInstance();
    DecimalFormat des = new DecimalFormat("#0.00");

    public void agregarDatosProducto() {

        System.out.println("Verificando si es string .............." + des.format(Float.valueOf(prodSelec.getPrecioventa()) * Float.valueOf(String.valueOf(cantProducto))));
        try {
            if (this.cantProducto == 0 || this.cantProducto == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Producto no agregado"));
            } else {
                productoActual = productoFacad.obtenerProducto(prodSelec);
                this.listDetCompra.add(new DetalleCompra(null, productoActual.getProducto(), productoActual.getPresentacion(), productoActual.getUnidad(), cantProducto,
                        productoActual.getPrecioventa(), des.format(Float.valueOf(prodSelec.getPrecioventa()) * Float.valueOf(String.valueOf(cantProducto))), null, null));
                //  context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Producto agregado correctamente"));
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
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Cantidad actualizada correctamente"));
        System.out.println("LLega hasta edicion de fila");

        //     this.calcularTotalFactura();
    }

    public void onRowCancel(RowEditEvent event) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelar", "No se realizo modificaciones"));
    }

    public void calcularTotalFactura() {
        Float IVA, descuento = null, totalGeneral;
        try {

            for (DetalleCompra item : listDetCompra) {

                Float totalCompraXProducto = Float.valueOf(item.getPreciocompra()) * item.getCantidadcompra();// getPrecioVenta().multiply(new BigDecimal(item.getCantidad()));
                item.setTotal(String.valueOf(des.format(totalCompraXProducto)));

                totalCompra = totalCompra + totalCompraXProducto;
                totalCompraIMP = des.format(totalCompra);
            }
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

    public void guardarCompra() {

        try {

            String a = String.valueOf(numeroCompra);
            System.out.println("................."+a);
            compraActual.setNumerocompra(String.valueOf(numeroCompra));
            compraActual.setEstado(true);
            compraActual.setFechacompra(new Date());
            compraActual.setIdempresa(emplis.get(0));//-
            compraActual.setIdproveedor(proveedorActual);//-
            compraActual.setProveedor(proveedorActual.getRazonzocial());
            compraActual.setCedulaproveedor(proveedorActual.getCedula());
            compraActual.setComprador(emplis.get(0).getNombreempresa());
            compraActual.setMoneda("Dolar");
       //     compraActual.setIdusuarios(usuariosFacade.find(userLogeado.getIdusuarios()));//-
            compraActual.setSubtotal(totalCompraIMP);
            compraActual.setTotaliva(IVAIMP);
            compraActual.setDescuento(desceuntoIMP);
            compraActual.setTotal(totalGenIMP);

            System.out.println("Pasa los guardados ");

          //  persist(PersistAction.CREATE, ResourceBundle.getBundle("/mensajes").getString("CompraCeado"));

               comprasFacade.guardarCompra(compraActual);
            compraActual = comprasFacade.obtenerUltimoRegistro();

            for (DetalleCompra item : listDetCompra) {

                productoActual = productoFacad.obtenerProducto(prodSelec);
                item.setIdcompras(compraActual);
                item.setIdproducto(productoActual);
                detalleCompraFacad.guardarVentaDetalleCompra(item);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
///


    ////////////////
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

    public String getTotalCompraIMP() {
        return totalCompraIMP;
    }

    public void setTotalCompraIMP(String totalCompraIMP) {
        this.totalCompraIMP = totalCompraIMP;
    }

    public long getNumeroCompra() {
        return numeroCompra;
    }

    public void setNumeroCompra(long numeroCompra) {
        this.numeroCompra = numeroCompra;
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

    public List<DetalleCompra> getListDetCompra() {
        return listDetCompra;
    }

    public void setListDetCompra(List<DetalleCompra> listDetCompra) {
        this.listDetCompra = listDetCompra;
    }

    public Compras getCompraActual() {
        return compraActual;
    }

    public void setCompraActual(Compras compraActual) {
        this.compraActual = compraActual;
    }

    public Proveedor getProveedorActual() {
        return proveedorActual;
    }

    public void setProveedorActual(Proveedor proveedorActual) {
        this.proveedorActual = proveedorActual;
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

}
