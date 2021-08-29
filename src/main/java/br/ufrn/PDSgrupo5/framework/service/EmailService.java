package br.ufrn.PDSgrupo5.framework.service;

import java.util.List;

import br.ufrn.PDSgrupo5.framework.strategy.NotificacaoStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.ufrn.PDSgrupo5.framework.model.Atendimento;

@Service
@EnableScheduling
public class EmailService {
	private static final String TIME_ZONE = "America/Sao_Paulo";
	private JavaMailSender emailSender;
	private AtendimentoService atendimentoService;
	private NotificacaoStrategy notificacaoStrategy;
	
	@Autowired
	EmailService(JavaMailSender emailSender, AtendimentoService atendimentoService,
				NotificacaoStrategy notificacaoStrategy){
		this.emailSender = emailSender;
		this.atendimentoService = atendimentoService;
		this.notificacaoStrategy = notificacaoStrategy;
	}
	
	public void enviarEmailSimples(String destinatario, String assunto, String mensagem) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setFrom("sistemadesaudev1@gmail.com");
		email.setTo(destinatario);
		email.setSubject(assunto);
		email.setText(mensagem);
		
		emailSender.send(email);
	}
	
	//@Scheduled(cron = "0 0 12 * * *", zone = TIME_ZONE)
	@Scheduled(cron = "* * * * * *", zone = TIME_ZONE) //a cada um minuto para teste
	public void enviarNotificacoes() {
		List<Atendimento> atendimentos = atendimentoService.buscarAtendimentosRequeremNotificacao();
		for(Atendimento a : atendimentos) {
			
			enviarEmailSimples(a.getPaciente().getPessoa().getEmail(), notificacaoStrategy.construirAssuntoNotificacao(), notificacaoStrategy.construirMensagemNotificacao(a));
			
			a.setRequerNotificacao(false);
			atendimentoService.salvar(a);
		}
	}
}
