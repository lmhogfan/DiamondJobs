package controllers;

import models.EmployeeModels.Employee;
import play.mvc.Controller;

public class ApplicationController extends Controller
{

    public void login(String username)
    {
        session().put("loggedin", username);
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