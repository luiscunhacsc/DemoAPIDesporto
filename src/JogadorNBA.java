// JogadorNBA.java
// Classe concreta para jogadores da NBA
// Demonstra herança e polimorfismo

public class JogadorNBA extends JogadorBasquete {
    public JogadorNBA(String nome, String nacionalidade, int idade) {
        super(nome, nacionalidade, idade);
    }

    @Override
    public String mostrarEstatisticas() {
        StringBuilder sb = new StringBuilder();
        sb.append("Jogador NBA: ").append(getNome()).append("\n");
        sb.append("Nacionalidade: ")
          .append(getNacionalidade() == null || getNacionalidade().equals("Não disponível") ? "Não disponível" : getNacionalidade())
          .append("\n");
        sb.append("Idade: ")
          .append(getIdade() <= 0 ? "Não disponível" : getIdade())
          .append("\n\n");
        sb.append("Estatísticas:\n");
        for (Estatistica est : getEstatisticas()) {
            sb.append("- ").append(est.toString()).append("\n");
        }
        return sb.toString();
    }
}
