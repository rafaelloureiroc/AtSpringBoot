package at.springBoot.AtSpring.Controller;

import at.springBoot.AtSpring.Model.Aluno;
import at.springBoot.AtSpring.Model.Disciplina;
import at.springBoot.AtSpring.Model.Matricula;
import at.springBoot.AtSpring.Service.AlunoService;
import at.springBoot.AtSpring.Service.DisciplinaService;
import at.springBoot.AtSpring.Service.MatriculaService;
import at.springBoot.AtSpring.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(MatriculaController.class)
@Import(TestSecurityConfig.class)
public class MatriculaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlunoService alunoService;

    @MockBean
    private DisciplinaService disciplinaService;

    @MockBean
    private MatriculaService matriculaService;

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void criarMatriculaTest() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Aluno Teste");

        Disciplina disciplina = new Disciplina();
        disciplina.setId(1L);
        disciplina.setNome("Matematica");

        Matricula matricula = new Matricula();
        matricula.setAluno(aluno);
        matricula.setDisciplina(disciplina);

        when(alunoService.findById(1L)).thenReturn(Optional.of(aluno));
        when(disciplinaService.findById(1L)).thenReturn(Optional.of(disciplina));
        when(matriculaService.save(any(Matricula.class))).thenReturn(matricula);

        mockMvc.perform(post("/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "alunoId": 1,
                          "disciplinaId": 1
                        }"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.aluno.nome").value(aluno.getNome()))
                .andExpect(jsonPath("$.disciplina.nome").value(disciplina.getNome()));
    }

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void criarMatriculaAlunoNaoEncontradoTest() throws Exception {
        when(alunoService.findById(1L)).thenReturn(Optional.empty());
        when(disciplinaService.findById(1L)).thenReturn(Optional.of(new Disciplina()));

        mockMvc.perform(post("/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "alunoId": 1,
                          "disciplinaId": 1
                        }"""))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void criarMatriculaDisciplinaNaoEncontradaTest() throws Exception {
        when(alunoService.findById(1L)).thenReturn(Optional.of(new Aluno()));
        when(disciplinaService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "alunoId": 1,
                          "disciplinaId": 1
                        }"""))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void atribuirNotaTest() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Aluno Teste");

        Disciplina disciplina = new Disciplina();
        disciplina.setId(1L);
        disciplina.setNome("Matematica");

        Matricula matricula = new Matricula();
        matricula.setId(1L);
        matricula.setAluno(aluno);
        matricula.setDisciplina(disciplina);
        matricula.setNota(8.0);

        when(matriculaService.findById(1L)).thenReturn(Optional.of(matricula));
        when(matriculaService.save(any(Matricula.class))).thenReturn(matricula);

        mockMvc.perform(post("/matriculas/nota")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "matriculaId": 1,
                          "nota": 8.0
                        }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nota").value(8.0));
    }

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void atribuirNotaMatriculaNaoEncontradaTest() throws Exception {
        when(matriculaService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/matriculas/nota")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "matriculaId": 1,
                          "nota": 8.0
                        }"""))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void listarAprovadosTest() throws Exception {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(1L);
        disciplina.setNome("Matematica");

        Aluno aluno1 = new Aluno();
        aluno1.setId(1L);
        aluno1.setNome("Aluno Aprovado");
        Matricula matriculaAprovado = new Matricula();
        matriculaAprovado.setAluno(aluno1);
        matriculaAprovado.setDisciplina(disciplina);
        matriculaAprovado.setNota(8.0);

        List<Matricula> aprovados = new ArrayList<>();
        aprovados.add(matriculaAprovado);

        when(matriculaService.findAprovados(any(Disciplina.class))).thenReturn(aprovados);

        mockMvc.perform(get("/matriculas/aprovados")
                        .param("disciplinaId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].aluno.nome").value(aluno1.getNome()));
    }

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void listarReprovadosTest() throws Exception {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(1L);
        disciplina.setNome("Matematica");

        Aluno aluno2 = new Aluno();
        aluno2.setId(2L);
        aluno2.setNome("Aluno Reprovado");
        Matricula matriculaReprovado = new Matricula();
        matriculaReprovado.setAluno(aluno2);
        matriculaReprovado.setDisciplina(disciplina);
        matriculaReprovado.setNota(5.0);

        List<Matricula> reprovados = new ArrayList<>();
        reprovados.add(matriculaReprovado);

        when(matriculaService.findReprovados(any(Disciplina.class))).thenReturn(reprovados);

        mockMvc.perform(get("/matriculas/reprovados")
                        .param("disciplinaId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].aluno.nome").value(aluno2.getNome()));
    }
}