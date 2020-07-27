package br.com.cbf.campeonatobrasileiro.services;

import br.com.cbf.campeonatobrasileiro.dto.TimeDTO;
import br.com.cbf.campeonatobrasileiro.entity.Time;
import br.com.cbf.campeonatobrasileiro.repository.TimeRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimeService {

    @Autowired
    private TimeRepository timeRepository;

    private Boolean timeJaExiste(String nome, Integer id) {
        return timeRepository.findByNomeIgnoreCaseAndAndIdNot(nome, id).size() > 0;
    }

    public List<Time> findAll() {
        return timeRepository.findAll();
    }

    public List<TimeDTO> listarTimes() {
        return timeRepository.findAll().stream()
                .map(timeEntity -> toDto(timeEntity)).collect(Collectors.toList());
    }

    public TimeDTO obterTime(Integer id) throws Exception {
        final Optional<Time> optionalTime = timeRepository.findById(id);
        if (optionalTime.isPresent()) {
            final Time timeEntity = optionalTime.get();
            return toDto(timeEntity);
        } else {
            throw new Exception(String.format("Time com id %d inexistente", id));
        }
    }

    public TimeDTO cadastrarTime(TimeDTO timeDTO) throws Exception {
        Time timeEntity = toEntity(timeDTO);
        if (timeDTO.getId() == null) {
            timeEntity = timeRepository.save(timeEntity);
            return toDto(timeEntity);
        } else {
            throw new Exception(String.format("O time %s já existe!" + timeDTO.getNome()));
        }
    }

    public TimeDTO atualizarTime(Integer id, TimeDTO timeDTO) throws Exception {
        if (timeJaExiste(timeDTO.getNome(), id)) {
            throw new Exception(String.format("Time %s já existe.", timeDTO.getNome()));
        }
        final Optional<Time> optionalTime = timeRepository.findById(id);
        if (optionalTime.isPresent()) {
            return toDto(timeRepository.save(toEntity(timeDTO)));
        } else {
            throw new Exception(String.format("Time com id %d inexistente", id));
        }
    }


    public void deletarTime(Integer id) throws Exception {
        final Optional<Time> optionalTime = timeRepository.findById(id);
        if (optionalTime.isPresent()) {
            timeRepository.delete(optionalTime.get());
        } else {
            throw new Exception(String.format("Time com id %d inexistente", id));
        }
    }

    private Time toEntity(TimeDTO timeDTO) {
        Time time = new Time();
        time.setId(timeDTO.getId());
        time.setEstadio(timeDTO.getEstadio());
        time.setSigla(timeDTO.getSigla());
        time.setUf(timeDTO.getUf());
        return time;
    }

    TimeDTO toDto(Time time) {
        TimeDTO timeDTO = new TimeDTO();
        timeDTO.setId(time.getId());
        timeDTO.setEstadio(time.getEstadio());
        timeDTO.setSigla(time.getSigla());
        timeDTO.setUf(time.getUf());
        return timeDTO;
    }
}
