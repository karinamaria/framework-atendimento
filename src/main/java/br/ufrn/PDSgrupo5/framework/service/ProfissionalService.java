package br.ufrn.PDSgrupo5.framework.service;

import br.ufrn.PDSgrupo5.framework.enumeration.EnumSituacaoProfissionalSaude;
import br.ufrn.PDSgrupo5.framework.enumeration.EnumTipoPapel;
import br.ufrn.PDSgrupo5.framework.exception.AcessoNegadoException;
import br.ufrn.PDSgrupo5.framework.exception.ValidacaoException;
import br.ufrn.PDSgrupo5.framework.handler.UsuarioHelper;
import br.ufrn.PDSgrupo5.framework.model.HorarioAtendimento;
import br.ufrn.PDSgrupo5.framework.model.Pessoa;
import br.ufrn.PDSgrupo5.framework.model.Profissional;
import br.ufrn.PDSgrupo5.framework.model.Usuario;
import br.ufrn.PDSgrupo5.framework.repository.HorarioAtendimentoRepository;
import br.ufrn.PDSgrupo5.framework.repository.ProfissionalRepository;
import br.ufrn.PDSgrupo5.framework.strategy.ValidarProfissionalStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProfissionalService {
	private final ProfissionalRepository profissionalRepository;

	private final UsuarioHelper usuarioHelper;

	private final HorarioAtendimentoRepository horarioAtendimentoRepository;
	
	private final ValidarProfissionalStrategy validarProfissionalStrategy;

	private final PessoaService pessoaService;
	
	@Autowired
	public ProfissionalService(ProfissionalRepository profissionalRepository,
									UsuarioHelper usuarioHelper,
									HorarioAtendimentoRepository hr,
									ValidarProfissionalStrategy validarProfissionalStrategy,
							   		PessoaService pessoaService) {
		this.profissionalRepository = profissionalRepository;
		this.usuarioHelper = usuarioHelper;
		this.horarioAtendimentoRepository = hr;
		this.validarProfissionalStrategy = validarProfissionalStrategy;
		this.pessoaService = pessoaService;
	}
	
	public Profissional salvar(Profissional ps) {
		return profissionalRepository.save(ps);
	}
	
	public void salvarProfissional(Profissional ps) {
		if(ps.getId() == null) {
			ps.setAtivo(true);
			ps.getPessoa().setUsuario(prepararUsuarioProfissional(ps.getPessoa().getUsuario()));
			ps.setSituacaoProfissionalSaude(EnumSituacaoProfissionalSaude.AGUARDANDO_ANALISE);
		} else {
			Profissional psAux = buscarProfissionalPorUsuarioLogado();
			ps.setLegalizado(psAux.isLegalizado());
			ps.setHorarioAtendimento(psAux.getHorarioAtendimento());
		}

		salvar(ps);
	}

	public Usuario prepararUsuarioProfissional(Usuario usuario){
		usuario.setEnumTipoPapel(EnumTipoPapel.PROFISSIONAL);
		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));

		return usuario;
	}
	
	public void validarDados(Profissional profissional, BindingResult br) throws ValidacaoException {
		br = validarProfissionalStrategy.validarProfissional(profissional, br);
		
		if(!profissional.getPessoa().getEmail().matches("^(.+)@(.+)$")){
            br.rejectValue("pessoa.email", "","E-mail inválido");
        }
		
        if(pessoaService.loginDaPessoaJaExiste(profissional.getPessoa())){
            br.rejectValue("pessoa.usuario.login", "","Login já existe");
        }
        
        Pessoa pessoa = pessoaService.buscarPessoaPorEmail(profissional.getPessoa().getEmail());
		if(Objects.nonNull(pessoa)){
			if(pessoa.getId() != profissional.getPessoa().getId()){
				br.rejectValue("pessoa.email", "","E-mail já pertence a outro usuário");
			}
		}
        
		if(br.hasErrors()){
			throw new ValidacaoException(br);
		}
	}

	public Profissional verificarEdicao(Profissional ps) {
        Profissional psAux = buscarProfissionalPorUsuarioLogado();
		//nenhum atributo do usuário será modificado na edição
		ps.getPessoa().setUsuario(psAux.getPessoa().getUsuario());

		ps.setId(psAux.getId());
		ps.setSituacaoProfissionalSaude(psAux.getSituacaoProfissionalSaude());

        if(Objects.isNull(ps.getPessoa().getEndereco())){
            ps.getPessoa().setEndereco(null);
        }else{
        	ps.getPessoa().getEndereco().setId(psAux.getPessoa().getEndereco().getId());
		}

        return ps;
	}
	
	public void excluir(Profissional ps) {
		profissionalRepository.delete(ps);
	}
	
	public Profissional buscarProfissionalPorUsuarioLogado() {
		return profissionalRepository.findByUsuario(usuarioHelper.getUsuarioLogado());
	}
	
	public Profissional buscarProfissionalPorUsuario(Long id){
        return profissionalRepository.findByUsuarioId(id);
    }

    public List<Profissional> listarProfissionaisStatusLegalizacao(boolean legalizado){
		return profissionalRepository.findAllByLegalizado(legalizado);
	}
    
	public Profissional buscarProfissionalPorId(Long id){
		return profissionalRepository.findById(id).orElse(null);
	}

	public Profissional adicionarHorarioAtendimento(HorarioAtendimento ha) {
		Profissional ps = buscarProfissionalPorUsuarioLogado();
		ps.getHorarioAtendimento().add(ha);
		return salvar(ps);
	}

	public List<HorarioAtendimento> buscarHorariosAtendimento() {
		Profissional ps = buscarProfissionalPorUsuarioLogado();
		return ps.getHorarioAtendimento();
	}
	
	public List<HorarioAtendimento> buscarHorariosAtendimentoLivres(Long id){
		Profissional ps = buscarProfissionalPorId(id);
		List<HorarioAtendimento> todosHorarios = ps.getHorarioAtendimento();
		
		List<HorarioAtendimento> horariosLivres = new ArrayList<>();
		for (HorarioAtendimento horario : todosHorarios) {
			if(horario.isLivre()) {
				horariosLivres.add(horario);
			}
		}
		return horariosLivres;
	}

	public Profissional excluirHorarioAtendimento(Long idHorarioAtendimento){
		Profissional ps = buscarProfissionalPorUsuarioLogado();

		ps.getHorarioAtendimento().removeIf(x -> x.getId().equals(idHorarioAtendimento));
		horarioAtendimentoRepository.delete(horarioAtendimentoRepository.findById(idHorarioAtendimento).get());

		return salvar(ps);
	}

	public void verificarPermissao(Profissional ps) throws AcessoNegadoException {
		if(ps.getId() == null){ //eh usuário novo
			return;
		}

		Profissional psLogado = buscarProfissionalPorUsuarioLogado();

		if(usuarioHelper.getUsuarioLogado().getEnumTipoPapel() == EnumTipoPapel.VALIDADOR
				|| ps.getId() == null){
			return;
		}

		if( ps.getId().equals(psLogado.getId()) || ps.getPessoa().getId() != psLogado.getPessoa().getId()
				|| ps.getPessoa().getUsuario().getId().equals(psLogado.getPessoa().getUsuario().getId())) {
			throw new AcessoNegadoException("Você não tem permissão para editar esse usuário");
		}
	}
}
