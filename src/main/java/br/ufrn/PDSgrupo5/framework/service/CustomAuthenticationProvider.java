package br.ufrn.PDSgrupo5.framework.service;

import br.ufrn.PDSgrupo5.framework.model.Usuario;
import br.ufrn.PDSgrupo5.framework.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private UsuarioRepository usuarioRepository;

    @Autowired
    public CustomAuthenticationProvider(UsuarioRepository usuarioRepository){
        this.usuarioRepository=usuarioRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        Usuario usuario = Optional.ofNullable(usuarioRepository.findByLogin(name))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if(new BCryptPasswordEncoder().matches(password, usuario.getSenha())){
            List<GrantedAuthority> permissoes = AuthorityUtils.createAuthorityList(usuario.getEnumTipoPapel().getDescricao());
            return new UsernamePasswordAuthenticationToken(
                    name, password, permissoes);
        }

        throw new BadCredentialsException("Usuário e/ou senha inválidos");

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
