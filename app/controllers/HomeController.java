package controllers;


import models.CustomerModels.Customer;
import models.EmployeeModels.Employee;
import models.EmployeeModels.Password;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

public class HomeController extends Controller
{

    private JPAApi jpaApi;
    private FormFactory formFactory;

    @Inject
    public HomeController(JPAApi jpaApi, FormFactory formFactory)
    {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }

    public Result index()
    {
        return ok(views.html.index.render());
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

}
