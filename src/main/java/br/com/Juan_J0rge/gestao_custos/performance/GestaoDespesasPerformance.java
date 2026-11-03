package br.com.Juan_J0rge.gestao_custos.performance;

import br.com.Juan_J0rge.gestao_custos.Entity.Despesas;
import br.com.Juan_J0rge.gestao_custos.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/gestao/performance")
@RestController
public class GestaoDespesasPerformance {

    @Autowired
    DespesaRepository despesaRepository;

    @GetMapping("sem-paginacao")
    public ResponseEntity<List<Despesas>>  listaSemPaginacao() {
        long inicio = System.currentTimeMillis();
        var despesas = despesaRepository.findAll();

        long fim = System.currentTimeMillis();
        System.out.println("Tempo sem paginação = " + (fim - inicio) + " ms");
        return ResponseEntity.ok(despesas);
    }

    @GetMapping("com-paginacao") // o page automaticamente cria um ?page= &size=
    public ResponseEntity<Page<Despesas>>  listaComPaginacao(Pageable pageable) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        var despesas = despesaRepository.findAll(pageable);
        stopWatch.stop();
        System.out.println("Tempo com paginação = " + stopWatch.getTotalTimeMillis() + " ms");
        return ResponseEntity.ok(despesas);
    }

    @GetMapping("com-paginacao/{email}") // o page automaticamente cria um ?page= &size=
    public ResponseEntity<Page<Despesas>>  listaComPaginacao(@PathVariable String email, Pageable pageable) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        var despesas = despesaRepository.findByEmail(email ,pageable);
        stopWatch.stop();
        System.out.println("Tempo com paginação = " + stopWatch.getTotalTimeMillis() + " ms");
        return ResponseEntity.ok(despesas);
    }


}
