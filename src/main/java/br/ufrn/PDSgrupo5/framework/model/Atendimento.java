package br.ufrn.PDSgrupo5.framework.model;

import javax.persistence.*;

@Entity
@Table(name = "atendimento")
public class Atendimento extends EntidadeAbstrata {
	
	@OneToOne(cascade = CascadeType.MERGE)
	private HorarioAtendimento horarioAtendimento;

	private String titulo;

	private String descricao;

	@Column(columnDefinition = "boolean default false")
	private Boolean confirmado = false;

	@OneToOne
	private Cliente cliente;

	@OneToOne(cascade = CascadeType.MERGE)
	private Profissional profissional;

	@Column(name="tipo_atendimento")
	private String tipoAtendimento;
	
	@Column(name="requer_notificacao")
	private Boolean requerNotificacao = true;

	public Atendimento() {
	}

	public HorarioAtendimento getHorarioAtendimento() {
		return horarioAtendimento;
	}

	public void setHorarioAtendimento(HorarioAtendimento horarioAtendimento) {
		this.horarioAtendimento = horarioAtendimento;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getConfirmado() {
		return confirmado;
	}

	public void setConfirmado(Boolean status) {
		this.confirmado = status;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	public String getTipoAtendimento() {
		return tipoAtendimento;
	}

	public void setTipoAtendimento(String tipoAtendimento) {
		this.tipoAtendimento = tipoAtendimento;
	}
	
	public Boolean getRequerNotificacao() {
		return requerNotificacao;
	}

	public void setRequerNotificacao(Boolean requerNotificacao) {
		this.requerNotificacao = requerNotificacao;
	}
}
