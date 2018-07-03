package models.RepairModels;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RepairsCompletedOnTime
{
    @Id
    private String completeCategoryName;

    private int completedCount;

    public String getCompleteCategoryName()
    {
        return completeCategoryName;
    }

    public int getCompletedCount()
    {
        return completedCount;
    }
}
