package models.CustomerModels;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CustomerDetail
{
    @Id
    private int customerId;

    private String lastName;
    private String firstName;
    private String areaCode;
    private String numPrefix;
    private String phoneAddress;

    public CustomerDetail(int customerId, String lastName, String firstName, String areaCode, String numPrefix, String phoneAddress)
    {
        this.customerId = customerId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.areaCode = areaCode;
        this.numPrefix = numPrefix;
        this.phoneAddress = phoneAddress;
    }

    public int getCustomerId()
    {
        return customerId;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getAreaCode()
    {
        return areaCode;
    }

    public String getNumPrefix()
    {
        return numPrefix;
    }

    public String getPhoneAddress()
    {
        return phoneAddress;
    }
}