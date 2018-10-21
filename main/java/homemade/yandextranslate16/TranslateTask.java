package homemade.yandextranslate16;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class TranslateTask extends AsyncTask <String, Void, String> {

    protected String buildQuery(String... strings) {
        String apiKey = strings[0];
        String text = strings[1];
        String originalLang = strings[2];
        String necessaryLang = strings[3];
        String addr = String.format("https://translate.yandex.net/api/v1.5/tr.json/translate?key=%s&text=%s&lang=%s-%s&format=plain", apiKey, text, originalLang, necessaryLang);
        return addr;
    }

    protected String readRawAnswer(URL url, String charsetName) {
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

    protected String getTranslateFromJSON(JSONObject jsonObject) {
        String result;
        try {
            JSONArray lines = jsonObject.getJSONArray("text");
            StringBuilder builder = new StringBuilder();
            int i, n;
            n = lines.length();
            for (i = 0; i < n; i++) {
                builder.append(lines.getString(i));
            }
            result = builder.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    @Override
    protected String doInBackground(String... strings) {
        String answer;
        try {
            String addr = buildQuery(strings);
            String rawAnswer = readRawAnswer(new URL(addr),"UTF-8");
            JSONObject jsonAnser = new JSONObject(rawAnswer);
            answer = getTranslateFromJSON(jsonAnser);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            answer = null;
        } catch (JSONException e) {
            e.printStackTrace();
            answer = null;
        }
        return answer;
    }
}
