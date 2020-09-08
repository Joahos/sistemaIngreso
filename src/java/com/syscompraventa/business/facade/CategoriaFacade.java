
package com.syscompraventa.business.facade;

import com.syscompraventa.data.entities.Categoria;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CategoriaFacade extends AbstractFacade<Categoria> {

    @PersistenceContext(unitName = "sysCompraVenta1.0.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoriaFacade() {
        super(Categoria.class);
    }

    public List<Categoria> listarCategoria() {
        String jpql = "SELECT u FROM Categoria u WHERE u.estado = TRUE";
        Query q = em.createQuery(jpql);
        return q.getResultList();
    }
    
    public List<Categoria> listarCatInactiva() {
        String jpql = "SELECT u FROM Categoria u WHERE u.estado = FALSE";
        Query q = em.createQuery(jpql);
        return q.getResultList();
    }

}
