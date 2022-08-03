package com.penkra.gcc.Helpers;

import org.json.JSONException;

public interface Response {
    void getResponse(String response, int identifier) throws JSONException;
    void getError(String error, String message, int identifier);
}
