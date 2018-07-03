package controllers;


import models.CustomerModels.Customer;
import models.CustomsModels.CustomsCompletedOnTime;
import models.EmployeeModels.Employee;
import models.EmployeeModels.Password;
import models.RepairModels.Repair;
import models.RepairModels.RepairsCompletedOnTime;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeController extends ApplicationController
{

    private JPAApi jpaApi;
    private FormFactory formFactory;

    @Inject
    public HomeController(JPAApi jpaApi, FormFactory formFactory)
    {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }

    @Transactional(readOnly = true)
    public Result index()
    {
        if(isLoggedIn())
        {
            String sql="SELECT CASE WHEN DATEDIFF(jobFinished, jobStarted)<= 5 THEN 'Less than 5 days' " +
                    "WHEN DATEDIFF(jobFinished, jobStarted)>5 THEN 'More than 5 days' END AS completeCategoryName, " +
                    "COUNT(*) AS completedCount " +
                    "FROM Repair " +
                    "WHERE jobFinished IS NOT NULL " +
                    "GROUP BY 1";
            List<RepairsCompletedOnTime>repairsCompletedOnTimeList=jpaApi.em()
                    .createNativeQuery(sql,RepairsCompletedOnTime.class).getResultList();

            String sql2="SELECT CASE WHEN DATEDIFF(jobFinished, jobStarted)<= 5 THEN 'Less than 5 days' " +
                    "WHEN DATEDIFF(jobFinished, jobStarted)>5 THEN 'More than 5 days' END AS completeCategoryName, " +
                    "COUNT(*) AS completedCount " +
                    "FROM Custom " +
                    "WHERE jobFinished IS NOT NULL " +
                    "GROUP BY 1";
            List<CustomsCompletedOnTime>customsCompletedOnTimes=jpaApi.em()
                    .createNativeQuery(sql2,CustomsCompletedOnTime.class).getResultList();
            return ok(views.html.index.render(repairsCompletedOnTimeList,customsCompletedOnTimes));
        }
        else
        {
            return redirect(routes.HomeController.getLogin());
        }
    }

    public Result getManagerTools()
    {
        return ok(views.html.EmployeeViews.manager.render());
    }

    public Result getEmployeeTools()
    {
        return ok(views.html.EmployeeViews.employeetools.render());
    }

    @Transactional
    public Result getTestPage()
    {
        String sql="SELECT c FROM Customer c";
        List<Customer> customers=jpaApi.em().createQuery(sql,Customer.class).getResultList();
        return ok(views.html.testpage.render(customers));
    }

    public Result getLogin()
    {
        return ok(views.html.login.render(""));
    }

    @Transactional(readOnly =true)
    public Result postLogin()
    {
        DynamicForm form=formFactory.form().bindFromRequest();
        String username=form.get("username");
        String password=form.get("password");

        String sql= "SELECT e FROM Employee e "+
                "WHERE username= :username";

        List<Employee> employees=jpaApi.em().createQuery(sql, Employee.class)
                .setParameter("username",username).getResultList();

        if(employees.size()==1)
        {
            Employee loggedInEmployee=employees.get(0);
            byte salt[]=loggedInEmployee.getSalt();
            byte hashedPassword[]=Password.hashPassword(password.toCharArray(),salt);

            if(Arrays.equals(hashedPassword,loggedInEmployee.getPassword()))
            {
                login(loggedInEmployee.getUserName(),loggedInEmployee.getEmployeeId(),
                        loggedInEmployee.getEmployeeTitleId());
                return redirect(routes.HomeController.index());
            }
            else
            {
                return ok(views.html.login.render("Invalid username or password"));
            }
        }
        else
        {
            try
            {
                byte salt[]=Password.getNewSalt();
                Password.hashPassword(password.toCharArray(),salt);
            }
            catch(Exception e)
            {

            }
        }
        return ok(views.html.login.render("Invalid username or password"));
    }

    public Result postLogout()
    {
        logout();
        return redirect(routes.HomeController.getLogin());
    }


}
