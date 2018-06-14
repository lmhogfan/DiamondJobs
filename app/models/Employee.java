package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Employee
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int employeeId;

    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private int employeeTitleId;

    public int getEmployeeId()
    {
        return employeeId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public int getEmployeeTitleId()
    {
        return employeeTitleId;
    }

    public void setEmployeeTitleId(int employeeTitleId)
    {
        this.employeeTitleId = employeeTitleId;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
