package at.springBoot.AtSpring.Controller;

import at.springBoot.AtSpring.Model.Aluno;
import at.springBoot.AtSpring.Service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @PostMapping
    public ResponseEntity<Aluno> createAluno(@RequestBody Aluno aluno) {
        Aluno savedAluno = alunoService.save(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAluno);
    }

    @GetMapping
    public ResponseEntity<List<Aluno>> getAllAlunos() {
        List<Aluno> alunos = alunoService.findAll();
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> getAlunoById(@PathVariable Long id) {
        Optional<Aluno> alunoOptional = alunoService.findById(id);
        return alunoOptional.map(aluno -> ResponseEntity.ok().body(aluno))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}