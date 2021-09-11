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
    private static final int RESTA_APENAS_UMA_VAGA  = 1;
    private static final int NENHUMA_VAGA = 0;

    public ValidarHorarioAtendimentoStrategySalao(AtendimentoRepository atendimentoRepository) {
        this.atendimentoRepository = atendimentoRepository;
    }

    @Override
    public int calcularVagasHorarioAtendimento(Atendimento atendimento) {

        List<Atendimento> atendimentos = atendimentoRepository.findByHorarioAtendimento(atendimento.getHorarioAtendimento());

        if(atendimentos.isEmpty()){
            Salao salao = (Salao) atendimento.getProfissional();
            return salao.getQntTotalFuncionarios();
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

        Integer quantidadeVagasRestantes;
        if(atendimento.getTipoAtendimento().equalsIgnoreCase(ATENDIMENTO_EM_DOMICILO)){
            if(++qtdAtendimentosAgendadosDomicilio < salao.getQntFuncionariosAtendemDomicilio()){
                quantidadeVagasRestantes = ((qtdAtendimentosAgendadosDomicilio + qtdAtendimentosAgendadosSalao) - salao.getQntTotalFuncionarios());
            }
            else if(qtdAtendimentosAgendadosDomicilio == salao.getQntFuncionariosAtendemDomicilio()){
                quantidadeVagasRestantes = RESTA_APENAS_UMA_VAGA;
            }else{
                quantidadeVagasRestantes = NENHUMA_VAGA;
            }
        }else{
            if(++qtdAtendimentosAgendadosSalao < (salao.getQntTotalFuncionarios() - salao.getQntFuncionariosAtendemDomicilio())){
                quantidadeVagasRestantes = ((qtdAtendimentosAgendadosSalao + qtdAtendimentosAgendadosDomicilio) - salao.getQntTotalFuncionarios());
            }else if(qtdAtendimentosAgendadosSalao == (salao.getQntTotalFuncionarios() - salao.getQntFuncionariosAtendemDomicilio())){
                quantidadeVagasRestantes = RESTA_APENAS_UMA_VAGA;
            }else{
                quantidadeVagasRestantes = NENHUMA_VAGA;
            }
        }
        return quantidadeVagasRestantes;
    }
}
