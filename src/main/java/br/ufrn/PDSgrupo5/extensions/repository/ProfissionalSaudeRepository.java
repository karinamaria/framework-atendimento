package br.ufrn.PDSgrupo5.extensions.repository;

import br.ufrn.PDSgrupo5.extensions.model.ProfissionalSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaude, Long> {
    @Query(value="SELECT p FROM ProfissionalSaude p WHERE p.pessoa.cpfOuCnpj=?1")
    ProfissionalSaude buscarProfissionalPorCpfOuCnpj(String cpfOuCnpj);

    ProfissionalSaude findByNumeroRegistro(Long numeroRegistro);
}
