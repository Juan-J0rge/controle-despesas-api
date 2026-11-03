package br.com.Juan_J0rge.gestao_custos.Controller;

import br.com.Juan_J0rge.gestao_custos.Entity.Despesas;
import br.com.Juan_J0rge.gestao_custos.custom_message.ErrorMessage;
import br.com.Juan_J0rge.gestao_custos.useCases.BuscarDespesasUseCase;
import br.com.Juan_J0rge.gestao_custos.useCases.CadastroDespesasUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// classe responsavel pelas requisições e APIS

@RequestMapping("/gestao")
@RestController
public class GestaoDespesasController {

    /*
    criar:
    - criar o cadastro
    - Tabela no banco
    - A entidade
     */

    @Autowired //deixo o spring responsavel por instânciar a classe (necessita de um @Service na classe do objeto)
    CadastroDespesasUseCase cadastroDespesasUseCase;

    @Autowired
    BuscarDespesasUseCase buscarDespesasUseCase;

    @PostMapping("/create")
    // esse ResponseEntity vai ser usado para retornar o erro tanto na api quanto na pagina.
    public ResponseEntity<?> create(@RequestBody Despesas despesas) {

        // deixando mais bonito pro usuario ver o erro (que ja aparece na api (logica na useCase de Cadastro))
        try {
            var result = cadastroDespesasUseCase.execute(despesas);
            return ResponseEntity.ok(result);
        }catch (IllegalArgumentException e) {
            var errorMassage = new ErrorMessage(e.getMessage(), "INVALID_PARAMS");
            return ResponseEntity.status(400).body(errorMassage);
        }

    }

    @GetMapping("/{email}")
    public List<Despesas> findByEmailAndDate(@PathVariable String email, @RequestParam(required = false)LocalDate data) {
        return  buscarDespesasUseCase.execute(email, data);
    }
}
