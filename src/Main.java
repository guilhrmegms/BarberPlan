import java.util.Scanner; // Importa a classe Scanner para ler entradas do usuário

public class Main {
    // Método principal: é o ponto de entrada executado pela JVM.
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Barbearia barbearia = new Barbearia();
        boolean executando = true;

        // O menu principal continua disponível até o usuário escolher sair.
        while (executando) {
            System.out.println("\n=== BARBERPLAN ===");
            System.out.println("1. Login do cliente");
            System.out.println("2. Login do administrador");
            System.out.println("3. Login do barbeiro");
            System.out.println("4. Cadastrar cliente");
            System.out.println("0. Sair");
            int opcao = lerOpcao(scanner, "Escolha: ");

            if (opcao == 1) {
                Usuario usuario = entrar(scanner, barbearia, "CLIENTE");
                if (usuario != null) {
                    executarMenu(scanner, barbearia, usuario);
                }
            } else if (opcao == 2) {
                Usuario usuario = entrar(scanner, barbearia, "ADMINISTRADOR");
                if (usuario != null) {
                    executarMenu(scanner, barbearia, usuario);
                }
            } else if (opcao == 3) {
                Usuario usuario = entrar(scanner, barbearia, "BARBEIRO");
                if (usuario != null) {
                    executarMenu(scanner, barbearia, usuario);
                }
            } else if (opcao == 4) {
                cadastrarCliente(scanner, barbearia);
            } else if (opcao == 0) {
                executando = false;
                System.out.println("Fechando sistema...");
            } else {
                System.out.println("Opção inválida.");
            }
        }

        scanner.close();

        /* Salva os dados da barbearia em um arquivo JSON ao encerrar o programa.
        if (barbearia.salvarDadosJson()) {
            System.out.println("Dados salvos com sucesso.");
        } else {
            System.out.println("Erro ao salvar dados.");
        }
         */

    }

    private static Usuario entrar(Scanner scanner, Barbearia barbearia, String tipoAcesso) {
        System.out.print("Login: ");
        String login = scanner.nextLine().trim();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Usuario usuario = barbearia.autenticar(login, senha);
        // Também verifica se o perfil acessado corresponde ao tipo de conta solicitado.
        if (usuario == null || !tipoAcesso.equals(usuario.getPerfil())) {
            System.out.println("Login ou senha inválidos.");
            return null;
        }

        System.out.println("Bem-vindo(a), " + usuario.getNome() + "!");
        return usuario;
    }

    private static void cadastrarCliente(Scanner scanner, Barbearia barbearia) { // Solicita informações do cliente e tenta cadastrá-lo na barbearia
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Crie um login: ");
        String login = scanner.nextLine();
        System.out.print("Crie uma senha: ");
        String senha = scanner.nextLine();

        if (barbearia.cadastrarCliente(nome, telefone, login, senha)) {
            System.out.println("Cadastro realizado. Você já pode entrar.");
        } else {
            System.out.println("Não foi possível cadastrar. Verifique os dados ou escolha outro login.");
        }
    }

    private static void cadastrarBarbeiro(Scanner scanner, Barbearia barbearia) { // Solicita informações do barbeiro e tenta cadastrá-lo na barbearia
        System.out.print("Nome do barbeiro: ");
        String nome = scanner.nextLine();
        System.out.print("Crie um login para o barbeiro: ");
        String login = scanner.nextLine();
        System.out.print("Crie uma senha para o barbeiro: ");
        String senha = scanner.nextLine();

        if (barbearia.cadastrarBarbeiro(nome, login, senha)) {
            System.out.println("Barbeiro cadastrado com sucesso.");
        } else {
            System.out.println("Não foi possível cadastrar. Verifique os dados ou escolha outro login.");
        }
    }

    private static void executarMenu(Scanner scanner, Barbearia barbearia, Usuario usuario) { // Exibe o menu de opções de acordo com o perfil do usuário logado
        boolean conectado = true;
        while (conectado) {
            String perfil = usuario.getPerfil();
            System.out.println("\n=== MENU " + perfil + " ===");
            // Cada perfil visualiza somente as opções permitidas para seu tipo de usuário.
            if ("CLIENTE".equals(perfil)) {
                System.out.println("1. Novo agendamento");
                System.out.println("2. Meus agendamentos");
                System.out.println("3. Ver serviços");
            } else if ("ADMINISTRADOR".equals(perfil)) {
                System.out.println("1. Novo agendamento");
                System.out.println("2. Listar agendamentos");
                System.out.println("3. Ver serviços");
                System.out.println("4. Cadastrar barbeiro");
            } else {
                System.out.println("1. Listar agendamentos");
                System.out.println("2. Cancelar agendamento");
            }
            System.out.println("0. Sair da conta");
            int opcao = lerOpcao(scanner, "Escolha: ");

            if ("CLIENTE".equals(perfil)) {                     // O cliente pode agendar, listar seus agendamentos ou ver os serviços disponíveis
                if (opcao == 1) {
                    agendar(scanner, barbearia, usuario, false);
                } else if (opcao == 2) {
                    barbearia.listarPorTelefone(usuario.getTelefone());
                } else if (opcao == 3) {
                    listarServicos(barbearia);
                } else if (opcao == 0) {
                    conectado = false;
                } else {
                    System.out.println("Opção inválida.");
                }
            } else if ("ADMINISTRADOR".equals(perfil)) {
                if (opcao == 1) {
                    agendar(scanner, barbearia, usuario, true);
                } else if (opcao == 2) {
                    barbearia.listar();
                } else if (opcao == 3) {
                    listarServicos(barbearia);
                } else if ("ADMINISTRADOR".equals(perfil) && opcao == 4) {
                    cadastrarBarbeiro(scanner, barbearia);
                } else if (opcao == 0) {
                    conectado = false;
                } else {
                    System.out.println("Opção inválida.");
                }
            } else if ("BARBEIRO".equals(perfil)) {
                if (opcao == 1) {
                    barbearia.listar();
                } else if (opcao == 2) {
                    barbearia.listar();
                    System.out.print("ID do agendamento para cancelar: ");
                    int id = lerOpcao(scanner, "");
                    if (barbearia.cancelarAgendamento(id)) {
                        System.out.println("Agendamento cancelado com sucesso.");
                    } else {
                        System.out.println("Agendamento não encontrado.");
                    }
                } else if (opcao == 0) {
                    conectado = false;
                } else {
                    System.out.println("Opção inválida.");
                }
            }
        }
    }

    private static void agendar( // Solicita informações para criar um novo agendamento, podendo ser feito pelo cliente ou pelo administrador
            Scanner scanner, Barbearia barbearia, Usuario usuario, boolean atendidoPorFuncionario) {
        String nome;
        String telefone;
        // Administradores informam os dados do cliente; clientes usam os dados da própria conta.
        if (atendidoPorFuncionario) {
            System.out.print("Nome do cliente: ");
            nome = scanner.nextLine();
            System.out.print("Telefone do cliente: ");
            telefone = scanner.nextLine();
        } else {
            nome = usuario.getNome();
            telefone = usuario.getTelefone();
        }

        listarServicos(barbearia);
        System.out.print("Serviço: ");
        String servico = scanner.nextLine();
        System.out.print("Data (ex: 15/10/2026): ");
        String data = scanner.nextLine();
        System.out.print("Horário (ex: 14:30): ");
        String horario = scanner.nextLine();

        if (barbearia.cadastrar(nome, telefone, servico, data, horario)) {
            System.out.println("Agendamento cadastrado com sucesso!");
        } else {
            System.out.println("Informações inválidas, por favor tente novamente.");
        }
    }

    private static void listarServicos(Barbearia barbearia) {
        System.out.println("\n--- SERVIÇOS ---");
        Servico[] servicos = barbearia.getServicos();
        for (Servico servico : servicos) {
            System.out.println(servico.getNome() + " - R$ " + servico.getPreco());
        }
    }

    private static int lerOpcao(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        String entrada = scanner.nextLine();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
