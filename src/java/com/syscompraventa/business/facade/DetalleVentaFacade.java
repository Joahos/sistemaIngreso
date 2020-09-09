package com.syscompraventa.business.facade;

import com.syscompraventa.data.entities.DetalleVenta;
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
    
    public List<DetalleVenta> listarCompraXID(Integer idCompr) throws Exception{
        String jpql = "SELECT d FROM DetalleCompra d WHERE d.idcompras.idcompras = :idComp";
        Query q = em.createQuery(jpql);
        q.setParameter("idComp", idCompr);
        return q.getResultList();
    }

}
