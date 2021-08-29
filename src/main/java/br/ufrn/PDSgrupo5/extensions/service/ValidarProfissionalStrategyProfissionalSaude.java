package br.ufrn.PDSgrupo5.extensions.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import br.ufrn.PDSgrupo5.extensions.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.framework.model.Profissional;
import br.ufrn.PDSgrupo5.framework.service.PessoaService;
import br.ufrn.PDSgrupo5.framework.strategy.ValidarProfissionalStrategy;

import java.util.Objects;

@Service
public class ValidarProfissionalStrategyProfissionalSaude implements ValidarProfissionalStrategy{
	private final PessoaService pessoaService;

	private final ProfissionalSaudeService profissionalSaudeService;
	
	public ValidarProfissionalStrategyProfissionalSaude(PessoaService pessoaService,
														ProfissionalSaudeService profissionalSaudeService) {
		this.pessoaService = pessoaService;
		this.profissionalSaudeService = profissionalSaudeService;
	}
	
	@Override
	public BindingResult validarProfissional(Profissional p, BindingResult br) {
		ProfissionalSaude ps = (ProfissionalSaude) p;

		if(!pessoaService.ehCpfOuCnpjValido(ps.getPessoa().getCpfOuCnpj())) {
			br.rejectValue("cpf", "", "CPF inválido");
		}
		
		ProfissionalSaude profissional = profissionalSaudeService.buscarProfissionalSaudeCpfOuCnpj(ps.getPessoa().getCpfOuCnpj());
		if(Objects.nonNull(profissional)) {
			if(profissional.getId() != ps.getId()) {
				br.rejectValue("cpf", "", "CPF já pertence a outro profissional");
			}
		}
	    profissional=null;
	    profissional = profissionalSaudeService.buscarProfissionalPorNumeroRegistro(ps.getNumeroRegistro());
		if(Objects.nonNull(profissional)) {
			if(profissional.getId() != ps.getId()) {
				br.rejectValue("numeroRegistro", "", "Registro profissional já pertence a outro usuário");
			}
		}
		
		return br;
	}

}
