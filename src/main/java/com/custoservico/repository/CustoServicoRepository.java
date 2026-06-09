package com.custoservico.repository;

import com.custoservico.model.CustoServico;
import com.custoservico.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustoServicoRepository extends JpaRepository<CustoServico, Long> {

    List<CustoServico> findByAtivoTrueOrderByCriadoEmDesc();

    List<CustoServico> findByUsuarioAndAtivoTrueOrderByCriadoEmDesc(Usuario usuario);

    @Query("SELECT COUNT(c) FROM CustoServico c WHERE c.ativo = true")
    long countAtivos();

    @Query("SELECT SUM(c.remuneracaoMensal) FROM CustoServico c WHERE c.ativo = true")
    Double sumRemuneracaoMensal();
}
