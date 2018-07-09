package controllers;

import models.CustomerModels.Customer;
import models.CustomerModels.CustomerDetail;
import models.CustomsModels.*;
import models.EmployeeModels.Employee;
import models.RepairModels.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import services.Email;
import views.html.RepairViews.repairemail;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class JobController extends ApplicationController
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
    public Result getAllRepairs()
    {
        DynamicForm form = formFactory.form().bindFromRequest();
        String searchCriteria = form.get("searchCriteria");
        if (searchCriteria == null)
        {
            searchCriteria = "";
        }
        String queryParameter = "%" + searchCriteria + "%";
        String sql = "SELECT NEW models.RepairModels.RepairUpdate(c.customerId,r.repairsId, c.firstName,c.lastName,r.itemDescription," +
                "r.envelopeNumber, r.jobStarted, rs.statusChange, rsd.repairStatusName, r.jobFinished) " +
                "FROM Customer c " +
                "JOIN Repair r ON c.customerId=r.customerId " +
                "JOIN RepairStatus rs ON r.repairStatusId=rs.repairStatusId " +
                "JOIN RepairStatusDetail rsd ON rs.repairStatusCode=rsd.repairStatusCode " +
                "WHERE c.firstName LIKE :searchCriteria " +
                "OR c.lastName LIKE :searchCriteria " +
                "ORDER BY rsd.repairStatusName";
        List<RepairUpdate> repairUpdates = jpaApi.em().createQuery(sql, RepairUpdate.class)
                .setParameter("searchCriteria", queryParameter).getResultList();
        return ok(views.html.RepairViews.allrepairs.render(repairUpdates, searchCriteria));
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


        return (redirect(routes.JobController.getUpdateRepairs()));
    }

    @Transactional(readOnly = true)
    public Result getUpdateRepairs()
    {
        DynamicForm form = formFactory.form().bindFromRequest();

        String sql = "SELECT NEW models.RepairModels.RepairUpdate(c.customerId,r.repairsId, c.firstName,c.lastName,r.itemDescription," +
                "r.envelopeNumber, r.jobStarted, rs.statusChange, rsd.repairStatusName, r.jobFinished) " +
                "FROM Customer c " +
                "JOIN Repair r ON c.customerId=r.customerId " +
                "JOIN RepairStatus rs ON r.repairStatusId=rs.repairStatusId " +
                "JOIN RepairStatusDetail rsd ON rs.repairStatusCode=rsd.repairStatusCode " +
                "WHERE (rs.repairStatusCode=1 OR rs.repairStatusCode=2 OR rs.repairStatusCode=3) " +
                "ORDER BY rsd.repairStatusName";
        List<RepairUpdate> repairUpdates = jpaApi.em().createQuery(sql, RepairUpdate.class)
                .getResultList();

        return ok(views.html.RepairViews.updaterepairs.render(repairUpdates, false));
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

        String sql2 = "SELECT NEW models.RepairModels.RepairUpdate(c.customerId,r.repairsId, c.firstName,c.lastName,r.itemDescription," +
                "r.envelopeNumber, r.jobStarted, rs.statusChange, rsd.repairStatusName, r.jobFinished) " +
                "FROM Customer c " +
                "JOIN Repair r ON c.customerId=r.customerId " +
                "JOIN RepairStatus rs ON r.repairStatusId=rs.repairStatusId " +
                "JOIN RepairStatusDetail rsd ON rs.repairStatusCode=rsd.repairStatusCode " +
                "WHERE (rs.repairStatusCode=1 OR rs.repairStatusCode=2 OR rs.repairStatusCode=3) " +
                "ORDER BY rsd.repairStatusName";
        List<RepairUpdate> repairUpdates = jpaApi.em().createQuery(sql2, RepairUpdate.class)
                .getResultList();

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

        String cust = "SELECT c FROM Customer c " +
                "WHERE customerId= :customerId";
        Customer customer = jpaApi.em().createQuery(cust, Customer.class)
                .setParameter("customerId", repair.getCustomerId()).getSingleResult();
        if (statusId == 3)
        {
            BigDecimal finalprice = new BigDecimal(form.get("totalprice"));
            String notify = form.get("notify");
            repair.setJobFinished(LocalDateTime.now());
            repair.setTotalPrice(finalprice);
            jpaApi.em().persist(repair);
            if (notify.equals("yes"))
            {
                Email.sendEmail("<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>Repair Complete</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<p>Dear " + customer.getFirstName() + ",</p>\n" +
                        "<p>Your repair is complete! Your total balance is: $" + repair.getTotalPrice() + ". Please stop by the store during regular business hours to pick up your items.\n" +
                        "If you have any questions, please call 501-327-2825. Thank you again for your business!</p>\n" +
                        "Brooks Fine Jewelry\n<br>" +
                        "1304 W. Oak St.\n<br>" +
                        "Conway, AR 72034\n<br>" +
                        "501-327-2825\n" +
                        "</body>\n" +
                        "</html>", customer.getEmail());
            }
            return ok(views.html.RepairViews.updaterepairs.render(repairUpdates,true));

        } else if (statusId == 4)
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
        DynamicForm form = formFactory.form().bindFromRequest();
        String searchCriteria = form.get("searchCriteria");
        if (searchCriteria == null)
        {
            searchCriteria = "";
        }
        String queryParameter = "%" + searchCriteria + "%";

        String sql = "SELECT NEW models.RepairModels.RepairUpdate(c.customerId,r.repairsId, c.firstName,c.lastName,r.itemDescription," +
                "r.envelopeNumber, r.jobStarted, rs.statusChange, rsd.repairStatusName, r.jobFinished) " +
                "FROM Customer c " +
                "JOIN Repair r ON c.customerId=r.customerId " +
                "JOIN RepairStatus rs ON r.repairStatusId=rs.repairStatusId " +
                "JOIN RepairStatusDetail rsd ON rs.repairStatusCode=rsd.repairStatusCode " +
                "WHERE rs.repairStatusCode=4 " +
                "AND (c.firstName LIKE :searchCriteria " +
                "OR c.lastName LIKE :searchCriteria) " +
                "ORDER BY c.lastName,c.firstName";
        List<RepairUpdate> repairUpdates = jpaApi.em().createQuery(sql, RepairUpdate.class)
                .setParameter("searchCriteria", queryParameter).getResultList();
        return ok(views.html.RepairViews.completedrepairs.render(repairUpdates, searchCriteria));
    }

    @Transactional(readOnly = true)
    public Result getRepairHistory(Integer customerId)
    {
        String sql = "SELECT NEW models.RepairModels.RepairHistory(r.customerId, rs.repairStatusId, rsd.repairStatusCode, " +
                "rsd.repairStatusName, rs.statusChange, r.repairsId, c.firstName, c.lastName, rs.notes, r.itemDescription, e.userName) " +
                "FROM RepairStatus rs " +
                "JOIN Repair r ON rs.repairStatusId=r.repairStatusId " +
                "JOIN RepairStatusDetail rsd ON rs.repairStatusCode=rsd.repairStatusCode " +
                "JOIN Customer c ON r.customerId=c.customerId " +
                "JOIN Employee e ON rs.employeeId=e.employeeId " +
                "WHERE r.customerId= :customerId " +
                "GROUP BY rs.repairStatusId";
        List<RepairHistory> repairHistory = jpaApi.em().createQuery(sql, RepairHistory.class)
                .setParameter("customerId", customerId).getResultList();
        String cust = "SELECT c FROM Customer c " +
                "WHERE customerId= :customerId";
        Customer customer = jpaApi.em().createQuery(cust, Customer.class)
                .setParameter("customerId", customerId).getSingleResult();
        return ok(views.html.RepairViews.repairhistory.render(repairHistory, customer));
    }

    @Transactional(readOnly = true)
    public Result getRepairDetail(Integer repairsId)
    {
        String sql = "SELECT NEW models.RepairModels.RepairHistory(r.customerId, rs.repairStatusId, rsd.repairStatusCode, " +
                "rsd.repairStatusName, rs.statusChange, r.repairsId, c.firstName, c.lastName, rs.notes, r.itemDescription, e.userName) " +
                "FROM RepairStatus rs " +
                "JOIN Repair r ON rs.repairStatusId=r.repairStatusId " +
                "JOIN RepairStatusDetail rsd ON rs.repairStatusCode=rsd.repairStatusCode " +
                "JOIN Customer c ON r.customerId=c.customerId " +
                "JOIN Employee e ON rs.employeeId=e.employeeId " +
                "WHERE rs.repairsId= :repairsId " +
                "GROUP BY rs.repairStatusId";
        RepairHistory repairHistory = jpaApi.em().createQuery(sql, RepairHistory.class)
                .setParameter("repairsId", repairsId).getSingleResult();
        String details = "SELECT NEW models.RepairModels.RepairDetail(rs.repairStatusId, rs.repairStatusCode, rsd.repairStatusName, " +
                "rs.statusChange, e.userName, rs.notes) " +
                "FROM RepairStatus rs " +
                "JOIN RepairStatusDetail rsd ON rs.repairStatusCode=rsd.repairStatusCode " +
                "JOIN Employee e ON rs.employeeId=e.employeeId " +
                "WHERE rs.repairsId= :repairsId " +
                "GROUP BY rs.repairStatusId";
        List<RepairDetail> repairDetails = jpaApi.em().createQuery(details, RepairDetail.class)
                .setParameter("repairsId", repairsId).getResultList();
        return ok(views.html.RepairViews.repairdetail.render(repairHistory, repairDetails));
    }

    @Transactional(readOnly = true)
    public Result getUpdateCustoms()
    {
        DynamicForm form = formFactory.form().bindFromRequest();
        String searchCriteria = form.get("searchCriteria");
        if (searchCriteria == null)
        {
            searchCriteria = "";
        }
        String queryParameter = "%" + searchCriteria + "%";
        String sql = "SELECT NEW models.CustomsModels.CustomUpdate(cu.customId, c.firstName,c.lastName, " +
                "cu.itemDescription, cu.jobStarted, cs.customStatusCode, csd.customStatusName, cs.employeeId, e.userName, " +
                "cs.notes,cu.quote, cu.jobFinished, cs.statusChanged, cu.envelopeNumber) " +
                "FROM Custom cu " +
                "JOIN CustomStatus cs ON cu.customStatusId=cs.customStatusId " +
                "JOIN Customer c ON cu.customerId=c.customerId " +
                "JOIN CustomStatusDetail csd ON cs.customStatusCode=csd.customStatusCode " +
                "JOIN Employee e ON cs.employeeId=e.employeeId " +
                "WHERE cs.customStatusCode <> 7 " +
                "AND (c.firstName LIKE :searchCriteria " +
                "OR c.lastName LIKE :searchCriteria) " +
                "GROUP BY cu.customId";
        List<CustomUpdate> customUpdates = jpaApi.em().createQuery(sql, CustomUpdate.class)
                .setParameter("searchCriteria", queryParameter).getResultList();
        return ok(views.html.CustomsViews.customs.render(customUpdates, searchCriteria));
    }

    @Transactional(readOnly = true)
    public Result getNewCustoms()
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

        return ok(views.html.CustomsViews.newcustoms.render(customers, searchCriteria));
    }

    @Transactional(readOnly = true)
    public Result getNewCustom(int customerId)
    {
        String sql = "SELECT c FROM Customer c " +
                " WHERE customerId= :customerId";
        Customer customer = jpaApi.em().createQuery(sql, Customer.class).setParameter("customerId", customerId)
                .getSingleResult();

        String codelist = "SELECT csd FROM CustomStatusDetail csd";
        List<CustomStatusDetail> customStatusDetails = jpaApi.em().createQuery(codelist, CustomStatusDetail.class)
                .getResultList();

        String userlist = "SELECT e FROM Employee e";
        List<Employee> employees = jpaApi.em().createQuery(userlist, Employee.class).getResultList();
        return ok(views.html.CustomsViews.newcustom.render(customer, customStatusDetails, employees));
    }

    @Transactional
    public Result postNewCustom(int customerId)
    {
        DynamicForm form = formFactory.form().bindFromRequest();
        int custId = Integer.parseInt(form.get("customerId"));
        String itemDescription = form.get("itemDescription");
        int envelopeNumber = Integer.parseInt(form.get("envelopeNumber"));
        String quote = form.get("quote");
        int customStatusCode = Integer.parseInt(form.get("customStatusCode"));
        int username = Integer.parseInt(form.get("username"));

        Custom custom = new Custom();
        custom.setCustomerId(custId);
        custom.setItemDescription(itemDescription);
        custom.setJobStarted(LocalDateTime.now());
        custom.setEnvelopeNumber(envelopeNumber);
        if (quote != null)
        {
            custom.setQuote(quote);
        }
        jpaApi.em().persist(custom);

        CustomStatus customStatus = new CustomStatus();
        customStatus.setStatusChanged(LocalDateTime.now());
        customStatus.setCustomStatusCode(customStatusCode);
        customStatus.setEmployeeId(username);
        customStatus.setCustomId(custom.getCustomId());
        jpaApi.em().persist(customStatus);

        custom.setCustomStatusId(customStatus.getCustomStatusId());
        jpaApi.em().persist(custom);

        return redirect(routes.JobController.getUpdateCustoms());
    }


    @Transactional(readOnly = true)
    public Result getCompletedCustoms()
    {
        String sql = "SELECT NEW models.CustomsModels.CustomUpdate(cu.customId, c.firstName,c.lastName, " +
                "cu.itemDescription, cu.jobStarted, cs.customStatusCode, csd.customStatusName, cs.employeeId, e.userName, " +
                "cs.notes,cu.quote, cu.jobFinished, cs.statusChanged, cu.envelopeNumber) " +
                "FROM Custom cu " +
                "JOIN CustomStatus cs ON cu.customStatusId=cs.customStatusId " +
                "JOIN Customer c ON cu.customerId=c.customerId " +
                "JOIN CustomStatusDetail csd ON cs.customStatusCode=csd.customStatusCode " +
                "JOIN Employee e ON cs.employeeId=e.employeeId " +
                "WHERE cs.customStatusCode = 7 " +
                "GROUP BY cu.customId";
        List<CustomUpdate> customUpdates = jpaApi.em().createQuery(sql, CustomUpdate.class).getResultList();
        return ok(views.html.CustomsViews.completedcustoms.render(customUpdates));
    }

    @Transactional(readOnly = true)
    public Result getUpdateCustom(int customId)
    {
        String sql = "SELECT NEW models.CustomsModels.CustomUpdate(cu.customId, c.firstName,c.lastName, " +
                "cu.itemDescription, cu.jobStarted, cs.customStatusCode, csd.customStatusName, cs.employeeId, e.userName, " +
                "cs.notes,cu.quote, cu.jobFinished, cs.statusChanged, cu.envelopeNumber) " +
                "FROM Custom cu " +
                "JOIN CustomStatus cs ON cu.customStatusId=cs.customStatusId " +
                "JOIN Customer c ON cu.customerId=c.customerId " +
                "JOIN CustomStatusDetail csd ON cs.customStatusCode=csd.customStatusCode " +
                "JOIN Employee e ON cs.employeeId=e.employeeId " +
                "WHERE cu.customId= :customId " +
                "ORDER BY cu.customId";
        CustomUpdate customUpdate = jpaApi.em().createQuery(sql, CustomUpdate.class)
                .setParameter("customId", customId).getSingleResult();

        String userlist = "SELECT e FROM Employee e";
        List<Employee> employees = jpaApi.em().createQuery(userlist, Employee.class).getResultList();

        String status = "SELECT csd FROM CustomStatusDetail csd";
        List<CustomStatusDetail> customStatusDetails = jpaApi.em().createQuery(status, CustomStatusDetail.class)
                .getResultList();
        return ok(views.html.CustomsViews.updatecustom.render(customUpdate, employees, customStatusDetails));
    }

    @Transactional
    public Result postUpdateCustom(int customId)
    {
        DynamicForm form = formFactory.form().bindFromRequest();
        String sql = "SELECT c FROM Custom c " +
                "WHERE customId= :customId";
        Custom custom = jpaApi.em().createQuery(sql, Custom.class).setParameter("customId", customId).getSingleResult();

        LocalDateTime statusChange = LocalDateTime.now();
        int statusId = Integer.parseInt(form.get("statusId"));
        int employeeId = Integer.parseInt(form.get("username"));
        String notes = form.get("notes");

        CustomStatus customStatus = new CustomStatus();
        customStatus.setStatusChanged(statusChange);
        customStatus.setCustomStatusCode(statusId);
        customStatus.setEmployeeId(employeeId);
        customStatus.setNotes(notes);
        customStatus.setCustomId(custom.getCustomId());
        jpaApi.em().persist(customStatus);

        custom.setCustomStatusId(customStatus.getCustomStatusId());
        jpaApi.em().persist(custom);

        String cust = "SELECT c FROM Customer c " +
                "WHERE customerId= :customerId";
        Customer customer = jpaApi.em().createQuery(cust, Customer.class)
                .setParameter("customerId", custom.getCustomerId()).getSingleResult();
        if (statusId == 6)
        {
            BigDecimal finalprice = new BigDecimal(form.get("totalprice"));
            String notify = form.get("notify");
            custom.setJobFinished(LocalDateTime.now());
            custom.setTotalPrice(finalprice);
            jpaApi.em().persist(custom);
            if (notify.equals("yes"))
            {
                Email.sendEmail("<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>Custom Job Complete</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<p>Dear " + customer.getFirstName() + ",</p>\n" +
                        "<p>Your new custom job is complete! Your total balance is: $" + custom.getTotalPrice() + ". Please stop by the store during regular business hours to pick up your items.\n" +
                        "If you have any questions, please call 501-327-2825. Thank you again for your business!</p>\n" +
                        "Brooks Fine Jewelry\n<br>" +
                        "1304 W. Oak St.\n<br>" +
                        "Conway, AR 72034\n<br>" +
                        "501-327-2825\n" +
                        "</body>\n" +
                        "</html>", customer.getEmail());
            }
            return redirect(routes.JobController.getUpdateCustoms());

        } else if (statusId == 7)
        {
            custom.setJobFinished(LocalDateTime.now());
            jpaApi.em().persist(custom);
            return redirect(routes.JobController.getUpdateCustoms());

        } else
        {
            return redirect(routes.JobController.getUpdateCustoms());
        }
    }

    @Transactional(readOnly = true)
    public Result getCustomDetail(int customId)
    {
        String sql = "SELECT NEW models.CustomsModels.CustomUpdate(cu.customId, c.firstName,c.lastName, " +
                "cu.itemDescription, cu.jobStarted, cs.customStatusCode, csd.customStatusName, cs.employeeId, e.userName, " +
                "cs.notes,cu.quote, cu.jobFinished, cs.statusChanged, cu.envelopeNumber) " +
                "FROM Custom cu " +
                "JOIN CustomStatus cs ON cu.customStatusId=cs.customStatusId " +
                "JOIN Customer c ON cu.customerId=c.customerId " +
                "JOIN CustomStatusDetail csd ON cs.customStatusCode=csd.customStatusCode " +
                "JOIN Employee e ON cs.employeeId=e.employeeId " +
                "WHERE cu.customId= :customId " +
                "ORDER BY cu.customId";
        CustomUpdate customUpdate = jpaApi.em().createQuery(sql, CustomUpdate.class)
                .setParameter("customId", customId).getSingleResult();

        String updates = "SELECT NEW models.CustomsModels.CustomDetail(cs.customStatusId, cs.customStatusCode, " +
                "csd.customStatusName, cs.notes, cs.statusChanged, e.userName) " +
                "FROM CustomStatus cs " +
                "JOIN CustomStatusDetail csd ON cs.customStatusCode=csd.customStatusCode " +
                "JOIN Employee e ON cs.employeeId=e.employeeId " +
                "WHERE cs.customId= :customId " +
                "GROUP BY cs.customStatusId";
        List<CustomDetail> customDetails = jpaApi.em().createQuery(updates, CustomDetail.class)
                .setParameter("customId", customId).getResultList();
        return ok(views.html.CustomsViews.customdetail.render(customUpdate, customDetails));
    }

    @Transactional(readOnly = true)
    public Result getCustomHistory(int customerId)
    {
        String sql = "SELECT NEW models.CustomsModels.CustomUpdate(cu.customId, c.firstName,c.lastName, " +
                "cu.itemDescription, cu.jobStarted, cs.customStatusCode, csd.customStatusName, cs.employeeId, e.userName, " +
                "cs.notes,cu.quote, cu.jobFinished, cs.statusChanged, cu.envelopeNumber) " +
                "FROM Custom cu " +
                "JOIN CustomStatus cs ON cu.customStatusId=cs.customStatusId " +
                "JOIN Customer c ON cu.customerId=c.customerId " +
                "JOIN CustomStatusDetail csd ON cs.customStatusCode=csd.customStatusCode " +
                "JOIN Employee e ON cs.employeeId=e.employeeId " +
                "WHERE cu.customerId= :customerId " +
                "ORDER BY cu.customId";
        List<CustomUpdate> customUpdates = jpaApi.em().createQuery(sql, CustomUpdate.class)
                .setParameter("customerId", customerId).getResultList();
        return ok(views.html.CustomsViews.customhistory.render(customUpdates));
    }

}