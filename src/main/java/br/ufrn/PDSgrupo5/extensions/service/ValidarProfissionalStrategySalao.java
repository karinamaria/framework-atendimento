package br.ufrn.PDSgrupo5.extensions.service;

import br.ufrn.PDSgrupo5.extensions.model.Salao;
import br.ufrn.PDSgrupo5.framework.model.Pessoa;
import br.ufrn.PDSgrupo5.framework.model.Profissional;
import br.ufrn.PDSgrupo5.framework.service.PessoaService;
import br.ufrn.PDSgrupo5.framework.strategy.ValidarProfissionalStrategy;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Objects;

@Service
public class ValidarProfissionalStrategySalao implements ValidarProfissionalStrategy {
    private final PessoaService pessoaService;

    public ValidarProfissionalStrategySalao(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @Override
    public BindingResult validarProfissional(Profissional p, BindingResult br) {
        Salao salao = (Salao) p;

        if(!pessoaService.ehCpfOuCnpjValido(salao.getPessoa().getCpfOuCnpj())) {
            br.rejectValue("pessoa.cpfOuCnpj", "", "CNPJ inválido");
        }

        Pessoa pessoa = pessoaService.buscarPessoaPorCpf(p.getPessoa().getCpfOuCnpj());
        if(Objects.nonNull(pessoa)) {
            if(pessoa.getId() != salao.getPessoa().getId()) {
                br.rejectValue("pessoa.cpfOuCnpj", "", "CNPJ já pertence a outro profissional");
            }
        }

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
