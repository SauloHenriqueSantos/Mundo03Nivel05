/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Pessoas;
import model.PessoasJuridicas;
import model.PessoasFisicas;

/**
 *
 * @author saulo
 */
public class PessoasJpaController implements Serializable {

    public PessoasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pessoas pessoas) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoasJuridicas pessoasJuridicas = pessoas.getPessoasJuridicas();
            if (pessoasJuridicas != null) {
                pessoasJuridicas = em.getReference(pessoasJuridicas.getClass(), pessoasJuridicas.getCnpj());
                pessoas.setPessoasJuridicas(pessoasJuridicas);
            }
            PessoasFisicas pessoasFisicas = pessoas.getPessoasFisicas();
            if (pessoasFisicas != null) {
                pessoasFisicas = em.getReference(pessoasFisicas.getClass(), pessoasFisicas.getCpf());
                pessoas.setPessoasFisicas(pessoasFisicas);
            }
            em.persist(pessoas);
            if (pessoasJuridicas != null) {
                Pessoas oldPessoaIDOfPessoasJuridicas = pessoasJuridicas.getPessoaID();
                if (oldPessoaIDOfPessoasJuridicas != null) {
                    oldPessoaIDOfPessoasJuridicas.setPessoasJuridicas(null);
                    oldPessoaIDOfPessoasJuridicas = em.merge(oldPessoaIDOfPessoasJuridicas);
                }
                pessoasJuridicas.setPessoaID(pessoas);
                pessoasJuridicas = em.merge(pessoasJuridicas);
            }
            if (pessoasFisicas != null) {
                Pessoas oldPessoaIDOfPessoasFisicas = pessoasFisicas.getPessoaID();
                if (oldPessoaIDOfPessoasFisicas != null) {
                    oldPessoaIDOfPessoasFisicas.setPessoasFisicas(null);
                    oldPessoaIDOfPessoasFisicas = em.merge(oldPessoaIDOfPessoasFisicas);
                }
                pessoasFisicas.setPessoaID(pessoas);
                pessoasFisicas = em.merge(pessoasFisicas);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPessoas(pessoas.getId()) != null) {
                throw new PreexistingEntityException("Pessoas " + pessoas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pessoas pessoas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoas persistentPessoas = em.find(Pessoas.class, pessoas.getId());
            PessoasJuridicas pessoasJuridicasOld = persistentPessoas.getPessoasJuridicas();
            PessoasJuridicas pessoasJuridicasNew = pessoas.getPessoasJuridicas();
            PessoasFisicas pessoasFisicasOld = persistentPessoas.getPessoasFisicas();
            PessoasFisicas pessoasFisicasNew = pessoas.getPessoasFisicas();
            if (pessoasJuridicasNew != null) {
                pessoasJuridicasNew = em.getReference(pessoasJuridicasNew.getClass(), pessoasJuridicasNew.getCnpj());
                pessoas.setPessoasJuridicas(pessoasJuridicasNew);
            }
            if (pessoasFisicasNew != null) {
                pessoasFisicasNew = em.getReference(pessoasFisicasNew.getClass(), pessoasFisicasNew.getCpf());
                pessoas.setPessoasFisicas(pessoasFisicasNew);
            }
            pessoas = em.merge(pessoas);
            if (pessoasJuridicasOld != null && !pessoasJuridicasOld.equals(pessoasJuridicasNew)) {
                pessoasJuridicasOld.setPessoaID(null);
                pessoasJuridicasOld = em.merge(pessoasJuridicasOld);
            }
            if (pessoasJuridicasNew != null && !pessoasJuridicasNew.equals(pessoasJuridicasOld)) {
                Pessoas oldPessoaIDOfPessoasJuridicas = pessoasJuridicasNew.getPessoaID();
                if (oldPessoaIDOfPessoasJuridicas != null) {
                    oldPessoaIDOfPessoasJuridicas.setPessoasJuridicas(null);
                    oldPessoaIDOfPessoasJuridicas = em.merge(oldPessoaIDOfPessoasJuridicas);
                }
                pessoasJuridicasNew.setPessoaID(pessoas);
                pessoasJuridicasNew = em.merge(pessoasJuridicasNew);
            }
            if (pessoasFisicasOld != null && !pessoasFisicasOld.equals(pessoasFisicasNew)) {
                pessoasFisicasOld.setPessoaID(null);
                pessoasFisicasOld = em.merge(pessoasFisicasOld);
            }
            if (pessoasFisicasNew != null && !pessoasFisicasNew.equals(pessoasFisicasOld)) {
                Pessoas oldPessoaIDOfPessoasFisicas = pessoasFisicasNew.getPessoaID();
                if (oldPessoaIDOfPessoasFisicas != null) {
                    oldPessoaIDOfPessoasFisicas.setPessoasFisicas(null);
                    oldPessoaIDOfPessoasFisicas = em.merge(oldPessoaIDOfPessoasFisicas);
                }
                pessoasFisicasNew.setPessoaID(pessoas);
                pessoasFisicasNew = em.merge(pessoasFisicasNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pessoas.getId();
                if (findPessoas(id) == null) {
                    throw new NonexistentEntityException("The pessoas with id " + id + " no longer exists.");
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
            Pessoas pessoas;
            try {
                pessoas = em.getReference(Pessoas.class, id);
                pessoas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoas with id " + id + " no longer exists.", enfe);
            }
            PessoasJuridicas pessoasJuridicas = pessoas.getPessoasJuridicas();
            if (pessoasJuridicas != null) {
                pessoasJuridicas.setPessoaID(null);
                pessoasJuridicas = em.merge(pessoasJuridicas);
            }
            PessoasFisicas pessoasFisicas = pessoas.getPessoasFisicas();
            if (pessoasFisicas != null) {
                pessoasFisicas.setPessoaID(null);
                pessoasFisicas = em.merge(pessoasFisicas);
            }
            em.remove(pessoas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pessoas> findPessoasEntities() {
        return findPessoasEntities(true, -1, -1);
    }

    public List<Pessoas> findPessoasEntities(int maxResults, int firstResult) {
        return findPessoasEntities(false, maxResults, firstResult);
    }

    private List<Pessoas> findPessoasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pessoas.class));
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

    public Pessoas findPessoas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pessoas.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pessoas> rt = cq.from(Pessoas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
