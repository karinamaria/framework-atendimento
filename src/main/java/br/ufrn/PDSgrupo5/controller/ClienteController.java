package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.framework.exception.ValidacaoException;
import br.ufrn.PDSgrupo5.framework.model.Atendimento;
import br.ufrn.PDSgrupo5.framework.model.Cliente;
import br.ufrn.PDSgrupo5.framework.service.AtendimentoService;
import br.ufrn.PDSgrupo5.framework.service.ClienteService;
import br.ufrn.PDSgrupo5.framework.service.ProfissionalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("paciente")
public class ClienteController {
    private ClienteService clienteService;
    private ProfissionalService profissionalService;
    private AtendimentoService atendimentoService;

    @Autowired
    public ClienteController(ClienteService clienteService, ProfissionalService profissionalService,
    						  AtendimentoService atendimentoService){
        this.clienteService = clienteService;
        this.profissionalService = profissionalService;
        this.atendimentoService = atendimentoService;
    }

    @GetMapping
    public String salvar(){
        return "";
    }

    @GetMapping("/form")
    public String form(Model model){
        if(!model.containsAttribute("cliente")){
            model.addAttribute(new Cliente());
        }
        return "paciente/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Cliente paciente, BindingResult br, Model model){

        try{
            paciente = clienteService.verificarEdicao(paciente);
            clienteService.validarCliente(paciente, br);

            clienteService.salvarCliente(paciente);
        }catch(ValidacaoException validacaoException){
            model.addAttribute("message", "Erro ao salvar paciente");
            model.addAttribute(paciente);
            return form(model);
        }

        return "redirect:/dashboard";
    }
    
    //o usu??rio edita seu pr??prio cadastro
    @GetMapping("/editar")
    public String editar(Model model){
        model.addAttribute(clienteService.buscarClientePorUsuarioLogado());
        return "paciente/form";
    }

    @GetMapping("/perfil")
    public String visualizarPerfil(Model model){
        model.addAttribute(clienteService.buscarClientePorUsuarioLogado());
        return "paginadevisualizacaoPerfil";
    }
    
    @DeleteMapping("/excluirPerfil")
    public String excluirPerfil(){
        Cliente paciente = clienteService.buscarClientePorUsuarioLogado();

        clienteService.excluirCliente(paciente);

        return "redirect:/login";
    }
    
    @GetMapping("/visualizarProfissional/{id}")
    public String visualizarProfissional(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("profissional", profissionalService.buscarProfissionalPorId(id));
    	model.addAttribute("horariosAtendimento", profissionalService.buscarHorariosAtendimentoLivres(id));
    	model.addAttribute("atendimento", new Atendimento());
    	
    	return "paciente/formAtendimento";
    }
    
    @PostMapping("/agendarAtendimento")
    public String agendarAtendimento(@RequestParam("horarioAtendimentoId") Long idHorario, @RequestParam("profissionalId") Long idProfissional,
                                     @Valid Atendimento atendimento, RedirectAttributes ra) {
    	try{
            
    		atendimento = atendimentoService.construirAtendimento(atendimento, idHorario, idProfissional);
            atendimentoService.agendarAtendimento(atendimento);
            
        }catch(ValidacaoException validacaoException){
            ra.addFlashAttribute("message", validacaoException.getMessage());
            ra.addFlashAttribute("atendimento",atendimento);
        }

    	return "redirect:/dashboard";
    }
}
