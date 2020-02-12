package br.com.alura.instalura.dtos.outputs;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.alura.instalura.models.Foto;
import br.com.alura.instalura.models.Usuario;

public class FotoResponse {

	private final String urlPerfil;
	private final String loginUsuario;
	private final String horario;
	private final String urlFoto;
	private final Integer id;
	private boolean likeada;
	private Set<LikerResponse> likers = new HashSet<>();
	private List<ComentarioResponse> comentarios = new ArrayList<>();
	private String comentario;

	public FotoResponse(Foto foto, Usuario usuarioLogado){
		this.urlPerfil = foto.getUsuario().getUrlFotoPerfil();
		this.loginUsuario= foto.getUsuario().getLogin();
		this.horario = foto.getInstante().format(DateTimeFormatter.ofPattern("dd/MM/yyyy kk:mm"));
		this.urlFoto = foto.getUrl();
		this.id = foto.getId();
		this.comentario = foto.getComentario();
		this.likeada = !foto.getLikers().stream()
				.filter(liker -> liker.getLogin().equals(usuarioLogado.getLogin()))
				.collect(Collectors.toList())
				.isEmpty();
		
		this.likers.addAll(LikerResponse.map(foto.getLikers()));
		this.comentarios.addAll(ComentarioResponse.map(foto.getComentarios()));
	}
	
	public String getComentario() {
		return comentario;
	}

	public String getUrlPerfil() {
		return urlPerfil;
	}

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public String getHorario() {
		return horario;
	}

	public String getUrlFoto() {
		return urlFoto;
	}
	
	public Integer getId() {
		return id;
	}
	
	public boolean isLikeada() {
		return likeada;
	}
	
	public Set<LikerResponse> getLikers() {
		return likers;
	}
	
	public List<ComentarioResponse> getComentarios() {
		return comentarios;
	}
	
	public static List<FotoResponse> map(List<Foto> fotos, Usuario usuarioLogado) {
		return fotos.stream()
				.map(foto -> new FotoResponse(foto, usuarioLogado))
				.collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return "FotoResponse [urlPerfil=" + urlPerfil + ", loginUsuario=" + loginUsuario + ", horario=" + horario
				+ ", urlFoto=" + urlFoto + ", id=" + id + ", likeada=" + likeada + ", likers=" + likers
				+ ", comentarios=" + comentarios + ", comentario=" + comentario + "]";
	}
	
	
}
