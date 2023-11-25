/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package cadastroserver;

import controller.ProdutosJpaController;
import controller.UsuariosJpaController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import model.Produtos;

public class CadastroThread extends Thread {
    private final ProdutosJpaController ctrl;
    private final UsuariosJpaController ctrlUsu;
    private final Socket s1;

    public CadastroThread(ProdutosJpaController ctrl, UsuariosJpaController ctrlUsu, Socket socket) {
        this.ctrl = ctrl;
        this.ctrlUsu = ctrlUsu;
        this.s1 = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream input = new ObjectInputStream(s1.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(s1.getOutputStream());

            String login = (String) input.readObject();
            String senha = (String) input.readObject();

            if (login != null && senha != null) {
                // Verifique o login do usuário
                if (ctrlUsu.findUsuario(login, senha) != null) {
                    // Loop de resposta
                    while (true) {
                        String comando = (String) input.readObject();
                        if (comando != null && comando.equals("L")) {
                            // Comando "L" - Listar produtos
                            List<Produtos> produtos = ctrl.findProdutosEntities();
                            output.writeObject(produtos);
                        }
                    }
                }
            }

            // Feche os recursos após o uso
            input.close();
            output.close();
            s1.close();
        } catch (IOException | ClassNotFoundException ex) {
        }
    }
}
