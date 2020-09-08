package com.syscompraventa.business.facade;

import auxiliar.validations;
import com.syscompraventa.data.entities.Usuarios;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class UsuariosFacade extends AbstractFacade<Usuarios> {

    @PersistenceContext(unitName = "sysCompraVenta1.0.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuariosFacade() {
        super(Usuarios.class);
    }

    public Usuarios Autenticar(String valUs, String valCon) throws Exception {
        String jpql = "select u from Usuarios u where u.usuario=:p1 and u.paswword=:param2 ";
        Query q = em.createQuery(jpql);
        q.setParameter("p1", valUs);
        q.setParameter("param2", valCon);
        List<Usuarios> lista = q.getResultList();
        if (lista.size() > 0) {
            return (Usuarios) lista.get(0);
        } else {
            return null;
        }
    }

    public Usuarios usuarioUnico(String ci) throws Exception {
        String jpql = "select u from Usuarios u where u.cedula=:param2 ";
        Query q = em.createQuery(jpql);
        q.setParameter("param2", ci);
        List<Usuarios> lista = q.getResultList();
        if (lista.size() > 0) {
            return (Usuarios) lista.get(0);
        } else {
            return null;
        }
    }
    
        public Usuarios CambiarUsPass (Usuarios us) throws Exception {
        String jpql = "select u from Usuarios u where u.cedula=:p1 and u.paswword=:p2 ";
        Query q = em.createQuery(jpql);
        q.setParameter("p1", us.getCedula());
        q.setParameter("p2", validations.sha512(us.getPaswword()));
        List<Usuarios> lista = q.getResultList();
        if (lista.size() > 0) {
            return (Usuarios) lista.get(0);
        } else {
            return null;
        }
    }

    

    public List<Usuarios> listarUsuarios() {
        String jpql = "SELECT u FROM Usuarios u WHERE u.estado = TRUE order by u.apellidos";
        Query q = em.createQuery(jpql);
        return q.getResultList();
    }

    public List<Usuarios> listarUsInactivos() {
        String jpql = "SELECT u FROM Usuarios u WHERE u.estado = FALSE";
        Query q = em.createQuery(jpql);
        return q.getResultList();
    }

}
