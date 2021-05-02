package com.spochi.controller;

import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.entity.Initiative;
import com.spochi.service.InitiativeService;
import com.spochi.service.query.InitiativeSorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/initiative")
public class InitiativeController {

    @Autowired
    InitiativeService service;

    @GetMapping("/all")
    public List<InitiativeResponseDTO> getAll(@RequestParam (required = false) Integer order) {
        final Comparator<Initiative> sorter;

        if (order != null) {
            sorter = InitiativeSorter.fromIdOrElseThrow(order).getComparator();
        } else {
            sorter = InitiativeSorter.DEFAULT_COMPARATOR.getComparator();
        }

        return service.getAll(sorter);
    }
}
