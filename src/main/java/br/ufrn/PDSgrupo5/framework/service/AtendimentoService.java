package br.ufrn.PDSgrupo5.framework.service;

import br.ufrn.PDSgrupo5.framework.model.Atendimento;
import br.ufrn.PDSgrupo5.framework.model.HorarioAtendimento;
import br.ufrn.PDSgrupo5.framework.model.Profissional;
import br.ufrn.PDSgrupo5.framework.repository.AtendimentoRepository;

import br.ufrn.PDSgrupo5.framework.strategy.NotificacaoStrategy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AtendimentoService {
    private PacienteService pacienteService;

    private ProfissionalService profissionalSaudeService;

    private AtendimentoRepository atendimentoRepository;

    private HorarioAtendimentoService horarioAtendimentoService;
    
    private NotificacaoStrategy notificacaoStrategy;

    public AtendimentoService(PacienteService pacienteService, ProfissionalService psService,
                              AtendimentoRepository atendimentoRepository, NotificacaoStrategy notificacaoStrategy){
        this.pacienteService = pacienteService;
        this.profissionalSaudeService = psService;
        this.atendimentoRepository = atendimentoRepository;
        this.notificacaoStrategy = notificacaoStrategy;
    }
    
    public Atendimento salvar(Atendimento a) {
    	return atendimentoRepository.save(a);
    }

    public List<Atendimento> buscarProximosAtendimentosPaciente(){
        Long idPacienteLogado = pacienteService.buscarPacientePorUsuarioLogado().getId();

        return atendimentoRepository.buscarProximosAtendimentosPaciente(idPacienteLogado, somarQuinzeDiasADataAtual());
    }

    public List<Atendimento> buscarProximosAtendimentosProfissional(){
        Long idProfissionalLogado = profissionalSaudeService.buscarProfissionalPorUsuarioLogado().getId();

        return atendimentoRepository.buscarProximosAtendimentosProfissional(idProfissionalLogado, somarQuinzeDiasADataAtual());
    }

    public List<Atendimento> buscarAtendimentosAguardandoConfirmacao(){
        Profissional profissionalSaude = profissionalSaudeService.buscarProfissionalPorUsuarioLogado();

        return atendimentoRepository.buscarAtendimentosAguardandoConfirmacao(profissionalSaude);
    }

    public Date somarQuinzeDiasADataAtual(){
        LocalDate dataMaisQuinzeDias = LocalDate.now().plusDays(14);

        return Date.from(dataMaisQuinzeDias.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public void aceitarRecusarAtendimento(Long id, boolean autorizacao) {
        Atendimento atendimento = atendimentoRepository.getById(id);

        if(autorizacao){//atendimento autorizado
            atendimento.setConfirmado(true);
            atendimentoRepository.save(atendimento);
        }else{
            HorarioAtendimento horarioAtendimento = atendimento.getHorarioAtendimento();
            horarioAtendimento.setLivre(true);
            horarioAtendimentoService.salvarHorario(horarioAtendimento);//salvar horário de atendimento
            atendimentoRepository.delete(atendimento);
        }
    }
    
    public List<Atendimento> buscarAtendimentosRequeremNotificacao(){
        List<Atendimento> atendimentos = atendimentoRepository.buscarAtendimentosRequeremNotificacao(notificacaoStrategy.obterDiasParaNotificacao());
        
        return   atendimentos.stream()
                .filter(a -> requerNotificacao(a))
                .collect(Collectors.toList());
    }

    /**
     * Verifica se é necessário enviar notificação para o cliente
     * @param a atendimento que foi realizado há x dias até a notificação ou mais.
     * @return false: não precisa enviar notificação; true: caso contrário
     */
    public Boolean requerNotificacao(Atendimento a){
        //buscar todos os próximos atendimentos e os que foram realizados nos últimos dias até a data de notificação
        List<Atendimento> todosProximosAtendimentos = atendimentoRepository.buscarTodosProximosAtendimentosPaciente(a.getPaciente().getId(), notificacaoStrategy.obterDiasParaNotificacao());
        
        boolean requerNotificacao = true;
        for(Atendimento atendimento : todosProximosAtendimentos) {
        	if(atendimento.getProfissional().equals(a.getProfissional())){
        		a.setRequerNotificacao(false);
        		salvar(a);
        		requerNotificacao = false;
        		break;
        	}
        }
        return requerNotificacao;
        
//        return todosProximosAtendimentos == null || todosProximosAtendimentos.isEmpty()? true : todosProximosAtendimentos.stream()
//                .filter(atendimento -> atendimento.getProfissionalSaude().equals(a.getProfissionalSaude()))
//                .count() != 0;
    }
}
