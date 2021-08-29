package br.ufrn.PDSgrupo5.framework.strategy;

import org.springframework.validation.BindingResult;

import br.ufrn.PDSgrupo5.framework.model.Profissional;

public interface ValidarProfissionalStrategy {
	BindingResult validarProfissional(Profissional p, BindingResult br);
}
