package controllers;

import json.DropDown;
import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

public class CustomerController extends Controller
{
    private JPAApi jpaApi;
    private FormFactory formFactory;

    @Inject CustomerController(JPAApi jpaApi,FormFactory formFactory)
    {
        this.jpaApi=jpaApi;
        this.formFactory=formFactory;
    }

    @Transactional(readOnly = true)
    public Result getCustomers()
    {
        DynamicForm form=formFactory.form().bindFromRequest();
        String searchCriteria=form.get("searchCriteria");
        if (searchCriteria==null)
        {
            searchCriteria="";
        }
        String queryParameter="%"+searchCriteria+"%";
        String sql="SELECT NEW models.CustomerDetail(c.customerId,c.lastName,c.firstName,p.areaCode,p.numPrefix,p.phoneAddress) "+
                "FROM Customer c " +
                "JOIN PhoneNumber p ON c.customerId = p.customerId "+
                "WHERE c.firstName LIKE :searchCriteria "+
                "OR c.lastName LIKE :searchCriteria "+
                "OR CONCAT(p.areaCode,p.numPrefix,p.phoneAddress) LIKE :searchCriteria";
        List<CustomerDetail> customers= jpaApi.em().createQuery(sql, CustomerDetail.class)
                .setParameter("searchCriteria",queryParameter).getResultList();

        return ok(views.html.customers.render(customers,searchCriteria));
    }

    @Transactional
    public Result getNewCustomer()
    {
        String stateSql="SELECT NEW models.CityState(s.stateId,s.state,c.city) "+
                "FROM States s "+
                "JOIN Cities c ON s.stateId=c.stateId "+
                "GROUP BY s.stateId";
        List<CityState> states=jpaApi.em().createQuery(stateSql, CityState.class).getResultList();

        return ok(views.html.newcustomer.render(states));
    }

    @Transactional
    public Result getCities()
    {
        DynamicForm form=formFactory.form().bindFromRequest();
        String state=form.get("stateId");

        DropDown.Menu[] cities;
        String stateSql="SELECT c FROM Cities c "+
                "WHERE stateId= :stateId";
        List<Cities> citiesList=jpaApi.em().createQuery(stateSql, Cities.class).setParameter("stateId",state).getResultList();
        cities= new DropDown.Menu[citiesList.size()];

        for (int i=0; i<citiesList.size(); i++)
        {
            cities[i] = new DropDown.Menu(citiesList.get(i).getCity(), citiesList.get(i).getCity());
        }

        DropDown dropDown= new DropDown(true,"notused",cities);

        return ok(Json.toJson(dropDown));
    }

    @Transactional
    public Result postNewCustomer()
    {
        DynamicForm form=formFactory.form().bindFromRequest();
        String firstName=form.get("firstName");
        String lastName=form.get("lastName");
        String address=form.get("address");
        String areaCode=form.get("areaCode");
        String numPrefix=form.get("numPrefix");
        String phoneAddress=form.get("phoneAddress");
        String city=form.get("city");
        String stateId=form.get("stateId");
        String email=form.get("email");
        String notes=form.get("notes");
        String zipcode=form.get("zipCode");

        Customer customer=new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setAddress(address);
        customer.setCity(city);
        customer.setStateId(stateId);
        customer.setEmail(email);
        customer.setNotes(notes);
        customer.setZipCode(zipcode);
        jpaApi.em().persist(customer);

        PhoneNumber phoneNumber=new PhoneNumber();
        phoneNumber.setAreaCode(areaCode);
        phoneNumber.setNumPrefix(numPrefix);
        phoneNumber.setPhoneAddress(phoneAddress);
        phoneNumber.setCustomerId(customer.getCustomerId());
        jpaApi.em().persist(phoneNumber);

        return ok("Saved new customer id: "+customer.getCustomerId());
    }

    @Transactional(readOnly = true)
    public Result getCustomer(Integer customerId)
    {
        String sql="SELECT c FROM Customer c "+
                " WHERE customerId= :customerId";
        Customer customer=jpaApi.em().createQuery(sql,Customer.class).setParameter("customerId",customerId)
                .getSingleResult();
        return ok(views.html.customer.render(customer));
    }
}
