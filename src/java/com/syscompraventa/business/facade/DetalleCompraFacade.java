
package com.syscompraventa.business.facade;

import com.syscompraventa.data.entities.DetalleCompra;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
}
