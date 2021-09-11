package br.ufrn.PDSgrupo5.extensions.model;

import br.ufrn.PDSgrupo5.framework.model.Profissional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "salao")
@PrimaryKeyJoinColumn(name = "profissional_id")
public class Salao extends Profissional {
    @Column(name = "qnt_total_funcionarios")
    @NotNull(message = "A quantidade total de funcionários não pode ser nula")
    private Integer qntTotalFuncionarios;

    @Column(name = "qnt_funcionarios_atendem_domicilio")
    @NotNull(message = "A quantidade de funcionários que atendem à domícilo" +
            " não pode ser nula")
    private Integer qntFuncionariosAtendemDomicilio;

    public Salao() {
    }

    public Integer getQntTotalFuncionarios() {
        return qntTotalFuncionarios;
    }

    public void setQntTotalFuncionarios(Integer qntTotalFuncionarios) {
        this.qntTotalFuncionarios = qntTotalFuncionarios;
    }

    public Integer getQntFuncionariosAtendemDomicilio() {
        return qntFuncionariosAtendemDomicilio;
    }

    public void setQntFuncionariosAtendemDomicilio(Integer qntFuncionariosAtendemDomicilio) {
        this.qntFuncionariosAtendemDomicilio = qntFuncionariosAtendemDomicilio;
    }
}
