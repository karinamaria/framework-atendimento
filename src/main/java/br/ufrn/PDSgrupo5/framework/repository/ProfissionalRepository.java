package br.ufrn.PDSgrupo5.framework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.ufrn.PDSgrupo5.framework.model.Profissional;
import br.ufrn.PDSgrupo5.framework.model.Usuario;

import java.util.List;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
	@Query(value="SELECT p FROM Profissional p WHERE p.pessoa.usuario=?1")
    Profissional findByUsuario(Usuario usuario);
	
	List<Profissional> findAllByLegalizado(boolean legalizado);
}
