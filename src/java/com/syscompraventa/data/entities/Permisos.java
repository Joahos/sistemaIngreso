/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.syscompraventa.data.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "sistcompraventa.permisos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permisos.findAll", query = "SELECT p FROM Permisos p")})
public class Permisos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idpermisos")
    private Integer idpermisos;
    @Size(max = 100)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "idpermisos")
    private List<Usuarios> usuariosList;

    public Permisos() {
    }

    public Permisos(Integer idpermisos) {
        this.idpermisos = idpermisos;
    }

    public Integer getIdpermisos() {
        return idpermisos;
    }

    public void setIdpermisos(Integer idpermisos) {
        this.idpermisos = idpermisos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Usuarios> getUsuariosList() {
        return usuariosList;
    }

    public void setUsuariosList(List<Usuarios> usuariosList) {
        this.usuariosList = usuariosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpermisos != null ? idpermisos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permisos)) {
            return false;
        }
        Permisos other = (Permisos) object;
        if ((this.idpermisos == null && other.idpermisos != null) || (this.idpermisos != null && !this.idpermisos.equals(other.idpermisos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.syscompraventa.data.entities.Permisos[ idpermisos=" + idpermisos + " ]";
    }
    
}
