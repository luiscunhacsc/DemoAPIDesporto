// ApiSportsBasqueteFetcher.java
// Implementação real da interface EstatisticasBasqueteFetcher usando a API-Sports NBA
// Versão sem Gson/.jar

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiSportsBasqueteFetcher implements EstatisticasBasqueteFetcher {
    private static final String API_KEY = "477011391cfcac1d9cc8a76c78ed90b1";
    private static final String BASE_URL = "https://v1.basketball.api-sports.io/players";

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
        // Parse manual do JSON (procura pelo primeiro jogador)
        // Exemplo de resposta esperada: {"response":[{"player":{"firstname":"LeBron","lastname":"James","nationality":"USA","age":38},"statistics":[{"team":{"name":"Lakers"}, ...}]}]}
        int idxResp = json.indexOf("\"response\":");
        if (idxResp == -1) return null;
        int idxArrStart = json.indexOf('[', idxResp);
        int idxArrEnd = json.indexOf(']', idxArrStart);
        if (idxArrStart == -1 || idxArrEnd == -1 || idxArrEnd <= idxArrStart+1) return null;
        String jogadorObj = json.substring(idxArrStart+1, idxArrEnd);
        
        // Extrair campos do jogador
        String primeiroNome = extrairCampo(jogadorObj, "firstname");
        String ultimoNome = extrairCampo(jogadorObj, "lastname");
        if (primeiroNome == null || ultimoNome == null) {
            return null; // Dados mínimos ausentes
        }
        String nacionalidade = extrairCampo(jogadorObj, "nationality");
        String idadeStr = extrairCampo(jogadorObj, "age");
        int idade = -1;
        if (idadeStr != null && !idadeStr.isEmpty()) {
            try {
                idade = Integer.parseInt(idadeStr);
            } catch (NumberFormatException e) {
                idade = -1;
            }
        }
        String nomeCompleto = primeiroNome + " " + ultimoNome;
        JogadorNBA jogador = new JogadorNBA(nomeCompleto, nacionalidade != null ? nacionalidade : "Desconhecida", idade);

        // Estatísticas básicas
        int idxStats = jogadorObj.indexOf("\"statistics\":");
        if (idxStats != -1) {
            int idxStatsArrStart = jogadorObj.indexOf('[', idxStats);
            int idxStatsArrEnd = jogadorObj.indexOf(']', idxStatsArrStart);
            if (idxStatsArrStart != -1 && idxStatsArrEnd != -1 && idxStatsArrEnd > idxStatsArrStart+1) {
                String statsObj = jogadorObj.substring(idxStatsArrStart+1, idxStatsArrEnd);
                String team = extrairCampo(statsObj, "name");
                String season = extrairCampo(statsObj, "season");
                String jogos = extrairCampo(statsObj, "appearences");
                String minutos = extrairCampo(statsObj, "minutes");
                String pontos = extrairCampo(statsObj, "average", "points");
                String assist = extrairCampo(statsObj, "average", "assists");
                String rebotes = extrairCampo(statsObj, "average", "rebounds");
                if (team != null) jogador.adicionarEstatistica(new Estatistica("Time", team));
                if (season != null) jogador.adicionarEstatistica(new Estatistica("Temporada", season));
                if (jogos != null) jogador.adicionarEstatistica(new Estatistica("Jogos", jogos));
                if (minutos != null) jogador.adicionarEstatistica(new Estatistica("Minutos por jogo", minutos));
                if (pontos != null) jogador.adicionarEstatistica(new Estatistica("Pontos por jogo", pontos));
                if (assist != null) jogador.adicionarEstatistica(new Estatistica("Assistências por jogo", assist));
                if (rebotes != null) jogador.adicionarEstatistica(new Estatistica("Rebotes por jogo", rebotes));
            }
        }
        return jogador;
    }

    // Função auxiliar para extrair o valor de um campo simples do JSON
    private String extrairCampo(String json, String campo) {
        String busca = "\"" + campo + "\":";
        int idx = json.indexOf(busca);
        if (idx == -1) return null;
        int idxValor = idx + busca.length();
        char c = json.charAt(idxValor);
        if (c == '"') {
            int idxFim = json.indexOf('"', idxValor+1);
            if (idxFim == -1) return null;
            return json.substring(idxValor+1, idxFim);
        } else {
            int idxFim = json.indexOf(',', idxValor);
            if (idxFim == -1) idxFim = json.indexOf('}', idxValor);
            if (idxFim == -1) return null;
            String valor = json.substring(idxValor, idxFim).replaceAll("[^0-9]", "");
            if (valor.isEmpty()) return null;
            return valor;
        }
    }
    // Função auxiliar para extrair campo aninhado (ex: "points":{"average":"30.1"})
    private String extrairCampo(String json, String campo, String objetoPai) {
        String buscaPai = "\"" + objetoPai + "\":{";
        int idxPai = json.indexOf(buscaPai);
        if (idxPai == -1) return null;
        int idxObj = json.indexOf('{', idxPai);
        int idxFimObj = json.indexOf('}', idxObj);
        String obj = json.substring(idxObj+1, idxFimObj);
        return extrairCampo(obj, campo);
    }
}
