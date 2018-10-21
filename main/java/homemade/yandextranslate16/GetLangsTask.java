package homemade.yandextranslate16;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GetLangsTask extends AsyncTask<String, Void, List<String>> {

    protected String buildQuery(String apiKey) {
        String addr = String.format("https://translate.yandex.net/api/v1.5/tr.json/getLangs?key=%s", apiKey);
        return addr;
    }

    protected String getRawAnswer(URL url, String charsetName) {
        StringBuilder builder = new StringBuilder();
        String rawAnswer;
        try {
            Scanner reader = new Scanner(url.openStream(), charsetName);
            while (reader.hasNext()) {
                builder.append(reader.nextLine());
            }
            reader.close();
            rawAnswer = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            rawAnswer = null;
        }
        return rawAnswer;
    }

    protected List<String> getTranslateVariantsListFromJSON(JSONObject jsonObject) {
        List<String> translateVariants = new ArrayList<String>(){};
        try {
            JSONArray variants = jsonObject.getJSONArray("dirs");
            int i, n;
            n = variants.length();
            for (i = 0; i < n; i++) {
                translateVariants.add(variants.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            translateVariants = null;
        }
        return translateVariants;
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        List<String> translateVariants;
        try {
            String apiKey = strings[0];
            String addr = buildQuery(apiKey);
            String rawAnser = getRawAnswer(new URL(addr), "UTF-8");
            translateVariants = getTranslateVariantsListFromJSON(new JSONObject(rawAnser));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            translateVariants = null;
        } catch (JSONException e) {
            e.printStackTrace();
            translateVariants = null;
        }
        return translateVariants;
    }
}
