package at.springBoot.AtSpring.Controller;

import at.springBoot.AtSpring.Model.Disciplina;
import at.springBoot.AtSpring.Service.DisciplinaService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DisciplinaController.class)
@Import(TestSecurityConfig.class)
public class DisciplinaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DisciplinaService disciplinaService;

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void criarDisciplinaTest() throws Exception {
        Disciplina disciplina = new Disciplina();
        disciplina.setNome("Matematica");
        disciplina.setCodigo("MAT101");

        when(disciplinaService.save(any(Disciplina.class))).thenReturn(disciplina);

        mockMvc.perform(post("/disciplinas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "nome": "Matematica",
                          "codigo": "MAT101"
                        }"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(disciplina.getNome()))
                .andExpect(jsonPath("$.codigo").value(disciplina.getCodigo()));
    }

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void listarDisciplinaTest() throws Exception {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(1L);
        disciplina.setNome("Matematica");
        disciplina.setCodigo("MAT101");

        List<Disciplina> disciplinas = new ArrayList<>();
        disciplinas.add(disciplina);

        when(disciplinaService.findAll()).thenReturn(disciplinas);

        mockMvc.perform(get("/disciplinas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value(disciplina.getNome()))
                .andExpect(jsonPath("$[0].codigo").value(disciplina.getCodigo()));
    }

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void listarDisciplinaVaziaTest() throws Exception {
        when(disciplinaService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/disciplinas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void obterDisciplinaPorIdExistenteTest() throws Exception {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(1L);
        disciplina.setNome("Matematica");
        disciplina.setCodigo("MAT101");

        when(disciplinaService.findById(1L)).thenReturn(Optional.of(disciplina));

        mockMvc.perform(get("/disciplinas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(disciplina.getId()))
                .andExpect(jsonPath("$.nome").value(disciplina.getNome()))
                .andExpect(jsonPath("$.codigo").value(disciplina.getCodigo()));
    }

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void obterDisciplinaPorIdNaoExistenteTest() throws Exception {
        when(disciplinaService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/disciplinas/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void criarDisciplinaComDadosInvalidosTest() throws Exception {
        mockMvc.perform(post("/disciplinas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "nome": ,
                          "codigo": ""
                        }"""))
                .andExpect(status().isBadRequest());
    }
}