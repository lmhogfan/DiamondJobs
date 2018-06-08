package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class States
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String stateId;

    private String state;

    public String getStateId()
    {
        return stateId;
    }

    public String getStateName()
    {
        return state;
    }

    public void setStateName(String stateName)
    {
        this.state = stateName;
    }

}
