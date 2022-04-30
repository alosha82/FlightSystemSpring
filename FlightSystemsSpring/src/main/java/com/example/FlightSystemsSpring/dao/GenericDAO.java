package com.example.FlightSystemsSpring.dao;

import com.rits.cloning.Cloner;
import com.example.FlightSystemsSpring.entities.*;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.math3.util.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class GenericDAO<T extends IEntities>
{
    private String dataBaseName="FlightSystem";
    private String tableName;
    private T entityType;
    private ArrayList<T> arrayOfEntityType;
    private PostgresConnection postgresConnection;
    private Connection connection;
    private Statement stm;

    private static GenericDAO<Administrators> administratorsDAO;
    private static GenericDAO<AirlineCompanies> airlineCompaniesDAO;
    private static GenericDAO<Countries> countriesDAO;
    private static GenericDAO<Customers> customersDAO;
    private static GenericDAO<Flights> flightsDAO;
    private static GenericDAO<Tickets> ticketsDAO;
    private static GenericDAO<UserRoles> userRolesDAO;
    private static GenericDAO<Users> usersDAO;

    public static GenericDAO<Administrators> getAdministratorsDAO()
    {
        if (administratorsDAO==null)
        {
            administratorsDAO=new GenericDAO<>("Administrators", new Administrators());
            return administratorsDAO;
        }
        return administratorsDAO;
    }

    public static GenericDAO<AirlineCompanies> getAirlineCompaniesDAO()
    {
        if (airlineCompaniesDAO==null)
        {
            airlineCompaniesDAO=new GenericDAO<>("Airline_Companies", new AirlineCompanies());
            return airlineCompaniesDAO;
        }
        return airlineCompaniesDAO;
    }

    public static GenericDAO<Countries> getCountriesDAO()
    {
        if (countriesDAO==null)
        {
            countriesDAO=new GenericDAO<>("Countries", new Countries());
            return countriesDAO;
        }
        return countriesDAO;
    }

    public static GenericDAO<Customers> getCustomersDAO()
    {
        if (customersDAO==null)
        {
            customersDAO=new GenericDAO<>("Customers", new Customers());
            return customersDAO;
        }
        return customersDAO;
    }

    public static GenericDAO<Flights> getFlightsDAO()
    {
        if (flightsDAO==null)
        {
            flightsDAO=new GenericDAO<>("Flights", new Flights());
            return flightsDAO;
        }
        return flightsDAO;
    }

    public static GenericDAO<Tickets> getTicketsDAO()
    {
        if (ticketsDAO==null)
        {
            ticketsDAO=new GenericDAO<>("Tickets", new Tickets());
            return ticketsDAO;
        }
        return ticketsDAO;
    }

    public static GenericDAO<UserRoles> getUserRolesDAO()
    {
        if (userRolesDAO==null)
        {
            userRolesDAO=new GenericDAO<>("User_Roles", new UserRoles());
            return userRolesDAO;
        }
        return userRolesDAO;
    }

    public static GenericDAO<Users> getUsersDAO()
    {
        if (usersDAO==null)
        {
            usersDAO=new GenericDAO<>("Users", new Users());
            return usersDAO;
        }
        return usersDAO;
    }

    private GenericDAO(String tableName, T tableType) {
        this.tableName=tableName;
        this.entityType = tableType;
        this.arrayOfEntityType = new ArrayList<>();
    }

    public void openDAOConnection()
    {
        this.postgresConnection =new PostgresConnection();
        this.connection = postgresConnection.getConnection(dataBaseName,"1");
        this.stm = postgresConnection.getStatement();
    }

    @SneakyThrows
    /** Gets everything from a table and saves in the proper entity.
     * Returns: Arraylist of the DAO's type(entity)  */
    public ArrayList<T> getAll()
    {
        arrayOfEntityType=getAllWithWhereClause("");
        return arrayOfEntityType;
    }
    public ArrayList<T> getAllWithWhereClause(String whereClause)
    {
        openDAOConnection();
        arrayOfEntityType=executeQueryAndSaveInTheProperEntity("select * from "+quote(tableName)+" "+whereClause,entityType);
        return arrayOfEntityType;
    }
    @SneakyThrows
    /** Executes an SQL function.
     * Returns: ResultSet
     * the caller needs to close the ResultSet connection */
    public ResultSet runSQLFunctionGetResultSet(String functionName, List<String> properlyFormattedParameters)
    {
        openDAOConnection();
        String stringToExecute ="SELECT * FROM "+functionName+"(";
        for (int i = 0; i < properlyFormattedParameters.size(); i++) {
            stringToExecute=stringToExecute+properlyFormattedParameters.get(i)+",";
        }
        stringToExecute = stringToExecute.substring(0,stringToExecute.length()-1);
        return stm.executeQuery(stringToExecute+")");
    }

    @SneakyThrows
    /** Executes an SQL function. Used to execute get_flights_by_parameters from sql stored functions
     * Has to receive an entity That supports the output table of the function.
     * Returns: ArrayList of the received entity.
     *  */
    public <V extends IEntities> ArrayList<V> runSQLFunction(String functionName, List<String> properlyFormattedParameters,V typeOfEntity)
    {
        String stringToExecute ="SELECT * FROM "+functionName+"(";
        for (int i = 0; i < properlyFormattedParameters.size(); i++) {
            stringToExecute=stringToExecute+properlyFormattedParameters.get(i)+",";
        }
        stringToExecute = stringToExecute.substring(0,stringToExecute.length()-1);
        System.out.println(stringToExecute);
        return executeQueryAndSaveInTheProperEntity(stringToExecute+")",typeOfEntity);
    }

    @SneakyThrows
    /** Gets one line from the table because the id is unique.
     * Returns:  DAO's type(entity)  */
    public T getById(long id)
    {
        return  getByFieldType(""+id,"Id");
    }

    @SneakyThrows
    /** Gets only one line from the table(the first one) matching the parameter. If needed more the one value use getByFieldTypeArr
     * Returns:  DAO's type(entity)  */
    public T getByFieldType(String formattedByPostgresSQLStandardsParameter,String fieldName)
    {
        openDAOConnection();
        Cloner cloner = new Cloner();
        String tempString="";
        if (fieldName.contains("("))
            tempString=fieldName;
        else
            tempString=quote(fieldName);
        ResultSet result= stm.executeQuery("select * from "+quote(tableName)+" WHERE "+tempString+"="+formattedByPostgresSQLStandardsParameter);
        //needed because it starts on wrong column
        result.next();
        entityType.setAll(result);
        T clone = cloner.shallowClone(entityType);
        result.close();
        return clone;
    }

    @SneakyThrows
    /** Gets every line from the table matching the parameter.
     * Returns: Arraylist of the DAO's type(entity)  */
    public ArrayList<T> getByFieldTypeArr(String formattedByPostgresSQLStandardsParameter,String fieldName)
    {
        /**checks if fieldName has a modifier like DATE for example*/
        String tempString="";
        if (fieldName.contains("("))
            tempString=fieldName;
        else
            tempString=quote(fieldName);
        String stringToExecute ="select * from "+quote(tableName)+" WHERE "+tempString+"="+formattedByPostgresSQLStandardsParameter;
        // might be a problem because the entityType might already have values.(might be problematic if the table has null fields. not sure)
        return executeQueryAndSaveInTheProperEntity(stringToExecute,entityType);
    }
    /** Removes a line from the table by its id.
     * Will fail if the entity in the table has somebody pointing to it
     * @return true if executed successfully
     * */
//    @SneakyThrows
    public boolean remove(T typeOfEntity)
    {
        openDAOConnection();
        try {
            stm.executeUpdate("DELETE from " + quote(tableName) + " WHERE " + quote("Id") + "=" + typeOfEntity.getId());
        }catch (Exception e) {return false;}

        System.out.println("done");
        return true;
    }
    @SneakyThrows
    /** Adds an entity to the table.
     * Will fail if the entity has the same values for the columns marked unique  */
    public void add(T typeOfEntity)
    {
        openDAOConnection();
        LinkedHashMap<String,String>  fieldsAndValuesInStringForm = typeOfEntity.getAllNeededValuesExceptIdInStringFormat();
        String stringForExecution = "INSERT INTO "+quote(tableName)+" ("
                + fieldsAndValuesInStringForm.keySet().stream().map(this::quote).collect(Collectors.joining(","))
                +") VALUES (" + String.join(",", fieldsAndValuesInStringForm.values()) + ")";
        System.out.println(stringForExecution);
        stm.executeUpdate(stringForExecution);
    }
    @SneakyThrows
    /** Updates an entity to the table.
     * Can fail*/
    public void update(T typeOfEntity,long id)
    {
        openDAOConnection();
        LinkedHashMap<String,String>  fieldsAndValuesInStringForm = typeOfEntity.getAllNeededValuesExceptIdInStringFormat();
        String stringForUpdate ="UPDATE "+quote(tableName)+" SET ";
        stringForUpdate = stringForUpdate+ fieldsAndValuesInStringForm.entrySet().stream().map((K)-> quote(K.getKey())+"="+K.getValue())
                .collect(Collectors.joining(","));
        System.out.println(stringForUpdate);
        stm.executeUpdate(stringForUpdate+" WHERE "+quote("Id")+"="+id);
    }

    /** Sample to the output query SELECT "Customers"."First_Name","Customers"."Last_Name"
     FROM "Tickets"
     JOIN "Flights" ON "Tickets"."Flight_Id" = "Flights"."Id"  JOIN "Customers" ON "Tickets"."Customer_Id" = "Customers"."Id"
     JOIN "Users" on "Customers"."User_Id="Users"."id"
     */
    /**Generates a query to the join function.
     * Returns: String(the generated query)
     * Parameters:
     * tablesToColumnsMap - Map of table names to a collection of the names of the columns you want to see
     * from - the name of the table after FROM statement
     * foreignFieldToOriginalField - Map with two pairs in it. The pairs are the parameters from each side of the = in the condition part
     * whereClose - the ware condition after join. Might be passed as "" if not needed*/
    @SneakyThrows
    public String generateJoinMultipleByQuery(Map<String,Collection<String>> tablesToColumnsMap,
                                              String from,
                                              LinkedHashMap<Pair<String, String>, Pair<String, String>> foreignFieldToOriginalField,
                                              String whereClause)
    {
        openDAOConnection();
        val selectContents = "SELECT " + tablesToColumnsMap.entrySet().stream()
                        .map(e -> e.getValue().stream()
                                .map(colName -> strDotStrQuoted(e.getKey(),colName))
                                .collect(Collectors.joining(",")))
                        .collect(Collectors.joining(","));

        // we work on a single foreign table, so they all have the same table name
        String fromContents = "FROM " + quote(from);

        val joinContents = foreignFieldToOriginalField.entrySet().stream()
                .map(entry -> {
                    val foreign = entry.getKey();
                    val original = entry.getValue();
                    String foreignTableName = foreign.getFirst();
                    String foreignFieldName = foreign.getSecond();
                    String originalTableName = original.getFirst();
                    String originalFieldName = original.getSecond();
                    return "JOIN " + quote(foreignTableName) + " ON "
                            + strDotStrQuoted(foreignTableName, foreignFieldName) +"=" +strDotStrQuoted(originalTableName, originalFieldName);
                })
                .collect(Collectors.joining(" "));
        String stringToExecute = String.join(" ", selectContents, fromContents, joinContents);
        return stringToExecute+" "+whereClause;
    }
    /** Adds quotes */
    private String quote(String unquoted) {return "\""+unquoted+"\"";}
    /** Quotes the strings then joins them by a dot */
    private String strDotStrQuoted(String str1, String str2) {return quote(str1)+"."+quote(str2);}

    @SneakyThrows
    /** Executes a query and saves the result in an array list of the received entity type. Note: the type may not be the same as the DAO's*/
    private <V extends IEntities> ArrayList<V> executeQueryAndSaveInTheProperEntity(String query,V typeOfEntity)
    {
        openDAOConnection();
        Cloner cloner = new Cloner();
        ArrayList<V> ArrayListOfTypeOfEntity=new ArrayList<>();
        ResultSet result= stm.executeQuery(query);
        while(result.next())
        {
            typeOfEntity.setAll(result);
            V clone = cloner.shallowClone(typeOfEntity);
            ArrayListOfTypeOfEntity.add(clone);
        }
        result.close();
        return ArrayListOfTypeOfEntity;
    }
    /**
    function joinTwoBy, and its brothers in name, joins the current type of the DAO with another table.
     Returns:An array list of the received entity type. Note: the type may not be the same as the DAO's
     * Parameters:
     * tablesToColumnsToDisplayMap - Map of table names to a collection of the names of the columns you want to see
     * thisFK - Name of the column that is the foreign key of this DAO table
     * otherTableName - Name of the table you want to join
     * otherPK - Name of the column that is the primary key of the other table
     * typeOfEntity - Type of entity that can house inside the join output by your specification
    */
    public<V extends IEntities> ArrayList<V> joinTwoBy(Map<String,Collection<String>> tablesToColumnsToDisplayMap,
                                                       String thisFK,
                                                       String otherTableName,
                                                       String otherPK,
                                                       V typeOfEntity)
    {
        return joinTwoByWithWhereClause(tablesToColumnsToDisplayMap,thisFK,otherTableName,otherPK,typeOfEntity,"");
    }
    /**
     function joinTwoBy, and its brothers in name, joins the current type of the DAO with another table.
     Returns: ResultSet
     * Parameters:
     * tablesToColumnsToDisplayMap - Map of table names to a collection of the names of the columns you want to see
     * thisFK - Name of the column that is the foreign key of this DAO table
     * otherTableName - Name of the table you want to join
     * otherPK - Name of the column that is the primary key of the other table
     */
    /**the caller needs to close the ResultSet connection */
    public ResultSet joinTwoByGetResultSet(Map<String,Collection<String>> tablesToColumnsTODisplayMap,String thisFK,String otherTableName,String otherPK)
    {
        return joinTwoByWithWhereClauseGetResultSet(tablesToColumnsTODisplayMap,thisFK,otherTableName,otherPK,"");
    }
    /**
     function joinTwoBy, and its brothers in name, joins the current type of the DAO with another table.
     Returns:An array list of the received entity type. Note: the type may not be the same as the DAO's
     * Parameters:
     * tablesToColumnsToDisplayMap - Map of table names to a collection of the names of the columns you want to see
     * thisFK - Name of the column that is the foreign key of this DAO table
     * otherTableName - Name of the table you want to join
     * otherPK - Name of the column that is the primary key of the other table
     * typeOfEntity - Type of entity that can house inside the join output by your specification
     * whereClause -the ware condition after join. Might be passed as "" if not needed
     */
    public <V extends IEntities> ArrayList<V> joinTwoByWithWhereClause(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                                       String thisFK,String otherTableName,
                                                                       String otherPK,
                                                                       V typeOfEntity,
                                                                       String whereClause)
    {
        LinkedHashMap<Pair<String,String>,Pair<String,String>> foreignToOrigins=new LinkedHashMap<>();
        foreignToOrigins.put(new Pair<>(tableName, thisFK), new Pair<>(otherTableName, otherPK));
        String stringToExecute = generateJoinMultipleByQuery(tablesToColumnsTODisplayMap,otherTableName,foreignToOrigins,whereClause);
        return executeQueryAndSaveInTheProperEntity(stringToExecute,typeOfEntity);
    }

    @SneakyThrows
    /**
     function joinTwoBy, and its brothers in name, joins the current type of the DAO with another table.
     Returns: ResultSet
     * Parameters:
     * tablesToColumnsToDisplayMap - Map of table names to a collection of the names of the columns you want to see
     * thisFK - Name of the column that is the foreign key of this DAO table
     * otherTableName - Name of the table you want to join
     * otherPK - Name of the column that is the primary key of the other table
     */
    /**the caller needs to close the ResultSet connection */
    public ResultSet joinTwoByWithWhereClauseGetResultSet(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                          String thisFK,
                                                          String otherTableName,
                                                          String otherPK,
                                                          String whereClause)
    {
        LinkedHashMap<Pair<String,String>,Pair<String,String>> foreignToOrigins=new LinkedHashMap<>();
        foreignToOrigins.put(new Pair<>(tableName, thisFK), new Pair<>(otherTableName, otherPK));
        String stringToExecute = generateJoinMultipleByQuery(tablesToColumnsTODisplayMap,otherTableName,foreignToOrigins,whereClause);
        System.out.println(stringToExecute);
        return stm.executeQuery(stringToExecute);
    }

    /**function joinMultipleBy, and its brothers in name, joins the multiple tables(no connection to this DAO's table name).
     * Returns: An array list of the received entity type. Note: the type may not be the same as the DAO's
     * Parameters:
     * tablesToColumnsMap - Map of table names to a collection of the names of the columns you want to see
     * from - the name of the table after FROM statement
     * foreignFieldToOriginalField - Map with two pares in it. The pairs are the parameters from each side of the = in the condition part
     * typeOfEntity - Type of entity that can house inside the join output by your specification
     */
    public <V extends IEntities> ArrayList<V> joinMultipleBy(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                             String from,
                                                             LinkedHashMap<Pair<String, String>, Pair<String, String>> foreignFieldToOriginalField,
                                                             V typeOfEntity)
    {
        return joinMultipleByWithWhereClause(tablesToColumnsTODisplayMap,from,foreignFieldToOriginalField,typeOfEntity,"");
    }
    /**function joinMultipleBy, and its brothers in name, joins the multiple tables(no connection to this DAO's table name).
     * Returns: An array list of the received entity type. Note: the type may not be the same as the DAO's
     * Parameters:
     * tablesToColumnsMap - Map of table names to a collection of the names of the columns you want to see
     * from - the name of the table after FROM statement
     * foreignFieldToOriginalField - Map with two pares in it. The pairs are the parameters from each side of the = in the condition part
     */
    /**the caller needs to close the ResultSet connection */
    public ResultSet joinMultipleByGetResultSet(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                String from,
                                                LinkedHashMap<Pair<String, String>, Pair<String, String>> foreignFieldToOriginalField)
    {
        return joinMultipleByWithWhereClauseGetResultSet(tablesToColumnsTODisplayMap,from,foreignFieldToOriginalField,"");
    }


    /**function joinMultipleBy, and its brothers in name, joins the multiple tables(no connection to this DAO's table name).
     * Returns: An array list of the received entity type. Note: the type may not be the same as the DAO's
     * Parameters:
     * tablesToColumnsMap - Map of table names to a collection of the names of the columns you want to see
     * from - the name of the table after FROM statement
     * foreignFieldToOriginalField - Map with two pares in it. The pairs are the parameters from each side of the = in the condition part
     * typeOfEntity - Type of entity that can house inside the join output by your specification
     * whereClose - the ware condition after join. Might be passed as "" if not needed
     */
    public <V extends IEntities> ArrayList<V> joinMultipleByWithWhereClause(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                                            String from,
                                                                            LinkedHashMap<Pair<String, String>, Pair<String, String>> foreignFieldToOriginalField,
                                                                            V typeOfEntity,
                                                                            String whereClause)
    {
        String query = generateJoinMultipleByQuery(tablesToColumnsTODisplayMap,from,foreignFieldToOriginalField,whereClause);
        System.out.println(query);
        return executeQueryAndSaveInTheProperEntity(query,typeOfEntity);
    }

    @SneakyThrows
    /**function joinMultipleBy, and its brothers in name, joins the multiple tables(no connection to this DAO's table name).
     * Returns: An array list of the received entity type. Note: the type may not be the same as the DAO's
     * Parameters:
     * tablesToColumnsMap - Map of table names to a collection of the names of the columns you want to see
     * from - the name of the table after FROM statement
     * foreignFieldToOriginalField - Map with two pares in it. The pairs are the parameters from each side of the = in the condition part
     * whereClose - the ware condition after join. Might be passed as "" if not needed
     */
    /**the caller needs to close the ResultSet connection */
    public ResultSet joinMultipleByWithWhereClauseGetResultSet(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                               String from,
                                                               LinkedHashMap<Pair<String, String>, Pair<String, String>> foreignFieldToOriginalField,
                                                               String whereClause)
    {
        String query = generateJoinMultipleByQuery(tablesToColumnsTODisplayMap,from,foreignFieldToOriginalField,whereClause);
        return stm.executeQuery(query);
    }

    @SneakyThrows
    /**closes connection */
    public void closeAllDAOConnections()
    {
        connection.close();
        stm.close();
    }

}
