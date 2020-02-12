package br.com.alura.instalura.controllers;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.instalura.models.Foto;
import br.com.alura.instalura.models.Usuario;

@RestController
@Transactional
public class GeracaoDeCoisasController {

	@PersistenceContext
	private EntityManager em;

	private List<Usuario> geraUsuariosEAmigos() {
		Usuario alberto = new Usuario("alots", "123456","https://s3.amazonaws.com/caelum-online-public/react-native-parte-2/images/adittional-resources/profile-photo-alberto.jpg");
		Usuario rafael = new Usuario("rafael", "123456","https://s3.amazonaws.com/caelum-online-public/react-native-parte-2/images/adittional-resources/profile-photo-rafael.jpg");
		Usuario vitor = new Usuario("vitor", "123456","https://s3.amazonaws.com/caelum-online-public/react-native-parte-2/images/adittional-resources/profile-photo-vitor.jpg");
		
		
		alberto.adicionaAmigo(rafael);
		alberto.adicionaAmigo(vitor);
		rafael.adicionaAmigo(vitor);
		vitor.adicionaAmigo(alberto);
		
		em.persist(alberto);
		em.persist(rafael);
		em.persist(vitor);
		
		return Arrays.asList(alberto,rafael,vitor);
	}

	
	private void geraFotos(Integer usuarioId) {
		Usuario usuario = em.find(Usuario.class, usuarioId);
		Foto foto1 = new Foto(
				"https://s3.amazonaws.com/caelum-online-public/react-native-parte-2/images/adittional-resources/photo-1.jpg",
				"Legenda da foto",
				usuario);
		Foto foto2 = new Foto(
				"https://s3.amazonaws.com/caelum-online-public/react-native-parte-2/images/adittional-resources/photo-2.jpg",
				"Legenda da foto",
				usuario);
		
		em.persist(foto1);
		em.persist(foto2);
	}
	
	@GetMapping("/gera/dados")
	public String geraDados(){
		List usuariosRegistrados = em.createQuery("select u from Usuario u").getResultList();
		if(!usuariosRegistrados.isEmpty()){
			return "<p>Opa, você já gerou os dados, não precisa rodar essa url de novo</p>";
		}
		List<Usuario> usuarios = geraUsuariosEAmigos();
		for (Usuario usuario : usuarios) {
			geraFotos(usuario.getId());
		}
		return "<p>os dados foram gerados e inseridos no banco de dados <b>instalura</b></p>";
	}
}
