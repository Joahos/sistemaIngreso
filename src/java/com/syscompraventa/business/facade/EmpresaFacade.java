
package com.syscompraventa.business.facade;

import com.syscompraventa.data.entities.Empresa;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class EmpresaFacade extends AbstractFacade<Empresa> {

    @PersistenceContext(unitName = "sysCompraVenta1.0.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpresaFacade() {
        super(Empresa.class);
    }
    
    public Empresa sacarEmpresa() throws Exception {
        String jpql = "SELECT e FROM Empresa e";
        Query q = em.createQuery(jpql).setMaxResults(1);
        return  (Empresa) q.getSingleResult();
    }
    
}
