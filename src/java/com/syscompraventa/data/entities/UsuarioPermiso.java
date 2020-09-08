
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
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "sistcompraventa.usuario_permiso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioPermiso.findAll", query = "SELECT u FROM UsuarioPermiso u")})
public class UsuarioPermiso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idusuariopermiso")
    private Integer idusuariopermiso;
    @JoinColumn(name = "idpermisos", referencedColumnName = "idpermisos")
    @ManyToOne(optional = false)
    private Permisos idpermisos;
    @JoinColumn(name = "idusuarios", referencedColumnName = "idusuarios")
    @ManyToOne(optional = false)
    private Usuarios idusuarios;

    public UsuarioPermiso() {
    }

    public UsuarioPermiso(Integer idusuariopermiso) {
        this.idusuariopermiso = idusuariopermiso;
    }

    public Integer getIdusuariopermiso() {
        return idusuariopermiso;
    }

    public void setIdusuariopermiso(Integer idusuariopermiso) {
        this.idusuariopermiso = idusuariopermiso;
    }

    public Permisos getIdpermisos() {
        return idpermisos;
    }

    public void setIdpermisos(Permisos idpermisos) {
        this.idpermisos = idpermisos;
    }

    public Usuarios getIdusuarios() {
        return idusuarios;
    }

    public void setIdusuarios(Usuarios idusuarios) {
        this.idusuarios = idusuarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idusuariopermiso != null ? idusuariopermiso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioPermiso)) {
            return false;
        }
        UsuarioPermiso other = (UsuarioPermiso) object;
        if ((this.idusuariopermiso == null && other.idusuariopermiso != null) || (this.idusuariopermiso != null && !this.idusuariopermiso.equals(other.idusuariopermiso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.syscompraventa.data.entities.UsuarioPermiso[ idusuariopermiso=" + idusuariopermiso + " ]";
    }
    
}
