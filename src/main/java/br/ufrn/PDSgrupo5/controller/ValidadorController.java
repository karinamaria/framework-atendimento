package br.ufrn.PDSgrupo5.controller;

import br.ufrn.PDSgrupo5.framework.enumeration.EnumSituacaoProfissionalSaude;
import br.ufrn.PDSgrupo5.framework.service.PessoaService;
import br.ufrn.PDSgrupo5.framework.service.ProfissionalService;
import br.ufrn.PDSgrupo5.framework.service.ValidadorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("validador")
public class ValidadorController {
    private PessoaService pessoaService;

    private ProfissionalService profissionalService;

    private ValidadorService validadorService;

    @Autowired
    public ValidadorController(PessoaService pessoaService, ProfissionalService profissionalService,
                               ValidadorService validadorService){
        this.pessoaService = pessoaService;
        this.profissionalService = profissionalService;
        this.validadorService = validadorService;
    }

    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute(pessoaService.buscarPessoaPorUsuarioLogado());
        model.addAttribute("profissionais", profissionalService.listarProfissionaisStatusLegalizacao(false));
        return "";
    }

    @GetMapping("/visualizarProfissional/{id}")
    public String visualizarProfissional(@PathVariable Long id, Model model){
        model.addAttribute("profissional", profissionalService.buscarProfissionalPorId(id));

        return "";
    }

    @PostMapping("/salvarProfissional")
    public String editarProfissionalSaude(@RequestParam("profissionalId") Long id,
                                          @RequestParam("autorizacao") boolean autorizacao, @RequestParam("justificativa") EnumSituacaoProfissionalSaude justificativa){

        validadorService.salvarEdicaoProfissionalSaude(id, autorizacao, justificativa);

        return "redirect:/dashboard";
    }
}
