package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class RepairStatus
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int repairStatusId;

    private LocalDateTime statusChange;
    private int repairStatusCode;
    private int employeeId;
    private String notes;
    private int repairsId;

    public int getRepairStatusId()
    {
        return repairStatusId;
    }

    public LocalDateTime getStatusChange()
    {
        return statusChange;
    }

    public void setStatusChange(LocalDateTime statusChange)
    {
        this.statusChange = statusChange;
    }

    public int getRepairStatusCode()
    {
        return repairStatusCode;
    }

    public void setRepairStatusCode(int repairStatusCode)
    {
        this.repairStatusCode = repairStatusCode;
    }

    public int getEmployeeId()
    {
        return employeeId;
    }

    public void setEmployeeId(int employeeId)
    {
        this.employeeId = employeeId;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public int getRepairsId()
    {
        return repairsId;
    }

    public void setRepairsId(int repairsId)
    {
        this.repairsId = repairsId;
    }
}
