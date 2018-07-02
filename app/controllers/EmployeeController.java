package controllers;

import models.CustomerModels.Cities;
import models.CustomerModels.CityState;
import models.EmployeeModels.Employee;
import models.EmployeeModels.EmployeeTitle;
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

public class EmployeeController extends ApplicationController
{
    private JPAApi jpaApi;
    private FormFactory formFactory;

    @Inject
    public EmployeeController(JPAApi jpaApi, FormFactory formFactory)
    {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }

    @Transactional(readOnly = true)
    public Result getEmployees()
    {
        if (session().get("title").equals("2"))
        {
            String sql = "SELECT e FROM Employee e ";
            List<Employee> employees = jpaApi.em().createQuery(sql, Employee.class).getResultList();
            String title = "SELECT et FROM EmployeeTitle et";
            List<EmployeeTitle> employeeTitles = jpaApi.em().createQuery(title, EmployeeTitle.class)
                    .getResultList();
            return ok(views.html.EmployeeViews.employees.render(employees, employeeTitles));
        }
        else
        {
            return ok(views.html.EmployeeViews.unauthorized.render());
        }
    }

    @Transactional
    public Result getNewEmployee()
    {
        if (session().get("title").equals("2"))
        {
            String sql = "SELECT et FROM EmployeeTitle et";
            List<EmployeeTitle> employeeTitles = jpaApi.em().createQuery(sql, EmployeeTitle.class).getResultList();

            String stateSql = "SELECT NEW models.CustomerModels.CityState(s.stateId,s.state,c.city) " +
                    "FROM States s " +
                    "JOIN Cities c ON s.stateId=c.stateId " +
                    "GROUP BY s.stateId";
            List<CityState> states = jpaApi.em().createQuery(stateSql, CityState.class).getResultList();
            return ok(views.html.EmployeeViews.newemployee.render(employeeTitles, states, ""));
        }
        else
        {
            return ok(views.html.EmployeeViews.unauthorized.render());
        }
    }

    @Transactional
    public Result postNewEmployee()
    {
        if (session().get("title").equals("2"))
        {
            DynamicForm form = formFactory.form().bindFromRequest();

            String userName = form.get("userName");
            String password = form.get("password");
            String confirm = form.get("confirm");
            String firstName = form.get("firstName");
            String lastName = form.get("lastName");
            String email = form.get("email");
            String phoneNumber = form.get("phoneNumber");
            int title = Integer.parseInt(form.get("employeeTitleId"));
            String address = form.get("address");
            String city = form.get("city");
            String stateId = form.get("stateId");
            String zipcode = form.get("zipCode");

            if (password.equals(confirm))
            {
                Employee employee = new Employee();

                employee.setUserName(userName);
                employee.setFirstName(firstName);
                employee.setLastName(lastName);
                employee.setEmail(email);
                employee.setPhoneNumber(phoneNumber);
                employee.setEmployeeTitleId(title);
                employee.setAddress(address);
                employee.setCity(city);
                employee.setStateId(stateId);
                employee.setZipCode(zipcode);
                try
                {
                    byte[] salt = Password.getNewSalt();
                    byte[] hashedPassword = Password.hashPassword(password.toCharArray(), salt);
                    password = null;
                    confirm = null;
                    employee.setSalt(salt);
                    employee.setPassword(hashedPassword);
                    jpaApi.em().persist(employee);
                } catch (Exception e)
                {
                    return ok("Failed to save User");
                }
                return redirect(routes.EmployeeController.getEmployees());
            } else
            {
                String sql = "SELECT et FROM EmployeeTitle et";
                List<EmployeeTitle> employeeTitles = jpaApi.em().createQuery(sql, EmployeeTitle.class).getResultList();

                String stateSql = "SELECT NEW models.CustomerModels.CityState(s.stateId,s.state,c.city) " +
                        "FROM States s " +
                        "JOIN Cities c ON s.stateId=c.stateId " +
                        "GROUP BY s.stateId";
                List<CityState> states = jpaApi.em().createQuery(stateSql, CityState.class).getResultList();
                return ok(views.html.EmployeeViews.newemployee.render(employeeTitles, states, "Passwords do not match"));
            }
        }
        else
        {
            return ok(views.html.EmployeeViews.unauthorized.render());
        }
    }

    @Transactional(readOnly = true)
    public Result getEmployee(int employeeId)
    {
        if (employeeId == Integer.parseInt(session().get("employeeId"))||session().get("title").equals("2"))
        {
            String sql = "SELECT e FROM Employee e " +
                    "WHERE employeeId= :employeeId";
            Employee employee = jpaApi.em().createQuery(sql, Employee.class)
                    .setParameter("employeeId", employeeId).getSingleResult();
            int titleId = employee.getEmployeeTitleId();
            String title = "SELECT et FROM EmployeeTitle et " +
                    "WHERE employeeTitleId= :employeeTitleId";
            EmployeeTitle employeeTitle = jpaApi.em().createQuery(title, EmployeeTitle.class)
                    .setParameter("employeeTitleId", titleId).getSingleResult();

            return ok(views.html.EmployeeViews.employee.render(employee, employeeTitle));
        }
        else
        {
            return ok(views.html.EmployeeViews.unauthorized.render());
        }
    }

    @Transactional(readOnly = true)
    public Result getEditEmployee(int employeeId)
    {
        if (employeeId == Integer.parseInt(session().get("employeeId"))||session().get("title").equals("2"))
        {
            String sql = "SELECT et FROM EmployeeTitle et";
            List<EmployeeTitle> employeeTitles = jpaApi.em().createQuery(sql, EmployeeTitle.class).getResultList();

            String stateSql = "SELECT NEW models.CustomerModels.CityState(s.stateId,s.state,c.city) " +
                    "FROM States s " +
                    "JOIN Cities c ON s.stateId=c.stateId " +
                    "GROUP BY s.stateId";
            List<CityState> states = jpaApi.em().createQuery(stateSql, CityState.class).getResultList();

            String emp = "SELECT e FROM Employee e " +
                    "WHERE employeeId= :employeeId";
            Employee employee = jpaApi.em().createQuery(emp, Employee.class)
                    .setParameter("employeeId", employeeId).getSingleResult();

            String city = "SELECT c FROM Cities c";
            List<Cities> cities = jpaApi.em().createQuery(city, Cities.class).getResultList();
            return ok(views.html.EmployeeViews.editemployee.render(employeeTitles, states, employee, cities));
        } else
        {
            return ok(views.html.EmployeeViews.unauthorized.render());
        }
    }

    @Transactional
    public Result postEditEmployee(int employeeId)
    {
        if (employeeId == Integer.parseInt(session().get("employeeId"))||session().get("title").equals("2"))
        {
            String emp = "SELECT e FROM Employee e " +
                    "WHERE employeeId= :employeeId";
            Employee employee = jpaApi.em().createQuery(emp, Employee.class)
                    .setParameter("employeeId", employeeId).getSingleResult();

            DynamicForm form = formFactory.form().bindFromRequest();

            String email = form.get("email");
            String phoneNumber = form.get("phoneNumber");
            int title = Integer.parseInt(form.get("employeeTitleId"));
            String address = form.get("address");
            String city = form.get("city");
            String stateId = form.get("stateId");
            String zipcode = form.get("zipCode");

            employee.setEmail(email);
            employee.setPhoneNumber(phoneNumber);
            employee.setEmployeeTitleId(title);
            employee.setCity(city);
            employee.setStateId(stateId);
            employee.setAddress(address);
            employee.setZipCode(zipcode);

            jpaApi.em().persist(employee);
            return redirect(routes.EmployeeController.getEmployee(employeeId));
        } else
        {
            return ok(views.html.EmployeeViews.unauthorized.render());
        }
    }

    @Transactional(readOnly = true)
    public Result getChangePassword(int employeeId)
    {
        if (employeeId == Integer.parseInt(session().get("employeeId")))
        {
            String sql = "SELECT e FROM Employee e " +
                    "WHERE employeeId= :employeeId";
            Employee employee = jpaApi.em().createQuery(sql, Employee.class)
                    .setParameter("employeeId", employeeId).getSingleResult();
            return ok(views.html.EmployeeViews.changepassword.render("", employee));
        } else
        {
            return ok(views.html.EmployeeViews.unauthorized.render());
        }
    }

    @Transactional
    public Result postChangePassword(int employeeId)
    {
        if (employeeId == Integer.parseInt(session().get("employeeId")))
        {
            String sql = "SELECT e FROM Employee e " +
                    "WHERE employeeId= :employeeId";
            Employee employee = jpaApi.em().createQuery(sql, Employee.class)
                    .setParameter("employeeId", employeeId).getSingleResult();
            DynamicForm form = formFactory.form().bindFromRequest();
            String current = form.get("current");
            String newpw = form.get("new");
            String confirm = form.get("confirm");

            byte[] salt = employee.getSalt();
            byte[] hashedpassword = Password.hashPassword(current.toCharArray(), salt);
            if (Arrays.equals(hashedpassword, employee.getPassword()))
            {
                if (newpw.equals(confirm))
                {
                    try
                    {
                        byte[] salt2 = Password.getNewSalt();
                        byte[] hashedPassword2 = Password.hashPassword(newpw.toCharArray(), salt2);
                        newpw = null;
                        confirm = null;
                        employee.setSalt(salt2);
                        employee.setPassword(hashedPassword2);
                        jpaApi.em().persist(employee);
                    } catch (Exception e)
                    {
                        return ok("Failed to save User");
                    }
                    return redirect(routes.EmployeeController.getEmployees());
                } else
                {
                    return ok(views.html.EmployeeViews.changepassword.render("New passwords do not match", employee));
                }
            } else
            {
                return ok(views.html.EmployeeViews.changepassword.render("Current password is invalid", employee));
            }
        } else
        {
            return ok(views.html.EmployeeViews.unauthorized.render());
        }
    }
}
