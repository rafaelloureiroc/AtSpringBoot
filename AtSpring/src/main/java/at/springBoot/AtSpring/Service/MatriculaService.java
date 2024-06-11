package at.springBoot.AtSpring.Service;

import at.springBoot.AtSpring.Model.Disciplina;
import at.springBoot.AtSpring.Model.Matricula;
import at.springBoot.AtSpring.Repository.AlunoRepository;
import at.springBoot.AtSpring.Repository.DisciplinaRepository;
import at.springBoot.AtSpring.Repository.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    public Matricula save(Matricula matricula) {
        return matriculaRepository.save(matricula);
    }

    public List<Matricula> findAprovados(Disciplina disciplina) {
        return matriculaRepository.findByDisciplinaAndNotaGreaterThanEqual(disciplina, 7.0);
    }

    public List<Matricula> findReprovados(Disciplina disciplina) {
        return matriculaRepository.findByDisciplinaAndNotaLessThan(disciplina, 7.0);
    }

    public Optional<Matricula> findById(Long id) {
        return matriculaRepository.findById(id);
    }
}