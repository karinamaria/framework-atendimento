package br.ufrn.PDSgrupo5.framework.service;

import br.ufrn.PDSgrupo5.framework.enumeration.EnumSituacaoProfissionalSaude;
import br.ufrn.PDSgrupo5.framework.model.Profissional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidadorService {
    private ProfissionalService profissionalService;

    @Autowired
    public ValidadorService(ProfissionalService ps){
        this.profissionalService = ps;
    }
    
    public void salvarEdicaoProfissionalSaude(Long idProfissional, boolean autorizacao, EnumSituacaoProfissionalSaude justificativa){
        Profissional psAuxiliar = profissionalService.buscarProfissionalPorId(idProfissional);

        psAuxiliar.setSituacaoProfissionalSaude(justificativa);
        psAuxiliar.setLegalizado(autorizacao);

        profissionalService.salvar(psAuxiliar);
    }
}
