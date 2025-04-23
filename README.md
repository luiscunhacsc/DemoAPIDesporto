# TomasAdalgisa Basketball Stats App

## Setup

1. **Clone the repository**
2. **Configure your API key**
   - Copy `.env.example` to `.env` and add your API key:
     ```
     API_SPORTS_KEY=YOUR_API_KEY_HERE
     ```
   - Or set the environment variable `API_SPORTS_KEY`.
3. **Build and run the project**

## Security
- **Never commit your real API key**. The `.env` file is in `.gitignore`.
- The app loads the API key from the environment or `.env` file.

## Requirements
- Java 11 or higher

## Usage
- Run `Main.java` to launch the Swing UI.



### Como obter APIs para outras modalidades
1. Acede ao site oficial: [https://www.api-sports.io/](https://www.api-sports.io/)
2. Cria uma conta gratuita ou faz login.
3. Escolhe a modalidade pretendida (ex: Football, Tennis, Formula 1, etc.).
4. Solicita a tua chave de API específica para essa modalidade.
5. Consulta a documentação para endpoints e exemplos de cada desporto.

### Exemplos de endpoints base
- **Basquetebol:** `https://v1.basketball.api-sports.io/`
- **Futebol:** `https://v3.football.api-sports.io/`
- **Ténis:** `https://v1.tennis.api-sports.io/`
- **Fórmula 1:** `https://v1.formula-1.api-sports.io/`

### Como adaptar o projeto para outra modalidade
- Altera o endpoint base (`BASE_URL`) no código para o da modalidade pretendida.
- Usa a chave de API correspondente.
- Ajusta os parâmetros e tratamento de dados conforme a documentação da nova modalidade.

Para mais detalhes e documentação de cada API, visita: [https://www.api-sports.io/documentation](https://www.api-sports.io/documentation)

---

For any issues or contributions, open a pull request or issue on GitHub.


---

