package br.com.Juan_J0rge.gestao_custos.performance;

import br.com.Juan_J0rge.gestao_custos.Entity.Despesas;
import br.com.Juan_J0rge.gestao_custos.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


// simulando um grande numero de dados para testas
//@Component // informa que quero que rode junto da aplicação
public class GestaoDespesasSeeder implements CommandLineRunner /*vai executar comando ao iniciar*/ {

    @Autowired
    DespesaRepository despesaRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Despesas> despesas = new ArrayList<>();
        System.out.println("Iniciando geração de seed");
        for (int i = 0; i < 150000; i ++) {
            Despesas despesa = new Despesas();
            despesa.setDescricao("Gasto n: " + i);
            despesa.setValor(BigDecimal.valueOf(10 + (i % 50)));
            despesa.setData(LocalDate.now().minusDays(i % 30));
            despesa.setCategoria("TESTE");
            despesa.setEmail("performace@gmail.com");

            despesas.add(despesa);
        }

        despesaRepository.saveAll(despesas);
        System.out.println("Finalizou geração de seed");
    }
}
