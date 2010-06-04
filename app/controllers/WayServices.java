package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Referential;
import models.Way;
import controllers.admin.Admin;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.i18n.Messages;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import play.mvc.results.NotFound;

@With(Secure.class)
public class WayServices extends Controller {

    private static void notFoundJSONIfNull(Object object, String message) {
        if(object==null) {
            response.status = 404;
            renderJSON("{message:\"" + Messages.get(message) + "\"}");
        }
    }

    private static void validationJSON() {
        if(Validation.hasErrors()) {
            response.status = 400;
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("message", Messages.get("validErrors"));
            jsonMap.put("errors", Validation.errors());
            renderJSON(jsonMap);
        }
    }

    @Before
    public static void referentialLoad() {
        String referentialCode = params.get("referential.code");
        notFoundJSONIfNull(referentialCode, "referential.notFound");
        Referential referential = Referential.findByCode(referentialCode);
        notFoundJSONIfNull(referential, "referential.notFound");
    }

    private static Referential getReferential() {
        return (Referential) renderArgs.get("referential");
    }

    public static void create(String referentialCode, @Valid Way way) {
        validationJSON();
        way.referential = getReferential();
        way.save();
        renderJSON(way.toJson());
    }

    public static void read(String referentialCode, Long wayId) {
        Way way = Way.findById(wayId);
        notFoundJSONIfNull(way, "way.notFound");
        renderJSON(way.toJson());
    }

    public static void update(String referentialCode, Long wayId, @Valid Way way) {
        Way dbWay = Way.findById(wayId);
        notFoundJSONIfNull(dbWay, "way.notFound");
        Validation.required("way.referential", way);
        dbWay.edit("way", params);
        if(dbWay.referential == null || !dbWay.referential.equals(getReferential())) {
           Validation.addError("way.referential", "way.wrongReferential");
        }
        validationJSON();
        dbWay.save();
    }

    public static void delete(String referentialCode, Long wayId) {
        Way way = Way.findById(wayId);
        notFoundJSONIfNull(way, "way.notFound");
        way.delete();
    }

}
