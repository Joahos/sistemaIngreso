
package com.syscompraventa.data.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "sistcompraventa.detalle_venta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleVenta.findAll", query = "SELECT d FROM DetalleVenta d")})
public class DetalleVenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iddetalleventa")
    private Integer iddetalleventa;
    @Size(max = 50)
    @Column(name = "unidad")
    private String unidad;
    @Size(max = 100)
    @Column(name = "producto")
    private String producto;
    @Column(name = "cantidadventa")
    private Integer cantidadventa;
    @Column(name = "fechaventa")
    @Temporal(TemporalType.DATE)
    private Date fechaventa;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 45)
    @Column(name = "precioventa")
    private String precioventa;
    @Size(max = 45)
    @Column(name = "descuento")
    private String descuento;
    @Size(max = 45)
    @Column(name = "total")
    private String total;
    @JoinColumn(name = "idproducto", referencedColumnName = "idproducto")
    @ManyToOne(optional = false)
    private Producto idproducto;
    @JoinColumn(name = "idventas", referencedColumnName = "idventas")
    @ManyToOne(optional = false)
    private Ventas idventas;

    public DetalleVenta() {
    }

    public DetalleVenta(Integer iddetalleventa, String unidad, String producto, Integer cantidadventa, Date fechaventa, String precioventa, String descuento, String total, Producto idproducto, Ventas idventas) {
        this.iddetalleventa = iddetalleventa;
        this.unidad = unidad;
        this.producto = producto;
        this.cantidadventa = cantidadventa;
        this.fechaventa = fechaventa;
        this.precioventa = precioventa;
        this.descuento = descuento;
        this.total = total;
        this.idproducto = idproducto;
        this.idventas = idventas;
    }
    
    

    public DetalleVenta(Integer iddetalleventa) {
        this.iddetalleventa = iddetalleventa;
    }

    public Integer getIddetalleventa() {
        return iddetalleventa;
    }

    public void setIddetalleventa(Integer iddetalleventa) {
        this.iddetalleventa = iddetalleventa;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }



    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Integer getCantidadventa() {
        return cantidadventa;
    }

    public void setCantidadventa(Integer cantidadventa) {
        this.cantidadventa = cantidadventa;
    }

    public Date getFechaventa() {
        return fechaventa;
    }

    public void setFechaventa(Date fechaventa) {
        this.fechaventa = fechaventa;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getPrecioventa() {
        return precioventa;
    }

    public void setPrecioventa(String precioventa) {
        this.precioventa = precioventa;
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

    public Producto getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(Producto idproducto) {
        this.idproducto = idproducto;
    }

    public Ventas getIdventas() {
        return idventas;
    }

    public void setIdventas(Ventas idventas) {
        this.idventas = idventas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iddetalleventa != null ? iddetalleventa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleVenta)) {
            return false;
        }
        DetalleVenta other = (DetalleVenta) object;
        if ((this.iddetalleventa == null && other.iddetalleventa != null) || (this.iddetalleventa != null && !this.iddetalleventa.equals(other.iddetalleventa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.syscompraventa.data.entities.DetalleVenta[ iddetalleventa=" + iddetalleventa + " ]";
    }
    
}
