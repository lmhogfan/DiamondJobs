package models.RepairModels;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RepairStatusDetail
{
    @Id
    private int repairStatusCode;
    private String repairStatusName;

    public int getRepairStatusCode()
    {
        return repairStatusCode;
    }

    public String getRepairStatusName()
    {
        return repairStatusName;
    }

    public void setRepairStatusName(String repairStatusName)
    {
        this.repairStatusName = repairStatusName;
    }
}
