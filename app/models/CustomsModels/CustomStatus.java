package models.CustomsModels;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class CustomStatus
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customStatusId;

    private LocalDateTime statusChanged;
    private int customStatusCode;
    private int employeeId;
    private String notes;
    private int customId;

    public int getCustomStatusId()
    {
        return customStatusId;
    }

    public LocalDateTime getStatusChanged()
    {
        return statusChanged;
    }

    public void setStatusChanged(LocalDateTime statusChanged)
    {
        this.statusChanged = statusChanged;
    }

    public int getCustomStatusCode()
    {
        return customStatusCode;
    }

    public void setCustomStatusCode(int customStatusCode)
    {
        this.customStatusCode = customStatusCode;
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

    public int getCustomId()
    {
        return customId;
    }

    public void setCustomId(int customId)
    {
        this.customId = customId;
    }

}
