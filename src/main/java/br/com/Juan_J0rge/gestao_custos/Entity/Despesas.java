package br.com.Juan_J0rge.gestao_custos.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

// esse são os atributos que o post(classe GestaoDeDespesas) vai receber
//ele recepe por meio da API

@Entity
@Table(name = "despesa")
@Getter
@Setter
@ToString
public class Despesas {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(length = 100, nullable = false) // mudo o tamanho do varchar
    private String categoria;

    @Column(nullable = false)
    private String email;


    @CreationTimestamp
    private LocalDate data_criacao;

}
