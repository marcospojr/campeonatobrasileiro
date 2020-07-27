package br.com.cbf.campeonatobrasileiro.services;

import br.com.cbf.campeonatobrasileiro.dto.ClassificacaoDTO;
import br.com.cbf.campeonatobrasileiro.dto.ClassificacaoTimeDTO;
import br.com.cbf.campeonatobrasileiro.dto.JogoDTO;
import br.com.cbf.campeonatobrasileiro.entity.Jogo;
import br.com.cbf.campeonatobrasileiro.entity.Time;
import br.com.cbf.campeonatobrasileiro.repository.JogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class JogoService {

    @Autowired
    JogoRepository jogoRepository;

    @Autowired
    TimeService timeService;

//    /**
//     * @param primeiraRodada Data da primeira rodada
//     * @param datasInvalidas Datas que não podem ter jogos (ex: Datas fifa)
//     */
    public void gerarJogos(LocalDateTime primeiraRodada) {
        final List<Time> times = timeService.findAll();
        List<Time> all1 = new ArrayList<>();
        List<Time> all2 = new ArrayList<>();
        all1.addAll(times);
        all2.addAll(times);

        jogoRepository.deleteAll();

        List<Jogo> jogos = new ArrayList<>();

        int t = times.size();
        int m = times.size() / 2;
        LocalDateTime dataJogo = primeiraRodada;
        Integer rodada = 0;
        for (int i = 0; i < t - 1; i++) {
            rodada = i + 1;
            for (int j = 0; j < m; j++) {
                //Teste para ajustar o mando de campo
                Time time1;
                Time time2;
                if (j % 2 == 1 || i % 2 == 1 && j == 0) {
                    time1 = times.get(t - j - 1);
                    time2 = times.get(j);
                } else {
                    time1 = times.get(j);
                    time2 = times.get(t - j - 1);
                }
                if (time1 == null) {
                    System.out.println("Time  1 null");
                }
                jogos.add(gerarJogo(dataJogo, rodada, time1, time2));
                dataJogo = dataJogo.plusDays(7);
            }
            //Gira os times no sentido horário, mantendo o primeiro no lugar
            times.add(1, times.remove(times.size() - 1));
        }

        jogos.forEach(jogo -> System.out.println(jogo));

        jogoRepository.saveAll(jogos);

        List<Jogo> jogos2 = new ArrayList<>();

        jogos.forEach(jogo -> {
            Time time1 = jogo.getTime2();
            Time time2 = jogo.getTime1();
            jogos2.add(gerarJogo(jogo.getData().plusDays(7 * jogos.size()), jogo.getRodada() + jogos.size(), time1, time2));
        });
        jogoRepository.saveAll(jogos2);
    }

    private Jogo gerarJogo(LocalDateTime plusDays, int i, Time time1, Time time2) {
        Jogo jogo = new Jogo();

        jogo.setTime1(time1);
        jogo.setTime2(time2);
        jogo.setRodada(i);
        jogo.setData(plusDays);
        jogo.setPublicoPagante(i);
        jogo.setGolsTime1(0);
        jogo.setGolsTime2(0);
        jogo.setEncerrado(true);

        return jogo;
    }

    private JogoDTO entityToDto(Jogo jogoEntity) {
        JogoDTO jogoDto = new JogoDTO();
        jogoDto.setTime1(timeService.toDto(jogoEntity.getTime1()));
        jogoDto.setTime2(timeService.toDto(jogoEntity.getTime2()));
        jogoDto.setRodada(jogoEntity.getRodada());
        jogoDto.setData(jogoEntity.getData());
        jogoDto.setPublicoPagante(jogoEntity.getPublicoPagante());
        jogoDto.setGolsTime1(0);
        jogoDto.setGolsTime2(0);
        jogoDto.setEncerrado(true);

        return jogoDto;
    }

    public List<Jogo> obterJogos() {
        return jogoRepository.findAll();
    }

    public JogoDTO finalizar(Integer id, JogoDTO jogoDTO) throws Exception {
        Optional<Jogo> optionalJogo = jogoRepository.findById(id);
        if(optionalJogo.isPresent()) {
            final Jogo jogo = optionalJogo.get();
            jogo.setGolsTime1(jogoDTO.getGolsTime1());
            jogo.setGolsTime2(jogoDTO.getGolsTime2());
            jogo.setEncerrado(true);
            jogo.setPublicoPagante(jogoDTO.getPublicoPagante());
            return entityToDto(jogoRepository.save(jogo));
        } else {
            throw new Exception("Erro");
        }
    }

    public JogoDTO obterJogo(Integer id) {
        return entityToDto(jogoRepository.findById(id).get());
    }



    public ClassificacaoDTO obterClassificacao() {
        ClassificacaoDTO classificacaoDTO = new ClassificacaoDTO();
        final List<Time> times = timeService.findAll();

        times.forEach(time -> {
            final List<Jogo> jogosMandante = jogoRepository.findByTime1AndEncerrado(time, true);
            final List<Jogo> jogosVisitante = jogoRepository.findByTime2AndEncerrado(time, true);
            AtomicReference<Integer> vitorias = new AtomicReference<>(0);
            AtomicReference<Integer> empates = new AtomicReference<>(0);
            AtomicReference<Integer> derrotas = new AtomicReference<>(0);
            AtomicReference<Integer> golsSofridos = new AtomicReference<>(0);;
            AtomicReference<Integer> golsMarcados = new AtomicReference<>(0);;

            jogosMandante.forEach(jogo -> {
                if (jogo.getGolsTime1() > jogo.getGolsTime2()) {
                    vitorias.getAndSet(vitorias.get() + 1);
                } else if (jogo.getGolsTime1() < jogo.getGolsTime2()) {
                    derrotas.getAndSet(derrotas.get() + 1);
                } else {
                    empates.getAndSet(empates.get() + 1);
                }
                golsMarcados.set(golsMarcados.get() + jogo.getGolsTime1());
                golsSofridos.set(golsSofridos.get() + jogo.getGolsTime2());
            });

            jogosVisitante.forEach(jogo -> {
                if (jogo.getGolsTime2() > jogo.getGolsTime1()) {
                    vitorias.getAndSet(vitorias.get() + 1);
                } else if (jogo.getGolsTime2() < jogo.getGolsTime1()) {
                    derrotas.getAndSet(derrotas.get() + 1);
                } else {
                    empates.getAndSet(empates.get() + 1);
                }
                golsMarcados.set(golsMarcados.get() + jogo.getGolsTime2());
                golsSofridos.set(golsSofridos.get() + jogo.getGolsTime1());
            });

            ClassificacaoTimeDTO classificacaoTimeDTO = new ClassificacaoTimeDTO();
            classificacaoTimeDTO.setIdTime(time.getId());
            classificacaoTimeDTO.setTime(time.getNome());
            classificacaoTimeDTO.setPontos((vitorias.get() * 3) + empates.get());
            classificacaoTimeDTO.setDerrotas(derrotas.get());
            classificacaoTimeDTO.setEmpates(empates.get());
            classificacaoTimeDTO.setVitorias(vitorias.get());
            classificacaoTimeDTO.setGolsMarcados(golsMarcados.get());
            classificacaoTimeDTO.setGolsSofridos(golsSofridos.get());
            classificacaoTimeDTO.setJogos(derrotas.get() + empates.get() + vitorias.get());
            classificacaoDTO.getTimes().add(classificacaoTimeDTO);

        });

        Collections.sort(classificacaoDTO.getTimes(), Collections.reverseOrder());
        int posicao = 0;
        for (ClassificacaoTimeDTO time : classificacaoDTO.getTimes()) {
            time.setPosicao(posicao++);
        }

        return classificacaoDTO;
    }

}
