package br.com.cbf.campeonatobrasileiro.dto;

import br.com.cbf.campeonatobrasileiro.dto.ClassificacaoTimeDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassificacaoDTO extends ClassificacaoTimeDTO {
    private List<ClassificacaoTimeDTO> times = new ArrayList<>();
}
