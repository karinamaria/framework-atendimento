package br.ufrn.PDSgrupo5.extensions.service;

import br.ufrn.PDSgrupo5.framework.model.HorarioAtendimento;
import br.ufrn.PDSgrupo5.framework.repository.HorarioAtendimentoRepository;
import br.ufrn.PDSgrupo5.framework.strategy.VagasHorarioAtendimentoStrategy;

import org.springframework.stereotype.Service;

@Service
public class ValidarHorarioAtendimentoStrategyProfissionalSaude implements VagasHorarioAtendimentoStrategy {
    private final HorarioAtendimentoRepository horarioAtendimentoRepository;
    
    public ValidarHorarioAtendimentoStrategyProfissionalSaude(HorarioAtendimentoRepository horarioAtendimentoRepository){
        this.horarioAtendimentoRepository = horarioAtendimentoRepository;
    }

    @Override
    public int calcularVagasHorarioAtendimento(HorarioAtendimento horarioAtendimento) {
        int quantidadeVagas = 0;
        
        if(horarioAtendimentoRepository.getById(horarioAtendimento.getId()).isLivre()){
            quantidadeVagas=1;
        }
        return quantidadeVagas;
    }
}
