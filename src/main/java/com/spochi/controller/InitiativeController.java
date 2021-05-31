package com.spochi.controller;

import com.spochi.controller.handler.Uid;
import com.spochi.dto.InitiativeRequestDTO;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.service.InitiativeService;
import com.spochi.service.query.InitiativeSorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/initiative")
public class InitiativeController {

    @Autowired
    InitiativeService service;

    @GetMapping("/all")
    public List<InitiativeResponseDTO> getAll(@RequestParam (required = false) Integer order, @Uid String uid) {
        final InitiativeSorter sorter;

        if (order != null) {
            sorter = InitiativeSorter.fromIdOrElseThrow(order);
        } else {
            sorter = InitiativeSorter.DEFAULT_COMPARATOR;
        }

        return service.getAll(sorter, uid);
    }

    @PutMapping("/approve")
    public InitiativeResponseDTO approveInitiative(@RequestParam String initiativeId){

        return service.approveInitiative(initiativeId);
    }

    @PostMapping
    public InitiativeResponseDTO create(@RequestBody InitiativeRequestDTO request, @Uid String uid) {
        return service.create(request, uid);
    }
}
