package br.com.Juan_J0rge.gestao_custos.useCases;

import br.com.Juan_J0rge.gestao_custos.Entity.Despesas;
import br.com.Juan_J0rge.gestao_custos.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// classe responsavel pela regra de negocios
@Service /* indica que a classe será um service (contem a regra de negocio)
Quando o spring escaneia o projeto vê a anotação service e ja cria uma instância automatica dela
que pode ser usado com @Autowired*/
public class CadastroDespesasUseCase {

    @Autowired
    private DespesaRepository despesaRepository;

    public Despesas execute(Despesas despesas) {
        //tratar erro caso os campos não sejam preenchidos
        if (despesas.getCategoria() == null || despesas.getData() == null || despesas.getDescricao() == null || despesas.getEmail() == null || despesas.getValor() == null) {
            throw new IllegalArgumentException("Preencher todos os campos");
        }

        // ele vai preencher automaticamento o campo id e o campo data_criação
        despesas = despesaRepository.save(despesas);
        return despesas;
    }
}
