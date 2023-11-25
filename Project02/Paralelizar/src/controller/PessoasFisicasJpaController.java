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
import model.Pessoas;
import model.MovimentosVenda;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.PessoasFisicas;

/**
 *
 * @author saulo
 */
public class PessoasFisicasJpaController implements Serializable {

    public PessoasFisicasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PessoasFisicas pessoasFisicas) throws PreexistingEntityException, Exception {
        if (pessoasFisicas.getMovimentosVendaCollection() == null) {
            pessoasFisicas.setMovimentosVendaCollection(new ArrayList<MovimentosVenda>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoas pessoaID = pessoasFisicas.getPessoaID();
            if (pessoaID != null) {
                pessoaID = em.getReference(pessoaID.getClass(), pessoaID.getId());
                pessoasFisicas.setPessoaID(pessoaID);
            }
            Collection<MovimentosVenda> attachedMovimentosVendaCollection = new ArrayList<MovimentosVenda>();
            for (MovimentosVenda movimentosVendaCollectionMovimentosVendaToAttach : pessoasFisicas.getMovimentosVendaCollection()) {
                movimentosVendaCollectionMovimentosVendaToAttach = em.getReference(movimentosVendaCollectionMovimentosVendaToAttach.getClass(), movimentosVendaCollectionMovimentosVendaToAttach.getId());
                attachedMovimentosVendaCollection.add(movimentosVendaCollectionMovimentosVendaToAttach);
            }
            pessoasFisicas.setMovimentosVendaCollection(attachedMovimentosVendaCollection);
            em.persist(pessoasFisicas);
            if (pessoaID != null) {
                PessoasFisicas oldPessoasFisicasOfPessoaID = pessoaID.getPessoasFisicas();
                if (oldPessoasFisicasOfPessoaID != null) {
                    oldPessoasFisicasOfPessoaID.setPessoaID(null);
                    oldPessoasFisicasOfPessoaID = em.merge(oldPessoasFisicasOfPessoaID);
                }
                pessoaID.setPessoasFisicas(pessoasFisicas);
                pessoaID = em.merge(pessoaID);
            }
            for (MovimentosVenda movimentosVendaCollectionMovimentosVenda : pessoasFisicas.getMovimentosVendaCollection()) {
                PessoasFisicas oldIDPessoaFisicaOfMovimentosVendaCollectionMovimentosVenda = movimentosVendaCollectionMovimentosVenda.getIDPessoaFisica();
                movimentosVendaCollectionMovimentosVenda.setIDPessoaFisica(pessoasFisicas);
                movimentosVendaCollectionMovimentosVenda = em.merge(movimentosVendaCollectionMovimentosVenda);
                if (oldIDPessoaFisicaOfMovimentosVendaCollectionMovimentosVenda != null) {
                    oldIDPessoaFisicaOfMovimentosVendaCollectionMovimentosVenda.getMovimentosVendaCollection().remove(movimentosVendaCollectionMovimentosVenda);
                    oldIDPessoaFisicaOfMovimentosVendaCollectionMovimentosVenda = em.merge(oldIDPessoaFisicaOfMovimentosVendaCollectionMovimentosVenda);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPessoasFisicas(pessoasFisicas.getCpf()) != null) {
                throw new PreexistingEntityException("PessoasFisicas " + pessoasFisicas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PessoasFisicas pessoasFisicas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoasFisicas persistentPessoasFisicas = em.find(PessoasFisicas.class, pessoasFisicas.getCpf());
            Pessoas pessoaIDOld = persistentPessoasFisicas.getPessoaID();
            Pessoas pessoaIDNew = pessoasFisicas.getPessoaID();
            Collection<MovimentosVenda> movimentosVendaCollectionOld = persistentPessoasFisicas.getMovimentosVendaCollection();
            Collection<MovimentosVenda> movimentosVendaCollectionNew = pessoasFisicas.getMovimentosVendaCollection();
            if (pessoaIDNew != null) {
                pessoaIDNew = em.getReference(pessoaIDNew.getClass(), pessoaIDNew.getId());
                pessoasFisicas.setPessoaID(pessoaIDNew);
            }
            Collection<MovimentosVenda> attachedMovimentosVendaCollectionNew = new ArrayList<MovimentosVenda>();
            for (MovimentosVenda movimentosVendaCollectionNewMovimentosVendaToAttach : movimentosVendaCollectionNew) {
                movimentosVendaCollectionNewMovimentosVendaToAttach = em.getReference(movimentosVendaCollectionNewMovimentosVendaToAttach.getClass(), movimentosVendaCollectionNewMovimentosVendaToAttach.getId());
                attachedMovimentosVendaCollectionNew.add(movimentosVendaCollectionNewMovimentosVendaToAttach);
            }
            movimentosVendaCollectionNew = attachedMovimentosVendaCollectionNew;
            pessoasFisicas.setMovimentosVendaCollection(movimentosVendaCollectionNew);
            pessoasFisicas = em.merge(pessoasFisicas);
            if (pessoaIDOld != null && !pessoaIDOld.equals(pessoaIDNew)) {
                pessoaIDOld.setPessoasFisicas(null);
                pessoaIDOld = em.merge(pessoaIDOld);
            }
            if (pessoaIDNew != null && !pessoaIDNew.equals(pessoaIDOld)) {
                PessoasFisicas oldPessoasFisicasOfPessoaID = pessoaIDNew.getPessoasFisicas();
                if (oldPessoasFisicasOfPessoaID != null) {
                    oldPessoasFisicasOfPessoaID.setPessoaID(null);
                    oldPessoasFisicasOfPessoaID = em.merge(oldPessoasFisicasOfPessoaID);
                }
                pessoaIDNew.setPessoasFisicas(pessoasFisicas);
                pessoaIDNew = em.merge(pessoaIDNew);
            }
            for (MovimentosVenda movimentosVendaCollectionOldMovimentosVenda : movimentosVendaCollectionOld) {
                if (!movimentosVendaCollectionNew.contains(movimentosVendaCollectionOldMovimentosVenda)) {
                    movimentosVendaCollectionOldMovimentosVenda.setIDPessoaFisica(null);
                    movimentosVendaCollectionOldMovimentosVenda = em.merge(movimentosVendaCollectionOldMovimentosVenda);
                }
            }
            for (MovimentosVenda movimentosVendaCollectionNewMovimentosVenda : movimentosVendaCollectionNew) {
                if (!movimentosVendaCollectionOld.contains(movimentosVendaCollectionNewMovimentosVenda)) {
                    PessoasFisicas oldIDPessoaFisicaOfMovimentosVendaCollectionNewMovimentosVenda = movimentosVendaCollectionNewMovimentosVenda.getIDPessoaFisica();
                    movimentosVendaCollectionNewMovimentosVenda.setIDPessoaFisica(pessoasFisicas);
                    movimentosVendaCollectionNewMovimentosVenda = em.merge(movimentosVendaCollectionNewMovimentosVenda);
                    if (oldIDPessoaFisicaOfMovimentosVendaCollectionNewMovimentosVenda != null && !oldIDPessoaFisicaOfMovimentosVendaCollectionNewMovimentosVenda.equals(pessoasFisicas)) {
                        oldIDPessoaFisicaOfMovimentosVendaCollectionNewMovimentosVenda.getMovimentosVendaCollection().remove(movimentosVendaCollectionNewMovimentosVenda);
                        oldIDPessoaFisicaOfMovimentosVendaCollectionNewMovimentosVenda = em.merge(oldIDPessoaFisicaOfMovimentosVendaCollectionNewMovimentosVenda);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pessoasFisicas.getCpf();
                if (findPessoasFisicas(id) == null) {
                    throw new NonexistentEntityException("The pessoasFisicas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoasFisicas pessoasFisicas;
            try {
                pessoasFisicas = em.getReference(PessoasFisicas.class, id);
                pessoasFisicas.getCpf();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoasFisicas with id " + id + " no longer exists.", enfe);
            }
            Pessoas pessoaID = pessoasFisicas.getPessoaID();
            if (pessoaID != null) {
                pessoaID.setPessoasFisicas(null);
                pessoaID = em.merge(pessoaID);
            }
            Collection<MovimentosVenda> movimentosVendaCollection = pessoasFisicas.getMovimentosVendaCollection();
            for (MovimentosVenda movimentosVendaCollectionMovimentosVenda : movimentosVendaCollection) {
                movimentosVendaCollectionMovimentosVenda.setIDPessoaFisica(null);
                movimentosVendaCollectionMovimentosVenda = em.merge(movimentosVendaCollectionMovimentosVenda);
            }
            em.remove(pessoasFisicas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PessoasFisicas> findPessoasFisicasEntities() {
        return findPessoasFisicasEntities(true, -1, -1);
    }

    public List<PessoasFisicas> findPessoasFisicasEntities(int maxResults, int firstResult) {
        return findPessoasFisicasEntities(false, maxResults, firstResult);
    }

    private List<PessoasFisicas> findPessoasFisicasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PessoasFisicas.class));
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

    public PessoasFisicas findPessoasFisicas(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PessoasFisicas.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoasFisicasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PessoasFisicas> rt = cq.from(PessoasFisicas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
