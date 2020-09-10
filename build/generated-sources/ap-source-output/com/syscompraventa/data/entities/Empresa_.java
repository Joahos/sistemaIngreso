package com.syscompraventa.data.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Empresa.class)
public abstract class Empresa_ {

	public static volatile ListAttribute<Empresa, Usuarios> usuariosList;
	public static volatile SingularAttribute<Empresa, Integer> idempresa;
	public static volatile SingularAttribute<Empresa, String> telefonoempresa;
	public static volatile SingularAttribute<Empresa, String> nombreempresa;
	public static volatile SingularAttribute<Empresa, String> correoempresa;
	public static volatile ListAttribute<Empresa, Compras> comprasList;
	public static volatile ListAttribute<Empresa, Ventas> ventasList;
	public static volatile SingularAttribute<Empresa, String> direccionempresa;
	public static volatile SingularAttribute<Empresa, String> cedulaempresa;

}

