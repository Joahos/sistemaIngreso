
package com.syscompraventa.data.entities;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "sistcompraventa.detalle_compra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleCompra.findAll", query = "SELECT d FROM DetalleCompra d")})
public class DetalleCompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iddetallecompra")
    private Integer iddetallecompra;
    @Size(max = 100)
    @Column(name = "producto")
    private String producto;
    @Size(max = 50)
    @Column(name = "presentacion")
    private String presentacion;
    @Size(max = 50)
    @Column(name = "unidad")
    private String unidad;
    @Column(name = "cantidadcompra")
    private Integer cantidadcompra;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 45)
    @Column(name = "preciocompra")
    private String preciocompra;
    @Size(max = 45)
    @Column(name = "total")
    private String total;
    @JoinColumn(name = "idcompras", referencedColumnName = "idcompras")
    @ManyToOne(optional = false)
    private Compras idcompras;
    @JoinColumn(name = "idproducto", referencedColumnName = "idproducto")
    @ManyToOne(optional = false)
    private Producto idproducto;

    public DetalleCompra() {
    }

    public DetalleCompra(Integer iddetallecompra) {
        this.iddetallecompra = iddetallecompra;
    }
/////////{

    public DetalleCompra(Integer iddetallecompra, String producto, String presentacion, String unidad, Integer cantidadcompra, 
            String preciocompra, String total, Compras idcompras, Producto idproducto) {
        this.iddetallecompra = iddetallecompra;
        this.producto = producto;
        this.presentacion = presentacion;
        this.unidad = unidad;
        this.cantidadcompra = cantidadcompra;
        this.preciocompra = preciocompra;
        this.total = total;
        this.idcompras = idcompras;
        this.idproducto = idproducto;
        
    }
    
    //}

    public Integer getIddetallecompra() {
        return iddetallecompra;
    }

    public void setIddetallecompra(Integer iddetallecompra) {
        this.iddetallecompra = iddetallecompra;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public Integer getCantidadcompra() {
        return cantidadcompra;
    }

    public void setCantidadcompra(Integer cantidadcompra) {
        this.cantidadcompra = cantidadcompra;
    }


    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getPreciocompra() {
        return preciocompra;
    }

    public void setPreciocompra(String preciocompra) {
        this.preciocompra = preciocompra;
    }
//
//    public String getDescuento() {
//        return descuento;
//    }
//
//    public void setDescuento(String descuento) {
//        this.descuento = descuento;
//    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Compras getIdcompras() {
        return idcompras;
    }

    public void setIdcompras(Compras idcompras) {
        this.idcompras = idcompras;
    }

    public Producto getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(Producto idproducto) {
        this.idproducto = idproducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iddetallecompra != null ? iddetallecompra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleCompra)) {
            return false;
        }
        DetalleCompra other = (DetalleCompra) object;
        if ((this.iddetallecompra == null && other.iddetallecompra != null) || (this.iddetallecompra != null && !this.iddetallecompra.equals(other.iddetallecompra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.syscompraventa.data.entities.DetalleCompra[ iddetallecompra=" + iddetallecompra + " ]";
    }
    
}
