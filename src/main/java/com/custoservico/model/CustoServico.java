package com.custoservico.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "custo_servico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustoServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String descricao;

    // I - REMUNERAÇÃO
    @Column(name = "remuneracao_mensal", precision = 12, scale = 2)
    private BigDecimal remuneracaoMensal;

    @Column(name = "gratificacao", precision = 12, scale = 2)
    private BigDecimal gratificacao;

    // II - ENCARGOS SOCIAIS
    @Column(name = "percentual_encargos_sociais", precision = 5, scale = 2)
    private BigDecimal percentualEncargosSociais;

    // III - INSUMOS
    @Column(name = "fardamento", precision = 12, scale = 2)
    private BigDecimal fardamento;

    @Column(name = "vale_transporte_dias")
    private Integer valeTransporteDias;

    @Column(name = "vale_transporte_valor", precision = 12, scale = 2)
    private BigDecimal valeTransporteValor;

    @Column(name = "auxilio_alimentacao_dias")
    private Integer auxilioAlimentacaoDias;

    @Column(name = "auxilio_alimentacao_valor", precision = 12, scale = 2)
    private BigDecimal auxilioAlimentacaoValor;

    @Column(name = "plano_saude", precision = 12, scale = 2)
    private BigDecimal planoSaude;

    @Column(name = "seguro_vida", precision = 12, scale = 2)
    private BigDecimal seguroVida;

    // IV - BONIFICAÇÃO
    @Column(name = "percentual_despesas_adm", precision = 5, scale = 2)
    private BigDecimal percentualDespesasAdm;

    @Column(name = "percentual_lucro", precision = 5, scale = 2)
    private BigDecimal percentualLucro;

    // V - TRIBUTOS (percentuais individuais)
    @Column(name = "percentual_irpj", precision = 5, scale = 2)
    private BigDecimal percentualIrpj;

    @Column(name = "percentual_csll", precision = 5, scale = 2)
    private BigDecimal percentualCsll;

    @Column(name = "percentual_pis", precision = 5, scale = 2)
    private BigDecimal percentualPis;

    @Column(name = "percentual_cofins", precision = 5, scale = 2)
    private BigDecimal percentualCofins;

    @Column(name = "percentual_iss", precision = 5, scale = 2)
    private BigDecimal percentualIss;

    // Metadados
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Column(nullable = false)
    private boolean ativo = true;

    @PrePersist
    public void prePersist() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

    // ========== CÁLCULOS ==========

    public BigDecimal getSubtotalI() {
        BigDecimal r = BigDecimal.ZERO;
        if (remuneracaoMensal != null) r = r.add(remuneracaoMensal);
        if (gratificacao != null) r = r.add(gratificacao);
        return r;
    }

    public BigDecimal getSubtotalII() {
        if (percentualEncargosSociais == null || remuneracaoMensal == null) return BigDecimal.ZERO;
        return remuneracaoMensal.multiply(percentualEncargosSociais).divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
    }

    public BigDecimal getValeTransporteCalculado() {
        if (valeTransporteDias == null || valeTransporteValor == null) return BigDecimal.ZERO;
        BigDecimal bruto = valeTransporteValor.multiply(new BigDecimal(valeTransporteDias));
        // Dedução de 6% sobre remuneração mensal
        BigDecimal deducao = BigDecimal.ZERO;
        if (remuneracaoMensal != null) {
            deducao = remuneracaoMensal.multiply(new BigDecimal("0.06"));
        }
        BigDecimal resultado = bruto.subtract(deducao);
        return resultado.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : resultado;
    }

    public BigDecimal getAuxilioAlimentacaoCalculado() {
        if (auxilioAlimentacaoDias == null || auxilioAlimentacaoValor == null) return BigDecimal.ZERO;
        return auxilioAlimentacaoValor.multiply(new BigDecimal(auxilioAlimentacaoDias));
    }

    public BigDecimal getSubtotalIII() {
        BigDecimal r = BigDecimal.ZERO;
        if (fardamento != null) r = r.add(fardamento);
        r = r.add(getValeTransporteCalculado());
        r = r.add(getAuxilioAlimentacaoCalculado());
        if (planoSaude != null) r = r.add(planoSaude);
        if (seguroVida != null) r = r.add(seguroVida);
        return r;
    }

    public BigDecimal getBaseParaBonificacao() {
        return getSubtotalI().add(getSubtotalII()).add(getSubtotalIII());
    }

    public BigDecimal getDespesasAdmCalculado() {
        if (percentualDespesasAdm == null) return BigDecimal.ZERO;
        return getBaseParaBonificacao().multiply(percentualDespesasAdm).divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
    }

    public BigDecimal getLucroCalculado() {
        if (percentualLucro == null) return BigDecimal.ZERO;
        return getBaseParaBonificacao().multiply(percentualLucro).divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
    }

    public BigDecimal getSubtotalIV() {
        return getDespesasAdmCalculado().add(getLucroCalculado());
    }

    public BigDecimal getBasePrecoMensal() {
        return getSubtotalI().add(getSubtotalII()).add(getSubtotalIII()).add(getSubtotalIV());
    }

    public BigDecimal getPercentualTotalTributos() {
        BigDecimal total = BigDecimal.ZERO;
        if (percentualIrpj != null) total = total.add(percentualIrpj);
        if (percentualCsll != null) total = total.add(percentualCsll);
        if (percentualPis != null) total = total.add(percentualPis);
        if (percentualCofins != null) total = total.add(percentualCofins);
        if (percentualIss != null) total = total.add(percentualIss);
        return total;
    }

    /**
     * Preço Mensal = BasePreco / (1 - %tributos/100)
     * Os tributos incidem sobre o faturamento (preço final), não sobre a base.
     */
    public BigDecimal getPrecoMensal() {
        BigDecimal base = getBasePrecoMensal();
        BigDecimal fator = BigDecimal.ONE.subtract(
            getPercentualTotalTributos().divide(new BigDecimal("100"), 6, java.math.RoundingMode.HALF_UP)
        );
        if (fator.compareTo(BigDecimal.ZERO) <= 0) return base;
        return base.divide(fator, 2, java.math.RoundingMode.HALF_UP);
    }

    public BigDecimal getIrpjCalculado() {
        if (percentualIrpj == null) return BigDecimal.ZERO;
        return getPrecoMensal().multiply(percentualIrpj).divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
    }

    public BigDecimal getCsllCalculado() {
        if (percentualCsll == null) return BigDecimal.ZERO;
        return getPrecoMensal().multiply(percentualCsll).divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
    }

    public BigDecimal getPisCalculado() {
        if (percentualPis == null) return BigDecimal.ZERO;
        return getPrecoMensal().multiply(percentualPis).divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
    }

    public BigDecimal getCofinsCalculado() {
        if (percentualCofins == null) return BigDecimal.ZERO;
        return getPrecoMensal().multiply(percentualCofins).divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
    }

    public BigDecimal getIssCalculado() {
        if (percentualIss == null) return BigDecimal.ZERO;
        return getPrecoMensal().multiply(percentualIss).divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
    }

    public BigDecimal getSubtotalV() {
        return getIrpjCalculado().add(getCsllCalculado()).add(getPisCalculado())
                .add(getCofinsCalculado()).add(getIssCalculado());
    }
}
