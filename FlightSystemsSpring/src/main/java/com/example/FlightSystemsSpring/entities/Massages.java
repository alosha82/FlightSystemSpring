package com.example.FlightSystemsSpring.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Massages")
@Entity //Data Transfer Object
public class Massages
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long massageId ;
    private String senderName;
    private String content;
}
