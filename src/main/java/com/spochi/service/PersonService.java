package com.spochi.service;

import com.spochi.dto.PersonDTO;
import com.spochi.entity.Person;
import com.spochi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {
    @Autowired
    PersonRepository repository;

    public void create(PersonDTO personDTO) {
        Person person = new Person();
        person.setName(personDTO.getName());
        person.setAge(personDTO.getAge());
        repository.save(person);
    }

    public List<PersonDTO> getAll() {
        return repository.findAll().stream().map(person -> {
            PersonDTO personDTO = new PersonDTO();
            personDTO.setName(person.getName());
            personDTO.setAge(person.getAge());
            return personDTO;
        }).collect(Collectors.toList());
    }

    public String indicateAgeMayority(String name) {
        Person person = repository.findByName(name);
        if (person.getAge() >= 18) {
            return "Es mayor de edad";
        } else {
            return "Es menor de edad";
        }
    }
}
