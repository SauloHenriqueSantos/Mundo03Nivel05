/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroclient;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CadastroClientV2 {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4321);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
    
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            Scanner scanner = new Scanner(System.in);
            String login = "op1";
            String senha = "op1";

            out.writeObject(login);
            out.writeObject(senha);

            // Janela para apresentação de mensagens
            Thread asyncThread = new Thread(new AsyncMessageHandler(in));
            asyncThread.start();

            String command;
            while (true) {
                System.out.println("Opções: L (Listar), E (Entrada), S (Saída), X (Finalizar)");
                System.out.print("Digite um comando: ");
                command = scanner.nextLine();
                out.writeObject(command);

                if (command.equalsIgnoreCase("X")) {
                    break;
                }

                if (command.equalsIgnoreCase("L")) {
                    continue;
                }

                // Para E ou S
                String pessoaId, produtoId, quantidade, valorUnitario;

                System.out.print("Digite o ID da pessoa: ");
                pessoaId = reader.readLine();
                System.out.print("Digite o ID do produto: ");
                produtoId = reader.readLine();
                System.out.print("Digite a quantidade: ");
                quantidade = reader.readLine();
                System.out.print("Digite o valor unitário: ");
                valorUnitario = reader.readLine();

                out.writeObject(pessoaId);
                out.writeObject(produtoId);
                out.writeObject(quantidade);
                out.writeObject(valorUnitario);
            }

            socket.close();
        } catch (IOException e) {
        }
    }
}

class AsyncMessageHandler implements Runnable {
    private final ObjectInputStream in;

    public AsyncMessageHandler(ObjectInputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = (String) in.readObject();
                System.out.println("Servidor: " + message);
            }
        } catch (IOException | ClassNotFoundException e) {
        }
 
    }
    
}

