package at.springBoot.AtSpring.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Aluno aluno;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Disciplina disciplina;

    private Double nota;


}
