package models.CustomsModels;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Custom
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customId;

    private int customerId;
    private String itemDescription;
    private LocalDateTime jobStarted;
    private LocalDateTime jobFinished;
    private int envelopeNumber;
    private String quote;
    private Integer customStatusId;

    public int getCustomId()
    {
        return customId;
    }

    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(int customerId)
    {
        this.customerId = customerId;
    }

    public String getItemDescription()
    {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription)
    {
        this.itemDescription = itemDescription;
    }

    public LocalDateTime getJobStarted()
    {
        return jobStarted;
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

    public int getEnvelopeNumber()
    {
        return envelopeNumber;
    }

    public void setEnvelopeNumber(int envelopeNumber)
    {
        this.envelopeNumber = envelopeNumber;
    }

    public String getQuote()
    {
        return quote;
    }

    public void setQuote(String quote)
    {
        this.quote = quote;
    }

    public Integer getCustomStatusId()
    {
        return customStatusId;
    }

    public void setCustomStatusId(Integer customStatusId)
    {
        this.customStatusId = customStatusId;
    }
}
