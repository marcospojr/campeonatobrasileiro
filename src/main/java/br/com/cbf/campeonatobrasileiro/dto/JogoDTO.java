package br.com.cbf.campeonatobrasileiro.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JogoDTO {

    private Integer id;
    private Integer golsTime1;
    private Integer golsTime2;
    private Integer publicoPagante;
    private LocalDateTime data;
    private Integer rodada;
    private Boolean encerrado;
    private TimeDTO time1;
    private TimeDTO time2;
}
