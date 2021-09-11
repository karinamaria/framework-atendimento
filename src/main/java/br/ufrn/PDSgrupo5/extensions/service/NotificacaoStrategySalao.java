package br.ufrn.PDSgrupo5.extensions.service;

import br.ufrn.PDSgrupo5.framework.model.Atendimento;
import br.ufrn.PDSgrupo5.framework.strategy.NotificacaoStrategy;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoStrategySalao implements NotificacaoStrategy {
    private static final int DIAS_PARA_NOTIFICACAO = 30;

    @Override
    public int obterDiasParaNotificacao() {
        return DIAS_PARA_NOTIFICACAO;
    }

    @Override
    public String construirMensagemNotificacao(Atendimento a) {
        String saudacao = "Oi, " + a.getCliente().getPessoa().getNome() + ", tudo bem?";

        String texto = "Notamos que já faz um tempo desde seu último atendimento no" +
                " dia " + a.getHorarioAtendimento().dataToString() + ". "+
                "Estamos ansiosos aguardando a sua volta e temos várias promoções especiais" +
                "para clientes únicos como você. ";

        String despedida = "Sistema de Atendimentos v1.0"
                + "\n\nNota: Este e-mail foi gerado automaticamente. Por favor, não responda esta mensagem.";

        String mensagem = saudacao + "\n\n" + texto + "\n\n" + despedida;
        return mensagem;
    }

    @Override
    public String construirAssuntoNotificacao() {
        String assunto = "Estamos sentindo a sua falta!";
        return assunto;
    }
}
