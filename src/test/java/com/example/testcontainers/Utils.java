package com.example.testcontainers;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
    @NotNull
    static JSONObject toJsonObject(Object object) throws JSONException {
        return new JSONObject(new Gson().toJson(object));
    }
}
