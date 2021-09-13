package br.ufrn.PDSgrupo5.extensions.service;

import br.ufrn.PDSgrupo5.extensions.model.Salao;
import br.ufrn.PDSgrupo5.framework.model.Atendimento;
import br.ufrn.PDSgrupo5.framework.repository.AtendimentoRepository;
import br.ufrn.PDSgrupo5.framework.strategy.VagasHorarioAtendimentoStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ValidarHorarioAtendimentoStrategySalao implements VagasHorarioAtendimentoStrategy {
    private final AtendimentoRepository atendimentoRepository;

    private static final String ATENDIMENTO_EM_DOMICILO = "em domicilio";
    private static final String ATENDIMENTO_NO_SALAO = "no salão";
    private static final int ULTIMA_VAGA = 1;
    private static final int NAO_EH_POSSIVEL_AGENDAR = 0;

    public ValidarHorarioAtendimentoStrategySalao(AtendimentoRepository atendimentoRepository) {
        this.atendimentoRepository = atendimentoRepository;
    }

    @Override
    public int calcularVagasHorarioAtendimento(Atendimento atendimento) {

        List<Atendimento> atendimentos = atendimentoRepository.findByHorarioAtendimento(atendimento.getHorarioAtendimento());

        if(atendimentos.isEmpty()){
            Salao salao = (Salao) atendimento.getProfissional();
            return atendimento.getTipoAtendimento().equalsIgnoreCase(ATENDIMENTO_EM_DOMICILO) ? salao.getQntFuncionariosAtendemDomicilio() :
                    salao.getQntTotalFuncionarios()-salao.getQntFuncionariosAtendemDomicilio();
        }

        Long qtdAtendimentosAgendadosDomicilio = atendimentos.stream()
                .filter(atd -> atd.getTipoAtendimento().equalsIgnoreCase(ATENDIMENTO_EM_DOMICILO))
                .count();

        Long qtdAtendimentosAgendadosSalao = atendimentos.stream()
                .filter(atd -> atd.getTipoAtendimento().equalsIgnoreCase(ATENDIMENTO_NO_SALAO))
                .count();

        Integer quantidadeVagasRestantes = calcularVagas(qtdAtendimentosAgendadosDomicilio.intValue(), qtdAtendimentosAgendadosSalao.intValue(),
                                                            atendimento);

        return quantidadeVagasRestantes;
    }

    private Integer calcularVagas(Integer qtdAtendimentosAgendadosDomicilio, Integer qtdAtendimentosAgendadosSalao, Atendimento atendimento) {
        Salao salao = (Salao) atendimento.getProfissional();

        Integer vagasSomenteSalao = (salao.getQntTotalFuncionarios() - salao.getQntFuncionariosAtendemDomicilio());
        Integer quantidadeVagasRestante = null;

        if(atendimento.getTipoAtendimento().equalsIgnoreCase(ATENDIMENTO_EM_DOMICILO)){
            if(++qtdAtendimentosAgendadosDomicilio > salao.getQntFuncionariosAtendemDomicilio()
                || salao.getQntFuncionariosAtendemDomicilio() == 0){
                quantidadeVagasRestante = NAO_EH_POSSIVEL_AGENDAR;
            }
        }else{
            if(++qtdAtendimentosAgendadosSalao > vagasSomenteSalao){
                quantidadeVagasRestante = NAO_EH_POSSIVEL_AGENDAR;
            }
        }

        if(Objects.isNull(quantidadeVagasRestante)){
            if(qtdAtendimentosAgendadosDomicilio == salao.getQntFuncionariosAtendemDomicilio() &&
                qtdAtendimentosAgendadosSalao == vagasSomenteSalao){
                quantidadeVagasRestante = ULTIMA_VAGA;
            }else{
                quantidadeVagasRestante = (salao.getQntTotalFuncionarios() - (qtdAtendimentosAgendadosDomicilio + qtdAtendimentosAgendadosSalao));
            }
        }
        return quantidadeVagasRestante;
    }
}
