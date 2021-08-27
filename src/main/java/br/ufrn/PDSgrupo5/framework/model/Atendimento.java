package br.ufrn.PDSgrupo5.framework.model;

import javax.persistence.*;

import br.ufrn.PDSgrupo5.framework.enumeration.EnumTipoAtendimento;

@Entity
@Table(name = "atendimento")
public class Atendimento extends EntidadeAbstrata {
	
	@OneToOne(cascade = CascadeType.ALL)
	private HorarioAtendimento horarioAtendimento;

	private String titulo;

	private String descricao;

	@Column(columnDefinition = "boolean default false")
	private Boolean confirmado = false;

	@OneToOne
	private Paciente paciente;

	@OneToOne(cascade = CascadeType.ALL)
	private Profissional profissional;

	@Column(name="tipo_atendimento")
	@Enumerated(EnumType.STRING)
	private EnumTipoAtendimento enumTipoAtendimento;
	
	@Column(name="requer_notificacao")
	private Boolean requerNotificacao = true;

	public Atendimento() {
	}

	public HorarioAtendimento getHorarioAtendimento() {
		return horarioAtendimento;
	}

	public void setHorarioatendimento(HorarioAtendimento horarioAtendimento) {
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

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	public EnumTipoAtendimento getEnumTipoAtendimento() {
		return enumTipoAtendimento;
	}

	public void setEnumTipoAtendimento(EnumTipoAtendimento enumTipoAtendimento) {
		this.enumTipoAtendimento = enumTipoAtendimento;
	}
	
	public Boolean getRequerNotificacao() {
		return requerNotificacao;
	}

	public void setRequerNotificacao(Boolean requerNotificacao) {
		this.requerNotificacao = requerNotificacao;
	}
}
