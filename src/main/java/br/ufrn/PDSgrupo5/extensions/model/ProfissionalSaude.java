package br.ufrn.PDSgrupo5.extensions.model;

import br.ufrn.PDSgrupo5.framework.model.Profissional;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

@Entity
@Table(name = "profissional_saude")
@PrimaryKeyJoinColumn(name = "profissional_id")
public class ProfissionalSaude extends Profissional {
    @Column(name = "tipo_registro")
    @Enumerated(EnumType.STRING)
    private EnumTipoRegistro enumTipoRegistro;

    @Column(name = "numero_registro")
    private Long numeroRegistro;

    @NotNull(message = "A data de aprovação do registro não pode ser vazia")
    @Past(message = "A data de aprovação deve ser anterior ao dia de hoje")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_aprovacao_registro")
    private Date dataAprovacaoRegistro;

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
}
