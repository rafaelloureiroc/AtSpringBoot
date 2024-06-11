package at.springBoot.AtSpring.Controller;

import at.springBoot.AtSpring.Model.Aluno;
import at.springBoot.AtSpring.Model.Disciplina;
import at.springBoot.AtSpring.Service.DisciplinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @PostMapping
    public ResponseEntity<Disciplina> createDisciplina(@RequestBody Disciplina disciplina) {
        Disciplina savedDisciplina = disciplinaService.save(disciplina);
        return new ResponseEntity<>(savedDisciplina, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Disciplina>> getAllDisciplinas() {
        List<Disciplina> disciplinas = disciplinaService.findAll();
        return new ResponseEntity<>(disciplinas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disciplina> getDisciplinaById(@PathVariable Long id) {
        Optional<Disciplina> disciplinaOptional = disciplinaService.findById(id);
        return disciplinaOptional.map(disciplina -> ResponseEntity.ok().body(disciplina))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}