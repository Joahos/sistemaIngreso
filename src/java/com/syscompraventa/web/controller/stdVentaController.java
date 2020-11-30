package com.syscompraventa.web.controller;

import com.syscompraventa.business.facade.DetalleVentaFacade;
import com.syscompraventa.data.entities.DetalleVenta;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.PieChartModel;

@Named(value = "stdVentaController")
@ViewScoped
public class stdVentaController implements Serializable {

    @EJB
    private DetalleVentaFacade detalleVentaFacade;
    private static final Logger LOG = Logger.getLogger(ComprasController.class.getName());

    private List<DetalleVenta> listVentas, listVentasFalse;
    private PieChartModel grfTotal, grfXAnio, grfCancel;

    public stdVentaController() {
    }

    @PostConstruct
    public void inicializar() {

        try {
            grfTotal = new PieChartModel();
            grfXAnio = new PieChartModel();
            grfCancel = new PieChartModel();
            listVentas = detalleVentaFacade.listarVentaStdTotal();
            listVentasFalse = detalleVentaFacade.listarVentaStdCancel();
            graficar(listVentas);
            graficarXAnio(listVentas);
            graficarCancel(listVentasFalse);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "error al iniciar", e);
        }
    }

    public void graficar(List<DetalleVenta> listCompras) {

        listCompras.forEach((DetalleVenta com) -> {
            grfTotal.set(com.getProducto(), com.getCantidadventa());
        });
        grfTotal.setLegendPosition("e");
        grfTotal.setFill(true);
        grfTotal.setShowDataLabels(true);
        grfTotal.setDiameter(150);

    }

    public void graficarXAnio(List<DetalleVenta> listCompras) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        listCompras.forEach((com) -> {
            String dr = sdf.format(com.getFecharemov());
            grfXAnio.set(dr, com.getCantidadventa());
        });
        grfXAnio.setLegendPosition("e");
        grfXAnio.setFill(true);
        grfXAnio.setShowDataLabels(true);
        grfXAnio.setDiameter(150);

    }

    public void graficarCancel(List<DetalleVenta> listCompras) {

        listCompras.forEach((com) -> {
            grfCancel.set(com.getProducto(), com.getCantidadventa());
        });

        grfCancel.setLegendPosition("e");
        grfCancel.setFill(true);
        grfCancel.setShowDataLabels(true);
        grfCancel.setDiameter(150);

    }

    public List<DetalleVenta> getListVentas() {
        return listVentas;
    }

    public void setListVentas(List<DetalleVenta> listVentas) {
        this.listVentas = listVentas;
    }

    public PieChartModel getGrfTotal() {
        return grfTotal;
    }

    public void setGrfTotal(PieChartModel grfTotal) {
        this.grfTotal = grfTotal;
    }

    public PieChartModel getGrfXAnio() {
        return grfXAnio;
    }

    public void setGrfXAnio(PieChartModel grfXAnio) {
        this.grfXAnio = grfXAnio;
    }

    public PieChartModel getGrfCancel() {
        return grfCancel;
    }

    public void setGrfCancel(PieChartModel grfCancel) {
        this.grfCancel = grfCancel;
    }

}
