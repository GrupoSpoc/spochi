package com.spochi.service;

import com.spochi.repository.InitiativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitiativeService {
   @Autowired
   InitiativeRepository repository;

}