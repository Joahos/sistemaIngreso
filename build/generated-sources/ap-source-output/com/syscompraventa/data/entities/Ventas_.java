package com.syscompraventa.data.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ventas.class)
public abstract class Ventas_ {

	public static volatile SingularAttribute<Ventas, String> vendedor;
	public static volatile SingularAttribute<Ventas, Boolean> estado;
	public static volatile SingularAttribute<Ventas, String> descuento;
	public static volatile SingularAttribute<Ventas, String> totaliva;
	public static volatile SingularAttribute<Ventas, String> tipopago;
	public static volatile SingularAttribute<Ventas, Integer> idventas;
	public static volatile SingularAttribute<Ventas, Cliente> idcliente;
	public static volatile SingularAttribute<Ventas, String> cliente;
	public static volatile SingularAttribute<Ventas, String> total;
	public static volatile SingularAttribute<Ventas, Empresa> idempresa;
	public static volatile SingularAttribute<Ventas, String> cedulacliente;
	public static volatile SingularAttribute<Ventas, String> subtotal;
	public static volatile SingularAttribute<Ventas, String> moneda;
	public static volatile SingularAttribute<Ventas, Date> fechaventa;
	public static volatile SingularAttribute<Ventas, String> numeroventa;

}

