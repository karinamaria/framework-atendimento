package br.ufrn.PDSgrupo5.extensions.service;

import br.ufrn.PDSgrupo5.framework.model.Atendimento;
import br.ufrn.PDSgrupo5.framework.repository.HorarioAtendimentoRepository;
import br.ufrn.PDSgrupo5.framework.strategy.VagasHorarioAtendimentoStrategy;

import org.springframework.stereotype.Service;

@Service
public class ValidarHorarioAtendimentoStrategyProfissionalSaude implements VagasHorarioAtendimentoStrategy {
    private static final int NAO_EH_POSSIVEL_AGENDAR = 0;
    private static final int ULTIMA_VAGA = 1;
	
	private final HorarioAtendimentoRepository horarioAtendimentoRepository;
    
    public ValidarHorarioAtendimentoStrategyProfissionalSaude(HorarioAtendimentoRepository horarioAtendimentoRepository){
        this.horarioAtendimentoRepository = horarioAtendimentoRepository;
    }

    @Override
    public int calcularVagasHorarioAtendimento(Atendimento atendimento) {
        int quantidadeVagas = NAO_EH_POSSIVEL_AGENDAR;
        
        if(horarioAtendimentoRepository.getById(atendimento.getHorarioAtendimento().getId()).isLivre()){
            quantidadeVagas = ULTIMA_VAGA;
        }
        return quantidadeVagas;
    }
}
