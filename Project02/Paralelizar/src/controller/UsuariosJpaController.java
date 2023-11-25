/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.MovimentosVenda;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.MovimentosCompra;
import model.Usuarios;

/**
 *
 * @author saulo
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) throws PreexistingEntityException, Exception {
        if (usuarios.getMovimentosVendaCollection() == null) {
            usuarios.setMovimentosVendaCollection(new ArrayList<MovimentosVenda>());
        }
        if (usuarios.getMovimentosCompraCollection() == null) {
            usuarios.setMovimentosCompraCollection(new ArrayList<MovimentosCompra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<MovimentosVenda> attachedMovimentosVendaCollection = new ArrayList<MovimentosVenda>();
            for (MovimentosVenda movimentosVendaCollectionMovimentosVendaToAttach : usuarios.getMovimentosVendaCollection()) {
                movimentosVendaCollectionMovimentosVendaToAttach = em.getReference(movimentosVendaCollectionMovimentosVendaToAttach.getClass(), movimentosVendaCollectionMovimentosVendaToAttach.getId());
                attachedMovimentosVendaCollection.add(movimentosVendaCollectionMovimentosVendaToAttach);
            }
            usuarios.setMovimentosVendaCollection(attachedMovimentosVendaCollection);
            Collection<MovimentosCompra> attachedMovimentosCompraCollection = new ArrayList<MovimentosCompra>();
            for (MovimentosCompra movimentosCompraCollectionMovimentosCompraToAttach : usuarios.getMovimentosCompraCollection()) {
                movimentosCompraCollectionMovimentosCompraToAttach = em.getReference(movimentosCompraCollectionMovimentosCompraToAttach.getClass(), movimentosCompraCollectionMovimentosCompraToAttach.getId());
                attachedMovimentosCompraCollection.add(movimentosCompraCollectionMovimentosCompraToAttach);
            }
            usuarios.setMovimentosCompraCollection(attachedMovimentosCompraCollection);
            em.persist(usuarios);
            for (MovimentosVenda movimentosVendaCollectionMovimentosVenda : usuarios.getMovimentosVendaCollection()) {
                Usuarios oldIDUsuarioOfMovimentosVendaCollectionMovimentosVenda = movimentosVendaCollectionMovimentosVenda.getIDUsuario();
                movimentosVendaCollectionMovimentosVenda.setIDUsuario(usuarios);
                movimentosVendaCollectionMovimentosVenda = em.merge(movimentosVendaCollectionMovimentosVenda);
                if (oldIDUsuarioOfMovimentosVendaCollectionMovimentosVenda != null) {
                    oldIDUsuarioOfMovimentosVendaCollectionMovimentosVenda.getMovimentosVendaCollection().remove(movimentosVendaCollectionMovimentosVenda);
                    oldIDUsuarioOfMovimentosVendaCollectionMovimentosVenda = em.merge(oldIDUsuarioOfMovimentosVendaCollectionMovimentosVenda);
                }
            }
            for (MovimentosCompra movimentosCompraCollectionMovimentosCompra : usuarios.getMovimentosCompraCollection()) {
                Usuarios oldIDUsuarioOfMovimentosCompraCollectionMovimentosCompra = movimentosCompraCollectionMovimentosCompra.getIDUsuario();
                movimentosCompraCollectionMovimentosCompra.setIDUsuario(usuarios);
                movimentosCompraCollectionMovimentosCompra = em.merge(movimentosCompraCollectionMovimentosCompra);
                if (oldIDUsuarioOfMovimentosCompraCollectionMovimentosCompra != null) {
                    oldIDUsuarioOfMovimentosCompraCollectionMovimentosCompra.getMovimentosCompraCollection().remove(movimentosCompraCollectionMovimentosCompra);
                    oldIDUsuarioOfMovimentosCompraCollectionMovimentosCompra = em.merge(oldIDUsuarioOfMovimentosCompraCollectionMovimentosCompra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarios(usuarios.getId()) != null) {
                throw new PreexistingEntityException("Usuarios " + usuarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getId());
            Collection<MovimentosVenda> movimentosVendaCollectionOld = persistentUsuarios.getMovimentosVendaCollection();
            Collection<MovimentosVenda> movimentosVendaCollectionNew = usuarios.getMovimentosVendaCollection();
            Collection<MovimentosCompra> movimentosCompraCollectionOld = persistentUsuarios.getMovimentosCompraCollection();
            Collection<MovimentosCompra> movimentosCompraCollectionNew = usuarios.getMovimentosCompraCollection();
            Collection<MovimentosVenda> attachedMovimentosVendaCollectionNew = new ArrayList<MovimentosVenda>();
            for (MovimentosVenda movimentosVendaCollectionNewMovimentosVendaToAttach : movimentosVendaCollectionNew) {
                movimentosVendaCollectionNewMovimentosVendaToAttach = em.getReference(movimentosVendaCollectionNewMovimentosVendaToAttach.getClass(), movimentosVendaCollectionNewMovimentosVendaToAttach.getId());
                attachedMovimentosVendaCollectionNew.add(movimentosVendaCollectionNewMovimentosVendaToAttach);
            }
            movimentosVendaCollectionNew = attachedMovimentosVendaCollectionNew;
            usuarios.setMovimentosVendaCollection(movimentosVendaCollectionNew);
            Collection<MovimentosCompra> attachedMovimentosCompraCollectionNew = new ArrayList<MovimentosCompra>();
            for (MovimentosCompra movimentosCompraCollectionNewMovimentosCompraToAttach : movimentosCompraCollectionNew) {
                movimentosCompraCollectionNewMovimentosCompraToAttach = em.getReference(movimentosCompraCollectionNewMovimentosCompraToAttach.getClass(), movimentosCompraCollectionNewMovimentosCompraToAttach.getId());
                attachedMovimentosCompraCollectionNew.add(movimentosCompraCollectionNewMovimentosCompraToAttach);
            }
            movimentosCompraCollectionNew = attachedMovimentosCompraCollectionNew;
            usuarios.setMovimentosCompraCollection(movimentosCompraCollectionNew);
            usuarios = em.merge(usuarios);
            for (MovimentosVenda movimentosVendaCollectionOldMovimentosVenda : movimentosVendaCollectionOld) {
                if (!movimentosVendaCollectionNew.contains(movimentosVendaCollectionOldMovimentosVenda)) {
                    movimentosVendaCollectionOldMovimentosVenda.setIDUsuario(null);
                    movimentosVendaCollectionOldMovimentosVenda = em.merge(movimentosVendaCollectionOldMovimentosVenda);
                }
            }
            for (MovimentosVenda movimentosVendaCollectionNewMovimentosVenda : movimentosVendaCollectionNew) {
                if (!movimentosVendaCollectionOld.contains(movimentosVendaCollectionNewMovimentosVenda)) {
                    Usuarios oldIDUsuarioOfMovimentosVendaCollectionNewMovimentosVenda = movimentosVendaCollectionNewMovimentosVenda.getIDUsuario();
                    movimentosVendaCollectionNewMovimentosVenda.setIDUsuario(usuarios);
                    movimentosVendaCollectionNewMovimentosVenda = em.merge(movimentosVendaCollectionNewMovimentosVenda);
                    if (oldIDUsuarioOfMovimentosVendaCollectionNewMovimentosVenda != null && !oldIDUsuarioOfMovimentosVendaCollectionNewMovimentosVenda.equals(usuarios)) {
                        oldIDUsuarioOfMovimentosVendaCollectionNewMovimentosVenda.getMovimentosVendaCollection().remove(movimentosVendaCollectionNewMovimentosVenda);
                        oldIDUsuarioOfMovimentosVendaCollectionNewMovimentosVenda = em.merge(oldIDUsuarioOfMovimentosVendaCollectionNewMovimentosVenda);
                    }
                }
            }
            for (MovimentosCompra movimentosCompraCollectionOldMovimentosCompra : movimentosCompraCollectionOld) {
                if (!movimentosCompraCollectionNew.contains(movimentosCompraCollectionOldMovimentosCompra)) {
                    movimentosCompraCollectionOldMovimentosCompra.setIDUsuario(null);
                    movimentosCompraCollectionOldMovimentosCompra = em.merge(movimentosCompraCollectionOldMovimentosCompra);
                }
            }
            for (MovimentosCompra movimentosCompraCollectionNewMovimentosCompra : movimentosCompraCollectionNew) {
                if (!movimentosCompraCollectionOld.contains(movimentosCompraCollectionNewMovimentosCompra)) {
                    Usuarios oldIDUsuarioOfMovimentosCompraCollectionNewMovimentosCompra = movimentosCompraCollectionNewMovimentosCompra.getIDUsuario();
                    movimentosCompraCollectionNewMovimentosCompra.setIDUsuario(usuarios);
                    movimentosCompraCollectionNewMovimentosCompra = em.merge(movimentosCompraCollectionNewMovimentosCompra);
                    if (oldIDUsuarioOfMovimentosCompraCollectionNewMovimentosCompra != null && !oldIDUsuarioOfMovimentosCompraCollectionNewMovimentosCompra.equals(usuarios)) {
                        oldIDUsuarioOfMovimentosCompraCollectionNewMovimentosCompra.getMovimentosCompraCollection().remove(movimentosCompraCollectionNewMovimentosCompra);
                        oldIDUsuarioOfMovimentosCompraCollectionNewMovimentosCompra = em.merge(oldIDUsuarioOfMovimentosCompraCollectionNewMovimentosCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarios.getId();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            Collection<MovimentosVenda> movimentosVendaCollection = usuarios.getMovimentosVendaCollection();
            for (MovimentosVenda movimentosVendaCollectionMovimentosVenda : movimentosVendaCollection) {
                movimentosVendaCollectionMovimentosVenda.setIDUsuario(null);
                movimentosVendaCollectionMovimentosVenda = em.merge(movimentosVendaCollectionMovimentosVenda);
            }
            Collection<MovimentosCompra> movimentosCompraCollection = usuarios.getMovimentosCompraCollection();
            for (MovimentosCompra movimentosCompraCollectionMovimentosCompra : movimentosCompraCollection) {
                movimentosCompraCollectionMovimentosCompra.setIDUsuario(null);
                movimentosCompraCollectionMovimentosCompra = em.merge(movimentosCompraCollectionMovimentosCompra);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuarios findUsuarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public Usuarios findUsuario(String login, String senha) {
    EntityManager em = getEntityManager();
    try {
        Query query = em.createQuery("SELECT u FROM Usuarios u WHERE u.login = :login AND u.senha = :senha");
        query.setParameter("login", login);
        query.setParameter("senha", senha);

        List<Usuarios> result = query.getResultList();
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null; // Retorna nulo se não encontrar um usuário com as credenciais fornecidas.
        }
    } finally {
        em.close();
    }
}

    
}
