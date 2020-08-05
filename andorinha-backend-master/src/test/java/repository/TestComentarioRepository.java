package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.List;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Comentario;
import model.Tweet;
import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TestComentarioRepository {

	private static final int ID_TWEET_CONSULTA = 1;
	private static final int ID_COMENTARIO_CONSULTA = 1;
	private static final int ID_USUARIO_CONSULTA = 1;

	private static final long DELTA_MILIS = 500;

	private UsuarioRepository usuarioRepository;
	private TweetRepository tweetRepository;
	private ComentarioRepository comentarioRepository;

	@Before
	public void setUp() {
		this.usuarioRepository = new UsuarioRepository();
		this.tweetRepository = new TweetRepository();
		this.comentarioRepository = new ComentarioRepository();
	}

	@Test
	public void testa_se_comentario_foi_inserido() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Usuario user = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);
		Tweet tweet = this.tweetRepository.consultar(ID_TWEET_CONSULTA);

		Comentario c = new Comentario();
		c.setConteudo("Meu coment�rio de teste");
		c.setUsuario(user);
		c.setTweet(tweet);

		this.comentarioRepository.inserir(c);

		assertThat( c.getId() ).isGreaterThan(0);

		Comentario inserido = this.comentarioRepository.consultar(c.getId());

		assertThat( inserido ).isNotNull();
		assertThat( inserido.getConteudo() ).isEqualTo(c.getConteudo());
		assertThat( Calendar.getInstance().getTime() )
			.isCloseTo(inserido.getData().getTime(), DELTA_MILIS);
	}

	@Test
	public void testa_consultar_comentario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Comentario c =  this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);

		assertThat( c ).isNotNull();
		assertThat( c.getConteudo() ).isEqualTo("Coment�rio 1");
		assertThat( c.getId() ).isEqualTo(ID_COMENTARIO_CONSULTA);
		assertThat( c.getUsuario() ).isNotNull();
		assertThat( c.getTweet() ).isNotNull();
		assertThat( c.getTweet().getUsuario() ).isNotNull();
	}

	@Test
	public void testa_alterar_comentario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Comentario c =  this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);
		c.setConteudo("Alterado!");

		this.comentarioRepository.atualizar(c);

		Comentario alterado = this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);

		assertThat( alterado.getConteudo() ).isEqualTo(c.getConteudo());
		assertThat( Calendar.getInstance().getTime() )
			.isCloseTo(alterado.getData().getTime(), DELTA_MILIS);
	}

	@Test
	public void testa_remover_comentario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		Comentario c =  this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);
		assertThat( c ).isNotNull();

		this.comentarioRepository.remover(ID_COMENTARIO_CONSULTA);

		Comentario removido =  this.comentarioRepository.consultar(ID_COMENTARIO_CONSULTA);
		assertThat( removido ).isNull();
	}

	@Test
	public void testa_listar_todos_os_comentarios() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		List<Comentario> comentarios = this.comentarioRepository.listarTodos();

		assertThat( comentarios ).isNotNull()
							.isNotEmpty()
							.hasSize(10)
							.extracting("conteudo")
							.containsExactlyInAnyOrder("Coment�rio 1", "Coment�rio 2", "Coment�rio 3", "Coment�rio 4", "Coment�rio 5",
														"Coment�rio 6", "Coment�rio 7", "Coment�rio 8", "Coment�rio 9", "Coment�rio 10");

		comentarios.stream().forEach(t -> {
			assertThat(t.getData()).isNotNull().isLessThan(Calendar.getInstance());
			assertThat(t.getUsuario()).isNotNull();
			assertThat(t.getTweet()).isNotNull();
			assertThat(t.getTweet().getUsuario()).isNotNull();
		});
	}


}