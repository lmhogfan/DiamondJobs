package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PhoneNumber
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int phoneNumberId;

    private String areaCode;
    private String numPrefix;
    private String phoneAddress;
    private int customerId;

    public int getPhoneNumberId()
    {
        return phoneNumberId;
    }

    public String getAreaCode()
    {
        return areaCode;
    }

    public void setAreaCode(String areaCode)
    {
        this.areaCode = areaCode;
    }

    public String getNumPrefix()
    {
        return numPrefix;
    }

    public void setNumPrefix(String numPrefix)
    {
        this.numPrefix = numPrefix;
    }

    public String getPhoneAddress()
    {
        return phoneAddress;
    }

    public void setPhoneAddress(String phoneAddress)
    {
        this.phoneAddress = phoneAddress;
    }

    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(int customerId)
    {
        this.customerId = customerId;
    }
}
