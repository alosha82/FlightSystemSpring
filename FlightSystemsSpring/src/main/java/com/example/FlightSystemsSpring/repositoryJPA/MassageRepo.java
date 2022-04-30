package com.example.FlightSystemsSpring.repositoryJPA;


import com.example.FlightSystemsSpring.entities.Massages;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface MassageRepo extends CrudRepository<Massages,Long>
{

}
