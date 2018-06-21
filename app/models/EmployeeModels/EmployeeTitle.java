package models.EmployeeModels;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EmployeeTitle
{
    @Id
    private int employeeTitleId;

    private String employeeTitleName;

    public String getEmployeeTitleName()
    {
        return employeeTitleName;
    }

    public void setEmployeeTitleName(String employeeTitleName)
    {
        this.employeeTitleName = employeeTitleName;
    }

    public int getEmployeeTitleId()
    {
        return employeeTitleId;
    }
}
