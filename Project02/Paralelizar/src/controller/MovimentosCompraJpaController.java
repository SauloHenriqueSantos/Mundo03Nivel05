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
import model.MovimentosCompra;
import model.PessoasJuridicas;
import model.Produtos;
import model.Usuarios;

/**
 *
 * @author saulo  
 */
public class MovimentosCompraJpaController implements Serializable {

    public MovimentosCompraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MovimentosCompra movimentosCompra) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoasJuridicas IDPessoaJuridica = movimentosCompra.getIDPessoaJuridica();
            if (IDPessoaJuridica != null) {
                IDPessoaJuridica = em.getReference(IDPessoaJuridica.getClass(), IDPessoaJuridica.getCnpj());
                movimentosCompra.setIDPessoaJuridica(IDPessoaJuridica);
            }
            Produtos IDProduto = movimentosCompra.getIDProduto();
            if (IDProduto != null) {
                IDProduto = em.getReference(IDProduto.getClass(), IDProduto.getId());
                movimentosCompra.setIDProduto(IDProduto);
            }
            Usuarios IDUsuario = movimentosCompra.getIDUsuario();
            if (IDUsuario != null) {
                IDUsuario = em.getReference(IDUsuario.getClass(), IDUsuario.getId());
                movimentosCompra.setIDUsuario(IDUsuario);
            }
            em.persist(movimentosCompra);
            if (IDPessoaJuridica != null) {
                IDPessoaJuridica.getMovimentosCompraCollection().add(movimentosCompra);
                IDPessoaJuridica = em.merge(IDPessoaJuridica);
            }
            if (IDProduto != null) {
                IDProduto.getMovimentosCompraCollection().add(movimentosCompra);
                IDProduto = em.merge(IDProduto);
            }
            if (IDUsuario != null) {
                IDUsuario.getMovimentosCompraCollection().add(movimentosCompra);
                IDUsuario = em.merge(IDUsuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMovimentosCompra(movimentosCompra.getId()) != null) {
                throw new PreexistingEntityException("MovimentosCompra " + movimentosCompra + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MovimentosCompra movimentosCompra) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MovimentosCompra persistentMovimentosCompra = em.find(MovimentosCompra.class, movimentosCompra.getId());
            PessoasJuridicas IDPessoaJuridicaOld = persistentMovimentosCompra.getIDPessoaJuridica();
            PessoasJuridicas IDPessoaJuridicaNew = movimentosCompra.getIDPessoaJuridica();
            Produtos IDProdutoOld = persistentMovimentosCompra.getIDProduto();
            Produtos IDProdutoNew = movimentosCompra.getIDProduto();
            Usuarios IDUsuarioOld = persistentMovimentosCompra.getIDUsuario();
            Usuarios IDUsuarioNew = movimentosCompra.getIDUsuario();
            if (IDPessoaJuridicaNew != null) {
                IDPessoaJuridicaNew = em.getReference(IDPessoaJuridicaNew.getClass(), IDPessoaJuridicaNew.getCnpj());
                movimentosCompra.setIDPessoaJuridica(IDPessoaJuridicaNew);
            }
            if (IDProdutoNew != null) {
                IDProdutoNew = em.getReference(IDProdutoNew.getClass(), IDProdutoNew.getId());
                movimentosCompra.setIDProduto(IDProdutoNew);
            }
            if (IDUsuarioNew != null) {
                IDUsuarioNew = em.getReference(IDUsuarioNew.getClass(), IDUsuarioNew.getId());
                movimentosCompra.setIDUsuario(IDUsuarioNew);
            }
            movimentosCompra = em.merge(movimentosCompra);
            if (IDPessoaJuridicaOld != null && !IDPessoaJuridicaOld.equals(IDPessoaJuridicaNew)) {
                IDPessoaJuridicaOld.getMovimentosCompraCollection().remove(movimentosCompra);
                IDPessoaJuridicaOld = em.merge(IDPessoaJuridicaOld);
            }
            if (IDPessoaJuridicaNew != null && !IDPessoaJuridicaNew.equals(IDPessoaJuridicaOld)) {
                IDPessoaJuridicaNew.getMovimentosCompraCollection().add(movimentosCompra);
                IDPessoaJuridicaNew = em.merge(IDPessoaJuridicaNew);
            }
            if (IDProdutoOld != null && !IDProdutoOld.equals(IDProdutoNew)) {
                IDProdutoOld.getMovimentosCompraCollection().remove(movimentosCompra);
                IDProdutoOld = em.merge(IDProdutoOld);
            }
            if (IDProdutoNew != null && !IDProdutoNew.equals(IDProdutoOld)) {
                IDProdutoNew.getMovimentosCompraCollection().add(movimentosCompra);
                IDProdutoNew = em.merge(IDProdutoNew);
            }
            if (IDUsuarioOld != null && !IDUsuarioOld.equals(IDUsuarioNew)) {
                IDUsuarioOld.getMovimentosCompraCollection().remove(movimentosCompra);
                IDUsuarioOld = em.merge(IDUsuarioOld);
            }
            if (IDUsuarioNew != null && !IDUsuarioNew.equals(IDUsuarioOld)) {
                IDUsuarioNew.getMovimentosCompraCollection().add(movimentosCompra);
                IDUsuarioNew = em.merge(IDUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = movimentosCompra.getId();
                if (findMovimentosCompra(id) == null) {
                    throw new NonexistentEntityException("The movimentosCompra with id " + id + " no longer exists.");
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
            MovimentosCompra movimentosCompra;
            try {
                movimentosCompra = em.getReference(MovimentosCompra.class, id);
                movimentosCompra.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movimentosCompra with id " + id + " no longer exists.", enfe);
            }
            PessoasJuridicas IDPessoaJuridica = movimentosCompra.getIDPessoaJuridica();
            if (IDPessoaJuridica != null) {
                IDPessoaJuridica.getMovimentosCompraCollection().remove(movimentosCompra);
                IDPessoaJuridica = em.merge(IDPessoaJuridica);
            }
            Produtos IDProduto = movimentosCompra.getIDProduto();
            if (IDProduto != null) {
                IDProduto.getMovimentosCompraCollection().remove(movimentosCompra);
                IDProduto = em.merge(IDProduto);
            }
            Usuarios IDUsuario = movimentosCompra.getIDUsuario();
            if (IDUsuario != null) {
                IDUsuario.getMovimentosCompraCollection().remove(movimentosCompra);
                IDUsuario = em.merge(IDUsuario);
            }
            em.remove(movimentosCompra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MovimentosCompra> findMovimentosCompraEntities() {
        return findMovimentosCompraEntities(true, -1, -1);
    }

    public List<MovimentosCompra> findMovimentosCompraEntities(int maxResults, int firstResult) {
        return findMovimentosCompraEntities(false, maxResults, firstResult);
    }

    private List<MovimentosCompra> findMovimentosCompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MovimentosCompra.class));
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

    public MovimentosCompra findMovimentosCompra(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MovimentosCompra.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovimentosCompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MovimentosCompra> rt = cq.from(MovimentosCompra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
