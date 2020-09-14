package com.syscompraventa.business.facade;

import com.syscompraventa.data.entities.Compras;
import com.syscompraventa.data.entities.DetalleCompra;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class DetalleCompraFacade extends AbstractFacade<DetalleCompra> {

    @PersistenceContext(unitName = "sysCompraVenta1.0.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetalleCompraFacade() {
        super(DetalleCompra.class);
    }

    public boolean guardarVentaDetalleCompra(DetalleCompra detallefactura) throws Exception {
        em.persist(detallefactura);
        return true;
    }

    public boolean listarCompraXID(Compras detallefactura) throws Exception {
        em.persist(detallefactura);
        return true;
    }

    public List<DetalleCompra> listarCompraXID(Integer idCompr) throws Exception {
        String jpql = "SELECT d FROM DetalleCompra d WHERE d.idcompras.idcompras = :idComp";
        Query q = em.createQuery(jpql);
        q.setParameter("idComp", idCompr);
        return q.getResultList();
    }
//
//    public void borradoLogXCompra(Compras idProduct) throws Exception {
//        String jpql = "update DetalleCompra d set d.estado=false where d.idcompras=:p1";
//        Query q = em.createQuery(jpql);
//        q.setParameter("p1", idProduct);
//        q.executeUpdate();
//    }

}
