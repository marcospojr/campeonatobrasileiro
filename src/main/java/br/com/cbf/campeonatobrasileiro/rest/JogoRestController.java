package br.com.cbf.campeonatobrasileiro.rest;

import br.com.cbf.campeonatobrasileiro.dto.JogoDTO;
import br.com.cbf.campeonatobrasileiro.entity.Jogo;
import br.com.cbf.campeonatobrasileiro.dto.ClassificacaoDTO;
import br.com.cbf.campeonatobrasileiro.services.JogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class JogoRestController {

    @Autowired
    private JogoService jogoService;

    @PostMapping(value = "gerar-jogos")
    public ResponseEntity<Void> gerarJogos() {
        jogoService.gerarJogos(LocalDateTime.now());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Jogo>> obterJogos() {
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/finalizar/{id}")
    public ResponseEntity<JogoDTO> finalizar(@PathVariable Integer id, @RequestBody JogoDTO jogoDTO) throws Exception {
        return ResponseEntity.ok().body(jogoService.finalizar(id, jogoDTO));
    }

    @GetMapping(value = "/classificacao")
    public ResponseEntity<ClassificacaoDTO> obterClassificacao(@PathVariable Integer id) {
        return ResponseEntity.ok().body(jogoService.obterClassificacao());
    }

    @GetMapping(value = "/jogo/{id}")
    public ResponseEntity<JogoDTO> obterJogo(@PathVariable Integer id) {
        return ResponseEntity.ok().body(jogoService.obterJogo(id));
    }

}
