package br.com.Juan_J0rge.gestao_custos.useCases;

import br.com.Juan_J0rge.gestao_custos.Entity.Despesas;
import br.com.Juan_J0rge.gestao_custos.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BuscarDespesasUseCase {
    @Autowired
    private DespesaRepository despesaRepository;

    // vai executar a lógica de buscar por email e data na classe controller
    public List<Despesas> execute(String email, LocalDate data) {
        List<Despesas> despesas;

        if (data != null) {
            despesas = despesaRepository.findByEmailAndData(email, data);
        } else {
            despesas = despesaRepository.findByEmail(email);
        }

      return despesas;
    }
}
