package com.syscompraventa.web.controller;

import com.syscompraventa.business.facade.DetalleCompraFacade;
import com.syscompraventa.data.entities.DetalleCompra;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.PieChartModel;

@Named(value = "stdCompraController")
@ViewScoped
public class stdCompraController implements Serializable {

    @EJB
    private DetalleCompraFacade detalleCompraFacade;
    private static final Logger LOG = Logger.getLogger(ComprasController.class.getName());

    private List<DetalleCompra> listCompras, listComprasFalse;

    private PieChartModel grfTotal, grfXAnio, grfCancel;
    private BarChartModel grfBarra;

    public stdCompraController() {
    }

    @PostConstruct
    public void inicializar() {

        try {
            grfTotal = new PieChartModel();
            grfXAnio = new PieChartModel();
            grfCancel = new PieChartModel();
            listCompras = detalleCompraFacade.listarCompraStdTotal();
            listComprasFalse = detalleCompraFacade.listarCompraStdCancel();
            graficar(listCompras);
            graficarXAnio(listCompras);
            graficarCancel(listComprasFalse);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public void graficar(List<DetalleCompra> listCompras) {

        listCompras.forEach((com) -> {
            grfTotal.set(com.getProducto(), com.getCantidadcompra());
        });
        grfTotal.setLegendPosition("e");
        grfTotal.setFill(true);
        grfTotal.setShowDataLabels(true);
        grfTotal.setDiameter(150);

//        for (int i = 0; i < listCompras.size(); i++) {
//            ChartSeries seri = new BarChartSeries();
//            seri.setLabel(listCompras.get(0).getProveedor());
//            seri.set(listCompras.get(0).getProveedor(), Integer.valueOf(listCompras.get(0).getNumerocompra()));
//            grfBarra.addSeries(seri);
//        }
//        for (Compras com : listCompras) {
//            ChartSeries seri = new BarChartSeries();
//            seri.setLabel(com.getProveedor());
//            seri.set(com.getProveedor(), Integer.valueOf(com.getNumerocompra()));
//            grfBarra.addSeries(seri);
//        }
//        grfBarra.setTitle("COMPRAS");
//        grfBarra.setLegendPosition("ne");
//        Axis Xaxis = grfBarra.getAxis(AxisType.X);
//        Xaxis.setLabel("Nombres");
//        Axis Yaxis = grfBarra.getAxis(AxisType.Y);
//        Yaxis.setLabel("Compras");
//        Yaxis.setMin(1);
//        Yaxis.setMax(3500);
    }

    public void graficarXAnio(List<DetalleCompra> listCompras) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        listCompras.forEach((com) -> {
            String dr = sdf.format(com.getFechaadd());
            grfXAnio.set(dr, com.getCantidadcompra());
        });
        grfXAnio.setLegendPosition("e");
        grfXAnio.setFill(true);
        grfXAnio.setShowDataLabels(true);
        grfXAnio.setDiameter(150);

    }

    public void graficarCancel(List<DetalleCompra> listCompras) {

        listCompras.forEach((com) -> {
            grfCancel.set(com.getProducto(), com.getCantidadcompra());
        });
        
        grfCancel.setLegendPosition("e");
        grfCancel.setFill(true);
        grfCancel.setShowDataLabels(true);
        grfCancel.setDiameter(150);

    }

    public PieChartModel getGrfXAnio() {
        return grfXAnio;
    }

    public void setGrfXAnio(PieChartModel grfXAnio) {
        this.grfXAnio = grfXAnio;
    }

    public BarChartModel getGrfBarra() {
        return grfBarra;
    }

    public void setGrfBarra(BarChartModel grfBarra) {
        this.grfBarra = grfBarra;
    }

    public PieChartModel getGrfTotal() {
        return grfTotal;
    }

    public void setGrfTotal(PieChartModel grfTotal) {
        this.grfTotal = grfTotal;
    }

    public PieChartModel getGrfCancel() {
        return grfCancel;
    }

    public void setGrfCancel(PieChartModel grfCancel) {
        this.grfCancel = grfCancel;
    }

    public List<DetalleCompra> getListCompras() {
        return listCompras;
    }

    public void setListCompras(List<DetalleCompra> listCompras) {
        this.listCompras = listCompras;
    }

}
