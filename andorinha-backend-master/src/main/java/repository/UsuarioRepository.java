package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;

public class UsuarioRepository extends AbstractCrudRepository {


	public void inserir(Usuario usuario) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		//abrir uma conexao com o banco
		try (Connection c = this.abrirConexao()) {

			//proximo valor da sequence
			int id = this.recuperarProximoValorDaSequence("seq_usuario");
			usuario.setId(id);

			//criar e executar a sql
			PreparedStatement ps = c.prepareStatement("insert into usuario (id, nome) values (?, ?)");
			ps.setInt(1, usuario.getId());
			ps.setString(2, usuario.getNome());
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao inserir usu�rio", e);
		}
	}
	
	//william
	public void atualizar(Usuario usuario) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = this.abrirConexao()) {
			
			PreparedStatement ps = c.prepareStatement("update usuario set nome = ? where id = ?");
			ps.setString(1, usuario.getNome());
			ps.setInt(2, usuario.getId());
			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao atualizar o usu�rio", e);
		}

	}
	

	public void remover(int id) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = this.abrirConexao()) {
			
			PreparedStatement ps = c.prepareStatement("delete from usuario where id = ?");
			ps.setInt(1, id);
			ps.executeUpdate();
			ps.close();
			
		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao deletar o usu�rio", e);
		}

	}
	
	public List<Usuario> listarTodos() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = this.abrirConexao()) {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			
			PreparedStatement ps = c.prepareStatement("select * from usuario;");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setNome(rs.getNString("nome"));
				usuarios.add(usuario);
			}
			ps.close();
			return usuarios;
		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao listar os usu�rios", e);
		}

		
	}
	//william
	
	public Usuario consultar(int id) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {

		try (Connection c = this.abrirConexao()) {

			Usuario user = null;

			PreparedStatement ps = c.prepareStatement("select id, nome from usuario where id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = new Usuario();
				user.setId( rs.getInt("id") );
				user.setNome( rs.getString("nome") );
			}
			rs.close();
			ps.close();

			return user;

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao consultar usu�rio", e);
		}
	}

}

/* Hallan
public class UsuarioRepository extends AbstractCrudRepository {

	public void inserir(Usuario usuario) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		//abrir uma conexao com o banco
		try (Connection c = this.abrirConexao()) {

			//proximo valor da sequence
			int id = this.recuperarProximoValorDaSequence("seq_usuario");
			usuario.setId(id);

			//criar e executar a sql
			PreparedStatement ps = c.prepareStatement("insert into usuario (id, nome) values (?, ?)");
			ps.setInt(1, usuario.getId());
			ps.setString(2, usuario.getNome());
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao inserir usu�rio", e);
		}
	}

	public void atualizar(Usuario usuario) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = this.abrirConexao()) {

			PreparedStatement ps = c.prepareStatement("update usuario set nome = ? where id = ?");
			ps.setString(1, usuario.getNome());
			ps.setInt(2, usuario.getId());
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao atualizar o usu�rio", e);
		}
	}

	public void remover(int id) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = this.abrirConexao()) {

			PreparedStatement ps = c.prepareStatement("delete from usuario where id = ?");
			ps.setInt(1, id);
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao remover o usu�rio", e);
		}
	}

	public Usuario consultar(int id) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {

		//abrir uma conexao com o banco
		try (Connection c = this.abrirConexao()) {

			Usuario user = null;

			//criar e executar a sql
			PreparedStatement ps = c.prepareStatement("select id, nome from usuario where id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = new Usuario();
				user.setId( rs.getInt("id") );
				user.setNome( rs.getString("nome") );
			}
			rs.close();
			ps.close();

			return user;

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao consultar usu�rio", e);
		}
	}

	public List<Usuario> listarTodos() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		try (Connection c = this.abrirConexao()) {

			List<Usuario> users = new ArrayList<>();

			PreparedStatement ps = c.prepareStatement("select * from usuario");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Usuario user = new Usuario();
				user.setId(rs.getInt("id"));
				user.setNome(rs.getString("nome"));

				users.add(user);
			}
			rs.close();
			ps.close();

			return users;

		} catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao listar todos os usu�rios", e);
		}
	}



}
*/
