package br.ufrn.PDSgrupo5.framework.service;

import br.ufrn.PDSgrupo5.framework.model.Atendimento;

public interface NotificacaoStrategy {
	int obterDiasParaNotificacao();
	
	String construirMensagemNotificacao(Atendimento a);
	
	String construirAssuntoNotificacao();
}
