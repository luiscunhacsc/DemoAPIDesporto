// JogadorBasquete.java
// Classe abstrata que representa um jogador de basquetebol genérico
// Demonstra encapsulamento, construtores, herança, composição e polimorfismo

import java.util.ArrayList;
import java.util.List;

public abstract class JogadorBasquete {
    // Atributos encapsulados
    private String nome;
    private String nacionalidade;
    private int idade;
    private List<Estatistica> estatisticas;

    public JogadorBasquete(String nome, String nacionalidade, int idade) {
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.idade = idade;
        this.estatisticas = new ArrayList<>();
    }

    public String getNome() { return nome; }
    public String getNacionalidade() { return nacionalidade; }
    public int getIdade() { return idade; }
    public List<Estatistica> getEstatisticas() { return estatisticas; }

    public void adicionarEstatistica(Estatistica estatistica) {
        estatisticas.add(estatistica);
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    // Método abstrato para exibir estatísticas
    public abstract String mostrarEstatisticas();
}
