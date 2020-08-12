 package repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import javax.ejb.EJB;
import javax.persistence.RollbackException;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import model.seletor.UsuarioSeletor;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TestUsuarioRepository {

	private static final int ID_USUARIO_CONSULTA = 1;
	private static final int ID_USUARIO_SEM_TWEET = 5;

	@EJB
	private UsuarioRepository usuarioRepository;

	@Before
	public void setUp() {
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
	}

	@Test
	public void testa_se_usuario_foi_inserido() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = new Usuario();
		user.setNome("Usuario novo");
		this.usuarioRepository.inserir(user);

		Usuario inserido = this.usuarioRepository.consultar(user.getId());

		assertThat( user.getId() ).isGreaterThan(0);

		assertThat( inserido ).isNotNull();
		assertThat( inserido.getNome() ).isEqualTo(user.getNome());
		assertThat( inserido.getId() ).isEqualTo(user.getId());
	}

	@Test
	public void testa_consultar_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);

		assertThat( user ).isNotNull();
		assertThat( user.getNome() ).isEqualTo("Usu�rio 1");
		assertThat( user.getId() ).isEqualTo(ID_USUARIO_CONSULTA);
	}

	@Test
	public void testa_alterar_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);
		user.setNome("Alterado!");

		this.usuarioRepository.atualizar(user);

		Usuario alterado = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);

		assertThat( alterado ).isEqualToComparingFieldByField(user);
	}

	@Test
	public void testa_remover_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = this.usuarioRepository.consultar(ID_USUARIO_SEM_TWEET);
		assertThat( user ).isNotNull();

		this.usuarioRepository.remover(ID_USUARIO_SEM_TWEET);

		Usuario removido = this.usuarioRepository.consultar(ID_USUARIO_SEM_TWEET);
		assertThat( removido ).isNull();
	}

	@Test
	public void testa_remover_usuario_com_tweet() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		assertThatThrownBy(() -> { this.usuarioRepository.remover(ID_USUARIO_CONSULTA); })
			.hasCauseInstanceOf(RollbackException.class);
	}

	@Test
	public void testa_listar_todos_os_usuarios() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		List<Usuario> usuarios = this.usuarioRepository.listarTodos();

		assertThat( usuarios ).isNotNull()
							.isNotEmpty()
							.hasSize(10)
							.extracting("nome")
							.containsExactlyInAnyOrder("Usu�rio 1", "Usu�rio 2",
			                        "Usu�rio 3", "Usu�rio 4", "Usu�rio 5", "Jo�o", "Jos�", "Maria", "Ana", "Joselito");
	}

	@Test
	public void testa_pesquisar_usuarios_por_nome() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		UsuarioSeletor seletor = new UsuarioSeletor();
		seletor.setNome("Jo");
		List<Usuario> usuarios = this.usuarioRepository.pesquisar(seletor);

		assertThat( usuarios ).isNotNull()
							.isNotEmpty()
							.hasSize(3)
							.extracting("nome")
							.containsExactlyInAnyOrder("Jo�o", "Jos�", "Joselito");
	}

	@Test
	public void testa_contar_usuarios_por_nome() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		UsuarioSeletor seletor = new UsuarioSeletor();
		seletor.setNome("Usu�rio");
		Long total = this.usuarioRepository.contar(seletor);

		assertThat( total ).isNotNull()
							.isEqualTo(5L);
	}

	@Test
	public void testa_pesquisar_usuarios_por_id() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		UsuarioSeletor seletor = new UsuarioSeletor();
		seletor.setId(3);
		List<Usuario> usuarios = this.usuarioRepository.pesquisar(seletor);

		assertThat( usuarios ).isNotNull()
							.isNotEmpty()
							.hasSize(1)
							.extracting("nome")
							.containsExactly("Usu�rio 3");
	}


	@Test
	public void testa_consultar_usuario_trazerndo_tweets() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);

		assertThat( user ).isNotNull();
		assertThat( user.getNome() ).isEqualTo("Usu�rio 1");
		assertThat( user.getId() ).isEqualTo(ID_USUARIO_CONSULTA);

		//assertThat( user.getTweets() ).isNotNull().isNotEmpty();
	}


}