package br.ufrn.PDSgrupo5.extensions.service;

import br.ufrn.PDSgrupo5.extensions.model.Restaurante;
import br.ufrn.PDSgrupo5.extensions.repository.RestauranteRepository;
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
		Restaurante restaurante = (Restaurante) p;

		if(!pessoaService.ehCpfOuCnpjValido(p.getPessoa().getCpfOuCnpj())) {
			br.rejectValue("pessoa.cpfOuCnpj", "", "CNPJ inválido");
		}
		
		Restaurante rAux = restauranteRepository.buscarRestaurantePorCnpj(p.getPessoa().getCpfOuCnpj());
		if(Objects.nonNull(rAux)) {
			if(rAux.getId() != restaurante.getPessoa().getId()) {
				br.rejectValue("pessoa.cpfOuCnpj", "", "CNPJ já pertence a outro estabelecimento");
			}
		}
		
	    Restaurante r = restauranteRepository.buscarRestaurantePorNome(restaurante.getPessoa().getNome());
		if(Objects.nonNull(r)) {
			if(r.getId() != restaurante.getId()) {
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
