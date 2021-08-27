package br.ufrn.PDSgrupo5.framework.model;

import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;

@ApiIgnore
@Entity
@Table(name = "paciente")
public class Paciente extends EntidadeAbstrata {

	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private Pessoa pessoa;

	public Paciente() {
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}
