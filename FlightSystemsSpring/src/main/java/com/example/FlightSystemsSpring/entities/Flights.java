package com.example.FlightSystemsSpring.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

@Getter
@EqualsAndHashCode
public class Flights implements IEntities
{
    @EqualsAndHashCode.Exclude
    private Long id;
    private Long airlineCompanyId;
    private Integer originCountryId;
    private Integer destinationCountryId;
    private Timestamp departureTime;
    private Timestamp landingTime;
    private Integer remainingTickets;
    private Set<String> columnNames=new HashSet<>();

    public Flights()
    {
        columnNames.add("Id");
    }

    public Flights(Flights flight) {
        columnNames.add("Id");
        if(flight.getId()!=null)
            setId(flight.getId());
        if(flight.getAirlineCompanyId()!=null)
            setAirlineCompanyId(flight.getAirlineCompanyId());
        if(flight.getOriginCountryId()!=null)
            setOriginCountryId(flight.getOriginCountryId());
        if(flight.getDepartureTime()!=null)
            setDestinationCountryId(flight.getDestinationCountryId());
        if(flight.getDepartureTime()!=null)
            setDepartureTime(flight.getDepartureTime());
        if(flight.getLandingTime()!=null)
            setLandingTime(flight.getLandingTime());
        if(flight.getRemainingTickets()!=null)
            setRemainingTickets(flight.getRemainingTickets());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAirlineCompanyId(Long airlineCompanyId) {
        this.airlineCompanyId = airlineCompanyId;
        if(!columnNames.contains("Airline_Company_Id"))
            columnNames.add("Airline_Company_Id");
    }

    public void setOriginCountryId(Integer originCountryId)
    {
        this.originCountryId = originCountryId;
        if(!columnNames.contains("Origin_Country_Id"))
            columnNames.add("Origin_Country_Id");
    }

    public void setDestinationCountryId(Integer destinationCountryId)
    {
        this.destinationCountryId = destinationCountryId;
        if(!columnNames.contains("Destination_Country_Id"))
            columnNames.add("Destination_Country_Id");
    }

    public void setDepartureTime(Timestamp departureTime)
    {
        this.departureTime = departureTime;
        if(!columnNames.contains("Departure_Time"))
            columnNames.add("Departure_Time");
    }

    public void setLandingTime(Timestamp landingTime)
    {
        this.landingTime = landingTime;
        if(!columnNames.contains("Landing_Time"))
            columnNames.add("Landing_Time");
    }

    public void setRemainingTickets(Integer remainingTickets)
    {
        this.remainingTickets = remainingTickets;
        if(!columnNames.contains("Remaining_Tickets"))
            columnNames.add("Remaining_Tickets");
    }

    @SneakyThrows
    public void setAll(ResultSet result)
    {
        ResultSetMetaData rsmd = result.getMetaData();
        ArrayList<String> columnNames = new ArrayList<>();
        for (int i = 1; i <=rsmd.getColumnCount(); i++)
        {
            columnNames.add(rsmd.getColumnName(i));
        }
        int i=0;
        setId(result.getLong(columnNames.get(i++)));
        setAirlineCompanyId(result.getLong(columnNames.get(i++)));
        setOriginCountryId(result.getInt(columnNames.get(i++)));
        setDestinationCountryId(result.getInt(columnNames.get(i++)));
        setDepartureTime(result.getTimestamp(columnNames.get(i++)));
        setLandingTime(result.getTimestamp(columnNames.get(i++)));
        setRemainingTickets(result.getInt(columnNames.get(i++)));
    }

    /**Returns list of values that were set in string format.
     *columnNames initiated with ia Id column as a placeholder*/
    public LinkedHashMap<String,String>  getAllNeededValuesExceptIdInStringFormat()
    {
        LinkedHashMap<String,String> getterArray = new LinkedHashMap<>();
        if(columnNames.contains("Airline_Company_Id"))
            getterArray.put("Airline_Company_Id",""+getAirlineCompanyId());
        if(columnNames.contains("Origin_Country_Id"))
            getterArray.put("Origin_Country_Id",""+getOriginCountryId());
        if(columnNames.contains("Destination_Country_Id"))
            getterArray.put("Destination_Country_Id",""+getDestinationCountryId());
        if(columnNames.contains("Departure_Time"))
            getterArray.put("Departure_Time","\'"+getDepartureTime()+"\'");
        if(columnNames.contains("Landing_Time"))
            getterArray.put("Landing_Time","\'"+getLandingTime()+"\'");
        if(columnNames.contains("Remaining_Tickets"))
            getterArray.put("Remaining_Tickets",""+getRemainingTickets());
        return getterArray;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", AirlineCompanyId=" + airlineCompanyId +
                ", originCountryId=" + originCountryId +
                ", destinationCountryId=" + destinationCountryId +
                ", departureDTime='" + departureTime + '\'' +
                ", arrivalTime='" + landingTime + '\'' +
                ", remainingTickets=" + remainingTickets +"\n"
                ;
    }
}
