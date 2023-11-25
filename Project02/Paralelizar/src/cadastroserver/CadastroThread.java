/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroserver;

/**
 *
 * @author saulo
 */
// Classe do servidor principal
// Classe do servidor principal
import controller.ProdutosJpaController;
import controller.UsuariosJpaController;
import controller.MovimentoJpaController;
import controller.PessoaJpaController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import model.Movimento;
import model.Produto;
import model.Pessoa;
import model.Usuarios;

public class CadastroThread extends Thread {
    private final ProdutosJpaController ctrlProd;
    private final UsuariosJpaController ctrlUsu;
    private final MovimentoJpaController ctrlMov;
    private final PessoaJpaController ctrlPessoa;
    private final Socket socket;
    
    public CadastroThread(
        ProdutosJpaController ctrlProd,
        UsuariosJpaController ctrlUsu,
        MovimentoJpaController ctrlMov,
        PessoaJpaController ctrlPessoa,
        Socket socket
    ) {
        this.ctrlProd = ctrlProd;
        this.ctrlUsu = ctrlUsu;
        this.ctrlMov = ctrlMov;
        this.ctrlPessoa = ctrlPessoa;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            
            // Receber login e senha do cliente
            String login = in.readUTF();
            String senha = in.readUTF();

            // Verificar as credenciais do usuário
            Usuarios usuario = ctrlUsu.findUsuario(login, senha);
            if (usuario == null) {
                // Credenciais inválidas, encerra a conexão
                socket.close();
                return;
            }

            // Loop de resposta
            while (true) {
                String comando = in.readUTF();
                switch (comando) {
                    case "L" -> {
                        // Comando para listar produtos
                        List<Produto> produtos = ctrlProd.findProdutoEntities();
                        out.writeObject(produtos);
                    }
                    case "E", "S" -> {
                        // Comando para entrada (E) ou saída (S) de produtos
                        int idPessoa = in.readInt();
                        int idProduto = in.readInt();
                        int quantidade = in.readInt();
                        double valorUnitario = in.readDouble();
                        // Verifica se a pessoa e o produto existem
                        Pessoa pessoa = ctrlPessoa.findPessoa(idPessoa);
                        Produto produto = ctrlProd.findProduto(idProduto);
                        if (pessoa != null && produto != null) {
                            // Cria um objeto de Movimento
                            Movimento movimento = new Movimento();
                            movimento.setTipoMovimento(comando);
                            movimento.setIdPessoa(pessoa);
                            movimento.setIdProduto(produto);
                            movimento.setQuantidade(quantidade);
                            movimento.setValorUnitario(valorUnitario);
                            
                            // Persiste o movimento no banco de dados
                            ctrlMov.create(movimento);
                            
                            // Atualiza a quantidade de produtos
                            if (comando.equals("E")) {
                                produto.setQuantidade(produto.getQuantidade() + quantidade);
                            } else if (comando.equals("S")) {
                                produto.setQuantidade(produto.getQuantidade() - quantidade);
                            }
                            
                            // Atualiza o produto no banco de dados
                            ctrlProd.edit(produto);
                            
                            // Envie uma confirmação de sucesso
                            out.writeUTF("Movimento registrado com sucesso.");
                        } else {
                            // Pessoa ou produto não encontrados
                            out.writeUTF("Pessoa ou produto não encontrados.");
                        }
                    }
                    default -> {
                        // Comando desconhecido, encerra a conexão
                        socket.close();
                        return;
                    }
                }
            }
        } catch (IOException ex) {
            // Trate exceções aqui
        }
    }
}

