/**
 * Classe que representa um agendamento de serviço.
 */

import java.time.LocalDate;
import java.time.LocalTime;

public class Agendamento {
    // Atributos do agendamento
    private final int id;
    private final String nome;
    private final String telefone;
    private final String servico;
    private final LocalDate data;
    private final LocalTime horario;
    
    public Agendamento(int id, String nome, String telefone, String servico,
        LocalDate data, LocalTime horario)
        // Construtor para inicializar um agendamento com os dados fornecidos
       {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.servico = servico;
        this.data = data;
        this.horario = horario;
    }
    // Getters para acessar os atributos do agendamento
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getServico() { return servico; }
    public LocalDate getData() { return data; }
    public LocalTime getHorario() { return horario; }
}
