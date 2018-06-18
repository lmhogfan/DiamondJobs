package models;



import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class RepairHistory
{
    @Id
    private int customerId;

    private int repairStatusId;
    private int repairStatusCode;
    private String repairStatusName;
    private LocalDateTime statusChange;
    private int repairsId;
    private String firstName;
    private String lastName;
    private String notes;

    public RepairHistory(int customerId, int repairStatusId, int repairStatusCode, String repairStatusName, LocalDateTime statusChange,
                         int repairsId, String firstName, String lastName, String notes)
    {
        this.repairsId = repairsId;
        this.repairStatusId = repairStatusId;
        this.repairStatusCode = repairStatusCode;
        this.repairStatusName = repairStatusName;
        this.statusChange = statusChange;
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.notes = notes;
    }

    public int getRepairsId()
    {
        return repairsId;
    }

    public int getRepairStatusId()
    {
        return repairStatusId;
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

    public int getCustomerId()
    {
        return customerId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getNotes()
    {
        return notes;
    }
}