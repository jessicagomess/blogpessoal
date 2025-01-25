package com.generation.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

//A Classe Tema servirá de modelo para construir a tabela tb_temas (Entidade) dentro do nosso Banco de dados db_blogpessoal
@Entity
@Table(name = "tb_temas")
public class Tema {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "O Atributo Descrição é obrigatório")
	private String descricao;
	
	// A anotação @OneToMany indica que a Classe Tema será o lado 1:N e terá uma Collection List contendo Objetos da Classe Postagem
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tema", cascade = CascadeType.REMOVE) //A propriedade fetch define a estratégia de busca e carregamento dos dados das entidades relacionadas durante uma busca. l utilizaremos o tipo LAZY (preguiçosa), ou seja, ao carregarmos os dados de uma Postagem, ele não carregará os dados do Tema associado a cada Postagem até que os dados sejam solicitados. 
	//CascadeType.REMOVE: Quando um Objeto da Classe Tema for apagado, todos os Objetos da Classe Postagem associados ao Tema apagado
	@JsonIgnoreProperties("tema")
	private List<Postagem> postagem;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}

	
}