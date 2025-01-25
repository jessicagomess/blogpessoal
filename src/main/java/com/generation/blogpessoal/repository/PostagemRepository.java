package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.blogpessoal.model.Postagem;

//Query Method são Métodos de Consulta personalizados, que permitem criar consultas específicas com qualquer Atributo da Classe associada a Interface Repositório (Postagem). Como a Interface JpaRepository possui apenas um Método de consulta específico pelo id (findById( Long id)), que é um Atributo comum em todas as Classes Model do Projeto, através das Query Methods podemos ampliar as nossas opções de consulta.
public interface PostagemRepository extends JpaRepository<Postagem, Long> {
	
	public List <Postagem> findAllByTituloContainingIgnoreCase(@Param("titulo") String titulo);
}
