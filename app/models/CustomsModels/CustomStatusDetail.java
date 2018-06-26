package models.CustomsModels;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CustomStatusDetail
{
    @Id
    private int customStatusCode;

    private String customStatusName;

    public int getCustomStatusCode()
    {
        return customStatusCode;
    }

    public String getCustomStatusName()
    {
        return customStatusName;
    }
}
