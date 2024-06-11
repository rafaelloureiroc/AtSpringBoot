package at.springBoot.AtSpring;

import at.springBoot.AtSpring.Controller.*;
import at.springBoot.AtSpring.Service.AlunoService;
import at.springBoot.AtSpring.Service.DisciplinaService;
import at.springBoot.AtSpring.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({AlunoController.class, DisciplinaController.class})
@Import(TestSecurityConfig.class)
class AtSpringApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AlunoService alunoService;

	@MockBean
	private DisciplinaService disciplinaService;

	@MockBean
	private AlunoControllerTest alunoControllerTest;

	@MockBean
	private DisciplinaControllerTest disciplinaControllerTest;

	@MockBean
	private MatriculaControllerTest matriculaControllerTest;

	@Test
	@WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
	void testAlunoController() throws Exception {
		alunoControllerTest.criarAlunoTest();
		alunoControllerTest.criarAlunoDadosInvalidosTest();
		alunoControllerTest.listarAlunosTest();
		alunoControllerTest.listarAlunosVazioTest();
		alunoControllerTest.obterAlunoPorIdTest();
		alunoControllerTest.obterAlunoPorIdNaoEncontradoTest();


	}

	@Test
	@WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
	void testDisciplinaController() throws Exception {
		disciplinaControllerTest.criarDisciplinaTest();
		disciplinaControllerTest.listarDisciplinaTest();
		disciplinaControllerTest.listarDisciplinaVaziaTest();
		disciplinaControllerTest.obterDisciplinaPorIdExistenteTest();
		disciplinaControllerTest.obterDisciplinaPorIdNaoExistenteTest();
		disciplinaControllerTest.criarDisciplinaComDadosInvalidosTest();

	}

	@Test
	@WithMockUser(username = "Professor", password = "Infnet", roles = "ADMIN")
	void testmatriculaController() throws Exception {
		matriculaControllerTest.criarMatriculaTest();
		matriculaControllerTest.criarMatriculaAlunoNaoEncontradoTest();
		matriculaControllerTest.criarMatriculaDisciplinaNaoEncontradaTest();
		matriculaControllerTest.atribuirNotaTest();
		matriculaControllerTest.atribuirNotaMatriculaNaoEncontradaTest();
		matriculaControllerTest.listarAprovadosTest();
		matriculaControllerTest.listarReprovadosTest();
	}


}