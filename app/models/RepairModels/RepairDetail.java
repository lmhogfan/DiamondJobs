package models.RepairModels;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class RepairDetail
{
    @Id
    private int repairStatusId;

    private int repairStatusCode;
    private String repairStatusName;
    private LocalDateTime statusChange;
    private String username;
    private String notes;

    public RepairDetail(int repairStatusId, int repairStatusCode, String repairStatusName,
                        LocalDateTime statusChange, String username, String notes)
    {
        this.repairStatusId = repairStatusId;
        this.repairStatusCode = repairStatusCode;
        this.repairStatusName = repairStatusName;
        this.statusChange = statusChange;
        this.username = username;
        this.notes=notes;
    }

    public int getRepairStatusCode()
    {
        return repairStatusCode;
    }

    public String getRepairStatusName()
    {
        return repairStatusName;
    }

    public LocalDateTime getStatusChange()
    {
        return statusChange;
    }

    public String getUsername()
    {
        return username;
    }

    public int getRepairStatusId()
    {
        return repairStatusId;
    }

    public String getNotes()
    {
        return notes;
    }
}
