package models.CustomsModels;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class CustomDetail
{
    @Id
    private int customStatusId;

    private int customStatusCode;
    private String customStatusName;
    private String notes;
    private LocalDateTime statusChanged;
    private String username;

    public CustomDetail(int customStatusId, int customStatusCode, String customStatusName, String notes, LocalDateTime statusChanged, String username)
    {
        this.customStatusId = customStatusId;
        this.customStatusCode = customStatusCode;
        this.customStatusName = customStatusName;
        this.notes = notes;
        this.statusChanged = statusChanged;
        this.username = username;
    }

    public int getCustomStatusId()
    {
        return customStatusId;
    }

    public int getCustomStatusCode()
    {
        return customStatusCode;
    }

    public void setCustomStatusCode(int customStatusCode)
    {
        this.customStatusCode = customStatusCode;
    }

    public String getCustomStatusName()
    {
        return customStatusName;
    }

    public void setCustomStatusName(String customStatusName)
    {
        this.customStatusName = customStatusName;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public LocalDateTime getStatusChanged()
    {
        return statusChanged;
    }

    public void setStatusChanged(LocalDateTime statusChanged)
    {
        this.statusChanged = statusChanged;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
