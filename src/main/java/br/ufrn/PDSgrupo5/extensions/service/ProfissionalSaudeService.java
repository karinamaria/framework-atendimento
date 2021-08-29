package br.ufrn.PDSgrupo5.extensions.service;

import br.ufrn.PDSgrupo5.extensions.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.extensions.repository.ProfissionalSaudeRepository;

import org.springframework.stereotype.Service;

@Service
public class ProfissionalSaudeService {
    private final ProfissionalSaudeRepository profissionalSaudeRepository;
    
    public ProfissionalSaudeService(ProfissionalSaudeRepository profissionalSaudeRepository){
        this.profissionalSaudeRepository = profissionalSaudeRepository;
    }
    
    public ProfissionalSaude buscarProfissionalSaudeCpfOuCnpj(String cpfOuCnpj){
        return profissionalSaudeRepository.buscarProfissionalPorCpfOuCnpj(cpfOuCnpj);
    }
    
    public ProfissionalSaude buscarProfissionalPorNumeroRegistro(Long numeroRegistro){
        return profissionalSaudeRepository.findByNumeroRegistro(numeroRegistro);
    }
}
