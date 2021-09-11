package br.ufrn.PDSgrupo5.framework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.ufrn.PDSgrupo5.framework.model.Atendimento;
import br.ufrn.PDSgrupo5.framework.model.HorarioAtendimento;
import br.ufrn.PDSgrupo5.framework.model.Profissional;

import java.util.Date;
import java.util.List;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    @Query(value="select a from Atendimento a where a.cliente.id=?1 and " +
            "a.horarioAtendimento.horarioInicio > CURRENT_DATE and a.horarioAtendimento.horarioInicio < ?2")
    List<Atendimento> buscarProximosAtendimentosCliente(Long idCliente, Date dataLimite);

    @Query(value="select a from Atendimento a where a.profissional.id=?1 and " +
            "a.horarioAtendimento.horarioInicio > CURRENT_DATE and a.horarioAtendimento.horarioInicio < ?2 and a.confirmado=true")
    List<Atendimento> buscarProximosAtendimentosProfissional(Long idProfissional, Date dataLimite);

    @Query(value="select a from Atendimento a where a.confirmado=false and a.profissional=?1")
    List<Atendimento> buscarAtendimentosAguardandoConfirmacao(Profissional profissional);
    
    @Query(value="select a from Atendimento a where a.confirmado=true and a.requerNotificacao=true and a.horarioAtendimento.horarioInicio <= (CURRENT_DATE - ?1)")
    List<Atendimento> buscarAtendimentosRequeremNotificacao(int diasParaNotificacao);

    @Query(value="select a from Atendimento a where a.confirmado=true and a.cliente.id=?1 and a.horarioAtendimento.horarioInicio > (CURRENT_DATE - ?2)")
    List<Atendimento> buscarTodosProximosAtendimentosCliente(Long idCliente, int diasParaNotificacao);

    List<Atendimento> findByHorarioAtendimento(HorarioAtendimento horarioAtendimento);
}
