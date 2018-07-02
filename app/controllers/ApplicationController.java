package controllers;

import models.EmployeeModels.Employee;
import play.mvc.Controller;

public class ApplicationController extends Controller
{

    public void login(String username, int employeeId, int employeeTitleId)
    {
        session().put("loggedin", username);
        session().put("employeeId",""+employeeId);
        session().put("title",""+employeeTitleId);
    }

    public void logout()
    {
        session().clear();
    }

    public boolean isLoggedIn()
    {
        boolean loggedIn=false;

        if((session().get("loggedin") !=null))
        {
            loggedIn=true;
        }

        return loggedIn;
    }
}