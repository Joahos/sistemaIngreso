
package com.syscompraventa.data.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "sistcompraventa.compras")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Compras.findAll", query = "SELECT c FROM Compras c")})
public class Compras implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcompras")
    private Integer idcompras;
    @Column(name = "fechacompra")//
    @Temporal(TemporalType.DATE)
    private Date fechacompra;
    @Size(max = 25)
    @Column(name = "numerocompra")//
    private String numerocompra;
    @Size(max = 100)
    @Column(name = "proveedor")//
    private String proveedor;
    @Size(max = 25)
    @Column(name = "cedulaproveedor")//
    private String cedulaproveedor;
    @Size(max = 100)
    @Column(name = "comprador")//
    private String comprador;
    @Size(max = 100)
    @Column(name = "moneda")//
    private String moneda;
    @Size(max = 100)
    @Column(name = "tipopago")
    private String tipopago;
    @Column(name = "estado")//
    private Boolean estado;
    @Size(max = 45)
    @Column(name = "subtotal")//
    private String subtotal;
    @Size(max = 45)
    @Column(name = "totaliva")//
    private String totaliva;
    @Size(max = 45)
    @Column(name = "descuento")//
    private String descuento;
    @Size(max = 45)
    @Column(name = "total")//
    private String total;
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa")//
    @ManyToOne(optional = false)
    private Empresa idempresa;
    @JoinColumn(name = "idproveedor", referencedColumnName = "idproveedor")//
    @ManyToOne(optional = false)
    private Proveedor idproveedor;
//    @JoinColumn(name = "idusuarios", referencedColumnName = "idusuarios")
//    @ManyToOne(optional = false)
//    private Usuarios idusuarios;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "idcompras")
    private List<DetalleCompra> detalleCompraList;

    public Compras() {
    }

    public Compras(Integer idcompras) {
        this.idcompras = idcompras;
    }

    public Integer getIdcompras() {
        return idcompras;
    }

    public void setIdcompras(Integer idcompras) {
        this.idcompras = idcompras;
    }

    public Date getFechacompra() {
        return fechacompra;
    }

    public void setFechacompra(Date fechacompra) {
        this.fechacompra = fechacompra;
    }

    public String getNumerocompra() {
        return numerocompra;
    }

    public void setNumerocompra(String numerocompra) {
        this.numerocompra = numerocompra;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getCedulaproveedor() {
        return cedulaproveedor;
    }

    public void setCedulaproveedor(String cedulaproveedor) {
        this.cedulaproveedor = cedulaproveedor;
    }

    public String getComprador() {
        return comprador;
    }

    public void setComprador(String comprador) {
        this.comprador = comprador;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getTipopago() {
        return tipopago;
    }

    public void setTipopago(String tipopago) {
        this.tipopago = tipopago;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotaliva() {
        return totaliva;
    }

    public void setTotaliva(String totaliva) {
        this.totaliva = totaliva;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Empresa getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(Empresa idempresa) {
        this.idempresa = idempresa;
    }

    public Proveedor getIdproveedor() {
        return idproveedor;
    }

    public void setIdproveedor(Proveedor idproveedor) {
        this.idproveedor = idproveedor;
    }

//    public Usuarios getIdusuarios() {
//        return idusuarios;
//    }
//
//    public void setIdusuarios(Usuarios idusuarios) {
//        this.idusuarios = idusuarios;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcompras != null ? idcompras.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compras)) {
            return false;
        }
        Compras other = (Compras) object;
        if ((this.idcompras == null && other.idcompras != null) || (this.idcompras != null && !this.idcompras.equals(other.idcompras))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.syscompraventa.data.entities.Compras[ idcompras=" + idcompras + " ]";
    }
    
}
