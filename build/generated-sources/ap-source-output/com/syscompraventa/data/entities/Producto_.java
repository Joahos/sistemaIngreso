package com.syscompraventa.data.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Producto.class)
public abstract class Producto_ {

	public static volatile ListAttribute<Producto, DetalleCompra> detalleCompraList;
	public static volatile SingularAttribute<Producto, Boolean> estado;
	public static volatile SingularAttribute<Producto, Usuarios> idusuarios;
	public static volatile SingularAttribute<Producto, String> presentacion;
	public static volatile SingularAttribute<Producto, String> preciocompra;
	public static volatile SingularAttribute<Producto, Categoria> idcategoria;
	public static volatile SingularAttribute<Producto, String> producto;
	public static volatile SingularAttribute<Producto, Date> fechavencimiento;
	public static volatile SingularAttribute<Producto, String> unidad;
	public static volatile ListAttribute<Producto, DetalleVenta> detalleVentaList;
	public static volatile SingularAttribute<Producto, String> moneda;
	public static volatile SingularAttribute<Producto, String> precioventa;
	public static volatile SingularAttribute<Producto, Integer> stock;
	public static volatile SingularAttribute<Producto, Integer> idproducto;

}

