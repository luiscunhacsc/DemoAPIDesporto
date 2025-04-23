// Main.java
// Classe principal que inicializa a aplicação Swing para NBA
// Demonstra o uso de encapsulamento, composição, herança, polimorfismo e tratamento de exceções

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Executa a interface gráfica na thread de eventos do Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Cria e exibe a janela principal da aplicação (agora BasqueteFrame)
                new BasqueteFrame();
            }
        });
    }
}
