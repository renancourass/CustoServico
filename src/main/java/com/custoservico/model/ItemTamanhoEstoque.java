package com.custoservico.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_tamanho_estoque",
       uniqueConstraints = @UniqueConstraint(columnNames = {"item_fardamento_id", "tamanho"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemTamanhoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_fardamento_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ItemFardamento itemFardamento;

    @Column(nullable = false, length = 5)
    private String tamanho;

    @Builder.Default
    @Column(nullable = false)
    private Integer quantidade = 0;
}
