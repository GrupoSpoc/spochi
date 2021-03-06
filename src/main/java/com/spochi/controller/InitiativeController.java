package com.spochi.controller;

import com.spochi.controller.handler.Uid;
import com.spochi.dto.InitiativeListResponseDTO;
import com.spochi.dto.InitiativeRequestDTO;
import com.spochi.dto.InitiativeResponseDTO;
import com.spochi.dto.RejectedInitiativeDTO;
import com.spochi.service.InitiativeService;
import com.spochi.service.query.InitiativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/initiative")
public class InitiativeController {

    @Autowired
    InitiativeService service;

    @GetMapping("/all")
    public InitiativeListResponseDTO getAll(@RequestParam(required = false) Integer order,
                                             @RequestParam(required = false) Integer[] statusId,
                                             @RequestParam(required = false) boolean currentUser,
                                             @RequestParam(required = false) String dateFrom,
                                             @RequestParam(required = false) String dateTo,
                                             @RequestParam(required = false) Integer limit,
                                             @RequestParam(required = false) Integer offset,
                                             @Uid String uid) {

        final InitiativeQuery query = new InitiativeQuery();

        query.withSorter(order);
        query.withStatuses(statusId);
        query.withDateFrom(dateFrom);
        query.withDateTo(dateTo);
        query.withLimit(limit);
        query.withOffset(offset);

        return service.getAll(query, uid, currentUser);
    }

    @RequestMapping(value = "/approve/{initiativeId}", method = RequestMethod.POST)
    public InitiativeResponseDTO approveInitiative(@PathVariable String initiativeId) {

        return service.approveInitiative(initiativeId);
    }

    @RequestMapping(value = "/reject", method = RequestMethod.POST)
    public InitiativeResponseDTO rejectInitiative(@RequestBody RejectedInitiativeDTO rejectedDTO) {

        return service.rejectInitiative(rejectedDTO);
    }

    @PostMapping
    public InitiativeResponseDTO create(@RequestBody InitiativeRequestDTO request, @Uid String uid) {
        return service.create(request, uid);
    }
}
