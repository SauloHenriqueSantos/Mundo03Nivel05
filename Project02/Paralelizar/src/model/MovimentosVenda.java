/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author saulo
 */
@Entity
@Table(name = "MovimentosVenda")
@NamedQueries({
    @NamedQuery(name = "MovimentosVenda.findAll", query = "SELECT m FROM MovimentosVenda m"),
    @NamedQuery(name = "MovimentosVenda.findById", query = "SELECT m FROM MovimentosVenda m WHERE m.id = :id"),
    @NamedQuery(name = "MovimentosVenda.findByQuantidadeVendida", query = "SELECT m FROM MovimentosVenda m WHERE m.quantidadeVendida = :quantidadeVendida"),
    @NamedQuery(name = "MovimentosVenda.findByDataVenda", query = "SELECT m FROM MovimentosVenda m WHERE m.dataVenda = :dataVenda")})
public class MovimentosVenda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "QuantidadeVendida")
    private Integer quantidadeVendida;
    @Column(name = "DataVenda")
    @Temporal(TemporalType.DATE)
    private Date dataVenda;
    @JoinColumn(name = "IDPessoaFisica", referencedColumnName = "CPF")
    @ManyToOne
    private PessoasFisicas iDPessoaFisica;
    @JoinColumn(name = "IDProduto", referencedColumnName = "ID")
    @ManyToOne
    private Produtos iDProduto;
    @JoinColumn(name = "IDUsuario", referencedColumnName = "ID")
    @ManyToOne
    private Usuarios iDUsuario;

    public MovimentosVenda() {
    }

    public MovimentosVenda(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(Integer quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public PessoasFisicas getIDPessoaFisica() {
        return iDPessoaFisica;
    }

    public void setIDPessoaFisica(PessoasFisicas iDPessoaFisica) {
        this.iDPessoaFisica = iDPessoaFisica;
    }

    public Produtos getIDProduto() {
        return iDProduto;
    }

    public void setIDProduto(Produtos iDProduto) {
        this.iDProduto = iDProduto;
    }

    public Usuarios getIDUsuario() {
        return iDUsuario;
    }

    public void setIDUsuario(Usuarios iDUsuario) {
        this.iDUsuario = iDUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimentosVenda)) {
            return false;
        }
        MovimentosVenda other = (MovimentosVenda) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.MovimentosVenda[ id=" + id + " ]";
    }
    
}
