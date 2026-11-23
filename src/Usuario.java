public class Usuario { // Classe que representa um usuário do sistema, seja cliente, administrador ou barbeiro.
    private String nome;
    private String telefone;
    private String login;
    private String senha;
    private String perfil;

    public Usuario(String nome, String telefone, String login, String senha, String perfil) { // Construtor para inicializar um usuário com os dados fornecidos
        this.nome = nome;
        this.telefone = telefone;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    public boolean autenticar(String login, String senha) {
        // O login ignora maiúsculas, enquanto a senha precisa ser escrita exatamente.
        return this.login.equalsIgnoreCase(login) && this.senha.equals(senha);
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getLogin() {
        return login;
    }

    public String getPerfil() {
        return perfil;
    }
}
