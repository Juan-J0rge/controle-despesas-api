package br.com.Juan_J0rge.gestao_custos.repository;

import br.com.Juan_J0rge.gestao_custos.Entity.Despesas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * normalmente os comandos criados para acessar funcoes no banco de dados (como criar, atualizar, deletar ...) ficam em uma interface chamada repository (é um padrão).
 *  ENTIDADE <- REPOSITORY -> BANCO DE DADOS.
 *  o extends JpaRepository recebe 2 argumentos.
 *  1 - A entidade que eu quero representar
 *  2 - O tipo da entidade
 */
public interface DespesaRepository extends JpaRepository<Despesas, UUID> {
// quando for usar a instância de DespesaRepository você vai ter acesso a varidos metodos da lógica de acesso ao banco de dados

    //consigo criar minha propria regra tambem

    /**
     * O Spring Data Jpa sabe interpretar o metodo para buscar email e email e data APENAS PELO NOME. Sim parece maigca.
     * find:
     * Ação de buscar
     * By:
     * Critério
     * Email:
     * Atributo da entidade
     */
    List<Despesas> findByEmail(String email);

    List<Despesas> findByEmailAndData(String email, LocalDate data);

    Page<Despesas> findByEmail(String email, Pageable pageable);
}

