package br.ufrn.PDSgrupo5.extensions.service;

import br.ufrn.PDSgrupo5.extensions.model.Restaurante;
import br.ufrn.PDSgrupo5.extensions.repository.RestauranteRepository;
import br.ufrn.PDSgrupo5.framework.model.Pessoa;
import br.ufrn.PDSgrupo5.framework.model.Profissional;
import br.ufrn.PDSgrupo5.framework.service.PessoaService;
import br.ufrn.PDSgrupo5.framework.strategy.ValidarProfissionalStrategy;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Objects;

@Service
public class ValidarProfissionalStrategyRestaurante implements ValidarProfissionalStrategy{
	private final PessoaService pessoaService;

	private final RestauranteRepository restauranteRepository;
	
	public ValidarProfissionalStrategyRestaurante(PessoaService pessoaService,
												RestauranteRepository restauranteRepository) {
		this.pessoaService = pessoaService;
		this.restauranteRepository = restauranteRepository;
	}
	
	@Override
	public BindingResult validarProfissional(Profissional p, BindingResult br) {
		if(!pessoaService.ehCpfOuCnpjValido(p.getPessoa().getCpfOuCnpj())) {
			br.rejectValue("pessoa.cpfOuCnpj", "", "CNPJ inválido");
		}
		
		Pessoa pessoa = pessoaService.buscarPessoaPorCpf(p.getPessoa().getCpfOuCnpj());
		if(Objects.nonNull(pessoa)) {
			if(pessoa.getId() != p.getPessoa().getId()) {
				br.rejectValue("pessoa.cpfOuCnpj", "", "CNPJ já pertence a outro usuário");
			}
		}
		
	    Restaurante rAux = restauranteRepository.buscarRestaurantePorNome(p.getPessoa().getNome());
		if(Objects.nonNull(rAux)) {
			if(rAux.getId() != p.getId()) {
				br.rejectValue("pessoa.nome", "", "Já existe um estabelecimento com esse nome cadastrado no sistema");
			}
		}
		
		if(((Restaurante) p).getQuantidadeMesas() <= 0) {
			br.rejectValue("quantidadeMesas", "", "Informe uma quantidade válida de mesas");
		}
		
		if(((Restaurante) p).getCadeirasPorMesa() <= 0) {
			br.rejectValue("cadeirasPorMesa", "", "Informe uma quantidade válida de cadeiras por mesa");
		}
		
		return br;
	}

}
