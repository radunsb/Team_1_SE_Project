package classes;

import java.io.FileNotFoundException;
import java.text.ParseException;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

public class AppMain {

    public static void main(String[] args) {
        //Read the CSV
        try{
            Main.readCSV();
        } catch(FileNotFoundException | ParseException e){
            System.out.println(e.getMessage());
        }

        //Set up Javalin api on port 7979
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
        }).start(7979);

        //TODO:add api endpoints to actually get data and interact with the backend

        // Search api path to search by name/code -> returns results list in json
        app.get("/search", SearchController::emptySearch);
        app.get("/search/{query}", SearchController::getResults);
        //app.get("/search/{query}/{filters}", SearchController::getResultsWithFilters);
        //TODO: add filter endpoints



    }
}
