package controllers;


import json.DropDown;
import models.Cities;
import models.CityState;
import models.Customer;
import models.States;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
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
        return ok(views.html.manager.render());
    }

    public Result getEmployeeTools()
    {
        return ok(views.html.employeetools.render());
    }

    @Transactional
    public Result getTestPage()
    {
        String sql="SELECT c FROM Customer c";
        List<Customer> customers=jpaApi.em().createQuery(sql,Customer.class).getResultList();
        return ok(views.html.testpage.render(customers));
    }


}
