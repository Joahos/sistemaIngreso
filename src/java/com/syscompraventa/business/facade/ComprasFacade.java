package com.syscompraventa.business.facade;

import com.syscompraventa.data.entities.Compras;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ComprasFacade extends AbstractFacade<Compras> {
    
    @PersistenceContext(unitName = "sysCompraVenta1.0.0PU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public ComprasFacade() {
        super(Compras.class);
    }
    
    public Compras obtenerTotalRegistrosEnCompra() {
        String jpql = "SELECT c FROM Compras c";
        Query q = em.createQuery(jpql);
        List<Compras> lista = q.getResultList();
        if (lista.size() > 0) {
            return (Compras) lista.get(0);
        } else {
            return null;
        }
    }
    
    public Compras obtenerUltimoRegistro() throws Exception {
        String jpql = "SELECT c FROM Compras c ORDER BY c.idcompras DESC";
        Query q = em.createQuery(jpql).setMaxResults(1);
        return (Compras) q.getSingleResult();
    }
    
    public boolean guardarCompra(Compras compr) throws Exception {
        em.merge(this.find(compr));
        //  em.persist(factura);
        System.out.println("Guardando ..........." + compr);
        return true;
    }
    
}
