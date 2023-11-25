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
import model.Produto;
import model.Produtos;

/**
 *
 * @author saulo
 */
public class ProdutosJpaController implements Serializable {

    public ProdutosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Produtos produtos) throws PreexistingEntityException, Exception {
        if (produtos.getMovimentosVendaCollection() == null) {
            produtos.setMovimentosVendaCollection(new ArrayList<MovimentosVenda>());
        }
        if (produtos.getMovimentosCompraCollection() == null) {
            produtos.setMovimentosCompraCollection(new ArrayList<MovimentosCompra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<MovimentosVenda> attachedMovimentosVendaCollection = new ArrayList<MovimentosVenda>();
            for (MovimentosVenda movimentosVendaCollectionMovimentosVendaToAttach : produtos.getMovimentosVendaCollection()) {
                movimentosVendaCollectionMovimentosVendaToAttach = em.getReference(movimentosVendaCollectionMovimentosVendaToAttach.getClass(), movimentosVendaCollectionMovimentosVendaToAttach.getId());
                attachedMovimentosVendaCollection.add(movimentosVendaCollectionMovimentosVendaToAttach);
            }
            produtos.setMovimentosVendaCollection(attachedMovimentosVendaCollection);
            Collection<MovimentosCompra> attachedMovimentosCompraCollection = new ArrayList<MovimentosCompra>();
            for (MovimentosCompra movimentosCompraCollectionMovimentosCompraToAttach : produtos.getMovimentosCompraCollection()) {
                movimentosCompraCollectionMovimentosCompraToAttach = em.getReference(movimentosCompraCollectionMovimentosCompraToAttach.getClass(), movimentosCompraCollectionMovimentosCompraToAttach.getId());
                attachedMovimentosCompraCollection.add(movimentosCompraCollectionMovimentosCompraToAttach);
            }
            produtos.setMovimentosCompraCollection(attachedMovimentosCompraCollection);
            em.persist(produtos);
            for (MovimentosVenda movimentosVendaCollectionMovimentosVenda : produtos.getMovimentosVendaCollection()) {
                Produtos oldIDProdutoOfMovimentosVendaCollectionMovimentosVenda = movimentosVendaCollectionMovimentosVenda.getIDProduto();
                movimentosVendaCollectionMovimentosVenda.setIDProduto(produtos);
                movimentosVendaCollectionMovimentosVenda = em.merge(movimentosVendaCollectionMovimentosVenda);
                if (oldIDProdutoOfMovimentosVendaCollectionMovimentosVenda != null) {
                    oldIDProdutoOfMovimentosVendaCollectionMovimentosVenda.getMovimentosVendaCollection().remove(movimentosVendaCollectionMovimentosVenda);
                    oldIDProdutoOfMovimentosVendaCollectionMovimentosVenda = em.merge(oldIDProdutoOfMovimentosVendaCollectionMovimentosVenda);
                }
            }
            for (MovimentosCompra movimentosCompraCollectionMovimentosCompra : produtos.getMovimentosCompraCollection()) {
                Produtos oldIDProdutoOfMovimentosCompraCollectionMovimentosCompra = movimentosCompraCollectionMovimentosCompra.getIDProduto();
                movimentosCompraCollectionMovimentosCompra.setIDProduto(produtos);
                movimentosCompraCollectionMovimentosCompra = em.merge(movimentosCompraCollectionMovimentosCompra);
                if (oldIDProdutoOfMovimentosCompraCollectionMovimentosCompra != null) {
                    oldIDProdutoOfMovimentosCompraCollectionMovimentosCompra.getMovimentosCompraCollection().remove(movimentosCompraCollectionMovimentosCompra);
                    oldIDProdutoOfMovimentosCompraCollectionMovimentosCompra = em.merge(oldIDProdutoOfMovimentosCompraCollectionMovimentosCompra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProdutos(produtos.getId()) != null) {
                throw new PreexistingEntityException("Produtos " + produtos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Produtos produtos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produtos persistentProdutos = em.find(Produtos.class, produtos.getId());
            Collection<MovimentosVenda> movimentosVendaCollectionOld = persistentProdutos.getMovimentosVendaCollection();
            Collection<MovimentosVenda> movimentosVendaCollectionNew = produtos.getMovimentosVendaCollection();
            Collection<MovimentosCompra> movimentosCompraCollectionOld = persistentProdutos.getMovimentosCompraCollection();
            Collection<MovimentosCompra> movimentosCompraCollectionNew = produtos.getMovimentosCompraCollection();
            Collection<MovimentosVenda> attachedMovimentosVendaCollectionNew = new ArrayList<MovimentosVenda>();
            for (MovimentosVenda movimentosVendaCollectionNewMovimentosVendaToAttach : movimentosVendaCollectionNew) {
                movimentosVendaCollectionNewMovimentosVendaToAttach = em.getReference(movimentosVendaCollectionNewMovimentosVendaToAttach.getClass(), movimentosVendaCollectionNewMovimentosVendaToAttach.getId());
                attachedMovimentosVendaCollectionNew.add(movimentosVendaCollectionNewMovimentosVendaToAttach);
            }
            movimentosVendaCollectionNew = attachedMovimentosVendaCollectionNew;
            produtos.setMovimentosVendaCollection(movimentosVendaCollectionNew);
            Collection<MovimentosCompra> attachedMovimentosCompraCollectionNew = new ArrayList<MovimentosCompra>();
            for (MovimentosCompra movimentosCompraCollectionNewMovimentosCompraToAttach : movimentosCompraCollectionNew) {
                movimentosCompraCollectionNewMovimentosCompraToAttach = em.getReference(movimentosCompraCollectionNewMovimentosCompraToAttach.getClass(), movimentosCompraCollectionNewMovimentosCompraToAttach.getId());
                attachedMovimentosCompraCollectionNew.add(movimentosCompraCollectionNewMovimentosCompraToAttach);
            }
            movimentosCompraCollectionNew = attachedMovimentosCompraCollectionNew;
            produtos.setMovimentosCompraCollection(movimentosCompraCollectionNew);
            produtos = em.merge(produtos);
            for (MovimentosVenda movimentosVendaCollectionOldMovimentosVenda : movimentosVendaCollectionOld) {
                if (!movimentosVendaCollectionNew.contains(movimentosVendaCollectionOldMovimentosVenda)) {
                    movimentosVendaCollectionOldMovimentosVenda.setIDProduto(null);
                    movimentosVendaCollectionOldMovimentosVenda = em.merge(movimentosVendaCollectionOldMovimentosVenda);
                }
            }
            for (MovimentosVenda movimentosVendaCollectionNewMovimentosVenda : movimentosVendaCollectionNew) {
                if (!movimentosVendaCollectionOld.contains(movimentosVendaCollectionNewMovimentosVenda)) {
                    Produtos oldIDProdutoOfMovimentosVendaCollectionNewMovimentosVenda = movimentosVendaCollectionNewMovimentosVenda.getIDProduto();
                    movimentosVendaCollectionNewMovimentosVenda.setIDProduto(produtos);
                    movimentosVendaCollectionNewMovimentosVenda = em.merge(movimentosVendaCollectionNewMovimentosVenda);
                    if (oldIDProdutoOfMovimentosVendaCollectionNewMovimentosVenda != null && !oldIDProdutoOfMovimentosVendaCollectionNewMovimentosVenda.equals(produtos)) {
                        oldIDProdutoOfMovimentosVendaCollectionNewMovimentosVenda.getMovimentosVendaCollection().remove(movimentosVendaCollectionNewMovimentosVenda);
                        oldIDProdutoOfMovimentosVendaCollectionNewMovimentosVenda = em.merge(oldIDProdutoOfMovimentosVendaCollectionNewMovimentosVenda);
                    }
                }
            }
            for (MovimentosCompra movimentosCompraCollectionOldMovimentosCompra : movimentosCompraCollectionOld) {
                if (!movimentosCompraCollectionNew.contains(movimentosCompraCollectionOldMovimentosCompra)) {
                    movimentosCompraCollectionOldMovimentosCompra.setIDProduto(null);
                    movimentosCompraCollectionOldMovimentosCompra = em.merge(movimentosCompraCollectionOldMovimentosCompra);
                }
            }
            for (MovimentosCompra movimentosCompraCollectionNewMovimentosCompra : movimentosCompraCollectionNew) {
                if (!movimentosCompraCollectionOld.contains(movimentosCompraCollectionNewMovimentosCompra)) {
                    Produtos oldIDProdutoOfMovimentosCompraCollectionNewMovimentosCompra = movimentosCompraCollectionNewMovimentosCompra.getIDProduto();
                    movimentosCompraCollectionNewMovimentosCompra.setIDProduto(produtos);
                    movimentosCompraCollectionNewMovimentosCompra = em.merge(movimentosCompraCollectionNewMovimentosCompra);
                    if (oldIDProdutoOfMovimentosCompraCollectionNewMovimentosCompra != null && !oldIDProdutoOfMovimentosCompraCollectionNewMovimentosCompra.equals(produtos)) {
                        oldIDProdutoOfMovimentosCompraCollectionNewMovimentosCompra.getMovimentosCompraCollection().remove(movimentosCompraCollectionNewMovimentosCompra);
                        oldIDProdutoOfMovimentosCompraCollectionNewMovimentosCompra = em.merge(oldIDProdutoOfMovimentosCompraCollectionNewMovimentosCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = produtos.getId();
                if (findProdutos(id) == null) {
                    throw new NonexistentEntityException("The produtos with id " + id + " no longer exists.");
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
            Produtos produtos;
            try {
                produtos = em.getReference(Produtos.class, id);
                produtos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produtos with id " + id + " no longer exists.", enfe);
            }
            Collection<MovimentosVenda> movimentosVendaCollection = produtos.getMovimentosVendaCollection();
            for (MovimentosVenda movimentosVendaCollectionMovimentosVenda : movimentosVendaCollection) {
                movimentosVendaCollectionMovimentosVenda.setIDProduto(null);
                movimentosVendaCollectionMovimentosVenda = em.merge(movimentosVendaCollectionMovimentosVenda);
            }
            Collection<MovimentosCompra> movimentosCompraCollection = produtos.getMovimentosCompraCollection();
            for (MovimentosCompra movimentosCompraCollectionMovimentosCompra : movimentosCompraCollection) {
                movimentosCompraCollectionMovimentosCompra.setIDProduto(null);
                movimentosCompraCollectionMovimentosCompra = em.merge(movimentosCompraCollectionMovimentosCompra);
            }
            em.remove(produtos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Produtos> findProdutosEntities() {
        return findProdutosEntities(true, -1, -1);
    }

    public List<Produtos> findProdutosEntities(int maxResults, int firstResult) {
        return findProdutosEntities(false, maxResults, firstResult);
    }

    private List<Produtos> findProdutosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Produtos.class));
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

    public Produtos findProdutos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Produtos.class, id);
        } finally {
            em.close();
        }
    }

    public int getProdutosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produtos> rt = cq.from(Produtos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Produto> findProdutoEntities() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Produto findProduto(int idProduto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void edit(Produto produto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
