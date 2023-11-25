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
import model.MovimentosCompra;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.PessoasJuridicas;

/**
 *
 * @author saulo
 */
public class PessoasJuridicasJpaController implements Serializable {

    public PessoasJuridicasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PessoasJuridicas pessoasJuridicas) throws PreexistingEntityException, Exception {
        if (pessoasJuridicas.getMovimentosCompraCollection() == null) {
            pessoasJuridicas.setMovimentosCompraCollection(new ArrayList<MovimentosCompra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoas pessoaID = pessoasJuridicas.getPessoaID();
            if (pessoaID != null) {
                pessoaID = em.getReference(pessoaID.getClass(), pessoaID.getId());
                pessoasJuridicas.setPessoaID(pessoaID);
            }
            Collection<MovimentosCompra> attachedMovimentosCompraCollection = new ArrayList<MovimentosCompra>();
            for (MovimentosCompra movimentosCompraCollectionMovimentosCompraToAttach : pessoasJuridicas.getMovimentosCompraCollection()) {
                movimentosCompraCollectionMovimentosCompraToAttach = em.getReference(movimentosCompraCollectionMovimentosCompraToAttach.getClass(), movimentosCompraCollectionMovimentosCompraToAttach.getId());
                attachedMovimentosCompraCollection.add(movimentosCompraCollectionMovimentosCompraToAttach);
            }
            pessoasJuridicas.setMovimentosCompraCollection(attachedMovimentosCompraCollection);
            em.persist(pessoasJuridicas);
            if (pessoaID != null) {
                PessoasJuridicas oldPessoasJuridicasOfPessoaID = pessoaID.getPessoasJuridicas();
                if (oldPessoasJuridicasOfPessoaID != null) {
                    oldPessoasJuridicasOfPessoaID.setPessoaID(null);
                    oldPessoasJuridicasOfPessoaID = em.merge(oldPessoasJuridicasOfPessoaID);
                }
                pessoaID.setPessoasJuridicas(pessoasJuridicas);
                pessoaID = em.merge(pessoaID);
            }
            for (MovimentosCompra movimentosCompraCollectionMovimentosCompra : pessoasJuridicas.getMovimentosCompraCollection()) {
                PessoasJuridicas oldIDPessoaJuridicaOfMovimentosCompraCollectionMovimentosCompra = movimentosCompraCollectionMovimentosCompra.getIDPessoaJuridica();
                movimentosCompraCollectionMovimentosCompra.setIDPessoaJuridica(pessoasJuridicas);
                movimentosCompraCollectionMovimentosCompra = em.merge(movimentosCompraCollectionMovimentosCompra);
                if (oldIDPessoaJuridicaOfMovimentosCompraCollectionMovimentosCompra != null) {
                    oldIDPessoaJuridicaOfMovimentosCompraCollectionMovimentosCompra.getMovimentosCompraCollection().remove(movimentosCompraCollectionMovimentosCompra);
                    oldIDPessoaJuridicaOfMovimentosCompraCollectionMovimentosCompra = em.merge(oldIDPessoaJuridicaOfMovimentosCompraCollectionMovimentosCompra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPessoasJuridicas(pessoasJuridicas.getCnpj()) != null) {
                throw new PreexistingEntityException("PessoasJuridicas " + pessoasJuridicas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PessoasJuridicas pessoasJuridicas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoasJuridicas persistentPessoasJuridicas = em.find(PessoasJuridicas.class, pessoasJuridicas.getCnpj());
            Pessoas pessoaIDOld = persistentPessoasJuridicas.getPessoaID();
            Pessoas pessoaIDNew = pessoasJuridicas.getPessoaID();
            Collection<MovimentosCompra> movimentosCompraCollectionOld = persistentPessoasJuridicas.getMovimentosCompraCollection();
            Collection<MovimentosCompra> movimentosCompraCollectionNew = pessoasJuridicas.getMovimentosCompraCollection();
            if (pessoaIDNew != null) {
                pessoaIDNew = em.getReference(pessoaIDNew.getClass(), pessoaIDNew.getId());
                pessoasJuridicas.setPessoaID(pessoaIDNew);
            }
            Collection<MovimentosCompra> attachedMovimentosCompraCollectionNew = new ArrayList<MovimentosCompra>();
            for (MovimentosCompra movimentosCompraCollectionNewMovimentosCompraToAttach : movimentosCompraCollectionNew) {
                movimentosCompraCollectionNewMovimentosCompraToAttach = em.getReference(movimentosCompraCollectionNewMovimentosCompraToAttach.getClass(), movimentosCompraCollectionNewMovimentosCompraToAttach.getId());
                attachedMovimentosCompraCollectionNew.add(movimentosCompraCollectionNewMovimentosCompraToAttach);
            }
            movimentosCompraCollectionNew = attachedMovimentosCompraCollectionNew;
            pessoasJuridicas.setMovimentosCompraCollection(movimentosCompraCollectionNew);
            pessoasJuridicas = em.merge(pessoasJuridicas);
            if (pessoaIDOld != null && !pessoaIDOld.equals(pessoaIDNew)) {
                pessoaIDOld.setPessoasJuridicas(null);
                pessoaIDOld = em.merge(pessoaIDOld);
            }
            if (pessoaIDNew != null && !pessoaIDNew.equals(pessoaIDOld)) {
                PessoasJuridicas oldPessoasJuridicasOfPessoaID = pessoaIDNew.getPessoasJuridicas();
                if (oldPessoasJuridicasOfPessoaID != null) {
                    oldPessoasJuridicasOfPessoaID.setPessoaID(null);
                    oldPessoasJuridicasOfPessoaID = em.merge(oldPessoasJuridicasOfPessoaID);
                }
                pessoaIDNew.setPessoasJuridicas(pessoasJuridicas);
                pessoaIDNew = em.merge(pessoaIDNew);
            }
            for (MovimentosCompra movimentosCompraCollectionOldMovimentosCompra : movimentosCompraCollectionOld) {
                if (!movimentosCompraCollectionNew.contains(movimentosCompraCollectionOldMovimentosCompra)) {
                    movimentosCompraCollectionOldMovimentosCompra.setIDPessoaJuridica(null);
                    movimentosCompraCollectionOldMovimentosCompra = em.merge(movimentosCompraCollectionOldMovimentosCompra);
                }
            }
            for (MovimentosCompra movimentosCompraCollectionNewMovimentosCompra : movimentosCompraCollectionNew) {
                if (!movimentosCompraCollectionOld.contains(movimentosCompraCollectionNewMovimentosCompra)) {
                    PessoasJuridicas oldIDPessoaJuridicaOfMovimentosCompraCollectionNewMovimentosCompra = movimentosCompraCollectionNewMovimentosCompra.getIDPessoaJuridica();
                    movimentosCompraCollectionNewMovimentosCompra.setIDPessoaJuridica(pessoasJuridicas);
                    movimentosCompraCollectionNewMovimentosCompra = em.merge(movimentosCompraCollectionNewMovimentosCompra);
                    if (oldIDPessoaJuridicaOfMovimentosCompraCollectionNewMovimentosCompra != null && !oldIDPessoaJuridicaOfMovimentosCompraCollectionNewMovimentosCompra.equals(pessoasJuridicas)) {
                        oldIDPessoaJuridicaOfMovimentosCompraCollectionNewMovimentosCompra.getMovimentosCompraCollection().remove(movimentosCompraCollectionNewMovimentosCompra);
                        oldIDPessoaJuridicaOfMovimentosCompraCollectionNewMovimentosCompra = em.merge(oldIDPessoaJuridicaOfMovimentosCompraCollectionNewMovimentosCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pessoasJuridicas.getCnpj();
                if (findPessoasJuridicas(id) == null) {
                    throw new NonexistentEntityException("The pessoasJuridicas with id " + id + " no longer exists.");
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
            PessoasJuridicas pessoasJuridicas;
            try {
                pessoasJuridicas = em.getReference(PessoasJuridicas.class, id);
                pessoasJuridicas.getCnpj();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoasJuridicas with id " + id + " no longer exists.", enfe);
            }
            Pessoas pessoaID = pessoasJuridicas.getPessoaID();
            if (pessoaID != null) {
                pessoaID.setPessoasJuridicas(null);
                pessoaID = em.merge(pessoaID);
            }
            Collection<MovimentosCompra> movimentosCompraCollection = pessoasJuridicas.getMovimentosCompraCollection();
            for (MovimentosCompra movimentosCompraCollectionMovimentosCompra : movimentosCompraCollection) {
                movimentosCompraCollectionMovimentosCompra.setIDPessoaJuridica(null);
                movimentosCompraCollectionMovimentosCompra = em.merge(movimentosCompraCollectionMovimentosCompra);
            }
            em.remove(pessoasJuridicas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PessoasJuridicas> findPessoasJuridicasEntities() {
        return findPessoasJuridicasEntities(true, -1, -1);
    }

    public List<PessoasJuridicas> findPessoasJuridicasEntities(int maxResults, int firstResult) {
        return findPessoasJuridicasEntities(false, maxResults, firstResult);
    }

    private List<PessoasJuridicas> findPessoasJuridicasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PessoasJuridicas.class));
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

    public PessoasJuridicas findPessoasJuridicas(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PessoasJuridicas.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoasJuridicasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PessoasJuridicas> rt = cq.from(PessoasJuridicas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
