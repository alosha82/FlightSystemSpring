package com.example.FlightSystemsSpring.repositoryJPA;


import com.example.FlightSystemsSpring.entities.Massages;
import org.springframework.data.repository.CrudRepository;

public interface MassageRepo extends CrudRepository<Massages,Integer>
{

}
