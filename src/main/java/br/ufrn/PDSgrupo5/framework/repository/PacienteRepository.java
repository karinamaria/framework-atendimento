package br.ufrn.PDSgrupo5.framework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.ufrn.PDSgrupo5.framework.model.Paciente;
import br.ufrn.PDSgrupo5.framework.model.Usuario;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    @Query(value="SELECT p FROM Paciente p WHERE p.pessoa.usuario=?1")
    Paciente findPacienteByUsuario(Usuario usuario);
}
