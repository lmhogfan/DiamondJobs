package models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CityState
{
    @Id
    private String stateId;
    private String state;
    private String City;


    public CityState(String stateId, String state, String city)
    {
        this.stateId = stateId;
        this.state = state;
        City = city;
    }

    public String getStateId()
    {
        return stateId;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getCity()
    {
        return City;
    }

    public void setCity(String city)
    {
        City = city;
    }
}
