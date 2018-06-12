package controllers;

import models.Employee;
import models.EmployeeTitle;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

public class EmployeeController extends Controller
{
    private JPAApi jpaApi;
    private FormFactory formFactory;

    @Inject
    public EmployeeController (JPAApi jpaApi,FormFactory formFactory)
    {
        this.jpaApi=jpaApi;
        this.formFactory=formFactory;
    }

    @Transactional (readOnly = true)
    public Result getEmployees()
    {
        String sql= "SELECT e FROM Employee e ";
        List<Employee>employees= jpaApi.em().createQuery(sql, Employee.class).getResultList();
        return ok(views.html.employees.render(employees));
    }

    @Transactional
    public Result getNewEmployee()
    {
        String sql= "SELECT et FROM EmployeeTitle et";
        List<EmployeeTitle> employeeTitles=jpaApi.em().createQuery(sql,EmployeeTitle.class).getResultList();
        return ok(views.html.newemployee.render(employeeTitles));
    }
}
