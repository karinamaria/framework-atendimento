package br.ufrn.PDSgrupo5.framework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.ufrn.PDSgrupo5.framework.model.Cliente;
import br.ufrn.PDSgrupo5.framework.model.Usuario;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query(value="SELECT c FROM Cliente c WHERE c.pessoa.usuario=?1")
    Cliente findClienteByUsuario(Usuario usuario);
}
