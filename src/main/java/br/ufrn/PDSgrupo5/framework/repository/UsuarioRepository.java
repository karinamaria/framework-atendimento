package br.ufrn.PDSgrupo5.framework.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufrn.PDSgrupo5.framework.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByLogin(String login);
}
