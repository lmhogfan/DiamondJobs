package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class State
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String stateId;

    private String stateName;

    public String getStateId()
    {
        return stateId;
    }

    public String getStateName()
    {
        return stateName;
    }

    public void setStateName(String stateName)
    {
        this.stateName = stateName;
    }
}
