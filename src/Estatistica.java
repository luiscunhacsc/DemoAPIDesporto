// Estatistica.java
// Classe simples para representar uma estatística de um tenista
// Demonstra encapsulamento, construtores e composição

public class Estatistica {
    private String descricao;
    private String valor;

    // Construtor
    public Estatistica(String descricao, String valor) {
        this.descricao = descricao;
        this.valor = valor;
    }

    // Getters
    public String getDescricao() { return descricao; }
    public String getValor() { return valor; }

    // Representação textual da estatística
    @Override
    public String toString() {
        return descricao + ": " + valor;
    }
}
