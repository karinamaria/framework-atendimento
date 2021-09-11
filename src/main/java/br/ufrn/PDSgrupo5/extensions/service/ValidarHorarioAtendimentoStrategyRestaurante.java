package br.ufrn.PDSgrupo5.extensions.service;

import br.ufrn.PDSgrupo5.extensions.model.Restaurante;
import br.ufrn.PDSgrupo5.framework.model.Atendimento;
import br.ufrn.PDSgrupo5.framework.repository.AtendimentoRepository;
import br.ufrn.PDSgrupo5.framework.strategy.VagasHorarioAtendimentoStrategy;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ValidarHorarioAtendimentoStrategyRestaurante implements VagasHorarioAtendimentoStrategy {
	private static final String UMA_PESSOA = "1 pessoa";
	private static final String DUAS_PESSOAS = "2 pessoas";
	private static final String TRES_OU_SEIS_PESSOAS = "3-6 pessoas";
	private static final String SETE_OU_DEZ_PESSOAS = "7-10 pessoas";

	private static final int NAO_EH_POSSIVEL_AGENDAR = 0;
	private static final int ULTIMA_VAGA = 1;

	private final AtendimentoRepository atendimentoRepository;

	public ValidarHorarioAtendimentoStrategyRestaurante(AtendimentoRepository atendimentoRepository) {
		this.atendimentoRepository = atendimentoRepository;
	}

	@Override
	public int calcularVagasHorarioAtendimento(Atendimento atendimento) {
		List<Atendimento> atendimentos = atendimentoRepository
				.findByHorarioAtendimento(atendimento.getHorarioAtendimento());

		int quantidadeVagas;
		try {
			int mesasOcupadas = 0;
			for (Atendimento a : atendimentos) {
				mesasOcupadas += mesasNecessarias(a);
			}

			Restaurante restaurante = (Restaurante) atendimento.getProfissional();
			quantidadeVagas = restaurante.getQuantidadeMesas() - mesasOcupadas;
			int mesasNecessarias = mesasNecessarias(atendimento);

			if (mesasNecessarias == quantidadeVagas) {
				quantidadeVagas = ULTIMA_VAGA;
			} else if (mesasNecessarias > quantidadeVagas) {
				quantidadeVagas = NAO_EH_POSSIVEL_AGENDAR;
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			quantidadeVagas = NAO_EH_POSSIVEL_AGENDAR;
		}

		return quantidadeVagas;
	}

	private int mesasNecessarias(Atendimento a) {
		int qtdCadeiras;
		switch(a.getTipoAtendimento()) {
			case UMA_PESSOA:
				qtdCadeiras = 1;
				break;
			case DUAS_PESSOAS:
				qtdCadeiras = 2;
				break;
			case TRES_OU_SEIS_PESSOAS:
				qtdCadeiras = 6;
				break;
			case SETE_OU_DEZ_PESSOAS:
				qtdCadeiras = 10;
				break;	
			default:
				throw new IllegalArgumentException();
		}

		int cadeirasPorMesa = ((Restaurante) a.getProfissional()).getCadeirasPorMesa();
		return ((qtdCadeiras % cadeirasPorMesa == 0) ? qtdCadeiras / cadeirasPorMesa
				: qtdCadeiras / cadeirasPorMesa + 1);
	}

}
