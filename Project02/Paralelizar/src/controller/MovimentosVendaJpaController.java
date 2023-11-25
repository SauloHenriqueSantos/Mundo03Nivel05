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
import model.MovimentosVenda;
import model.PessoasFisicas;
import model.Produtos;
import model.Usuarios;

/**
 *
 * @author saulo
 */
public class MovimentosVendaJpaController implements Serializable {

    public MovimentosVendaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MovimentosVenda movimentosVenda) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoasFisicas IDPessoaFisica = movimentosVenda.getIDPessoaFisica();
            if (IDPessoaFisica != null) {
                IDPessoaFisica = em.getReference(IDPessoaFisica.getClass(), IDPessoaFisica.getCpf());
                movimentosVenda.setIDPessoaFisica(IDPessoaFisica);
            }
            Produtos IDProduto = movimentosVenda.getIDProduto();
            if (IDProduto != null) {
                IDProduto = em.getReference(IDProduto.getClass(), IDProduto.getId());
                movimentosVenda.setIDProduto(IDProduto);
            }
            Usuarios IDUsuario = movimentosVenda.getIDUsuario();
            if (IDUsuario != null) {
                IDUsuario = em.getReference(IDUsuario.getClass(), IDUsuario.getId());
                movimentosVenda.setIDUsuario(IDUsuario);
            }
            em.persist(movimentosVenda);
            if (IDPessoaFisica != null) {
                IDPessoaFisica.getMovimentosVendaCollection().add(movimentosVenda);
                IDPessoaFisica = em.merge(IDPessoaFisica);
            }
            if (IDProduto != null) {
                IDProduto.getMovimentosVendaCollection().add(movimentosVenda);
                IDProduto = em.merge(IDProduto);
            }
            if (IDUsuario != null) {
                IDUsuario.getMovimentosVendaCollection().add(movimentosVenda);
                IDUsuario = em.merge(IDUsuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMovimentosVenda(movimentosVenda.getId()) != null) {
                throw new PreexistingEntityException("MovimentosVenda " + movimentosVenda + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MovimentosVenda movimentosVenda) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MovimentosVenda persistentMovimentosVenda = em.find(MovimentosVenda.class, movimentosVenda.getId());
            PessoasFisicas IDPessoaFisicaOld = persistentMovimentosVenda.getIDPessoaFisica();
            PessoasFisicas IDPessoaFisicaNew = movimentosVenda.getIDPessoaFisica();
            Produtos IDProdutoOld = persistentMovimentosVenda.getIDProduto();
            Produtos IDProdutoNew = movimentosVenda.getIDProduto();
            Usuarios IDUsuarioOld = persistentMovimentosVenda.getIDUsuario();
            Usuarios IDUsuarioNew = movimentosVenda.getIDUsuario();
            if (IDPessoaFisicaNew != null) {
                IDPessoaFisicaNew = em.getReference(IDPessoaFisicaNew.getClass(), IDPessoaFisicaNew.getCpf());
                movimentosVenda.setIDPessoaFisica(IDPessoaFisicaNew);
            }
            if (IDProdutoNew != null) {
                IDProdutoNew = em.getReference(IDProdutoNew.getClass(), IDProdutoNew.getId());
                movimentosVenda.setIDProduto(IDProdutoNew);
            }
            if (IDUsuarioNew != null) {
                IDUsuarioNew = em.getReference(IDUsuarioNew.getClass(), IDUsuarioNew.getId());
                movimentosVenda.setIDUsuario(IDUsuarioNew);
            }
            movimentosVenda = em.merge(movimentosVenda);
            if (IDPessoaFisicaOld != null && !IDPessoaFisicaOld.equals(IDPessoaFisicaNew)) {
                IDPessoaFisicaOld.getMovimentosVendaCollection().remove(movimentosVenda);
                IDPessoaFisicaOld = em.merge(IDPessoaFisicaOld);
            }
            if (IDPessoaFisicaNew != null && !IDPessoaFisicaNew.equals(IDPessoaFisicaOld)) {
                IDPessoaFisicaNew.getMovimentosVendaCollection().add(movimentosVenda);
                IDPessoaFisicaNew = em.merge(IDPessoaFisicaNew);
            }
            if (IDProdutoOld != null && !IDProdutoOld.equals(IDProdutoNew)) {
                IDProdutoOld.getMovimentosVendaCollection().remove(movimentosVenda);
                IDProdutoOld = em.merge(IDProdutoOld);
            }
            if (IDProdutoNew != null && !IDProdutoNew.equals(IDProdutoOld)) {
                IDProdutoNew.getMovimentosVendaCollection().add(movimentosVenda);
                IDProdutoNew = em.merge(IDProdutoNew);
            }
            if (IDUsuarioOld != null && !IDUsuarioOld.equals(IDUsuarioNew)) {
                IDUsuarioOld.getMovimentosVendaCollection().remove(movimentosVenda);
                IDUsuarioOld = em.merge(IDUsuarioOld);
            }
            if (IDUsuarioNew != null && !IDUsuarioNew.equals(IDUsuarioOld)) {
                IDUsuarioNew.getMovimentosVendaCollection().add(movimentosVenda);
                IDUsuarioNew = em.merge(IDUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = movimentosVenda.getId();
                if (findMovimentosVenda(id) == null) {
                    throw new NonexistentEntityException("The movimentosVenda with id " + id + " no longer exists.");
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
            MovimentosVenda movimentosVenda;
            try {
                movimentosVenda = em.getReference(MovimentosVenda.class, id);
                movimentosVenda.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movimentosVenda with id " + id + " no longer exists.", enfe);
            }
            PessoasFisicas IDPessoaFisica = movimentosVenda.getIDPessoaFisica();
            if (IDPessoaFisica != null) {
                IDPessoaFisica.getMovimentosVendaCollection().remove(movimentosVenda);
                IDPessoaFisica = em.merge(IDPessoaFisica);
            }
            Produtos IDProduto = movimentosVenda.getIDProduto();
            if (IDProduto != null) {
                IDProduto.getMovimentosVendaCollection().remove(movimentosVenda);
                IDProduto = em.merge(IDProduto);
            }
            Usuarios IDUsuario = movimentosVenda.getIDUsuario();
            if (IDUsuario != null) {
                IDUsuario.getMovimentosVendaCollection().remove(movimentosVenda);
                IDUsuario = em.merge(IDUsuario);
            }
            em.remove(movimentosVenda);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MovimentosVenda> findMovimentosVendaEntities() {
        return findMovimentosVendaEntities(true, -1, -1);
    }

    public List<MovimentosVenda> findMovimentosVendaEntities(int maxResults, int firstResult) {
        return findMovimentosVendaEntities(false, maxResults, firstResult);
    }

    private List<MovimentosVenda> findMovimentosVendaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MovimentosVenda.class));
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

    public MovimentosVenda findMovimentosVenda(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MovimentosVenda.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovimentosVendaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MovimentosVenda> rt = cq.from(MovimentosVenda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
