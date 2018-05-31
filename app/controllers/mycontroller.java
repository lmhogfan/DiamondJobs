package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class mycontroller extends Controller
{
    public Result getHelloWorld()
    {
        return ok(views.html.HelloWorld.render("Luke"));
    }
}
