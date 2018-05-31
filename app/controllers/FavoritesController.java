package controllers;

import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;

public class FavoritesController extends Controller
{
    FormFactory formFactory;

    @Inject
    public FavoritesController(FormFactory formFactory)
    {
        this.formFactory=formFactory;
    }
    public Result getStart()
    {
        return ok(views.html.favorites.render());
    }
    public Result postFavorites()
    {
        DynamicForm form= formFactory.form().bindFromRequest();
        String food= form.get("food");
        String movie=form.get("movie");
        String language=form.get("language");

        session().put("favoriteFood",food);
        session().put("favoriteMovie",movie);
        session().put("favoriteLanguage",language);
        return ok(views.html.findFavorites.render());
    }
    public Result getFood()
    {
        String food= session().get("favoriteFood");
        return ok(views.html.favoriteFood.render(food));
    }
    public Result getMovie()
    {
        String movie= session().get("favoriteMovie");
        return ok(views.html.favoriteMovie.render(movie));
    }
    public Result getLanguage()
    {
        String language= session().get("favoriteLanguage");
        return ok(views.html.favoriteLanguage.render(language));
    }
}
