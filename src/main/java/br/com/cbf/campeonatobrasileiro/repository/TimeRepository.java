package br.com.cbf.campeonatobrasileiro.repository;

import br.com.cbf.campeonatobrasileiro.dto.TimeDTO;
import br.com.cbf.campeonatobrasileiro.entity.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TimeRepository extends JpaRepository<Time, Integer> {

    List<Time> findByNomeIgnoreCaseAndAndIdNot(String nome, Integer id);
}
