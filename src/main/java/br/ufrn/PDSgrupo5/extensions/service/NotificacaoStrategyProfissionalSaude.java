package br.ufrn.PDSgrupo5.extensions.service;

import org.springframework.stereotype.Service;

import br.ufrn.PDSgrupo5.extensions.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.framework.model.Atendimento;
import br.ufrn.PDSgrupo5.framework.service.NotificacaoStrategy;

@Service
public class NotificacaoStrategyProfissionalSaude implements NotificacaoStrategy {
	private static final int DIAS_PARA_NOTIFICACAO = 60;
	
	@Override
	public int obterDiasParaNotificacao() {
		return DIAS_PARA_NOTIFICACAO;
	}

	@Override
	public String construirMensagemNotificacao(Atendimento a) {
		String saudacao = "Olá, " + a.getPaciente().getPessoa().getNome() + ", tudo bem?";
		
		ProfissionalSaude ps = (ProfissionalSaude) a.getProfissional();
		String texto = "Notamos que já faz um tempo desde seu atendimento com o "
						+ a.getProfissional().getNome()
						+ " (" + ps.getEnumTipoRegistro().getDescricao() + ")."
						+ " De acordo com nossa programação, esse período é ideal para realizar um retorno."
						+ "\nQue tal entrar no Sistema de Saúde v1.0 e realizar um novo agendamento?";
		
		String despedida = "Sistema de Saúde v1.0"
						+ "\n\nNota: Este e-mail foi gerado automaticamente. Por favor não responda esta mensagem.";
		
		String mensagem = saudacao + "\n\n" + texto + "\n\n" + despedida;
		return mensagem;
	}

	@Override
	public String construirAssuntoNotificacao() {
		String assunto = "Realização de retorno médico - Sistema de Saúde v1.0";
		return assunto;
	}
}
