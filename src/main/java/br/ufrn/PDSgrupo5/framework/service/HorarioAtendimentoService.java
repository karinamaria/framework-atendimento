package br.ufrn.PDSgrupo5.framework.service;

import br.ufrn.PDSgrupo5.framework.exception.ValidacaoException;
import br.ufrn.PDSgrupo5.framework.model.HorarioAtendimento;
import br.ufrn.PDSgrupo5.framework.model.Profissional;
import br.ufrn.PDSgrupo5.framework.repository.HorarioAtendimentoRepository;
import br.ufrn.PDSgrupo5.framework.strategy.VagasHorarioAtendimentoStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HorarioAtendimentoService {
	private ProfissionalService profissionalSaudeService;
	private DataHoraService dataHoraService;
	private HorarioAtendimentoRepository horarioAtendimentoRepository;
	private final VagasHorarioAtendimentoStrategy validarHorarioAtendimentoStrategy;
	
	@Autowired
	public HorarioAtendimentoService(ProfissionalService profissionalSaudeService, DataHoraService dataHoraService,
									 HorarioAtendimentoRepository horarioAtendimentoRepository,
									 VagasHorarioAtendimentoStrategy validarHorarioAtendimentoStrategy) {
		this.profissionalSaudeService = profissionalSaudeService;
		this.dataHoraService = dataHoraService;
		this.horarioAtendimentoRepository = horarioAtendimentoRepository;
		this.validarHorarioAtendimentoStrategy = validarHorarioAtendimentoStrategy;
	}
	
	public Profissional salvar(HorarioAtendimento ha) {
		return profissionalSaudeService.adicionarHorarioAtendimento(ha);
	}

	public HorarioAtendimento salvarHorario(HorarioAtendimento ha){
		return horarioAtendimentoRepository.save(ha);
	}
	
	public void validarHorario(HorarioAtendimento ha) throws ValidacaoException{
		if(ha.getPreco() <= 0) {
			throw new ValidacaoException("Preço inválido. Por favor, insira um valor maior que 0.");
		}
		
		if(ha.getHorarioInicio().compareTo(ha.getHorarioFim()) >= 0) {
			throw new ValidacaoException("Horário inválido. A hora de início deve ser anterior a de fim e elas não podem ser iguais.");
		}

		if(ha.getHorarioInicio().before(new Date())){
			throw new ValidacaoException("A data início do atendimento deve ser posterior a data atual");
		}

		List<HorarioAtendimento> horarios = profissionalSaudeService.buscarHorariosAtendimento();
		for(HorarioAtendimento horario : horarios ) {
			if( horariosTemChoque(ha, horario) ) {
				throw new ValidacaoException("Choque entre horários. Verifique seus horários já cadastrados.");
			}
		}
	}
	
	public HorarioAtendimento converterParaHorarioAtendimento(Date horarioInicio, Date horarioFim, Double preco) {	
		HorarioAtendimento ha = new HorarioAtendimento();
    	ha.setHorarioInicio(horarioInicio);
    	ha.setHorarioFim(horarioFim);
    	ha.setPreco(preco);
    	ha.setDiaSemana(dataHoraService.getDiaSemana(horarioInicio));
    	
		return ha;
	}
	
	public boolean horariosTemChoque(HorarioAtendimento horarioA, HorarioAtendimento horarioB) {
		boolean temChoque = true;
		
		//já foi testado fora do método que início do horário é anterior ao fim do horário
		if( horarioA.getHorarioFim().before(horarioB.getHorarioInicio()) || horarioA.getHorarioInicio().after(horarioB.getHorarioFim()) ) {
			temChoque = false;
		}
		
		return temChoque;
	}

	public HorarioAtendimento buscarHorarioPorId(Long id) {
		return horarioAtendimentoRepository.findById(id).get();
	}

	/**
	 * O método 'ocuparVaga' é responsável por fechar o recebimento de atendimentos
	 * para determinado horário de atendimento
	 * @param horarioAtendimento que deseja agendar o atendimento
	 * @return o horário de atendimento
	 * @throws ValidacaoException exceção lançada quando usuário tenta agendar atendimento
	 * em um horário que não está livre
	 */
	public HorarioAtendimento ocuparVaga(HorarioAtendimento horarioAtendimento) throws ValidacaoException {
		int quantidadeVagas = validarHorarioAtendimentoStrategy.calcularVagasHorarioAtendimento(horarioAtendimento);

		if(quantidadeVagas == 0){
			throw new ValidacaoException("Não há mais vagas para esse horário de atendimento. Por favor, escolha outro.");
		}
		if(quantidadeVagas == 1){
			horarioAtendimento.setLivre(false);
		}
		return horarioAtendimento;
	}
}
