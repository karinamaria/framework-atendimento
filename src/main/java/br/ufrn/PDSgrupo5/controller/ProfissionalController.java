package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.extensions.model.ProfissionalSaude;
import br.ufrn.PDSgrupo5.framework.exception.ValidacaoException;
import br.ufrn.PDSgrupo5.framework.model.HorarioAtendimento;
import br.ufrn.PDSgrupo5.framework.model.Profissional;
import br.ufrn.PDSgrupo5.framework.service.AtendimentoService;
import br.ufrn.PDSgrupo5.framework.service.DataHoraService;
import br.ufrn.PDSgrupo5.framework.service.HorarioAtendimentoService;
import br.ufrn.PDSgrupo5.framework.service.ProfissionalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Date;

@Controller
@RequestMapping("profissional-saude")
public class ProfissionalController {
	private ProfissionalService profissionalService;
	private HorarioAtendimentoService horarioAtendimentoService;
	private DataHoraService dataHoraService;
	private AtendimentoService atendimentoService;
	
	@Autowired
	ProfissionalController(ProfissionalService profissionalService, HorarioAtendimentoService horarioAtendimentoService,
								DataHoraService dataHoraService, AtendimentoService atendimentoService){
		this.profissionalService = profissionalService;
		this.horarioAtendimentoService = horarioAtendimentoService;
		this.dataHoraService = dataHoraService;
		this.atendimentoService = atendimentoService;
	}
	
	@GetMapping("/form")
	public String form(Model model) {
		if(!model.containsAttribute("profissional")) {
			model.addAttribute("profissional", new ProfissionalSaude());
		}
		
		return "profissional-saude/form";
	}
	
    @PostMapping("/salvar")
    public String salvar(@Valid ProfissionalSaude profissionalSaude, BindingResult br, RedirectAttributes ra, Model model) {
        
    	Profissional p = profissionalSaude;
    	
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
        return "profissional-saude/form";
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
    	return "profissional-saude/horariosAtendimento";
    }
    
    @PostMapping("/addHorarioAtendimento")
    public String addHorarioAtendimento(@RequestParam("data") String data, @RequestParam("preco") Double preco,
    								  @RequestParam("horaInicio") String horaInicio,
    								  @RequestParam("horaFim") String horaFim, Model model) {
    	
		try {
			Date horarioInicio = dataHoraService.converterParaDate(data, horaInicio);
			Date horarioFim = dataHoraService.converterParaDate(data, horaFim);
			HorarioAtendimento ha = horarioAtendimentoService.converterParaHorarioAtendimento(horarioInicio, horarioFim, preco);
			
			horarioAtendimentoService.validarHorario(ha);

			profissionalService.adicionarHorarioAtendimento(ha);
			
		} catch (ParseException e) {
			return "redirect:/profissional-saude/error";
		} catch (ValidacaoException validacaoException){
            model.addAttribute("mensagemErro", validacaoException.getMessage());
            return horariosAtendimento(model);
        }
    	
    	return "redirect:/profissional-saude/horariosAtendimento";
    }

    @GetMapping("/excluirHorarioAtendimento")
    public String excluirHorarioAtendimento(@RequestParam("idHorarioAtendimento") Long idHorarioAtendimento){
        profissionalService.excluirHorarioAtendimento(idHorarioAtendimento);

        return "redirect:/profissional-saude/horariosAtendimento";
    }

    @PostMapping("/aceitarRecusarAtendimento")
    public String aceitarRecusarAtendimento(@RequestParam("atendimentoId") Long id,
                                            @RequestParam("autorizacaoAtendimento") boolean autorizacao){
        atendimentoService.aceitarRecusarAtendimento(id, autorizacao);
        return "redirect:/dashboard";
    }
}
