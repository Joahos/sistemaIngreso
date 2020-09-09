package com.syscompraventa.data.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DetalleVenta.class)
public abstract class DetalleVenta_ {

	public static volatile SingularAttribute<DetalleVenta, String> unidad;
	public static volatile SingularAttribute<DetalleVenta, Integer> iddetalleventa;
	public static volatile SingularAttribute<DetalleVenta, Boolean> estado;
	public static volatile SingularAttribute<DetalleVenta, String> total;
	public static volatile SingularAttribute<DetalleVenta, String> descuento;
	public static volatile SingularAttribute<DetalleVenta, Integer> cantidadventa;
	public static volatile SingularAttribute<DetalleVenta, String> producto;
	public static volatile SingularAttribute<DetalleVenta, String> precioventa;
	public static volatile SingularAttribute<DetalleVenta, Ventas> idventas;
	public static volatile SingularAttribute<DetalleVenta, Date> fechaventa;
	public static volatile SingularAttribute<DetalleVenta, Producto> idproducto;

}

