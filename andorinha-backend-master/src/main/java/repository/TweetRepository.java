package repository;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Tweet;

import model.seletor.TweetSeletor;


@Stateless
public class TweetRepository extends AbstractCrudRepository {

	public void inserir(Tweet tweet) {
		tweet.setData(Calendar.getInstance());
		super.em.persist(tweet);
	}

	public void atualizar(Tweet tweet) {
		tweet.setData(Calendar.getInstance());
		super.em.merge(tweet);
	}

	public void remover(int id) {
		
		Tweet tweet = this.consultar(id);
		super.em.remove(tweet);
	}

	public Tweet consultar(int id) {
		
		return super.em.find(Tweet.class, id);
	}

	public List<Tweet> listarTodos() {
		
		return this.pesquisar( new TweetSeletor() );
	}
	
	public List<Tweet> pesquisar(TweetSeletor seletor) {
		
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT t FROM Tweet t JOIN t.usuario u ");
		
		this.criarFiltro(jpql, seletor);
		
		Query query = super.em.createQuery(jpql.toString());
		
		this.adicionarParametros(query, seletor);
		
		return query.getResultList();
	}
	
	public Long contar(TweetSeletor seletor) {
		
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT COUNT(t) FROM Tweet t ");
		
		this.criarFiltro(jpql, seletor);
		
		Query query = super.em.createQuery(jpql.toString());
		
		this.adicionarParametros(query, seletor);
		
		return (Long)query.getSingleResult();
	}
	
	private void criarFiltro(StringBuilder jpql, TweetSeletor seletor) {
		
		if (seletor.possuiFiltro()) {
			
			jpql.append("WHERE ");
			boolean primeiro = true;
			if ( seletor.getId() != null ) {
				
				jpql.append("t.id = :id ");
				primeiro = false;
			}
			
			if (seletor.getIdUsuario() != null) {
				if (!primeiro) {
					
					jpql.append("AND ");
				}else {
					primeiro = false;
				}
				
				jpql.append("t.usuario.id  = :id_usuario ");
			}
			
			if(seletor.getConteudo() != null && !seletor.getConteudo().trim().isEmpty()) {
				if(!primeiro) {
					
					jpql.append("AND ");
				}else {
					primeiro = false;
				}
				
				jpql.append("t.conteudo LIKE :conteudo ");
			}
			
			if (seletor.getData() != null) {
				if (!primeiro) {
					
					jpql.append("AND ");
				}else {
					primeiro = false;
				}
				
				jpql.append("date(t.data) = :data_postagem ");
			}
		}
	}

	private void adicionarParametros(Query query, TweetSeletor seletor) {

		if (seletor.possuiFiltro()) {
			if ( seletor.getId() != null ) {
				query.setParameter("id", seletor.getId());
			}

			if (seletor.getConteudo() != null && !seletor.getConteudo().trim().isEmpty() ) {
				query.setParameter("conteudo", String.format("%%%s%%", seletor.getConteudo()) );
			}
			
			if (seletor.getData() != null) {
				query.setParameter("data_postagem", seletor.getData().getTimeInMillis());
			}
			
			if (seletor.getIdUsuario() != null) {
				query.setParameter("id_usuario", seletor.getIdUsuario());
			}
		}
	}
}
