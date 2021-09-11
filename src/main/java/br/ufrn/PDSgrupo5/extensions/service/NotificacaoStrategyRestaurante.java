package br.ufrn.PDSgrupo5.extensions.service;

import org.springframework.stereotype.Service;

import br.ufrn.PDSgrupo5.framework.model.Atendimento;
import br.ufrn.PDSgrupo5.framework.strategy.NotificacaoStrategy;

@Service
public class NotificacaoStrategyRestaurante implements NotificacaoStrategy {
	private static final int DIAS_PARA_NOTIFICACAO = 7;
	
	@Override
	public int obterDiasParaNotificacao() {
		return DIAS_PARA_NOTIFICACAO;
	}
	
	@Override
	public String construirMensagemNotificacao(Atendimento a) {
		String saudacao = "Olá, " + a.getCliente().getPessoa().getNome() + ", tudo bem?";
		
		String texto = "Notamos que já faz um tempo desde seu atendimento no "
						+ a.getProfissional().getPessoa().getNome() + "."
						+ "\nQue tal entrar no Sistema de Atendimentos v1.0 e realizar um novo agendamento?"
						+ " Adoraríamos recebê-lo novamente!";
		
		String despedida = "Sistema de Atendimentos v1.0"
						+ "\n\nNota: Este e-mail foi gerado automaticamente. Por favor não responda esta mensagem.";
		
		String mensagem = saudacao + "\n\n" + texto + "\n\n" + despedida;
		return mensagem;
	}

	@Override
	public String construirAssuntoNotificacao() {
		String assunto = "Venha aproveitar novamente! - Sistema de Atendimentos v1.0";
		return assunto;
	}
}
