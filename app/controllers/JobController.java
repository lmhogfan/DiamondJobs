package controllers;

import models.CustomerModels.Customer;
import models.CustomerModels.CustomerDetail;
import models.EmployeeModels.Employee;
import models.RepairModels.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

public class JobController extends Controller
{
    private JPAApi jpaApi;
    private FormFactory formFactory;

    @Inject
    public JobController(JPAApi jpaApi, FormFactory formFactory)
    {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }

    @Transactional(readOnly = true)
    public Result getRepairTools()
    {
        return ok(views.html.RepairViews.repairoptions.render());
    }

    @Transactional(readOnly = true)
    public Result getRepairs()
    {
        DynamicForm form = formFactory.form().bindFromRequest();
        String searchCriteria = form.get("searchCriteria");
        if (searchCriteria == null)
        {
            searchCriteria = "";
        }
        String queryParameter = "%" + searchCriteria + "%";
        String sql = "SELECT NEW models.CustomerModels.CustomerDetail(c.customerId,c.lastName,c.firstName,p.areaCode,p.numPrefix,p.phoneAddress) " +
                "FROM Customer c " +
                "JOIN PhoneNumber p ON c.customerId = p.customerId " +
                "WHERE c.firstName LIKE :searchCriteria " +
                "OR c.lastName LIKE :searchCriteria " +
                "OR CONCAT(p.areaCode,p.numPrefix,p.phoneAddress) LIKE :searchCriteria";
        List<CustomerDetail> customers = jpaApi.em().createQuery(sql, CustomerDetail.class)
                .setParameter("searchCriteria", queryParameter).getResultList();

        return ok(views.html.RepairViews.repairs.render(customers, searchCriteria));
    }

    @Transactional
    public Result getCustoms()
    {
        return ok(views.html.CustomsViews.customs.render());
    }

    @Transactional(readOnly = true)
    public Result getNewRepair(Integer customerId)
    {
        String sql = "SELECT c FROM Customer c " +
                " WHERE customerId= :customerId";
        Customer customer = jpaApi.em().createQuery(sql, Customer.class).setParameter("customerId", customerId)
                .getSingleResult();

        String codelist = "SELECT rs FROM RepairStatusDetail rs";
        List<RepairStatusDetail> repairStatusDetails = jpaApi.em().createQuery(codelist, RepairStatusDetail.class).getResultList();

        String userlist = "SELECT e FROM Employee e";
        List<Employee> employees = jpaApi.em().createQuery(userlist, Employee.class).getResultList();
        return ok(views.html.RepairViews.newrepair.render(customer, repairStatusDetails, employees));
    }

    @Transactional
    public Result postNewRepair(Integer customerId)
    {
        DynamicForm form = formFactory.form().bindFromRequest();
        int custId = Integer.parseInt(form.get("customerId"));
        String itemDescription = form.get("itemDescription");
        String workNeeded = form.get("workNeeded");
        int envelopeNumber = Integer.parseInt(form.get("envelopeNumber"));
        String priceEstimate = form.get("priceEstimate");
        int repairStatusId = Integer.parseInt(form.get("repairStatusId"));
        int username = Integer.parseInt(form.get("username"));

        Repair repair = new Repair();
        repair.setCustomerId(custId);
        repair.setItemDescription(itemDescription);
        repair.setWorkNeeded(workNeeded);
        repair.setEnvelopeNumber(envelopeNumber);
        repair.setJobStarted(LocalDateTime.now());
        repair.setPriceEstimate(priceEstimate);
        jpaApi.em().persist(repair);


        RepairStatus repairStatus = new RepairStatus();
        repairStatus.setRepairsId(repair.getRepairsId());
        repairStatus.setStatusChange(LocalDateTime.now());
        repairStatus.setRepairStatusCode(repairStatusId);
        repairStatus.setEmployeeId(username);
        jpaApi.em().persist(repairStatus);

        repair.setRepairStatusId(repairStatus.getRepairStatusId());
        jpaApi.em().persist(repair);


        return ok("New job started, id: " + repair.getRepairsId());
    }

    @Transactional(readOnly = true)
    public Result getUpdateRepairs()
    {
        String sql = "SELECT NEW models.RepairModels.RepairUpdate(c.customerId,r.repairsId, c.firstName,c.lastName,r.itemDescription," +
                "r.envelopeNumber, r.jobStarted, rs.statusChange, rsd.repairStatusName, r.jobFinished) " +
                "FROM Customer c " +
                "JOIN Repair r ON c.customerId=r.customerId " +
                "JOIN RepairStatus rs ON r.repairStatusId=rs.repairStatusId " +
                "JOIN RepairStatusDetail rsd ON rs.repairStatusCode=rsd.repairStatusCode " +
                "WHERE rs.repairStatusCode=1 OR rs.repairStatusCode=2 OR rs.repairStatusCode=3 " +
                "ORDER BY rsd.repairStatusName";
        List<RepairUpdate> repairUpdates = jpaApi.em().createQuery(sql, RepairUpdate.class).getResultList();
        return ok(views.html.RepairViews.updaterepairs.render(repairUpdates));
    }

    @Transactional(readOnly = true)
    public Result getUpdateRepair(Integer repairsId)
    {

        String job = "SELECT NEW models.RepairModels.RepairUpdate(c.customerId,r.repairsId, c.firstName,c.lastName,r.itemDescription," +
                "r.envelopeNumber, r.jobStarted, rs.statusChange, rsd.repairStatusName, r.jobFinished) " +
                "FROM Customer c " +
                "JOIN Repair r ON c.customerId=r.customerId " +
                "JOIN RepairStatus rs ON r.repairStatusId=rs.repairStatusId " +
                "JOIN RepairStatusDetail rsd ON rs.repairStatusCode=rsd.repairStatusCode " +
                "WHERE r.repairsId= :repairsId " +
                "ORDER BY rsd.repairStatusName";
        RepairUpdate repairUpdate = jpaApi.em().createQuery(job, RepairUpdate.class)
                .setParameter("repairsId", repairsId).getSingleResult();

        String codelist = "SELECT rs FROM RepairStatusDetail rs";
        List<RepairStatusDetail> repairStatusDetails = jpaApi.em()
                .createQuery(codelist, RepairStatusDetail.class).getResultList();

        String userlist = "SELECT e FROM Employee e";
        List<Employee> employees = jpaApi.em().createQuery(userlist, Employee.class).getResultList();

        return ok(views.html.RepairViews.updaterepair.render(repairUpdate, repairStatusDetails, employees));
    }

    @Transactional
    public Result postUpdateRepair(Integer repairsId)
    {
        DynamicForm form = formFactory.form().bindFromRequest();
        String sql = "SELECT r FROM Repair r " +
                "WHERE repairsId= :repairsId";
        Repair repair = jpaApi.em().createQuery(sql, Repair.class).setParameter("repairsId", repairsId).getSingleResult();

        LocalDateTime statusChange = LocalDateTime.now();
        int statusId = Integer.parseInt(form.get("statusId"));
        int employeeId = Integer.parseInt(form.get("username"));
        String notes = form.get("notes");


        RepairStatus repairStatus = new RepairStatus();
        repairStatus.setStatusChange(statusChange);
        repairStatus.setRepairStatusCode(statusId);
        repairStatus.setEmployeeId(employeeId);
        repairStatus.setNotes(notes);
        repairStatus.setRepairsId(repair.getRepairsId());
        jpaApi.em().persist(repairStatus);

        repair.setRepairStatusId(repairStatus.getRepairStatusId());
        jpaApi.em().persist(repair);

        if (statusId == 3||statusId==4)
        {
            repair.setJobFinished(LocalDateTime.now());
            jpaApi.em().persist(repair);
            return redirect(routes.JobController.getUpdateRepairs());
        } else
        {
            return redirect(routes.JobController.getUpdateRepairs());
        }
    }

    @Transactional
    public Result getCompletedRepairs()
    {
        String sql = "SELECT NEW models.RepairModels.RepairUpdate(c.customerId,r.repairsId, c.firstName,c.lastName,r.itemDescription," +
                "r.envelopeNumber, r.jobStarted, rs.statusChange, rsd.repairStatusName, r.jobFinished) " +
                "FROM Customer c " +
                "JOIN Repair r ON c.customerId=r.customerId " +
                "JOIN RepairStatus rs ON r.repairStatusId=rs.repairStatusId " +
                "JOIN RepairStatusDetail rsd ON rs.repairStatusCode=rsd.repairStatusCode " +
                "WHERE rs.repairStatusCode=4 " +
                "ORDER BY c.lastName,c.firstName";
        List<RepairUpdate> repairUpdates = jpaApi.em().createQuery(sql, RepairUpdate.class).getResultList();
        return ok(views.html.RepairViews.completedrepairs.render(repairUpdates));
    }

    @Transactional (readOnly = true)
    public Result getRepairHistory(Integer customerId)
    {
        String sql= "SELECT NEW models.RepairModels.RepairHistory(r.customerId, rs.repairStatusId, rsd.repairStatusCode, " +
                "rsd.repairStatusName, rs.statusChange, r.repairsId, c.firstName, c.lastName, rs.notes, r.itemDescription, e.userName) " +
                "FROM RepairStatus rs "+
                "JOIN Repair r ON rs.repairStatusId=r.repairStatusId "+
                "JOIN RepairStatusDetail rsd ON rs.repairStatusCode=rsd.repairStatusCode "+
                "JOIN Customer c ON r.customerId=c.customerId "+
                "JOIN Employee e ON rs.employeeId=e.employeeId "+
                "WHERE r.customerId= :customerId "+
                "GROUP BY rs.repairStatusId";
        List <RepairHistory> repairHistory=jpaApi.em().createQuery(sql,RepairHistory.class)
                .setParameter("customerId",customerId).getResultList();

        return ok(views.html.RepairViews.repairhistory.render(repairHistory));
    }

    @Transactional (readOnly = true)
    public Result getRepairDetail(Integer repairsId)
    {
        String sql= "SELECT NEW models.RepairModels.RepairHistory(r.customerId, rs.repairStatusId, rsd.repairStatusCode, " +
                "rsd.repairStatusName, rs.statusChange, r.repairsId, c.firstName, c.lastName, rs.notes, r.itemDescription, e.userName) " +
                "FROM RepairStatus rs "+
                "JOIN Repair r ON rs.repairStatusId=r.repairStatusId "+
                "JOIN RepairStatusDetail rsd ON rs.repairStatusCode=rsd.repairStatusCode "+
                "JOIN Customer c ON r.customerId=c.customerId "+
                "JOIN Employee e ON rs.employeeId=e.employeeId "+
                "WHERE rs.repairsId= :repairsId "+
                "GROUP BY rs.repairStatusId";
        RepairHistory repairHistory=jpaApi.em().createQuery(sql, RepairHistory.class)
                .setParameter("repairsId",repairsId).getSingleResult();
        String details="SELECT NEW models.RepairModels.RepairDetail(rs.repairStatusId, rs.repairStatusCode, rsd.repairStatusName, "+
                "rs.statusChange, e.userName, rs.notes) "+
                "FROM RepairStatus rs "+
                "JOIN RepairStatusDetail rsd ON rs.repairStatusCode=rsd.repairStatusCode "+
                "JOIN Employee e ON rs.employeeId=e.employeeId "+
                "WHERE rs.repairsId= :repairsId "+
                "GROUP BY rs.repairStatusId";
        List<RepairDetail> repairDetails=jpaApi.em().createQuery(details,RepairDetail.class)
                .setParameter("repairsId",repairsId).getResultList();
        return ok(views.html.RepairViews.repairdetail.render(repairHistory,repairDetails));
    }

}


