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
        return true;
    }

    public List<Compras> listarProductosXFecha(Date date1, Date date2) {
        String jpql = "SELECT c FROM Compras c WHERE c.fechacompra Between :dat1 And :dat2";
        Query q = em.createQuery(jpql);
        q.setParameter("dat1", date1);
        q.setParameter("dat2", date2);
        return q.getResultList();
    }

    public List<Compras> listarProductosXMes(String mes) {

        String jpql = null;
        switch (mes) {
            case "Enero":
                jpql = "SELECT c FROM Compras c WHERE c.fechacompra Between 2020-01-01 And 2020-01-31 or c.fechacompra Between 2019-01-01 And 2019-01-31";
                break;
            case "Febrero":
                System.out.println("Estamos en febrero con .." + mes);
                jpql = "SELECT c FROM Compras c WHERE c.fechacompra Between '2020-02-01' And '2020-02-28' or c.fechacompra Between '2019-02-01' And '2019-02-28'";

                break;
            case "Marzo":
                jpql = "SELECT c FROM Compras c WHERE c.fechacompra Between '2020-03-01' And '2020-03-31' or c.fechacompra Between '2019-03-01' And '2019-03-31'";
                break;
            case "Abril":
                jpql = "SELECT c FROM Compras c WHERE c.fechacompra Between '2020-04-01' And '2020-04-30' or c.fechacompra Between '2019-04-01' And '2019-04-30'";
                break;
            case "Mayo":
                jpql = "SELECT c FROM Compras c WHERE c.fechacompra Between '2020-05-01' And '2020-05-31' or c.fechacompra Between '2019-05-01' And '2019-05-31'";
                break;
            case "Junio":
                jpql = "SELECT c FROM Compras c WHERE c.fechacompra Between '2020-06-01' And '2020-06-30' or c.fechacompra Between '2019-06-01' And '2019-06-30'";
                break;
            case "Julio":
                jpql = "SELECT c FROM Compras c WHERE c.fechacompra Between '2020-07-01' And '2020-07-31' or c.fechacompra Between '2019-07-01' And '2019-07-31'";
                break;
            case "Agosto":
                jpql = "SELECT c FROM Compras c WHERE c.fechacompra Between '2020-08-01' And '2020-08-31' or c.fechacompra Between '2019-08-01' And '2019-08-31'";
                break;
            case "Septiembre":
                jpql = "SELECT c FROM Compras c WHERE c.fechacompra Between '2020-09-01' And '2020-09-30' or c.fechacompra Between '2019-09-01' And '2019-09-30'";
                break;
            case "Octubre":
                jpql = "SELECT c FROM Compras c WHERE c.fechacompra Between '2020-10-01' And '2020-10-31' or c.fechacompra Between '2019-10-01' And '2019-10-31'";
                break;
            case "Noviembre":
                jpql = "SELECT c FROM Compras c WHERE c.fechacompra Between '2020-11-01' And '2020-11-30' or c.fechacompra Between '2019-11-01' And '2019-11-30'";
                break;
            default:
                jpql = "SELECT c FROM Compras c WHERE c.fechacompra Between '2020-12-01' And '2020-12-31' or c.fechacompra Between '2019-12-01' And '2019-12-31'";
                break;
        }
        Query q = em.createQuery(jpql);
        return q.getResultList();

    }

}
