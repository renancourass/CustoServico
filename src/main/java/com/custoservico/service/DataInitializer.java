package com.custoservico.service;

import com.custoservico.model.CustoServico;
import com.custoservico.model.Usuario;
import com.custoservico.repository.UsuarioRepository;
import com.custoservico.repository.CustoServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CustoServicoRepository custoServicoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Super Admin
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = Usuario.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .nome("Super Administrador")
                    .email("admin@sistema.com")
                    .role(Usuario.Role.SUPER_ADMIN)
                    .ativo(true)
                    .build();
            usuarioRepository.save(admin);
        }

        // Usuário comum
        if (!usuarioRepository.existsByUsername("usuario")) {
            Usuario usuario = Usuario.builder()
                    .username("usuario")
                    .password(passwordEncoder.encode("user123"))
                    .nome("Usuário Final")
                    .email("usuario@sistema.com")
                    .role(Usuario.Role.USUARIO)
                    .ativo(true)
                    .build();
            usuarioRepository.save(usuario);
        }

        // Dado de exemplo (ASG - conforme planilha)
        if (custoServicoRepository.count() == 0) {
            Usuario admin = usuarioRepository.findByUsername("admin").get();
            CustoServico exemplo = CustoServico.builder()
                    .descricao("ASG - Auxiliar de Serviços Gerais")
                    .remuneracaoMensal(new BigDecimal("1731.00"))
                    .gratificacao(new BigDecimal("346.20"))
                    .percentualEncargosSociais(new BigDecimal("69.00"))
                    .fardamento(new BigDecimal("70.00"))
                    .valeTransporteDias(56)
                    .valeTransporteValor(new BigDecimal("4.00"))
                    .auxilioAlimentacaoDias(22)
                    .auxilioAlimentacaoValor(new BigDecimal("23.00"))
                    .planoSaude(BigDecimal.ZERO)
                    .seguroVida(new BigDecimal("4.90"))
                    .percentualDespesasAdm(BigDecimal.ZERO)
                    .percentualLucro(BigDecimal.ZERO)
                    .percentualIrpj(new BigDecimal("4.80"))
                    .percentualCsll(new BigDecimal("2.88"))
                    .percentualPis(new BigDecimal("0.65"))
                    .percentualCofins(new BigDecimal("3.00"))
                    .percentualIss(new BigDecimal("5.00"))
                    .usuario(admin)
                    .ativo(true)
                    .build();
            custoServicoRepository.save(exemplo);
        }
    }
}
