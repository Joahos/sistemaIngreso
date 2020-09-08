package com.syscompraventa.data.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Compras.class)
public abstract class Compras_ {

	public static volatile SingularAttribute<Compras, Proveedor> idproveedor;
	public static volatile ListAttribute<Compras, DetalleCompra> detalleCompraList;
	public static volatile SingularAttribute<Compras, String> numerocompra;
	public static volatile SingularAttribute<Compras, Boolean> estado;
	public static volatile SingularAttribute<Compras, String> descuento;
	public static volatile SingularAttribute<Compras, String> totaliva;
	public static volatile SingularAttribute<Compras, String> tipopago;
	public static volatile SingularAttribute<Compras, String> total;
	public static volatile SingularAttribute<Compras, Empresa> idempresa;
	public static volatile SingularAttribute<Compras, Integer> idcompras;
	public static volatile SingularAttribute<Compras, String> subtotal;
	public static volatile SingularAttribute<Compras, String> moneda;
	public static volatile SingularAttribute<Compras, String> proveedor;
	public static volatile SingularAttribute<Compras, String> comprador;
	public static volatile SingularAttribute<Compras, String> cedulaproveedor;
	public static volatile SingularAttribute<Compras, Date> fechacompra;

}

