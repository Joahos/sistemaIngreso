package com.syscompraventa.business.facade;

import com.syscompraventa.data.entities.Proveedor;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ProveedorFacade extends AbstractFacade<Proveedor> {

    @PersistenceContext(unitName = "sysCompraVenta1.0.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProveedorFacade() {
        super(Proveedor.class);
    }

    public List<Proveedor> listarProveedor() {
        String jpql = "SELECT p FROM Proveedor p WHERE p.estado = TRUE";
        Query q = em.createQuery(jpql);
        return q.getResultList();
    }

    public List<Proveedor> listarProvInactiva() {
        String jpql = "SELECT p FROM Proveedor p WHERE p.estado = FALSE";
        Query q = em.createQuery(jpql);
        return q.getResultList();
    }

    public Proveedor obtenerProveedor(Proveedor idProveed) throws Exception {
        System.out.println("............FACADE DE PROVEEDOR...............");
        String jpql = "SELECT p FROM Proveedor p where p.idproveedor=:idPrv";
        Query q = em.createQuery(jpql);
        q.setParameter("idPrv", idProveed.getIdproveedor());
        System.out.println(" ............"+ q.getSingleResult());

        return (Proveedor) q.getSingleResult();
    }

}
