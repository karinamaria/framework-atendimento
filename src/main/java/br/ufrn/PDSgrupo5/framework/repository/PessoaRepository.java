package br.ufrn.PDSgrupo5.framework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.ufrn.PDSgrupo5.framework.model.Pessoa;
import br.ufrn.PDSgrupo5.framework.model.Usuario;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    Pessoa findByCpfOuCnpj(String cpfOuCnpj);

    Pessoa findByEmail(String email);

    Pessoa findByUsuario(Usuario usuario);

    @Query(value="SELECT p FROM Pessoa p WHERE p.usuario.login=?1")
    Pessoa findByLoginUsuario(String login);
}
