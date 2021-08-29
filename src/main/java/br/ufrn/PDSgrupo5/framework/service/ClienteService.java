package br.ufrn.PDSgrupo5.framework.service;

import br.ufrn.PDSgrupo5.framework.enumeration.EnumTipoPapel;
import br.ufrn.PDSgrupo5.framework.exception.AcessoNegadoException;
import br.ufrn.PDSgrupo5.framework.exception.ValidacaoException;
import br.ufrn.PDSgrupo5.framework.handler.UsuarioHelper;
import br.ufrn.PDSgrupo5.framework.model.Cliente;
import br.ufrn.PDSgrupo5.framework.model.Pessoa;
import br.ufrn.PDSgrupo5.framework.model.Usuario;
import br.ufrn.PDSgrupo5.framework.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Objects;

@Service
public class ClienteService {
    private ClienteRepository clienteRepository;

    private PessoaService pessoaService;

    private UsuarioService usuarioService;

    private UsuarioHelper usuarioHelper;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, PessoaService pessoaService,
                           UsuarioService usuarioService, UsuarioHelper usuarioHelper){
        this.clienteRepository = clienteRepository;
        this.pessoaService = pessoaService;
        this.usuarioService = usuarioService;
        this.usuarioHelper = usuarioHelper;
    }

    public Cliente salvar(Cliente cliente){
        return clienteRepository.save(cliente);
    }

    public void salvarCliente(Cliente cliente){
        if(cliente.getId() == null){
            cliente.setAtivo(true);
            cliente.getPessoa().setUsuario(usuarioService.prepararUsuarioParaCriacao(cliente.getPessoa().getUsuario()));
        }
        salvar(cliente);
    }

    /**
     * Valida os dados do cliente
     * @param cliente objeto que será validado
     * @param br onde os erros relativos a entidade `Cliente`são acumulados
     * @return um BindingResult que contém os erros, caso existam
     */
    public void validarCliente(Cliente cliente, BindingResult br) throws ValidacaoException{
    	if(!pessoaService.ehCpfOuCnpjValido(cliente.getPessoa().getCpfOuCnpj())){
            br.rejectValue("pessoa.cpfOuCnpj", "", "CPF inválido");
        }
        Pessoa pessoa = pessoaService.buscarPessoaPorCpf(cliente.getPessoa().getCpfOuCnpj());
        if(Objects.nonNull(pessoa)){
            if(pessoa.getId() != cliente.getPessoa().getId()){
                br.rejectValue("pessoa.cpfOuCnpj", "","CPF já pertence a outra pessoa");
            }
        }
        if(!cliente.getPessoa().getEmail().matches("^(.+)@(.+)$")){
            br.rejectValue("pessoa.email", "","E-mail inválido");
        }
        if(usuarioService.loginJaExiste(cliente.getPessoa().getUsuario())){
            br.rejectValue("pessoa.usuario.login", "","Login já existe");
        }
        if(cliente.getPessoa().getUsuario().getLogin().length() < 5){
            br.rejectValue("pessoa.usuario.login", "", "O login deve ter pelo menos cinco caracteres");
        }
        pessoa = pessoaService.buscarPessoaPorEmail(cliente.getPessoa().getEmail());
        if(Objects.nonNull(pessoa)){
            if(pessoa.getId() != cliente.getPessoa().getId()){
                br.rejectValue("pessoa.email", "","Email já pertence a outra pessoa");
            }
        }

        if(br.hasErrors()){
            throw new ValidacaoException(br);
        }
    }

    public Cliente buscarClientePorUsuarioLogado(){
        return clienteRepository.findClienteByUsuario(usuarioHelper.getUsuarioLogado());
    }

    public void excluirCliente(Cliente cliente){
        clienteRepository.delete(cliente);
    }

    public Cliente buscarClientePorUsuario(Long id){
        Usuario usuario = usuarioService.buscarUsuarioPeloId(id);
        return clienteRepository.findClienteByUsuario(usuario);
    }

    /**
     * Se for uma edição do cliente, o método carrega o tipo do papel e a senha
     * @param cliente que está sendo editado
     * @return cliente
     */
    public Cliente verificarEdicao(Cliente cliente) {
        Cliente cliente1 = buscarClientePorUsuarioLogado();
        
        //nenhum atributo do usuário será modificado na edição
        cliente.getPessoa().setUsuario(cliente1.getPessoa().getUsuario());

        cliente.setId(cliente1.getId());
        cliente.getPessoa().setId(cliente1.getPessoa().getId());

        if(Objects.isNull(cliente1.getPessoa().getEndereco())){
            cliente.getPessoa().setEndereco(null);
        }else{
            cliente.getPessoa().getEndereco().setId(cliente1.getPessoa().getEndereco().getId());
        }

        return cliente;
    }

    /**
     * Verifica se o usuário está editando o próprio cadastro
     * @param cliente Cliente que será editado
     * @throws AcessoNegadoException exceção lançada caso a edição não for permitida
     */
    public void verificarPermissao(Cliente cliente) throws AcessoNegadoException {
        if(cliente.getId() == null){ //eh usuário novo
            return;
        }

        Cliente clienteLogado = buscarClientePorUsuarioLogado();

        if(usuarioHelper.getUsuarioLogado().getEnumTipoPapel() == EnumTipoPapel.VALIDADOR
            || cliente.getId() == null){
            return;
        }

        if( cliente.getId() != clienteLogado.getId() || cliente.getPessoa().getId() != clienteLogado.getPessoa().getId()
            || cliente.getPessoa().getUsuario().getId() != clienteLogado.getPessoa().getUsuario().getId()){
            throw new AcessoNegadoException("Você não tem permissão para editar esse usuário");
        }
    }
}
