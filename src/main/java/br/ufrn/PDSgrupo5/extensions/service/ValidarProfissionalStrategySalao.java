package br.ufrn.PDSgrupo5.extensions.service;

import br.ufrn.PDSgrupo5.extensions.model.Salao;
import br.ufrn.PDSgrupo5.framework.model.Profissional;
import br.ufrn.PDSgrupo5.framework.strategy.ValidarProfissionalStrategy;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class ValidarProfissionalStrategySalao implements ValidarProfissionalStrategy {
    @Override
    public BindingResult validarProfissional(Profissional p, BindingResult br) {
        Salao salao = (Salao) p;

        if(salao.getQntFuncionariosAtendemDomicilio() < 0){
            br.rejectValue("qntTotalFuncionarios", "",
                    "Por favor, informe uma quantidade válida de funcionários");
        }
        if(salao.getQntFuncionariosAtendemDomicilio() < 0){
            br.rejectValue("qntTotalFuncionarios", "",
                    "Por favor, informe uma quantidade válida de funcionários que atendem" +
                            "a domícilio");
        }
        if(salao.getQntFuncionariosAtendemDomicilio() > salao.getQntTotalFuncionarios()){
            br.rejectValue("qntFuncionariosAtendemDomicilio", "",
                    "A quantidade de funcionários que atendem a domícilio não pode ser superior" +
                            " a quantidade total de funcionários.");
        }
        return br;
    }
}
