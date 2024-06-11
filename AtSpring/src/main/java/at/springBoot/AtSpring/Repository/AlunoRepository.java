package at.springBoot.AtSpring.Repository;

import at.springBoot.AtSpring.Model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository <Aluno, Long> {}
