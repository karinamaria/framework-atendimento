package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.extensions.model.Salao;
import br.ufrn.PDSgrupo5.framework.exception.ValidacaoException;
import br.ufrn.PDSgrupo5.framework.model.Profissional;
import br.ufrn.PDSgrupo5.framework.service.AtendimentoService;
import br.ufrn.PDSgrupo5.framework.service.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;

@Controller
@RequestMapping("profissional")
public class ProfissionalController {
	private ProfissionalService profissionalService;
	private AtendimentoService atendimentoService;
	
	@Autowired
	ProfissionalController(ProfissionalService profissionalService,
						AtendimentoService atendimentoService){
		this.profissionalService = profissionalService;;
		this.atendimentoService = atendimentoService;
	}
	
	@GetMapping("/form")
	public String form(Model model) {
		if(!model.containsAttribute("profissional")) {
			model.addAttribute("profissional", new Salao());
		}
		
		return "profissional/form";
	}
	
    @PostMapping("/salvar")
    public String salvar(@Valid Salao salao, BindingResult br, Model model) {
        
    	Profissional p = salao;
    	
    	try{
            p = profissionalService.verificarEdicao(p);
            profissionalService.validarDados(p, br);

            profissionalService.salvarProfissional(p);
        }catch (ValidacaoException validacaoException){
            model.addAttribute("message", "Erro ao cadastrar profissional da saúde");
            model.addAttribute(p);
            return form(model);
        }
        return "redirect:/dashboard";
    }

    //o usuário edita seu próprio cadastro
    @GetMapping("/editar")
    public String editar(Model model) {
        model.addAttribute("profissional", profissionalService.buscarProfissionalPorUsuarioLogado());
        return "profissional/form";
    }

    //usuário com papel "validador" pode editar qualquer profissional da saúde
    @GetMapping("/editar-usuario/{id}")
    public String editarOutroProfissional(@PathVariable Long id, Model model) {
        model.addAttribute(profissionalService.buscarProfissionalPorUsuario(id));
        return form(model);
    }

    /**
     * O profissional pode visualizar seu próprio perfil
     *
     * @param model
     * @return
     */
    @GetMapping("/perfil")
    public String visualizarPerfil(Model model) {
        model.addAttribute(profissionalService.buscarProfissionalPorUsuarioLogado());
        return "paginadevisualizacaoPerfil";
    }
    
    @DeleteMapping("/excluirPerfil")
    public String excluirPerfil(){
        Profissional ps = profissionalService.buscarProfissionalPorUsuarioLogado();
        profissionalService.excluir(ps);

        return "redirect:/login";
    }
    
    @GetMapping("/horariosAtendimento")
    public String horariosAtendimento(Model model) {
    	Profissional ps = profissionalService.buscarProfissionalPorUsuarioLogado();
    	model.addAttribute("horariosAtendimento", ps.getHorarioAtendimento());
    	return "profissional/horariosAtendimento";
    }
    
    @PostMapping("/addHorarioAtendimento")
    public String addHorarioAtendimento(@RequestParam("data") String data, @RequestParam("preco") Double preco,
    								  @RequestParam("horaInicio") String horaInicio,
    								  @RequestParam("horaFim") String horaFim, Model model) {
    	
		try {
			
			profissionalService.inserirHorarioAtendimento(data, preco, horaInicio, horaFim);
			
		} catch (ParseException e) {
			return "redirect:/profissional/error";
		} catch (ValidacaoException validacaoException){
            model.addAttribute("mensagemErro", validacaoException.getMessage());
            return horariosAtendimento(model);
        }
    	
    	return "redirect:/profissional/horariosAtendimento";
    }

    @GetMapping("/excluirHorarioAtendimento")
    public String excluirHorarioAtendimento(@RequestParam("idHorarioAtendimento") Long idHorarioAtendimento){
        profissionalService.excluirHorarioAtendimento(idHorarioAtendimento);

        return "redirect:/profissional/horariosAtendimento";
    }

    @PostMapping("/aceitarRecusarAtendimento")
    public String aceitarRecusarAtendimento(@RequestParam("atendimentoId") Long id,
                                            @RequestParam("autorizacaoAtendimento") boolean autorizacao){
        atendimentoService.aceitarRecusarAtendimento(id, autorizacao);
        return "redirect:/dashboard";
    }
}
