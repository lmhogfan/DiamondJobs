package models.CustomerModels;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Cities
{
    @Id
    private int cityId;

    private String stateId;
    private String city;
    public String getStateId()
    {
        return stateId;
    }

    public String getCity()
    {
        return city;
    }

    public int getCityId()
    {
        return cityId;
    }

    public void setCity(String city)
    {
        this.city = city;

    }

    public void setStateId(String stateId)
    {
        this.stateId = stateId;
    }
}
