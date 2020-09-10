package com.syscompraventa.data.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Proveedor.class)
public abstract class Proveedor_ {

	public static volatile SingularAttribute<Proveedor, Integer> idproveedor;
	public static volatile SingularAttribute<Proveedor, Date> fecha;
	public static volatile SingularAttribute<Proveedor, String> razonzocial;
	public static volatile SingularAttribute<Proveedor, Boolean> estado;
	public static volatile SingularAttribute<Proveedor, Usuarios> idusuarios;
	public static volatile SingularAttribute<Proveedor, String> cedula;
	public static volatile ListAttribute<Proveedor, Compras> comprasList;
	public static volatile SingularAttribute<Proveedor, String> correo;
	public static volatile SingularAttribute<Proveedor, String> direccion;
	public static volatile SingularAttribute<Proveedor, String> telefono;

}

