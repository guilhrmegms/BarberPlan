public class Servico { // Classe que representa um serviço oferecido pela barbearia.
    private final String nome;
    private final double preco;

    public Servico(String nome, double preco) { // Construtor para inicializar um serviço com nome e preço
        this.nome = nome;
        this.preco = preco;
    }


    /* Getters para acessar os atributos do serviço
     */


    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }
}
