package models.CustomsModels;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class CustomUpdate
{
    @Id
    private int customId;

    private String firstName;
    private String lastName;
    private String itemDescription;
    private LocalDateTime jobStarted;
    private int customStatusCode;
    private String customStatusName;
    private int employeeId;
    private String username;
    private String notes;
    private String quote;
    private LocalDateTime jobFinished;
    private LocalDateTime statusChanged;

    public CustomUpdate(int customId, String firstName, String lastName, String itemDescription,
                        LocalDateTime jobStarted, int customStatusCode, String customStatusName,
                        int employeeId, String username, String notes, String quote, LocalDateTime jobFinished,
                        LocalDateTime statusChanged)
    {
        this.customId = customId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.itemDescription = itemDescription;
        this.jobStarted = jobStarted;
        this.customStatusCode = customStatusCode;
        this.customStatusName = customStatusName;
        this.employeeId = employeeId;
        this.username = username;
        this.notes= notes;
        this.quote=quote;
        this.jobFinished=jobFinished;
        this.statusChanged=statusChanged;
    }

    public int getCustomId()
    {
        return customId;
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

    public LocalDateTime getJobStarted()
    {
        return jobStarted;
    }

    public int getCustomStatusCode()
    {
        return customStatusCode;
    }

    public String getCustomStatusName()
    {
        return customStatusName;
    }

    public int getEmployeeId()
    {
        return employeeId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public void setItemDescription(String itemDescription)
    {
        this.itemDescription = itemDescription;
    }

    public void setJobStarted(LocalDateTime jobStarted)
    {
        this.jobStarted = jobStarted;
    }

    public void setCustomStatusCode(int customStatusCode)
    {
        this.customStatusCode = customStatusCode;
    }

    public void setCustomStatusName(String customStatusName)
    {
        this.customStatusName = customStatusName;
    }

    public void setEmployeeId(int employeeId)
    {
        this.employeeId = employeeId;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public String getQuote()
    {
        return quote;
    }

    public void setQuote(String quote)
    {
        this.quote = quote;
    }

    public LocalDateTime getJobFinished()
    {
        return jobFinished;
    }

    public void setJobFinished(LocalDateTime jobFinished)
    {
        this.jobFinished = jobFinished;
    }

    public LocalDateTime getStatusChanged()
    {
        return statusChanged;
    }

    public void setStatusChanged(LocalDateTime statusChanged)
    {
        this.statusChanged = statusChanged;
    }
}
