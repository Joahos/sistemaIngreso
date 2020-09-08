
package com.syscompraventa.business.facade;

import com.syscompraventa.data.entities.Permisos;
import com.syscompraventa.data.entities.UsuarioPermiso;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class UsuarioPermisoFacade extends AbstractFacade<UsuarioPermiso> {

    @PersistenceContext(unitName = "sysCompraVenta1.0.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioPermisoFacade() {
        super(UsuarioPermiso.class);
    }
    
    public List<UsuarioPermiso> buscarUsPerm(Permisos perm){
        System.out.println("........... Segunda...........");
        String jpql = "SELECT u FROM UsuarioPermiso u where u.idpermisos=:ver ";
        Query q = em.createQuery(jpql);
        q.setParameter("ver", perm);
        System.out.println("........... Segunda primera...........");
        return q.getResultList();
    }
    
}
