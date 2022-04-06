package com.example.FlightSystemsSpring.controllers;

import com.example.FlightSystemsSpring.entities.Massages;
import com.example.FlightSystemsSpring.services.MassageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RequestMapping("ContactUs")
public class MassageController
{
    @Autowired
    MassageService massageService;

    @GetMapping("/massages")
    public List<Massages> getAll(){
        return massageService.getAllMessages();
    }
    @GetMapping("/massages/{id}")
    public Massages get(@PathVariable int id){
        var res= massageService.getMessage(id);
        return (res != null ? res:new Massages());
    }
    @PostMapping("/massages")
    public void add(@RequestBody Massages massage){
        massageService.addMassage(massage);
    }
}
