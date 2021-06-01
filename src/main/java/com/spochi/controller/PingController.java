package com.spochi.controller;

import com.spochi.entity.Initiative;
import com.spochi.repository.InitiativeRepository;
import com.spochi.service.query.InitiativeQuery;
import com.spochi.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class PingController {

    @Autowired
    InitiativeRepository initiativeRepository;

    @GetMapping("/ping")
    public String ping() {
        final InitiativeQuery query = new InitiativeQuery();
        query.withLimit(2);
        query.withDateTop(DateUtil.milliToDateUTC(1618200872038L).toString());
        query.withStatuses(new Integer[1]);
        query.withSorter(1);

        final List<Initiative> allInitiatives = initiativeRepository.getAllInitiatives(query);
        return "pong";
    }
}
