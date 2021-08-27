package br.ufrn.PDSgrupo5.framework.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import br.ufrn.PDSgrupo5.framework.enumeration.EnumSituacaoProfissionalSaude;

import java.util.List;

@Entity
@Table(name = "profissional")
@Inheritance(strategy = InheritanceType.JOINED)
public class Profissional extends EntidadeAbstrata {
	@NotNull(message = "O nome não pode ser vazio")
	private String nome;

	private String descricao;
	
	@NotNull(message = "O email não pode ser vazio")
	private String email;

	@Column(name="horario_atendimento")
	@OneToMany(cascade = CascadeType.ALL)
	private List<HorarioAtendimento> horarioAtendimento;
	
	private boolean legalizado = false;

	@Column(name="situacao_profissional")
	@Enumerated(EnumType.STRING)
	private EnumSituacaoProfissionalSaude situacaoProfissionalSaude;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	private Usuario usuario;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "endereco_id", referencedColumnName = "id")
	private Endereco endereco;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<HorarioAtendimento> getHorarioAtendimento() {
		return horarioAtendimento;
	}

	public void setHorarioAtendimento(List<HorarioAtendimento> horarioAtendimento) {
		this.horarioAtendimento = horarioAtendimento;
	}

	public boolean isLegalizado() {
		return legalizado;
	}

	public void setLegalizado(boolean legalizado) {
		this.legalizado = legalizado;
	}

	public EnumSituacaoProfissionalSaude getSituacaoProfissionalSaude() {
		return situacaoProfissionalSaude;
	}

	public void setSituacaoProfissionalSaude(EnumSituacaoProfissionalSaude situacaoProfissionalSaude) {
		this.situacaoProfissionalSaude = situacaoProfissionalSaude;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
}
