package com.syscompraventa.data.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Usuarios.class)
public abstract class Usuarios_ {

	public static volatile SingularAttribute<Usuarios, String> apellidos;
	public static volatile SingularAttribute<Usuarios, Boolean> estado;
	public static volatile SingularAttribute<Usuarios, Integer> idusuarios;
	public static volatile SingularAttribute<Usuarios, String> cedula;
	public static volatile SingularAttribute<Usuarios, String> direccion;
	public static volatile SingularAttribute<Usuarios, String> nombres;
	public static volatile ListAttribute<Usuarios, Cliente> clienteList;
	public static volatile ListAttribute<Usuarios, Proveedor> proveedorList;
	public static volatile SingularAttribute<Usuarios, Empresa> idempresa;
	public static volatile SingularAttribute<Usuarios, Permisos> idpermisos;
	public static volatile ListAttribute<Usuarios, Producto> productoList;
	public static volatile SingularAttribute<Usuarios, String> correo;
	public static volatile SingularAttribute<Usuarios, Date> fechaingreso;
	public static volatile SingularAttribute<Usuarios, String> usuario;
	public static volatile SingularAttribute<Usuarios, String> telefono;
	public static volatile SingularAttribute<Usuarios, Boolean> cargo;
	public static volatile SingularAttribute<Usuarios, String> paswword;
	public static volatile ListAttribute<Usuarios, Categoria> categoriaList;

}

