package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

/* Camada responsável por responder todas Requisições (HTTP Request), que for enviada de fora da aplicação para o Recurso Postagem
 * Nessa classe, implementamos os métodos de CRUD (Create, Read, Update, Delete) utilizando os métodos da interface JpaRepository,
 * além de métodos personalizados para consultas, que são definidos na interface PostagemRepository.
 */

/* A anotação @RestController define a classe como controladora REST, responsável por processar requisições HTTP.
 * Ela lida com:- URL (endpoint), verbo HTTP (método) e, opcionalmente, corpo da requisição (dados para persistir).
 * Após processar, retorna um código de status HTTP e o resultado (dados) no corpo da resposta.
 */
@RestController

/* A anotação @RequestMapping define a URL base (ex: /postagens) para os métodos da classe controladora.
 * Quando uma requisição é feita para essa URL (ex: http://localhost:8080/postagens),
 * o Spring direciona a requisição para os métodos da classe PostagemController. 
 */
@RequestMapping("/postagens")

/* A anotação @CrossOrigin indica que a controladora permitirá o recebimento de requisições de fora do domínio localhost
 * é essencial para que o front-end ou aplicativo mobile, tenha acesso aos Métodos e Recursos da nossa aplicação 
 * (O termo técnico é consumir a API). 
 */
@CrossOrigin(origins = "*", allowedHeaders = "*") //em produção, recomenda-se substituir o * (asterisco) pelo endereço na nuvem (deploy) do Frontend ou da aplicação mobile.

public class PostagemController {

	@Autowired // É usado para "injetar" automaticamente dependências no Spring, ou seja, o Spring cria e gerencia objetos pra gente. Isso reduz a necessidade de criar manualmente objetos dentro das classes e facilita a manutenção do código.
	private PostagemRepository postagemRepository;
	
	//Para termos acesso aos Métodos das Classes Tema e TemaController, precisamos inserir uma uma Injeção de Dependência do Recurso Tema
	@Autowired
	private TemaRepository temaRepository;
	
	/* @GetMapping mapeia requisições HTTP GET para um endpoint específico. 
	 * No caso, ele diz que o método getAll() irá responder a requisições GET enviadas para http://localhost:8080/postagens/.
	 */
	@GetMapping 
	public ResponseEntity<List<Postagem>> getAll(){
		/*O método getAll() retorna um ResponseEntity com status 200 (OK).
	     No corpo da resposta (body), é retornada uma lista contendo todos os objetos da classe Postagem
	     */
		return ResponseEntity.ok(postagemRepository.findAll());
		/* O método findAll() da interface JpaRepository retorna todos os registros da tabela tb_postagens.
	    Mesmo se a lista estiver vazia, o status retornado será sempre 200 (OK). 
	    */
	}
	
	/* @GetMapping("/{id}") mapeia as Requisições HTTP GET para um endpoint específico.
	 * ele diz que o Método getById( Long id ), responderá a todas as requisições GET enviadar para http://localhost:8080/postagens/id, 
	 * onde id é uma Variável de Caminho (Path Variable), que receberá o id da Postagem que será Consultada
	*/
	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById(@PathVariable Long id) { // @PathVariable Long id: Esta anotação insere o valor enviado no endereço do endpoint, na Variável de Caminho {id}, no parâmetro do Método getById( Long id ); 
			return postagemRepository.findById(id) // Retorna a execução do Método findById(id), que é um Método padrão da Interface JpaRepository. O Método retornará um Objeto da Classe Postagem persistido no Banco de dados Postagem, caso ele seja encontrado a partir do parâmetro Long id.  
					.map(resposta -> ResponseEntity.ok(resposta)) //Se o Objeto da Classe Postagem for encontrado, o Método map (Optional), mapeia no Objeto resposta o Objeto Postagem retornado pelo Método findById(id), insere o Objeto mapeado no Corpo da Resposta do Método ResponseEntity.ok(resp); e retorna o HTTP Status OK🡪200.
					.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
			
	}
	
	// Método post(Postagem postagem), persistirá um novo Objeto da Classe Postagem no Banco de dados.
	/*@PostMapping 
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){ //@RequestBody Postagem postagem: Esta anotação recebe o Objeto do tipo Postagem, que foi enviado no Corpo da Requisição (Request Body), no formato JSON e insere no parâmetro postagem do Método post.
		// o Método possui um parâmetro, que é um Objeto da Classe Postagem, chamado postagem.
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(postagemRepository.save(postagem));
	}*/
	
	//MÉTODO POST ATUALIZADO
	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
		if(temaRepository.existsById(postagem.getTema().getId()))
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(postagemRepository.save(postagem));
		
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!", null);
	}
	
	// atualiza um Objeto da Classe Postagem persistido no Banco de dados.
	/*@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) {
		return postagemRepository.findById(postagem.getId())
				.map(resposta -> ResponseEntity.status(HttpStatus.OK)
						.body(postagemRepository.save(postagem)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}*/
	
	//MÉTODO POST ATUALIZADO
	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem){
		if (postagemRepository.existsById(postagem.getId())) {
			
			if (temaRepository.existsById(postagem.getTema().getId()))
				return ResponseEntity.status(HttpStatus.OK)
						.body(postagemRepository.save(postagem));
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!", null);
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	
	}
	//A anotação @ResponseStatus indica que o Método delete(Long id), terá um Status HTTP específico quando a Requisição for bem sucedida
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) { // O Método delete(@PathVariable Long id) foi definido com o tipo void, porque ao deletar um Objeto da Classe Postagem do Banco de dados, ela deixa de existir, logo não existe um Objeto para ser Retornado
		//@PathVariable Long id: Esta anotação insere o valor enviado no endereço do endpoint, na Variável de Caminho {id}, no parâmetro do Método delete( Long id 
		Optional<Postagem> postagem = postagemRepository.findById(id); {
			//Cria um Objeto Optional da Classe Postagem chamado postagem, que receberá o resultado do Método findById(id). Como o Método pode retornar um Objeto Nulo, caso ele não seja encontrado, utilizaremos um Objeto Optional para encapsular a resposta do Método findById(id) e evitar o erro NullPointerException.
			
			if(postagem.isEmpty())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			
			//Caso contrário, o Método padrão da Interface JpaRepository deleteById(Long id) será executado e o HTTP Status NO_CONTENT 🡪 204, HTTP Status padrão do Método, será enviado na Resposta da Requisição, indicando que o Objeto foi Excluído.
			postagemRepository.deleteById(id);
		}
	}
	
}


