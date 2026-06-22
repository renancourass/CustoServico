package com.custoservico.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "entrega_fardamento",
       uniqueConstraints = @UniqueConstraint(columnNames = {"funcionario_id", "item_fardamento_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntregaFardamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Funcionario funcionario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_fardamento_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ItemFardamento itemFardamento;

    @Builder.Default
    @Column(nullable = false)
    private boolean entregue = false;

    @Column(name = "data_entrega")
    private LocalDate dataEntrega;

    // Tamanho entregue (vazio para itens sem tamanho)
    @Builder.Default
    @Column(length = 5, nullable = false, columnDefinition = "varchar(5) default ''")
    private String tamanho = "";
}
