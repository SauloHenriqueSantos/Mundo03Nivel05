/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author saulo
 */
@Entity
@Table(name = "PessoasJuridicas")
@NamedQueries({
    @NamedQuery(name = "PessoasJuridicas.findAll", query = "SELECT p FROM PessoasJuridicas p"),
    @NamedQuery(name = "PessoasJuridicas.findByCnpj", query = "SELECT p FROM PessoasJuridicas p WHERE p.cnpj = :cnpj")})
public class PessoasJuridicas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CNPJ")
    private String cnpj;
    @JoinColumn(name = "PessoaID", referencedColumnName = "ID")
    @OneToOne
    private Pessoas pessoaID;
    @OneToMany(mappedBy = "iDPessoaJuridica")
    private Collection<MovimentosCompra> movimentosCompraCollection;

    public PessoasJuridicas() {
    }

    public PessoasJuridicas(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Pessoas getPessoaID() {
        return pessoaID;
    }

    public void setPessoaID(Pessoas pessoaID) {
        this.pessoaID = pessoaID;
    }

    public Collection<MovimentosCompra> getMovimentosCompraCollection() {
        return movimentosCompraCollection;
    }

    public void setMovimentosCompraCollection(Collection<MovimentosCompra> movimentosCompraCollection) {
        this.movimentosCompraCollection = movimentosCompraCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cnpj != null ? cnpj.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PessoasJuridicas)) {
            return false;
        }
        PessoasJuridicas other = (PessoasJuridicas) object;
        if ((this.cnpj == null && other.cnpj != null) || (this.cnpj != null && !this.cnpj.equals(other.cnpj))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.PessoasJuridicas[ cnpj=" + cnpj + " ]";
    }
    
}
