package br.com.cbf.campeonatobrasileiro.rest;

import br.com.cbf.campeonatobrasileiro.dto.TimeDTO;
import br.com.cbf.campeonatobrasileiro.entity.Time;
import br.com.cbf.campeonatobrasileiro.services.TimeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/times")
public class TimeRestController {

    @Autowired
    private TimeService timeService;

    @GetMapping
    public ResponseEntity<List<TimeDTO>> getTimes() {
        return ResponseEntity.ok().body(timeService.listarTimes());
    }

    @ApiOperation(value = "Obtém os dados de um time")
    @GetMapping(value = "{id}")
    public ResponseEntity<TimeDTO> getTime(@PathVariable Integer id) throws Exception {
        return ResponseEntity.ok().body(timeService.obterTime(id));
    }

    @PostMapping
    public ResponseEntity<TimeDTO> cadastrarTime(@Valid @RequestBody TimeDTO time) throws Exception {
        timeService.cadastrarTime(time);
        return ResponseEntity.ok().body(timeService.cadastrarTime(time));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TimeDTO> atualizarTime(@RequestParam(value = "id", required = true) Integer id, @Valid @RequestBody TimeDTO timeDTO) throws Exception {
        return ResponseEntity.ok().body(timeService.atualizarTime(id, timeDTO));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarTime(@PathVariable Integer id) throws Exception {
        timeService.deletarTime(id);
        return ResponseEntity.ok().build();
    }

}
