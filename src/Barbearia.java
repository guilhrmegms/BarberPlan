import java.io.IOException; // Importa a classe IOException para tratar erros de entrada/saída de arquivos
import java.nio.charset.StandardCharsets;// Importa a classe StandardCharsets para definir a codificação de caracteres ao escrever arquivos
import java.nio.file.Files; // Importa a classe Files para manipulação de arquivos
import java.nio.file.Paths; // Importa a classe Paths para criar caminhos de arquivos
import java.nio.file.StandardOpenOption; // Importa a classe Paths para criar caminhos de arquivos e StandardOpenOption para definir opções de abertura de arquivos
import java.time.LocalDate; // Importa a classe LocalDate para manipulação de datas
import java.time.LocalTime; // Importa as classes LocalDate e LocalTime para manipulação de datas e horários
import java.time.format.DateTimeFormatter; // Importa a classe DateTimeFormatter para formatar datas e horários
/**
 * Classe que representa a barbearia, contendo métodos para gerenciar agendamentos, serviços e usuários.
 */
public class Barbearia {
    private final Agendamento[] agendamentos;
    private int totalAgendamentos;
    private int id;

    private final Servico[] servicos;
    private final Usuario[] usuarios;
    private int totalUsuarios;

    public Barbearia() {
        /*
         * Construtor da classe Barbearia, inicializando os arrays de agendamentos, serviços e usuários.
         * Define o tamanho máximo dos arrays e inicializa os serviços e usuários padrão.
         */
        this.agendamentos = new Agendamento[100];
        this.totalAgendamentos = 0;
        this.id = 1;

        // Inicializa os serviços disponíveis na barbearia
        this.servicos = new Servico[3];
        this.servicos[0] = new Servico("Corte", 25.0);
        this.servicos[1] = new Servico("Barba", 20.0);
        this.servicos[2] = new Servico("Corte + Barba", 45.0);

        this.usuarios = new Usuario[100];
        this.usuarios[0] = new Usuario("Barbeiro", "", "barbeiro", "barbeiro123", "BARBEIRO");
        this.usuarios[1] = new Usuario(
                "Administrador", "", "administrador", "administrador123", "ADMINISTRADOR");
        this.totalUsuarios = 2;
    }

    public Servico[] getServicos() {
        return servicos;
    }

    public Usuario autenticar(String login, String senha) {
        for (int i = 0; i < totalUsuarios; i++) {
            if (usuarios[i].autenticar(login, senha)) {
                return usuarios[i];
            }
        }
        return null; // Retorna null se não encontrar um usuário com o login e senha fornecidos
    }

    public boolean cadastrarCliente(String nome, String telefone, String login, String senha) { // Cadastra um novo cliente na barbearia, verificando se o telefone é válido
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }
        return cadastrarUsuario(nome, telefone.trim(), login, senha, "CLIENTE"); // Chama o método privado cadastrarUsuario para realizar o cadastro do cliente
    }

    public boolean cadastrarBarbeiro(String nome, String login, String senha) {
        return cadastrarUsuario(nome, "", login, senha, "BARBEIRO");
    }

    private boolean cadastrarUsuario(String nome, String telefone, String login, String senha, String perfil) { // Cadastra um novo usuário na barbearia, verificando se o login é válido e se há espaço no array de usuários
        if (nome == null || nome.trim().isEmpty()
                || login == null || login.trim().isEmpty()
                || senha == null || senha.trim().isEmpty()
                || totalUsuarios >= usuarios.length) {
            return false;
        }
        for (int i = 0; i < totalUsuarios; i++) {
            if (usuarios[i].getLogin().equalsIgnoreCase(login.trim())) {
                return false;
            }
        }

        usuarios[totalUsuarios] = new Usuario(nome.trim(), telefone, login.trim(), senha, perfil); //
        totalUsuarios++;
        salvarDadosJson();
        return true;
    }

    public boolean cadastrar(String nome, String telefone, String servico, String data, String horario) {
        if (totalAgendamentos >= agendamentos.length) {
            System.out.println("Erro: A agenda está cheia!");
            return false;
        }
        /*
            Variaveis para armazenar a data e o horário convertidos para os tipos LocalDate e LocalTime, respectivamente.
        */
        LocalDate dataDate;
        LocalTime horarioTime;
        // Verifica as exceções ao tentar converter a data e o horário para os tipos LocalDate e LocalTime, respectivamente.
        try {
            dataDate = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/uuuu"));
        } catch (Exception e) {
            System.out.println("Data inválida: " + data);
            return false;
        }
        try {
            horarioTime = LocalTime.parse(horario, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            System.out.println("Horário inválido: " + horario);
            return false;
        }
        Agendamento novo = new Agendamento(id, nome, telefone, servico, dataDate, horarioTime);

        agendamentos[totalAgendamentos] = novo;
        totalAgendamentos++;
        id++;

        System.out.println("Agendamento salvo! ID: " + novo.getId());
        salvarDadosJson();
        return true;
    }

    public void listar() { // Lista todos os agendamentos cadastrados na barbearia
        if (totalAgendamentos == 0) {
            System.out.println("Nenhum agendamento marcado.");
            return;
        }

        for (int i = 0; i < totalAgendamentos; i++) {
            Agendamento a = agendamentos[i];
            System.out.println("ID: " + a.getId() + " | " + a.getData() + " às " + a.getHorario() +
                    " | Cliente: " + a.getNome() + " | Serviço: " + a.getServico());
        }
    }

    public void listarPorTelefone(String telefone) {// Lista todos os agendamentos de um cliente específico, identificado pelo telefone
        boolean encontrou = false;
        for (int i = 0; i < totalAgendamentos; i++) {
            Agendamento a = agendamentos[i];
            if (a.getTelefone().equals(telefone)) {
                System.out.println("ID: " + a.getId() + " | " + a.getData() + " às " + a.getHorario()
                        + " | Serviço: " + a.getServico());
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Você não possui agendamentos.");
        }
    }

    public boolean cancelarAgendamento(int idAgendamento) {// Cancela um agendamento específico, identificado pelo ID
        for (int i = 0; i < totalAgendamentos; i++) {
            if (agendamentos[i].getId() == idAgendamento) {
                for (int j = i; j < totalAgendamentos - 1; j++) {
                    agendamentos[j] = agendamentos[j + 1];
                }
                agendamentos[totalAgendamentos - 1] = null;
                totalAgendamentos--;
                salvarDadosJson();
                return true;
            }
        }
        return false;
    }

    private boolean salvarDadosJson() {// Salva os dados da barbearia em um arquivo JSON
        StringBuilder json = new StringBuilder();
        json.append("{\n  \"usuarios\": [\n");

        for (int i = 0; i < totalUsuarios; i++) {
            Usuario usuario = usuarios[i];
            json.append("    {\"nome\": ").append(escaparJson(usuario.getNome()))
                    .append(", \"telefone\": ").append(escaparJson(usuario.getTelefone()))
                    .append(", \"login\": ").append(escaparJson(usuario.getLogin()))
                    .append(", \"perfil\": ").append(escaparJson(usuario.getPerfil()))
                    .append("}");
            if (i < totalUsuarios - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  ],\n  \"servicos\": [\n"); // Adiciona os serviços ao JSON

        for (int i = 0; i < servicos.length; i++) {
            Servico servico = servicos[i];
            json.append("    {\"nome\": ").append(escaparJson(servico.getNome())) 
            // Adiciona o nome do serviço ao JSON
                    .append(", \"preco\": ").append(servico.getPreco()).append("}");
            if (i < servicos.length - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        // Adiciona os agendamentos ao JSON, incluindo informações como ID, nome do cliente, telefone, serviço, data e horário.

        json.append("  ],\n  \"agendamentos\": [\n");
        for (int i = 0; i < totalAgendamentos; i++) {
            Agendamento agendamento = agendamentos[i];
            json.append("    {\"id\": ").append(agendamento.getId())
                    .append(", \"nome\": ").append(escaparJson(agendamento.getNome()))
                    .append(", \"telefone\": ").append(escaparJson(agendamento.getTelefone()))
                    .append(", \"servico\": ").append(escaparJson(agendamento.getServico()))
                    .append(", \"data\": ").append(escaparJson(agendamento.getData().toString()))
                    .append(", \"horario\": ").append(escaparJson(agendamento.getHorario().toString()))
                    .append("}");
            if (i < totalAgendamentos - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  ]\n}\n");

        try {
            Files.write( // Escreve o conteúdo do JSON no arquivo "dados.json" com a codificação UTF-8, criando ou substituindo o arquivo existente
                    Paths.get("dados.json"),
                    json.toString().getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
            return true;
        } catch (IOException e) {
            System.out.println("Não foi possível atualizar dados.json: " + e.getMessage());
            return false;
        }
    }

    private String escaparJson(String valor) { // Escapa caracteres especiais em uma string para que ela possa ser usada em um arquivo JSON
        StringBuilder escapado = new StringBuilder("\"");
        for (int i = 0; i < valor.length(); i++) {
            char caractere = valor.charAt(i);
            if (caractere == '"') {
                escapado.append("\\\"");
            } else if (caractere == '\\') {
                escapado.append("\\\\");
            } else if (caractere == '\n') {
                escapado.append("\\n");
            } else if (caractere == '\r') {
                escapado.append("\\r");
            } else if (caractere == '\t') {
                escapado.append("\\t");
            } else if (caractere < 0x20) {
                escapado.append(String.format("\\u%04x", (int) caractere));
            } else {
                escapado.append(caractere);

           // Adiciona o caractere ao JSON escapado 
                
                
            }
        }
        return escapado.append("\"").toString();
    }
}
