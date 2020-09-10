
package com.syscompraventa.business.facade;

import com.syscompraventa.data.entities.Cliente;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ClienteFacade extends AbstractFacade<Cliente> {

    @PersistenceContext(unitName = "sysCompraVenta1.0.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClienteFacade() {
        super(Cliente.class);
    }

    public List<Cliente> listarClientes() {
        String jpql = "SELECT c FROM Cliente c WHERE c.estado = TRUE";
        Query q = em.createQuery(jpql);
        return q.getResultList();
    }
    
    public List<Cliente> listarCliInactivo() {
        String jpql = "SELECT c FROM Cliente c WHERE c.estado = FALSE";
        Query q = em.createQuery(jpql);
        return q.getResultList();
    }
    
        public Cliente obtenerCliente(Cliente idClient) {
        String jpql = "SELECT c FROM Cliente c where c.idcliente=:idCli";
        Query q = em.createQuery(jpql);
        q.setParameter("idCli", idClient.getIdcliente());
        return  (Cliente) q.getSingleResult();
    }

}
