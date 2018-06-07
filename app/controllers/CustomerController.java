package controllers;

import models.Customer;
import models.PhoneNumber;
import models.State;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
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
        String sql="SELECT c FROM Customer c ";
        List<Customer> customers= jpaApi.em().createQuery(sql,Customer.class).getResultList();

        return ok(views.html.customers.render(customers));
    }

    @Transactional
    public Result getNewCustomer()
    {
        String stateSql="SELECT s FROM State s";
        List<State> states=jpaApi.em().createQuery(stateSql,State.class).getResultList();

        return ok(views.html.newcustomer.render(states));
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

        Customer customer=new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setAddress(address);
        customer.setCity(city);
        customer.setStateId(stateId);
        customer.setEmail(email);
        customer.setNotes(notes);
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
