package br.ufrn.PDSgrupo5.framework.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import br.ufrn.PDSgrupo5.framework.enumeration.EnumSituacaoProfissionalSaude;

import java.util.List;

@Entity
@Table(name = "profissional")
@Inheritance(strategy = InheritanceType.JOINED)
public class Profissional extends EntidadeAbstrata {

    @Column(name = "horario_atendimento")
    @OneToMany(cascade = CascadeType.ALL)
    private List<HorarioAtendimento> horarioAtendimento;

    private boolean legalizado = false;

    @Column(name = "situacao_profissional")
    @Enumerated(EnumType.STRING)
    private EnumSituacaoProfissionalSaude situacaoProfissionalSaude;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "pessoa_id", referencedColumnName = "id")
    private Pessoa pessoa;
    
    private String descricao;

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

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
