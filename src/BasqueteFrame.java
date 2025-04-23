// BasqueteFrame.java
// Interface gráfica para buscar estatísticas de jogadores de basquete NBA
// Agora usando ApiSportsBasqueteFetcher para integração real

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BasqueteFrame extends JFrame {
    private JTextField nomeField;
    private JButton buscarButton;
    private JTextArea resultadoArea;
    private EstatisticasBasqueteFetcher fetcher;

    public BasqueteFrame() {
        super("Estatísticas de Jogadores NBA");
        // Troque para ApiSportsJogadorFetcher() para integração real
        this.fetcher = new ApiSportsJogadorFetcher();
        nomeField = new JTextField(20);
        buscarButton = new JButton("Buscar");
        resultadoArea = new JTextArea(10, 40);
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Nome do Jogador:"));
        inputPanel.add(nomeField);
        inputPanel.add(buscarButton);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarJogador();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buscarJogador() {
        String nome = nomeField.getText().trim();
        resultadoArea.setText("");
        if (nome.isEmpty()) {
            resultadoArea.setText("Por favor, insira o nome de um jogador.");
            return;
        }
        try {
            JogadorBasquete jogador = fetcher.buscarJogador(nome);
            if (jogador != null) {
                resultadoArea.setText(jogador.mostrarEstatisticas());
            } else {
                resultadoArea.setText("Jogador não encontrado.");
            }
        } catch (Exception ex) {
            resultadoArea.setText("Erro ao buscar estatísticas: " + ex.getMessage());
        }
    }
}
