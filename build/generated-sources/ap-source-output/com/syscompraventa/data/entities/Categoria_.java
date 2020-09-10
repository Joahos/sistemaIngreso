package com.syscompraventa.data.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Categoria.class)
public abstract class Categoria_ {

	public static volatile SingularAttribute<Categoria, Boolean> estado;
	public static volatile SingularAttribute<Categoria, Usuarios> idusuarios;
	public static volatile ListAttribute<Categoria, Producto> productoList;
	public static volatile SingularAttribute<Categoria, String> categoria;
	public static volatile SingularAttribute<Categoria, Integer> idcategoria;

}

