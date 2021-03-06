package br.ufrn.PDSgrupo5.framework.service;

import br.ufrn.PDSgrupo5.framework.exception.ValidacaoException;
import br.ufrn.PDSgrupo5.framework.handler.UsuarioHelper;
import br.ufrn.PDSgrupo5.framework.model.Atendimento;
import br.ufrn.PDSgrupo5.framework.model.HorarioAtendimento;
import br.ufrn.PDSgrupo5.framework.model.Profissional;
import br.ufrn.PDSgrupo5.framework.repository.AtendimentoRepository;
import br.ufrn.PDSgrupo5.framework.repository.ClienteRepository;
import br.ufrn.PDSgrupo5.framework.repository.ProfissionalRepository;
import br.ufrn.PDSgrupo5.framework.strategy.NotificacaoStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;

    private final HorarioAtendimentoService horarioAtendimentoService;
    
    private final NotificacaoStrategy notificacaoStrategy;

    private final UsuarioHelper usuarioHelper;

    private final ClienteRepository clienteRepository;

    private final ProfissionalRepository profissionalRepository;
    
    @Autowired
    public AtendimentoService(AtendimentoRepository atendimentoRepository, NotificacaoStrategy notificacaoStrategy,
                              HorarioAtendimentoService horarioAtendimentoService, UsuarioHelper usuarioHelper,
                              ClienteRepository clienteRepository, ProfissionalRepository profissionalRepository){
        this.atendimentoRepository = atendimentoRepository;
        this.notificacaoStrategy = notificacaoStrategy;
        this.horarioAtendimentoService = horarioAtendimentoService;
        this.usuarioHelper = usuarioHelper;
        this.clienteRepository = clienteRepository;
        this.profissionalRepository = profissionalRepository;
    }
    
    public Atendimento salvar(Atendimento a) {
    	return atendimentoRepository.save(a);
    }

    public List<Atendimento> buscarProximosAtendimentosCliente(){
        Long idClienteLogado = clienteRepository.findClienteByUsuario(usuarioHelper.getUsuarioLogado()).getId();

        return atendimentoRepository.buscarProximosAtendimentosCliente(idClienteLogado, somarQuinzeDiasADataAtual());
    }

    public List<Atendimento> buscarProximosAtendimentosProfissional(){
        Long idProfissionalLogado = profissionalRepository.findByUsuario(usuarioHelper.getUsuarioLogado()).getId();

        return atendimentoRepository.buscarProximosAtendimentosProfissional(idProfissionalLogado, somarQuinzeDiasADataAtual());
    }

    public List<Atendimento> buscarAtendimentosAguardandoConfirmacao(){
        Profissional profissional = profissionalRepository.findByUsuario(usuarioHelper.getUsuarioLogado());

        return atendimentoRepository.buscarAtendimentosAguardandoConfirmacao(profissional);
    }

    public Date somarQuinzeDiasADataAtual(){
        LocalDate dataMaisQuinzeDias = LocalDate.now().plusDays(14);

        return Date.from(dataMaisQuinzeDias.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    
    public Atendimento construirAtendimento(Atendimento a, Long idHorario, Long idProfissional) {
    	a.setCliente(clienteRepository.findClienteByUsuario(usuarioHelper.getUsuarioLogado()));
        a.setProfissional(profissionalRepository.findById(idProfissional).get());
        a.setHorarioAtendimento(horarioAtendimentoService.buscarHorarioPorId(idHorario));
        
        return a;
    }
    
    public void agendarAtendimento(Atendimento atendimento) throws ValidacaoException {
    	atendimento.setHorarioAtendimento(
                horarioAtendimentoService.ocuparVaga(atendimento)
        );
        salvar(atendimento);
    }

    /**
     * M??todo que ?? usado quando um profissional deseja aceitar ou recusar atendimento
     * @param idAtendimento representa o ID do atendimento que ter?? estado modificado
     * @param ehAtendimentoAutorizado boolean que indica se atendimento foi autorizado
     */
    public void aceitarRecusarAtendimento(Long idAtendimento, boolean ehAtendimentoAutorizado) {
        Atendimento atendimento = atendimentoRepository.getById(idAtendimento);

        if(ehAtendimentoAutorizado){//atendimento autorizado
            atendimento.setConfirmado(true);
            atendimentoRepository.save(atendimento);
        }else{
            HorarioAtendimento horarioAtendimento = atendimento.getHorarioAtendimento();
            horarioAtendimento.setLivre(true);
            horarioAtendimentoService.salvarHorario(horarioAtendimento);//salvar hor??rio de atendimento
            atendimentoRepository.delete(atendimento);
        }
    }

    /**
     * M??todo respons??vel por buscar todos os atendimentos que precisam
     * receber uma notifica????o de retorno
     * @return uma lista de atendimentos que receber??o notifica????o
     */
    public List<Atendimento> buscarAtendimentosRequeremNotificacao(){
        List<Atendimento> atendimentos = atendimentoRepository.buscarAtendimentosRequeremNotificacao(notificacaoStrategy.obterDiasParaNotificacao());
        
        return   atendimentos.stream()
                .filter(a -> requerNotificacao(a))
                .collect(Collectors.toList());
    }

    /**
     * Verifica se ?? necess??rio enviar notifica????o para o cliente
     * @param a atendimento que foi realizado h?? x dias at?? a notifica????o ou mais.
     * @return false: n??o precisa enviar notifica????o; true: caso contr??rio
     */
    public Boolean requerNotificacao(Atendimento a){
        //buscar todos os pr??ximos atendimentos e os que foram realizados nos ??ltimos dias at?? a data de notifica????o
        List<Atendimento> todosProximosAtendimentos = atendimentoRepository.buscarTodosProximosAtendimentosCliente(a.getCliente().getId(), notificacaoStrategy.obterDiasParaNotificacao());
        
        boolean requerNotificacao = true;
        for(Atendimento atendimento : todosProximosAtendimentos) {
        	if(a.getProfissional().getId() == atendimento.getProfissional().getId()) {
        		a.setRequerNotificacao(false);
        		salvar(a);
        		requerNotificacao = false;
        		break;
        	}
        }
        return requerNotificacao;
    }
}
