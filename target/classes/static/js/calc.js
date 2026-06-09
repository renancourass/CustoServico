// calc.js – cálculos em tempo real no formulário de custo

(function () {
    'use strict';

    const fmt = (v) => v.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    const val = (id) => parseFloat(document.getElementById(id)?.value) || 0;
    const set = (id, v) => { const el = document.getElementById(id); if (el) el.value = v; };
    const setTxt = (id, v) => { const el = document.getElementById(id); if (el) el.textContent = v; };

    function calcular() {
        // I - REMUNERAÇÃO
        const remMensal = val('remuneracaoMensal');
        const gratif    = val('gratificacao');
        const subtI     = remMensal + gratif;
        set('subtotalI', fmt(subtI));

        // II - ENCARGOS SOCIAIS
        const pctEncargos = val('percentualEncargos');
        const subtII = remMensal * pctEncargos / 100;
        set('subtotalII', fmt(subtII));

        // III - INSUMOS
        const fardamento = parseFloat(document.querySelector('[id$="fardamento"]')?.value) || 0;
        const vtDias  = val('vtDias');
        const vtValor = val('vtValor');
        const deducaoVT = remMensal * 0.06;
        const vtCalc = Math.max(0, (vtDias * vtValor) - deducaoVT);
        set('vtCalc', fmt(vtCalc));

        const alimDias  = val('alimDias');
        const alimValor = val('alimValor');
        const alimCalc  = alimDias * alimValor;
        set('alimCalc', fmt(alimCalc));

        const planoSaude = parseFloat(document.querySelector('[id$="planoSaude"]')?.value) || 0;
        const seguroVida = parseFloat(document.querySelector('[id$="seguroVida"]')?.value) || 0;

        const subtIII = fardamento + vtCalc + alimCalc + planoSaude + seguroVida;
        set('subtotalIII', fmt(subtIII));

        // IV - BONIFICAÇÃO
        const baseIV = subtI + subtII + subtIII;
        const pctDespAdm = val('pctDespAdm');
        const pctLucro   = val('pctLucro');
        const despAdm    = baseIV * pctDespAdm / 100;
        const lucro      = baseIV * pctLucro / 100;
        const subtIV     = despAdm + lucro;
        set('subtotalIV', fmt(subtIV));

        // V - TRIBUTOS
        const pctIrpj   = val('pctIrpj');
        const pctCsll   = val('pctCsll');
        const pctPis    = val('pctPis');
        const pctCofins = val('pctCofins');
        const pctIss    = val('pctIss');
        const pctTotal  = pctIrpj + pctCsll + pctPis + pctCofins + pctIss;
        setTxt('pctTotalTributos', fmt(pctTotal));

        // Preço final: base / (1 - pctTotal/100)
        const base = baseIV + subtIV;
        const fator = 1 - (pctTotal / 100);
        const precoMensal = fator > 0 ? base / fator : base;

        const irpj   = precoMensal * pctIrpj   / 100;
        const csll   = precoMensal * pctCsll   / 100;
        const pis    = precoMensal * pctPis    / 100;
        const cofins = precoMensal * pctCofins / 100;
        const iss    = precoMensal * pctIss    / 100;
        const subtV  = irpj + csll + pis + cofins + iss;

        setTxt('vlrIrpj',   fmt(irpj));
        setTxt('vlrCsll',   fmt(csll));
        setTxt('vlrPis',    fmt(pis));
        setTxt('vlrCofins', fmt(cofins));
        setTxt('vlrIss',    fmt(iss));
        set('subtotalV', fmt(subtV));

        // VI - Preço final
        const el = document.getElementById('precoMensal');
        if (el) {
            el.textContent = 'R$ ' + fmt(precoMensal);
            el.style.color = precoMensal > 0 ? '#4ade80' : '#f87171';
        }
    }

    // Ouve todos os campos de entrada
    document.addEventListener('DOMContentLoaded', function () {
        document.querySelectorAll('.calc-input').forEach(function (input) {
            input.addEventListener('input', calcular);
            input.addEventListener('change', calcular);
        });
        // Calcula imediatamente para popular campos ao editar
        calcular();
    });
})();
