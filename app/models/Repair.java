package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Repair
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int repairsId;

    private int customerId;
    private String itemDescription;
    private String workNeeded;
    private int envelopeNumber;
    private LocalDateTime jobStarted;
    private LocalDateTime jobFinished;
    private String priceEstimate;
    private BigDecimal totalPrice;
    private Integer repairStatusId;

    public int getRepairsId()
    {
        return repairsId;
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

    public String getWorkNeeded()
    {
        return workNeeded;
    }

    public void setWorkNeeded(String workNeeded)
    {
        this.workNeeded = workNeeded;
    }

    public int getEnvelopeNumber()
    {
        return envelopeNumber;
    }

    public void setEnvelopeNumber(int envelopeNumber)
    {
        this.envelopeNumber = envelopeNumber;
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

    public String getPriceEstimate()
    {
        return priceEstimate;
    }

    public void setPriceEstimate(String priceEstimate)
    {
        this.priceEstimate = priceEstimate;
    }

    public BigDecimal getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public int getRepairStatusId()
    {
        return repairStatusId;
    }

    public void setRepairStatusId(int repairStatusId)
    {
        this.repairStatusId = repairStatusId;
    }
}
