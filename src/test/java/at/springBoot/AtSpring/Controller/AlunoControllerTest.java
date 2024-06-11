package at.springBoot.AtSpring.Controller;

import at.springBoot.AtSpring.Model.Aluno;
import at.springBoot.AtSpring.Service.AlunoService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlunoController.class)
@Import(TestSecurityConfig.class)
public class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlunoService alunoService;

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void criarAlunoTest() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setNome("Rafael");
        aluno.setCpf("123.456.789-00");
        aluno.setEmail("Rafael@example.com");
        aluno.setTelefone("(21) 1234-5678");
        aluno.setEndereco("Rua dos Testes, 123");

        when(alunoService.save(any(Aluno.class))).thenReturn(aluno);

        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "nome": "Rafael",
                          "cpf": "123.456.789-00",
                          "email": "Rafael@example.com",
                          "telefone": "(21) 1234-5678",
                          "endereco": "Rua dos Testes, 123"
                        }"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(aluno.getNome()))
                .andExpect(jsonPath("$.cpf").value(aluno.getCpf()))
                .andExpect(jsonPath("$.email").value(aluno.getEmail()))
                .andExpect(jsonPath("$.telefone").value(aluno.getTelefone()))
                .andExpect(jsonPath("$.endereco").value(aluno.getEndereco()));
    }

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void criarAlunoDadosInvalidosTest() throws Exception {
        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "nome": ,
                          "cpf": ,
                          "email": ,
                          "telefone": ,
                          "endereco": ""
                        }"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void listarAlunosTest() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Rafael");
        aluno.setCpf("12345678901");
        aluno.setEmail("Rafael@example.com");
        aluno.setTelefone("123456789");
        aluno.setEndereco("Rua Teste, 123");

        List<Aluno> alunos = new ArrayList<>();
        alunos.add(aluno);

        when(alunoService.findAll()).thenReturn(alunos);

        mockMvc.perform(get("/alunos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(aluno.getId()))
                .andExpect(jsonPath("$[0].nome").value(aluno.getNome()))
                .andExpect(jsonPath("$[0].cpf").value(aluno.getCpf()))
                .andExpect(jsonPath("$[0].email").value(aluno.getEmail()))
                .andExpect(jsonPath("$[0].telefone").value(aluno.getTelefone()))
                .andExpect(jsonPath("$[0].endereco").value(aluno.getEndereco()));
    }

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void listarAlunosVazioTest() throws Exception {
        when(alunoService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/alunos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void obterAlunoPorIdTest() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Rafael");
        aluno.setCpf("12345678901");
        aluno.setEmail("Rafael@example.com");
        aluno.setTelefone("123456789");
        aluno.setEndereco("Rua Teste, 123");

        when(alunoService.findById(1L)).thenReturn(Optional.of(aluno));

        mockMvc.perform(get("/alunos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(aluno.getId()))
                .andExpect(jsonPath("$.nome").value(aluno.getNome()))
                .andExpect(jsonPath("$.cpf").value(aluno.getCpf()))
                .andExpect(jsonPath("$.email").value(aluno.getEmail()))
                .andExpect(jsonPath("$.telefone").value(aluno.getTelefone()))
                .andExpect(jsonPath("$.endereco").value(aluno.getEndereco()));
    }

    @Test
    @WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
    public void obterAlunoPorIdNaoEncontradoTest() throws Exception {
        when(alunoService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/alunos/1"))
                .andExpect(status().isNotFound());
    }
}