package com.custoservico.repository;

import com.custoservico.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    List<Funcionario> findByAtivoTrue();
    List<Funcionario> findByFuncaoAndAtivoTrue(String funcao);
}
