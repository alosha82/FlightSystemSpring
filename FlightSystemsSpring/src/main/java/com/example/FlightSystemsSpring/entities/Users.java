package com.example.FlightSystemsSpring.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

@Getter
@EqualsAndHashCode
public class Users implements IEntities
{
    private Long id;
    private String userName;
    private String password;
    private String email;
    private Integer userRole;
    private Set<String> columnNames=new HashSet<>();

    public Users()
    {
        columnNames.add("Id");
    }

    public Users(Users user)
    {
        columnNames.add("Id");
        if(user.getId()!=null)
            setId(user.getId());
        if(user.getUserName()!=null)
            setUserName(user.getUserName());
        if(user.getPassword()!=null)
            setPassword(user.getPassword());
        if(user.getEmail()!=null)
            setEmail(user.getEmail());
        if(user.getUserRole()!=null)
            setUserRole(user.getUserRole());
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserName(String userName)
    {
        if(userName != null)
        {
            userName=userName.replace("\'","");
            this.userName = "\'"+userName+"\'";
        }
        if(!columnNames.contains("Username"))
            columnNames.add("Username");
    }

    public void setPassword(String password)
    {
        if(password != null)
        {
            password=password.replace("\'","");
            this.password = "\'"+password+"\'";
        }
        if(!columnNames.contains("Password"))
            columnNames.add("Password");
    }

    public void setEmail(String email)
    {
        if(email != null)
        {
            email=email.replace("\'","");
            this.email = "\'"+email+"\'";
        }
        if(!columnNames.contains("Email"))
            columnNames.add("Email");
    }

    public void setUserRole(int userRole)
    {
        this.userRole = userRole;
        if(!columnNames.contains("User_Role"))
            columnNames.add("User_Role");
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
        setId(result.getInt(columnNames.get(i++)));
        setUserName(result.getString(columnNames.get(i++)));
        setPassword(result.getString(columnNames.get(i++)));
        setEmail(result.getString(columnNames.get(i++)));
        setUserRole(result.getInt(columnNames.get(i++)));
    }

    /**Returns LinkedHashMap of values that were set, in string format.*/
    public LinkedHashMap<String,String> getAllNeededValuesExceptIdInStringFormat()
    {
        LinkedHashMap<String,String> getterArray = new LinkedHashMap<>();
        if (columnNames.contains("Username"))
            getterArray.put("Username",getUserName());
        if (columnNames.contains("Password"))
            getterArray.put("Password",getPassword());
        if (columnNames.contains("Email"))
            getterArray.put("Email",getEmail());
        if (columnNames.contains("User_Role"))
            getterArray.put("User_Role",""+getUserRole());
        return getterArray;
    }


    @Override
    public String toString() {
        return "id=" + id +
                ", userName= " + userName +
                ", password= " + password +
                ", email= " + email +
                ", userRole= " + userRole +"\n"
                ;
    }
}
