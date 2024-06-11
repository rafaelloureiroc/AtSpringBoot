package at.springBoot.AtSpring.Controller;


import at.springBoot.AtSpring.Model.Aluno;
import at.springBoot.AtSpring.Model.Disciplina;
import at.springBoot.AtSpring.Model.Matricula;
import at.springBoot.AtSpring.Request.MatriculaRequest;
import at.springBoot.AtSpring.Request.NotaRequest;
import at.springBoot.AtSpring.Service.AlunoService;
import at.springBoot.AtSpring.Service.DisciplinaService;
import at.springBoot.AtSpring.Service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @Autowired
    private DisciplinaService  disciplinaService;

    @Autowired
    private AlunoService alunoService;


    @PostMapping
    public ResponseEntity<Matricula> alocarAluno(@RequestBody MatriculaRequest request) {
        Long alunoId = request.getAlunoId();
        Long disciplinaId = request.getDisciplinaId();

        Optional<Aluno> alunoOptional = alunoService.findById(alunoId);
        Optional<Disciplina> disciplinaOptional = disciplinaService.findById(disciplinaId);

        if (alunoOptional.isPresent() && disciplinaOptional.isPresent()) {
            Matricula matricula = new Matricula();
            matricula.setAluno(alunoOptional.get());
            matricula.setDisciplina(disciplinaOptional.get());

            Matricula savedMatricula = matriculaService.save(matricula);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMatricula);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/nota")
    public ResponseEntity<Matricula> atribuirNota(@RequestBody NotaRequest request) {
        Long matriculaId = request.getMatriculaId();
        Double nota = request.getNota();

        Optional<Matricula> matriculaOptional = matriculaService.findById(matriculaId);

        if (matriculaOptional.isPresent()) {
            Matricula matricula = matriculaOptional.get();
            matricula.setNota(nota);

            Matricula savedMatricula = matriculaService.save(matricula);
            return ResponseEntity.ok(savedMatricula);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/aprovados")
    public ResponseEntity<List<Matricula>> getAprovados(@RequestParam Long disciplinaId) {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(disciplinaId);
        List<Matricula> aprovados = matriculaService.findAprovados(disciplina);
        return ResponseEntity.ok(aprovados);
    }

    @GetMapping("/reprovados")
    public ResponseEntity<List<Matricula>> getReprovados(@RequestParam Long disciplinaId) {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(disciplinaId);
        List<Matricula> reprovados = matriculaService.findReprovados(disciplina);
        return ResponseEntity.ok(reprovados);
    }
}