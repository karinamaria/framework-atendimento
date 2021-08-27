package br.ufrn.PDSgrupo5.framework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufrn.PDSgrupo5.framework.enumeration.EnumTipoPapel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "usuario")
public class Usuario extends EntidadeAbstrata {
	@Column(unique = true)
	@NotNull(message = "O login não pode ser vazio")
	private String login;

	@JsonIgnore
	@NotNull(message = "A senha não pode ser vazia")
	private String senha;

	@Column(name="papel")
	@Enumerated(EnumType.STRING)
	private EnumTipoPapel enumTipoPapel;

	public Usuario() {
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public EnumTipoPapel getEnumTipoPapel() {
		return enumTipoPapel;
	}

	public void setEnumTipoPapel(EnumTipoPapel enumTipoPapel) {
		this.enumTipoPapel = enumTipoPapel;
	}
}
