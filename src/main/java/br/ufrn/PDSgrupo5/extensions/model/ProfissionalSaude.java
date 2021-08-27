package br.ufrn.PDSgrupo5.extensions.model;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import br.ufrn.PDSgrupo5.framework.model.Profissional;

@Entity
@Table(name = "profissional_saude")
@PrimaryKeyJoinColumn(name="id")
public class ProfissionalSaude extends Profissional {
	@Column(name="tipo_registro")
	@Enumerated(EnumType.STRING)
	private EnumTipoRegistro enumTipoRegistro;
	
	@Column(name="numero_registro")
	private Long numeroRegistro;
	
	@NotNull(message = "A data de aprovação do registro não pode ser vazia")
	@Past(message="A data de aprovação deve ser anterior ao dia de hoje")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="data_aprovacao_registro")
	private Date dataAprovacaoRegistro;
	
	@NotNull(message = "O cpf não pode ser vazio")
	private String cpf;
	
	@NotNull(message = "O nome não pode ser vazio")
	private String sexo;
	
	@NotNull(message = "A data de nascimento não pode ser vazia")
	@Past(message="A data de nascimento deve ser anterior ao dia de hoje")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "data_nascimento", nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataNascimento;
	
	public ProfissionalSaude() {
	}
	
	public EnumTipoRegistro getEnumTipoRegistro() {
		return enumTipoRegistro;
	}

	public void setEnumTipoRegistro(EnumTipoRegistro enumTipoRegistro) {
		this.enumTipoRegistro = enumTipoRegistro;
	}

	public Long getNumeroRegistro() {
		return numeroRegistro;
	}

	public void setNumeroRegistro(Long numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public Date getDataAprovacaoRegistro() {
		return dataAprovacaoRegistro;
	}

	public void setDataAprovacaoRegistro(Date dataAprovacaoRegistro) {
		this.dataAprovacaoRegistro = dataAprovacaoRegistro;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
}
