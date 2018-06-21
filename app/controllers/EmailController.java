package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import services.Email;

public class EmailController extends Controller
{
    public Result getSendEmail()
    {
        return ok(views.html.sendemail.render());
    }

    public Result postSendEmail()
    {
        Email.sendEmail("This is a test!", "lmhogfan@gmail.com");
        return ok("Tried to send email");
    }
}
