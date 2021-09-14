package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.extensions.model.Salao;
import br.ufrn.PDSgrupo5.framework.enumeration.EnumTipoPapel;
import br.ufrn.PDSgrupo5.framework.exception.AcessoNegadoException;
import br.ufrn.PDSgrupo5.framework.exception.ValidacaoException;
import br.ufrn.PDSgrupo5.framework.handler.UsuarioHelper;
import br.ufrn.PDSgrupo5.framework.model.Cliente;
import br.ufrn.PDSgrupo5.framework.model.Profissional;
import br.ufrn.PDSgrupo5.framework.service.AtendimentoService;
import br.ufrn.PDSgrupo5.framework.service.ClienteService;
import br.ufrn.PDSgrupo5.framework.service.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class HomeController {
    private ClienteService clienteService;

    private ProfissionalService profissionalService;

    private AtendimentoService atendimentoService;

    private UsuarioHelper usuarioHelper;

    @Autowired
    public HomeController(ClienteService clienteService, ProfissionalService profissionalService,
                         AtendimentoService atendimento, UsuarioHelper usuarioHelper){
        this.clienteService = clienteService;
        this.profissionalService = profissionalService;
        this.atendimentoService = atendimento;
        this.usuarioHelper = usuarioHelper;
    }

    @RequestMapping("/dashboard")
    public String dashBoard(Model model){
        if(usuarioHelper.getUsuarioLogado().getEnumTipoPapel().equals(EnumTipoPapel.VALIDADOR)){
            model.addAttribute("profissionais", profissionalService.listarProfissionaisStatusLegalizacao(false));
        }
        else if(usuarioHelper.getUsuarioLogado().getEnumTipoPapel().equals(EnumTipoPapel.CLIENTE)){
            model.addAttribute("profissionais", profissionalService.listarProfissionaisStatusLegalizacao(true));
            model.addAttribute("proximosAtendimentos", atendimentoService.buscarProximosAtendimentosCliente());
        }
        else{
            model.addAttribute("atendimentosStatusPendente", atendimentoService.buscarAtendimentosAguardandoConfirmacao());
            model.addAttribute("proximosAtendimentos", atendimentoService.buscarProximosAtendimentosProfissional());
        }
        return "dashboard";
    }

    @RequestMapping("/login")
    public String login(Model model){

        if(!model.containsAttribute("cliente")){
            model.addAttribute("cliente",new Cliente());
        }
        if(!model.containsAttribute("profissional")){
        	Profissional p = new Salao();
            model.addAttribute("profissional", p);
        }

        if(!model.containsAttribute("active_tab")){
            model.addAttribute("active_tab",null);
        }

        return "login";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @PostMapping("/novo-paciente/salvar")
    public String novoPaciente(@Valid Cliente paciente, BindingResult br, RedirectAttributes ra){
        try{
            clienteService.verificarPermissao(paciente);
            clienteService.validarCliente(paciente, br);
            clienteService.salvarCliente(paciente);
            ra.addFlashAttribute("active_tab",null); //ir para página de login
        }catch(AcessoNegadoException ne){
            return "error/401.html";//o usuário não tem permissão para editar outro candidato. Apresente página de erro
        }catch(ValidacaoException validacaoException){
            ra.addFlashAttribute("org.springframework.validation.BindingResult.cliente", validacaoException.getBindingResult());
            ra.addFlashAttribute("message", "Erro ao salvar paciente");
            ra.addFlashAttribute("cliente", paciente);
            ra.addFlashAttribute("active_tab", "cliente");
        }

        return "redirect:/login";
    }

    @PostMapping("/novo-profissional/salvar")
    public String novoProfissionalSaude(@Valid Salao salao, BindingResult br, RedirectAttributes ra){
        
//    	Profissional p = salao;
    	
    	try{
            profissionalService.inserirProfissional(salao, br);
            ra.addFlashAttribute("active_tab",null);
        }catch(AcessoNegadoException ne){
            return "error/401.html";//usuário não tem permissão para edição
        }catch(ValidacaoException validacaoException){
            ra.addFlashAttribute("org.springframework.validation.BindingResult.profissional", validacaoException.getBindingResult());
            ra.addFlashAttribute("message", "Erro ao salvar salão");
            ra.addFlashAttribute("profissional", salao);
            ra.addFlashAttribute("active_tab", "profissional");
        }

        return "redirect:/login";
    }
}
