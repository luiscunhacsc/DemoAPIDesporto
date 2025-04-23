import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

public class ApiSportsJogadorFetcher implements EstatisticasBasqueteFetcher {
    private static final String API_KEY = "477011391cfcac1d9cc8a76c78ed90b1";
    private static final String BASE_URL = "https://v1.basketball.api-sports.io/players";
    private static final String SEASON = "2023";

    @Override
    public JogadorBasquete buscarJogador(String nome) throws Exception {
        String urlString = BASE_URL + "?search=" + nome.replace(" ", "%20");
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("x-apisports-key", API_KEY);
        con.setRequestProperty("Accept", "application/json");

        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("Erro na API: código " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String json = response.toString();

        // Procurar todos os jogadores devolvidos pela API
        int idxResp = json.indexOf("\"response\":");
        if (idxResp == -1) return null;
        int idxArrStart = json.indexOf('[', idxResp);
        int idxArrEnd = json.indexOf(']', idxArrStart);
        if (idxArrStart == -1 || idxArrEnd == -1 || idxArrEnd <= idxArrStart+1) return null;
        String arrayJogadores = json.substring(idxArrStart+1, idxArrEnd);

        String buscaLower = nome.toLowerCase();
        int idx = 0;
        while (idx < arrayJogadores.length()) {
            int nextObjStart = arrayJogadores.indexOf('{', idx);
            if (nextObjStart == -1) break;
            int nextObjEnd = encontrarFechoObjeto(arrayJogadores, nextObjStart);
            if (nextObjEnd == -1) break;
            String jogadorObj = arrayJogadores.substring(nextObjStart, nextObjEnd + 1);

            // DEBUG: imprime o JSON bruto deste jogador
            System.out.println("DEBUG jogadorObj: " + jogadorObj);

            String nomeCompletoAlt = extrairCampo(jogadorObj, "name");
            String idStr = extrairCampo(jogadorObj, "id");
            if (
                nomeCompletoAlt != null && idStr != null &&
                (nomeCompletoAlt.toLowerCase().contains(buscaLower) ||
                 buscaLower.contains(nomeCompletoAlt.toLowerCase()))
            ) {
                // Tentar extrair o país do campo 'birth' se não estiver na raiz
                String pais = extrairCampo(jogadorObj, "country");
                if (pais == null) pais = extrairCampo(jogadorObj, "country", "birth");
                if (pais == null) pais = "Não disponível";

                // Tentar extrair a idade diretamente, ou calcular a partir da data de nascimento
                String idadeStr = extrairCampo(jogadorObj, "age");
                int idade = -1;
                if (idadeStr != null) {
                    try {
                        idade = Integer.parseInt(idadeStr);
                    } catch (Exception e) {
                        idade = -1;
                    }
                } else {
                    String birthDate = extrairCampo(jogadorObj, "date", "birth");
                    if (birthDate != null && birthDate.length() >= 4) {
                        try {
                            int birthYear = Integer.parseInt(birthDate.substring(0, 4));
                            int anoAtual = java.time.Year.now().getValue();
                            idade = anoAtual - birthYear;
                        } catch (Exception e) {
                            idade = -1;
                        }
                    }
                }
                if (idade == -1) {
                    // Apresentar valor de recurso caso falhe
                    idade = -1;
                }

                String posicao = extrairCampo(jogadorObj, "position");
                String numero = extrairCampo(jogadorObj, "number");

                JogadorNBA jogador = new JogadorNBA(nomeCompletoAlt, pais, idade);

                // Adiciona as estatísticas biográficas como estatísticas também
                if (posicao != null) jogador.adicionarEstatistica(new Estatistica("Posição", posicao));
                if (numero != null) jogador.adicionarEstatistica(new Estatistica("Número", numero));

                // Buscar estatísticas detalhadas
                try {
                    int playerId = Integer.parseInt(idStr);
                    adicionarEstatisticasDetalhadas(jogador, playerId);
                } catch (Exception e) {
                    // Ignora se não conseguir obter ID
                }

                return jogador;
            }

            idx = nextObjEnd + 1;
        }

        return null;
    }

    private void adicionarEstatisticasDetalhadas(JogadorNBA jogador, int playerId) {
        try {
            String statsUrl = "https://v1.basketball.api-sports.io/players/statistics?player=" + playerId + "&season=" + SEASON;
            URL url = new URL(statsUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("x-apisports-key", API_KEY);
            con.setRequestProperty("Accept", "application/json");
            int responseCode = con.getResponseCode();
            if (responseCode != 200) return;
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String json = response.toString();
            int idxResp = json.indexOf("\"response\":");
            if (idxResp == -1) return;
            int idxArrStart = json.indexOf('[', idxResp);
            int idxArrEnd = json.indexOf(']', idxArrStart);
            if (idxArrStart == -1 || idxArrEnd == -1 || idxArrEnd <= idxArrStart+1) return;
            String statsObj = json.substring(idxArrStart+1, idxArrEnd);

            String jogos = extrairCampo(statsObj, "appearences", "games");
            String minutos = extrairCampo(statsObj, "minutes", "games");
            String pontos = extrairCampo(statsObj, "average", "points");
            String assist = extrairCampo(statsObj, "average", "assists");
            String rebotes = extrairCampo(statsObj, "average", "rebouts");

            if (jogos != null) jogador.adicionarEstatistica(new Estatistica("Jogos", jogos));
            if (minutos != null) jogador.adicionarEstatistica(new Estatistica("Minutos por jogo", minutos));
            if (pontos != null) jogador.adicionarEstatistica(new Estatistica("Pontos por jogo", pontos));
            if (assist != null) jogador.adicionarEstatistica(new Estatistica("Assistências por jogo", assist));
            if (rebotes != null) jogador.adicionarEstatistica(new Estatistica("Rebotes por jogo", rebotes));
        } catch (Exception e) {
            // Ignorar erros silenciosamente
        }
    }

    private String extrairCampo(String json, String campo) {
        String busca = "\"" + campo + "\":";
        int idx = json.indexOf(busca);
        if (idx == -1) return null;
        int idxValor = idx + busca.length();
        char c = json.charAt(idxValor);
        if (c == '"') {
            int idxFim = json.indexOf('"', idxValor + 1);
            if (idxFim == -1) return null;
            return json.substring(idxValor + 1, idxFim);
        } else {
            int idxFim = json.indexOf(',', idxValor);
            if (idxFim == -1) idxFim = json.indexOf('}', idxValor);
            if (idxFim == -1) return null;
            String valor = json.substring(idxValor, idxFim).replaceAll("[^0-9.]", "");
            return valor.isEmpty() ? null : valor;
        }
    }

    private String extrairCampo(String json, String campo, String objetoPai) {
        String buscaPai = "\"" + objetoPai + "\":{";
        int idxPai = json.indexOf(buscaPai);
        if (idxPai == -1) return null;
        int idxObj = json.indexOf('{', idxPai);
        int idxFimObj = json.indexOf('}', idxObj);
        if (idxObj == -1 || idxFimObj == -1) return null;
        String obj = json.substring(idxObj + 1, idxFimObj);
        return extrairCampo(obj, campo);
    }

    private int encontrarFechoObjeto(String json, int idxStart) {
        int nivel = 0;
        for (int i = idxStart; i < json.length(); i++) {
            if (json.charAt(i) == '{') nivel++;
            else if (json.charAt(i) == '}') {
                nivel--;
                if (nivel == 0) return i;
            }
        }
        return -1;
    }
}
