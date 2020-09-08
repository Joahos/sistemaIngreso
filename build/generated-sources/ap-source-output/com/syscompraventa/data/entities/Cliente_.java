package com.syscompraventa.data.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Cliente.class)
public abstract class Cliente_ {

	public static volatile SingularAttribute<Cliente, String> telefonocliente;
	public static volatile SingularAttribute<Cliente, String> direccioncliente;
	public static volatile SingularAttribute<Cliente, Boolean> estado;
	public static volatile SingularAttribute<Cliente, Usuarios> idusuarios;
	public static volatile SingularAttribute<Cliente, String> cedulacliente;
	public static volatile SingularAttribute<Cliente, Date> fechaingreso;
	public static volatile SingularAttribute<Cliente, Integer> idcliente;
	public static volatile SingularAttribute<Cliente, String> nombrecliente;
	public static volatile SingularAttribute<Cliente, String> apellidocliente;
	public static volatile SingularAttribute<Cliente, String> correocliente;

}

