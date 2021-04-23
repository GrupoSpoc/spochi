package com.spochi.controller;

import com.spochi.dto.PersonDTO;
import com.spochi.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    PersonService service;

    @PostMapping
    String create(@RequestBody PersonDTO personDTO) {
        service.create(personDTO);
        return "Creado con exito!";
    }

    @GetMapping
    List<PersonDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{name}")
    String getAgeMayority(@PathVariable String name) {
        return service.indicateAgeMayority(name);
    }
}
