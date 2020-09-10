package com.syscompraventa.business.facade;

import com.syscompraventa.data.entities.Ventas;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class VentasFacade extends AbstractFacade<Ventas> {

    @PersistenceContext(unitName = "sysCompraVenta1.0.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VentasFacade() {
        super(Ventas.class);
    }

    public Ventas obtenerTotalRegistrosEnCompra() throws Exception {
        String jpql = "SELECT v FROM Ventas v";
        Query q = em.createQuery(jpql);
        List<Ventas> lista = q.getResultList();
        if (lista.size() > 0) {
            return (Ventas) lista.get(0);
        } else {
            return null;
        }
    }

    public Ventas obtenerUltimoRegistro() throws Exception {
        String jpql = "SELECT v FROM Ventas v ORDER BY v.idventas DESC";
        Query q = em.createQuery(jpql).setMaxResults(1);
        return (Ventas) q.getSingleResult();
    }

    public List<Ventas> listarProductosXFecha(Date date1, Date date2) {
        String jpql = "SELECT v FROM Ventas v WHERE v.fechaventa Between :dat1 And :dat2";
        Query q = em.createQuery(jpql);
        q.setParameter("dat1", date1);
        q.setParameter("dat2", date2);
        return q.getResultList();
    }

    public boolean guardarVentas(Ventas vents) throws Exception {
        em.persist(vents);
        return true;
    }

    public List<Ventas> listarProductosXMes(String mes) {
        System.out.println("LLega a facede por mes");

        String jpql = null;
        switch (mes) {
            case "Enero":
                jpql = "SELECT c FROM Ventas c WHERE c.fechaventa Between 2020-01-01 And 2020-01-31 or c.fechaventa Between 2019-01-01 And 2019-01-31";
                break;
            case "Febrero":
                System.out.println("Estamos en febrero con .." + mes);
                jpql = "SELECT c FROM Ventas c WHERE c.fechaventa Between '2020-02-01' And '2020-02-28' or c.fechaventa Between '2019-02-01' And '2019-02-28'";

                break;
            case "Marzo":
                jpql = "SELECT c FROM Ventas c WHERE c.fechaventa Between '2020-03-01' And '2020-03-31' or c.fechaventa Between '2019-03-01' And '2019-03-31'";
                break;
            case "Abril":
                jpql = "SELECT c FROM Ventas c WHERE c.fechaventa Between '2020-04-01' And '2020-04-30' or c.fechaventa Between '2019-04-01' And '2019-04-30'";
                break;
            case "Mayo":
                jpql = "SELECT c FROM Ventas c WHERE c.fechaventa Between '2020-05-01' And '2020-05-31' or c.fechaventa Between '2019-05-01' And '2019-05-31'";
                break;
            case "Junio":
                jpql = "SELECT c FROM Ventas c WHERE c.fechaventa Between '2020-06-01' And '2020-06-30' or c.fechaventa Between '2019-06-01' And '2019-06-30'";
                break;
            case "Julio":
                jpql = "SELECT c FROM Ventas c WHERE c.fechaventa Between '2020-07-01' And '2020-07-31' or c.fechaventa Between '2019-07-01' And '2019-07-31'";
                break;
            case "Agosto":
                jpql = "SELECT c FROM Ventas c WHERE c.fechaventa Between '2020-08-01' And '2020-08-31' or c.fechaventa Between '2019-08-01' And '2019-08-31'";
                break;
            case "Septiembre":
                jpql = "SELECT c FROM Ventas c WHERE c.fechaventa Between '2020-09-01' And '2020-09-30' or c.fechaventa Between '2019-09-01' And '2019-09-30'";
                break;
            case "Octubre":
                jpql = "SELECT c FROM Ventas c WHERE c.fechaventa Between '2020-10-01' And '2020-10-31' or c.fechaventa Between '2019-10-01' And '2019-10-31'";
                break;
            case "Noviembre":
                jpql = "SELECT c FROM Ventas c WHERE c.fechaventa Between '2020-11-01' And '2020-11-30' or c.fechaventa Between '2019-11-01' And '2019-11-30'";
                break;
            default:
                jpql = "SELECT c FROM Ventas c WHERE c.fechaventa Between '2020-12-01' And '2020-12-31' or c.fechaventa Between '2019-12-01' And '2019-12-31'";
                break;
        }
        Query q = em.createQuery(jpql);
        System.out.println(" ..................." + q.getResultList());
        return q.getResultList();

    }

}
