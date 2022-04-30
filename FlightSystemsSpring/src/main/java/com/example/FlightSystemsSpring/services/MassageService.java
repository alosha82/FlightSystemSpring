package com.example.FlightSystemsSpring.services;

import com.example.FlightSystemsSpring.entities.Massages;
import com.example.FlightSystemsSpring.repositoryJPA.MassageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class MassageService
{
    @Autowired
    MassageRepo massageRepo;

    public List<Massages> getAllMessages() {
        List<Massages> massages = new ArrayList<>();
        massageRepo.findAll().forEach(massages::add);    //Iterator<Book>
        return massages;
    }

    public Massages getMessage(Long id) {
        var res = massageRepo.findById(id);
        return res.orElse(null);
    }

    public void addMassage(Massages massage){
        massageRepo.save(massage);
    }
}
