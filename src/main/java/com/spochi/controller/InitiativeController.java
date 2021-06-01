package com.spochi.controller;

import com.spochi.controller.handler.Uid;
import com.spochi.dto.InitiativeRequestDTO;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.service.InitiativeService;
import com.spochi.service.query.InitiativeQuery;
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
    public List<InitiativeResponseDTO> getAll(@RequestParam (required = false) Integer order,
                                              @RequestParam (required = false) Integer statusId,
                                              @RequestParam (required = false) Boolean currentUser,
                                              @RequestParam (required = false) String dateFrom,
                                              @Uid String uid) {

        final InitiativeQuery query = new InitiativeQuery();

        query.withSorter(order);
        query.withStatus(statusId);
        query.withDateFrom(dateFrom);

        return service.getAll(query, uid, currentUser);
    }

    @PostMapping
    public InitiativeResponseDTO create(@RequestBody InitiativeRequestDTO request, @Uid String uid) {
        return service.create(request, uid);
    }
}
