package bot.messenger.assist.assistbot.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sindhya on 12/3/16.
 */
public class BaseAsyncRequest extends AsyncTask<Void, Void, Boolean> {

    protected String serviceName;
    protected String endPoint;
    protected String verb;
    protected String applicationApiKey;

    // sessionToken is needed for every call except for login
    protected String sessionToken;

    // objects not already created (to save memory):
    protected Map<String, String> queryParams = null;
    protected JSONObject requestBody = null; // JSON object for request body

    // optional params:
    // format of request string, default to JSON
    protected String contentType = "application/json";

    // convert request body to string before sending it to API call
    // set the request string if you want to skip creating the JSON object
    protected String requestString = "";

    // tag to use for error messages and logging
    protected String callerName = "BaseAsyncRequest";

    // navi_header params other than API key and session token
    protected Map<String, String> headerParams = new HashMap<>();

    // instance url, for example http://localhost:8080/api/v2
    protected String baseInstanceUrl = ConfigConstant.BASE_URL;

    // set to true to print the request and response strings to verbose
    protected boolean use_logging = true;

    protected FileRequest fileRequest = null;


    @Override
    protected Boolean doInBackground(Void... params) {
        try {

            doSetup();

            String path = "/" + serviceName + "/" + endPoint;


            if (requestBody != null) {
                if (requestString != null && !requestString.isEmpty()) {
                    Log.w(callerName, "supplied both a request body and request string " +
                            "to BaseAsync task, overwriting request string with request body");
                }
                requestString = requestBody.toString();
            }
            if (use_logging) {
                Log.v(callerName, "request string is: " + requestString);
            }

            String response = ApiInvoker.getInstance().invokeAPI(baseInstanceUrl,
                    path,
                    verb,
                    queryParams,
                    requestString,
                    headerParams,
                    contentType,
                    fileRequest);

            if (use_logging) {
                Log.v(callerName, "response string is: " + response);
            }


            processResponse(response);
            
            return true;
        } catch (ApiException e) {
            Log.e(callerName, "going to on_failure() for an ApiException");
            onError(e);
        } catch (JSONException e) {
            Log.e(callerName, "going to onError() for a JSONException: " + e.toString());
            onError(e);
        } catch (Exception e) {
            Log.e(callerName, "going to on_failure() for an unexpected exception type");
            onError(e);
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (use_logging) {
            Log.v(callerName, "finished call");
        }
        onCompletion(result);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        onCompletion(false);
    }

    protected void doSetup() throws ApiException, JSONException {
    }


    protected void processResponse(String response) throws ApiException, JSONException {
    }

    protected void onCompletion(boolean success) {
    }


    protected void onError(Exception e) {
        Log.e(callerName + "::baserequest::onError", e.toString());
        if (e.getMessage().contains("{")) {
            String json = e.getMessage().substring(e.getMessage().indexOf('{'));
            JSONObject jsonObject = null;
            try {
                jsonObject = (new JSONObject(json)).getJSONObject("error");
                if (jsonObject != null) {

                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }
}
