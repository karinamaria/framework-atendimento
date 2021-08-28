package br.ufrn.PDSgrupo5.extensions.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import br.ufrn.PDSgrupo5.extensions.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.framework.model.Profissional;
import br.ufrn.PDSgrupo5.framework.service.PessoaService;
import br.ufrn.PDSgrupo5.framework.service.ValidarProfissionalStrategy;

@Service
public class ValidarProfissionalStrategyProfissionalSaude implements ValidarProfissionalStrategy{
	private PessoaService pessoaService;
	
	public ValidarProfissionalStrategyProfissionalSaude(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	
	@Override
	public BindingResult validarProfissional(Profissional p, BindingResult br) {
		ProfissionalSaude ps = (ProfissionalSaude) p;
		
		if(!pessoaService.ehCpfOuCnpjValido(ps.getPessoa().getCpfOuCnpj())) {
			br.rejectValue("cpf", "", "CPF inválido");
		}
		
//		ProfissionalSaude profissional = profissionalSaudeService.buscarPorCpf(ps.getCpf());
//		if(Objects.nonNull(profissional)) {
//			if(profissional.getId() != ps.getId()) {
//				br.rejectValue("cpf", "", "CPF já pertence a outro profissional");
//			}
//		}
	    
//	    ProfissionalSaude profissional = buscarProfissionalPorNumeroRegistro(ps.getNumeroRegistro());
//		if(Objects.nonNull(profissional)) {
//			if(profissional.getId() != ps.getId()) {
//				br.rejectValue("numeroRegistro", "", "Registro profissional já pertence a outro usuário");
//			}
//		}
		
		return br;
	}

}
