package com.syscompraventa.data.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DetalleCompra.class)
public abstract class DetalleCompra_ {

	public static volatile SingularAttribute<DetalleCompra, String> unidad;
	public static volatile SingularAttribute<DetalleCompra, Boolean> estado;
	public static volatile SingularAttribute<DetalleCompra, String> total;
	public static volatile SingularAttribute<DetalleCompra, Compras> idcompras;
	public static volatile SingularAttribute<DetalleCompra, Integer> iddetallecompra;
	public static volatile SingularAttribute<DetalleCompra, String> presentacion;
	public static volatile SingularAttribute<DetalleCompra, String> preciocompra;
	public static volatile SingularAttribute<DetalleCompra, String> producto;
	public static volatile SingularAttribute<DetalleCompra, Integer> cantidadcompra;
	public static volatile SingularAttribute<DetalleCompra, Producto> idproducto;

}

