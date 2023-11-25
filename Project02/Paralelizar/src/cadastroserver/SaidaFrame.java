/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;

import javax.swing.JDialog;
import javax.swing.JTextArea;

public class SaidaFrame extends JDialog {
    public JTextArea texto; // O atributo público JTextArea

    public SaidaFrame() {
        // Configurar as dimensões da janela
        setBounds(100, 100, 400, 300);

        // Definir o status modal como false
        setModal(false);

        // Criar o componente JTextArea e adicionar à janela
        texto = new JTextArea();
        add(texto);
    }
}
