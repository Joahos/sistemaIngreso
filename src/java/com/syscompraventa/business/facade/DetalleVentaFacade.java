package com.syscompraventa.business.facade;

import com.syscompraventa.data.entities.DetalleVenta;
import com.syscompraventa.data.entities.Ventas;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class DetalleVentaFacade extends AbstractFacade<DetalleVenta> {

    @PersistenceContext(unitName = "sysCompraVenta1.0.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetalleVentaFacade() {
        super(DetalleVenta.class);
    }

    public boolean guardarVentaDetalleCompra(DetalleVenta detallefactura) throws Exception {
        em.persist(detallefactura);
        return true;
    }

    public List<DetalleVenta> listarCompraXID(Integer idCompr) throws Exception {
        String jpql = "SELECT d FROM DetalleVenta d WHERE d.idventas.idventas = :idComp";
        Query q = em.createQuery(jpql);
        q.setParameter("idComp", idCompr);
        return q.getResultList();
    }

    public void borradoLogXVenta(Ventas idProduct) throws Exception {
        String jpql = "update DetalleVenta d set d.estado=false where d.idventas=:p1";
        Query q = em.createQuery(jpql);
        q.setParameter("p1", idProduct);
        q.executeUpdate();
    }

    public List<DetalleVenta> listarVentaStdTotal() throws Exception {
        String jpql = "SELECT d FROM DetalleVenta d WHERE d.estado = TRUE";
        Query q = em.createQuery(jpql);
        return q.getResultList();
    }

    public List<DetalleVenta> listarVentaStdCancel() throws Exception {
        String jpql = "SELECT d FROM DetalleVenta d WHERE d.estado = FALSE";
        Query q = em.createQuery(jpql);
        return q.getResultList();
    }

}
