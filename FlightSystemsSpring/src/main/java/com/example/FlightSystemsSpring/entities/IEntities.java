package com.example.FlightSystemsSpring.entities;

import java.sql.ResultSet;
import java.util.LinkedHashMap;

public interface IEntities
{
    public void setAll(ResultSet result);
    public LinkedHashMap<String,String> getAllNeededValuesExceptIdInStringFormat();
    public Long getId();
}
