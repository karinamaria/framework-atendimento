package br.ufrn.PDSgrupo5.framework.service;

import br.ufrn.PDSgrupo5.framework.enumeration.EnumTipoPapel;
import br.ufrn.PDSgrupo5.framework.model.Usuario;
import br.ufrn.PDSgrupo5.framework.repository.UsuarioRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UsuarioService {
    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public boolean loginJaExiste(Usuario usuario){
        Usuario aux = usuarioRepository.findByLogin(usuario.getLogin());
        if(Objects.isNull(aux)){
           return false;
        }
        //true - login existe, mas pertence a outro usuário
        //false - login é do próprio usuário
        return aux.getId() != usuario.getId();
    }

    public Usuario prepararUsuarioParaCriacao(Usuario usuario){
        usuario.setEnumTipoPapel(EnumTipoPapel.PACIENTE);
        usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));

        return usuario;
    }

    public Usuario buscarUsuarioPeloId(Long id){
        return usuarioRepository.findById(id).get();
    }
}
