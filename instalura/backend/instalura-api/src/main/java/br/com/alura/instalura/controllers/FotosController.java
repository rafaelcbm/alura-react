package br.com.alura.instalura.controllers;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.instalura.daos.ComentarioDao;
import br.com.alura.instalura.daos.FotoDao;
import br.com.alura.instalura.dtos.inputs.ComentarioForm;
import br.com.alura.instalura.dtos.outputs.ComentarioResponse;
import br.com.alura.instalura.dtos.outputs.FotoResponse;
import br.com.alura.instalura.dtos.outputs.LikerResponse;
import br.com.alura.instalura.models.Comentario;
import br.com.alura.instalura.models.Foto;
import br.com.alura.instalura.models.Usuario;

@RestController
public class FotosController {

	@Autowired
	private FotoDao fotoDao;
	
	@Autowired
	private ComentarioDao comentarioDao;

	@GetMapping(value = "/api/fotos", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FotoResponse> busca(@AuthenticationPrincipal Usuario logado) {
		List<Foto> fotos = fotoDao.buscaFotosDosAmigos(logado.getId());
		return FotoResponse.map(fotos, logado);
	}

	@Transactional
	@PostMapping(value = "/api/fotos/{idFoto}/like", produces = MediaType.APPLICATION_JSON_VALUE)
	public LikerResponse like(@PathVariable("idFoto") Integer id,@AuthenticationPrincipal Usuario logado) {
		Foto foto = fotoDao.findOne(id);		
		foto.toggleLike(logado);
		return new LikerResponse(logado);
	}

	@Transactional
	@PostMapping(value = "/api/fotos/{idFoto}/comment", 
		consumes = MediaType.APPLICATION_JSON_VALUE, 
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ComentarioResponse comment(@RequestBody ComentarioForm comentarioForm, @PathVariable("idFoto") Integer idFoto,@AuthenticationPrincipal Usuario logado ) {
		Comentario comentario = comentarioForm.build(logado);
		comentarioDao.save(comentario);
		
		Foto foto = fotoDao.findOne(idFoto);
		foto.adicionaComentario(comentario);

		return new ComentarioResponse(comentario);
	}

	@GetMapping(value="/api/public/fotos/{amigo}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("permitAll")
	public List<FotoResponse> buscaPorLogin(@AuthenticationPrincipal Usuario usuario,
			@PathVariable("amigo") String amigo){
		if(usuario == null) {
			usuario = new Usuario("1 Usuário Anônimo", "", "");
		}
		
		return FotoResponse.map(fotoDao.buscaFotosPeloUsuario(amigo), usuario);
	}

}
