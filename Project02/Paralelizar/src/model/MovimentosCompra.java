/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "MovimentosCompra")
@NamedQueries({
    @NamedQuery(name = "MovimentosCompra.findAll", query = "SELECT m FROM MovimentosCompra m"),
    @NamedQuery(name = "MovimentosCompra.findById", query = "SELECT m FROM MovimentosCompra m WHERE m.id = :id"),
    @NamedQuery(name = "MovimentosCompra.findByQuantidadeComprada", query = "SELECT m FROM MovimentosCompra m WHERE m.quantidadeComprada = :quantidadeComprada"),
    @NamedQuery(name = "MovimentosCompra.findByPrecoUnitarioCompra", query = "SELECT m FROM MovimentosCompra m WHERE m.precoUnitarioCompra = :precoUnitarioCompra"),
    @NamedQuery(name = "MovimentosCompra.findByDataCompra", query = "SELECT m FROM MovimentosCompra m WHERE m.dataCompra = :dataCompra")})
public class MovimentosCompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "QuantidadeComprada")
    private Integer quantidadeComprada;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PrecoUnitarioCompra")
    private BigDecimal precoUnitarioCompra;
    @Column(name = "DataCompra")
    @Temporal(TemporalType.DATE)
    private Date dataCompra;
    @JoinColumn(name = "IDPessoaJuridica", referencedColumnName = "CNPJ")
    @ManyToOne
    private PessoasJuridicas iDPessoaJuridica;
    @JoinColumn(name = "IDProduto", referencedColumnName = "ID")
    @ManyToOne
    private Produtos iDProduto;
    @JoinColumn(name = "IDUsuario", referencedColumnName = "ID")
    @ManyToOne
    private Usuarios iDUsuario;

    public MovimentosCompra() {
    }

    public MovimentosCompra(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantidadeComprada() {
        return quantidadeComprada;
    }

    public void setQuantidadeComprada(Integer quantidadeComprada) {
        this.quantidadeComprada = quantidadeComprada;
    }

    public BigDecimal getPrecoUnitarioCompra() {
        return precoUnitarioCompra;
    }

    public void setPrecoUnitarioCompra(BigDecimal precoUnitarioCompra) {
        this.precoUnitarioCompra = precoUnitarioCompra;
    }

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Date dataCompra) {
        this.dataCompra = dataCompra;
    }

    public PessoasJuridicas getIDPessoaJuridica() {
        return iDPessoaJuridica;
    }

    public void setIDPessoaJuridica(PessoasJuridicas iDPessoaJuridica) {
        this.iDPessoaJuridica = iDPessoaJuridica;
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
        if (!(object instanceof MovimentosCompra)) {
            return false;
        }
        MovimentosCompra other = (MovimentosCompra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.MovimentosCompra[ id=" + id + " ]";
    }
    
}
