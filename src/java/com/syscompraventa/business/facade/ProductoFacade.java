package com.syscompraventa.business.facade;

import com.syscompraventa.data.entities.Categoria;
import com.syscompraventa.data.entities.Producto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ProductoFacade extends AbstractFacade<Producto> {

    @PersistenceContext(unitName = "sysCompraVenta1.0.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductoFacade() {
        super(Producto.class);
    }

    public List<Producto> listarProductos() {
        String jpql = "SELECT p FROM Producto p WHERE p.estado = TRUE";
        Query q = em.createQuery(jpql);
        return q.getResultList();
    }

    public List<Producto> listarProdInactivo() {
        String jpql = "SELECT p FROM Producto p WHERE p.estado = FALSE";
        Query q = em.createQuery(jpql);
        return q.getResultList();
    }

    public List<Producto> produdctoAfectado(Categoria cate) throws Exception {
        String jpql = "SELECT p FROM Producto p WHERE p.idcategoria =:cat1";
        Query q = em.createQuery(jpql);
        q.setParameter("cat1", cate);
        return q.getResultList();
    }

    public void desactivarProdXXGateg(int idProduct) throws Exception {
        String jpql = "update Producto p set p.estado=false where p.idproducto=:p1";
        Query q = em.createQuery(jpql);
        q.setParameter("p1", idProduct);
        q.executeUpdate();
    }

    public Boolean obtenerStock(Producto prod) throws Exception {
        int cant = 1;

        String jpql = "update Producto set stock =:cant1 where idproducto=:p1";
        Query q = em.createQuery(jpql);
        q.setParameter("p1", prod.getIdproducto());
        q.setParameter("cant1", cant);
        q.executeUpdate();
        return true;
    }
 
    public Producto obtenerProductoS(String nombreProd) throws Exception {
        String jpql = "SELECT p FROM Producto p where p.producto =:nameProd";
        Query q = em.createQuery(jpql);
        q.setParameter("nameProd", nombreProd);
        return (Producto) q.getSingleResult();
    }

    public Producto obtenerProducto(Producto nombreProd) throws Exception {
        String jpql = "SELECT p FROM Producto p where p.producto =:nameProd";
        Query q = em.createQuery(jpql);
        q.setParameter("nameProd", nombreProd.getProducto());
        return (Producto) q.getSingleResult();
    }

    public void actualizarStock(Producto prod) throws Exception {
        String jpql = "update Producto p set p.stock = :rq1, p.preciocompra = :rq2, p.precioventa = :rq3, p.estado = :rq4 where p.producto = :comp";
        Query q = em.createQuery(jpql);
        q.setParameter("rq1", prod.getStock());
        q.setParameter("rq2", prod.getPreciocompra());
        q.setParameter("rq3", prod.getPrecioventa());
        q.setParameter("rq4", prod.getEstado());
        q.setParameter("comp", prod.getProducto());
        q.executeUpdate();
    }

    public void actualizarProducto(Producto prod) throws Exception {
        String jpql = "update Producto p set p.presentacion = :rq1, p.unidad = :rq2, p.moneda = :rq3, p.stock = :rq4, p.fechavencimiento = :rq5, p.preciocompra = :rq6, p.idcategoria = :rq7, p.idusuarios = :rq8 where p.idproducto = :comp1 or p.producto = :comp2";
        Query q = em.createQuery(jpql);
        q.setParameter("rq1", prod.getPresentacion());
        q.setParameter("rq2", prod.getUnidad());
        q.setParameter("rq3", prod.getMoneda());
        q.setParameter("rq4", prod.getStock());
        q.setParameter("rq5", prod.getFechavencimiento());
        q.setParameter("rq6", prod.getPreciocompra());
        q.setParameter("rq7", prod.getIdcategoria());
        q.setParameter("rq8", prod.getIdusuarios());
        q.setParameter("comp1", prod.getIdproducto());
        q.setParameter("comp2", prod.getProducto());
        q.executeUpdate();
    }

}
