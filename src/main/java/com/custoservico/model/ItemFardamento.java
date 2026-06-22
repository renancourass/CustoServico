package com.custoservico.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_fardamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemFardamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 20)
    private String funcao;

    @Builder.Default
    @Column(nullable = false)
    private Integer quantidade = 0;

    @Builder.Default
    @Column(nullable = false, length = 20)
    private String unidade = "peça";

    // NENHUM | VESTUARIO (P/M/G/GG) | CALCADO (35-45)
    @Builder.Default
    @Column(name = "tipo_tamanho", nullable = false, length = 20, columnDefinition = "varchar(20) default 'NENHUM'")
    private String tipoTamanho = "NENHUM";

    @Builder.Default
    @Column(nullable = false)
    private boolean ativo = true;
}
