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
     * Método que é usado quando um profissional deseja aceitar ou recusar atendimento
     * @param idAtendimento representa o ID do atendimento que terá estado modificado
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
            horarioAtendimentoService.salvarHorario(horarioAtendimento);//salvar horário de atendimento
            atendimentoRepository.delete(atendimento);
        }
    }

    /**
     * Método responsável por buscar todos os atendimentos que precisam
     * receber uma notificação de retorno
     * @return uma lista de atendimentos que receberão notificação
     */
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
