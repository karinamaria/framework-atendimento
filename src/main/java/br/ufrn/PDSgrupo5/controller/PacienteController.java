package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.framework.exception.ValidacaoException;
import br.ufrn.PDSgrupo5.framework.model.Atendimento;
import br.ufrn.PDSgrupo5.framework.model.Paciente;
import br.ufrn.PDSgrupo5.framework.service.AtendimentoService;
import br.ufrn.PDSgrupo5.framework.service.HorarioAtendimentoService;
import br.ufrn.PDSgrupo5.framework.service.PacienteService;
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
public class PacienteController {
    private PacienteService pacienteService;
    private ProfissionalService profissionalSaudeService;
    private AtendimentoService atendimentoService;
    private HorarioAtendimentoService horarioAtendimentoService;

    @Autowired
    public PacienteController(PacienteService pacienteService, ProfissionalService profissionalSaudeService,
    						  AtendimentoService atendimentoService, HorarioAtendimentoService horarioAtendimentoService){
        this.pacienteService = pacienteService;
        this.profissionalSaudeService = profissionalSaudeService;
        this.atendimentoService = atendimentoService;
        this.horarioAtendimentoService = horarioAtendimentoService;
    }

    @GetMapping
    public String salvar(){
        return "";
    }

    @GetMapping("/form")
    public String form(Model model){
        if(!model.containsAttribute("paciente")){
            model.addAttribute(new Paciente());
        }
        return "paciente/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Paciente paciente, BindingResult br, Model model){

        try{
            paciente = pacienteService.verificarEdicao(paciente);
            pacienteService.validarPaciente(paciente, br);

            pacienteService.salvarPaciente(paciente);
        }catch(ValidacaoException validacaoException){
            model.addAttribute("message", "Erro ao salvar paciente");
            model.addAttribute(paciente);
            return form(model);
        }

        return "redirect:/dashboard";
    }
    
    //o usuário edita seu próprio cadastro
    @GetMapping("/editar")
    public String editar(Model model){
        model.addAttribute(pacienteService.buscarPacientePorUsuarioLogado());
        return "paciente/form";
    }

    @GetMapping("/perfil")
    public String visualizarPerfil(Model model){
        model.addAttribute(pacienteService.buscarPacientePorUsuarioLogado());
        return "paginadevisualizacaoPerfil";
    }
    
    @DeleteMapping("/excluirPerfil")
    public String excluirPerfil(){
        Paciente paciente = pacienteService.buscarPacientePorUsuarioLogado();

        pacienteService.excluirPaciente(paciente);

        return "redirect:/login";
    }
    
    @GetMapping("/visualizarProfissional/{id}")
    public String visualizarProfissional(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("profissional", profissionalSaudeService.buscarProfissionalPorId(id));
    	model.addAttribute("horariosAtendimento", profissionalSaudeService.buscarHorariosAtendimentoLivres(id));
    	model.addAttribute("atendimento", new Atendimento());
    	
    	return "paciente/formAtendimento";
    }
    
    @PostMapping("/agendarAtendimento")
    public String agendarAtendimento(@RequestParam("horarioAtendimentoId") Long idHorario, @RequestParam("profissionalId") Long idProfissional,
                                     @Valid Atendimento atendimento, RedirectAttributes ra) {
    	try{
            atendimento.setPaciente(pacienteService.buscarPacientePorUsuarioLogado());
            atendimento.setProfissional(profissionalSaudeService.buscarProfissionalPorId(idProfissional));
            atendimento.setHorarioatendimento(horarioAtendimentoService.buscarHorarioPorId(idHorario));

            //atendimentoService.salvar(atendimento);
            atendimentoService.agendarAtendimento(atendimento);
        }catch(ValidacaoException validacaoException){
            ra.addFlashAttribute("message", validacaoException.getMessage());
            ra.addFlashAttribute("atendimento",atendimento);
        }

    	return "redirect:/dashboard";
    }
}
