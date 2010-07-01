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
public class WayServices extends Services {

    @Before
    public static void referentialLoad() {
        String referentialCode = params.get("referentialCode");
        notFoundJSONIfNull(referentialCode, "referential.notFound");
        Referential referential = Referential.findByCode(referentialCode);
        renderArgs.put("referential", referential);
        notFoundJSONIfNull(referential, "referential.notFound");
    }

    private static Referential getReferential() {
        return (Referential) renderArgs.get("referential");
    }

    public static void create(String referentialCode, @Valid Way way) {
        writer();
        validationJSON();
        way.referential = getReferential();
        way.save();
        renderJSON(way.toJson());
    }

    public static void read(String referentialCode, Long wayId) {
        Way way = Way.find("referential = ? AND id = ?", getReferential(), wayId).first();
        notFoundJSONIfNull(way, "way.notFound");
        renderJSON(way.toJson());
    }

    public static void update(String referentialCode, Long wayId, @Valid Way way) {
        writer();
        Way dbWay = Way.findById(wayId);
        notFoundJSONIfNull(dbWay, "way.notFound");
        way.referential = getReferential();
        dbWay.edit("way", params);
        validationJSON();
        dbWay.save();
    }

    public static void delete(String referentialCode, Long wayId) {
        writer();
        Way way = Way.findById(wayId);
        notFoundJSONIfNull(way, "way.notFound");
        way.delete();
    }

}
