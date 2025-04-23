# Dados Desportivos

## Configuração

1. **Clona o repositório**
2. **Configura a tua chave de API**
   - Copia o ficheiro `.env.example` para `.env` e adiciona a tua chave de API:
     ```
     API_SPORTS_KEY=A_TUA_CHAVE_API_AQUI
     ```
   - Ou define a variável de ambiente `API_SPORTS_KEY`.
3. **Compila e executa o projeto**

## Segurança
- **Nunca cometas a tua chave de API verdadeira**. O ficheiro `.env` está no `.gitignore`.
- A aplicação carrega a chave da API a partir do ambiente ou do ficheiro `.env`.

## Requisitos
- Java 11 ou superior

## Utilização
- Executa o ficheiro `Main.java` para iniciar a interface gráfica Swing.

---

### Como obter APIs para outras modalidades
1. Acede ao site oficial: [https://www.api-sports.io/](https://www.api-sports.io/)
2. Cria uma conta gratuita ou inicia sessão.
3. Escolhe a modalidade pretendida (ex: Futebol, Ténis, Fórmula 1, etc.).
4. Solicita a tua chave de API específica para essa modalidade.
5. Consulta a documentação para os endpoints e exemplos de cada desporto.

### Exemplos de endpoints base
- **Basquetebol:** `https://v1.basketball.api-sports.io/`
- **Futebol:** `https://v3.football.api-sports.io/`
- **Ténis:** `https://v1.tennis.api-sports.io/`
- **Fórmula 1:** `https://v1.formula-1.api-sports.io/`

### Como adaptar o projeto a outra modalidade
- Altera o endpoint base (`BASE_URL`) no código para o da modalidade pretendida.
- Usa a chave de API correspondente.
- Ajusta os parâmetros e o tratamento de dados conforme a documentação da nova modalidade.

Para mais detalhes e documentação de cada API, visita: [https://www.api-sports.io/documentation](https://www.api-sports.io/documentation)

---

Para quaisquer problemas ou contributos, abre um _pull request_ ou uma _issue_ no GitHub.

---
