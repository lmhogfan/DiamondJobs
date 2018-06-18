package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class RepairUpdate
{
    @Id
    private int customerId;

    private int repairsId;
    private String firstName;
    private String lastName;
    private String itemDescription;
    private int envelopeNumber;
    private LocalDateTime jobStarted;
    private LocalDateTime statusChange;
    private String repairStatusName;
    private LocalDateTime jobFinished;


    public RepairUpdate(int customerId, int repairsId, String firstName, String lastName,
                        String itemDescription, int envelopeNumber, LocalDateTime jobStarted,
                        LocalDateTime statusChange, String repairStatusName,LocalDateTime jobFinished)
    {
        this.customerId = customerId;
        this.repairsId= repairsId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.itemDescription = itemDescription;
        this.envelopeNumber = envelopeNumber;
        this.jobStarted = jobStarted;
        this.statusChange=statusChange;
        this.repairStatusName = repairStatusName;
        this.jobFinished=jobFinished;

    }

    public int getRepairsId()
    {
        return repairsId;
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

    public String getItemDescription()
    {
        return itemDescription;
    }

    public int getEnvelopeNumber()
    {
        return envelopeNumber;
    }

    public LocalDateTime getJobStarted()
    {
        return jobStarted;
    }

    public String getRepairStatusName()
    {
        return repairStatusName;
    }

    public void setRepairStatusName(String repairStatusName)
    {
        this.repairStatusName = repairStatusName;
    }

    public LocalDateTime getStatusChange()
    {
        return statusChange;
    }

    public void setStatusChange(LocalDateTime statusChange)
    {
        this.statusChange = statusChange;
    }

    public void setJobStarted(LocalDateTime jobStarted)
    {
        this.jobStarted = jobStarted;
    }

    public LocalDateTime getJobFinished()
    {
        return jobFinished;
    }

    public void setJobFinished(LocalDateTime jobFinished)
    {
        this.jobFinished = jobFinished;
    }
}
