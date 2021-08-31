package br.ufrn.PDSgrupo5.framework.service;

import br.ufrn.PDSgrupo5.framework.enumeration.EnumSituacaoProfissionalSaude;
import br.ufrn.PDSgrupo5.framework.model.Profissional;
import br.ufrn.PDSgrupo5.framework.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidadorService {

    private final ProfissionalRepository profissionalRepository;

    @Autowired
    public ValidadorService(ProfissionalRepository profissionalRepository){
        this.profissionalRepository = profissionalRepository;
    }
    
    public void salvarEdicaoProfissionalSaude(Long idProfissional, boolean autorizacao, EnumSituacaoProfissionalSaude justificativa){
        Profissional psAuxiliar = profissionalRepository.getById(idProfissional);

        psAuxiliar.setSituacaoProfissionalSaude(justificativa);
        psAuxiliar.setLegalizado(autorizacao);

        profissionalRepository.save(psAuxiliar);
    }
}
