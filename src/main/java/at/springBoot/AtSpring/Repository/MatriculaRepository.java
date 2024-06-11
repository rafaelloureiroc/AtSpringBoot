package at.springBoot.AtSpring.Repository;

import at.springBoot.AtSpring.Model.Disciplina;
import at.springBoot.AtSpring.Model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    List<Matricula> findByDisciplinaAndNotaGreaterThanEqual(Disciplina disciplina, Double nota);
    List<Matricula> findByDisciplinaAndNotaLessThan(Disciplina disciplina, Double nota);



}