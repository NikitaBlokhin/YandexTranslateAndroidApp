package homemade.yandextranslate16;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public static List<String> translateVariants;
    public static String orignalLang;
    public static String necessayLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().hasExtra("translateVariants")) {
            translateVariants = getIntent().getStringArrayListExtra("translateVariants");
        } else {
            translateVariants = loadTranslateVariants();
        }
        if (getIntent() != null) {
            if (getIntent().hasExtra("langs")) {
                List<String> langs = getIntent().getStringArrayListExtra("langs");
                orignalLang = langs.get(0);
                necessayLang = langs.get(1);
                showChosenLanguages();
            } else {
                showSelectLanguageMessage();
            }
        } else {
            showSelectLanguageMessage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void showSelectLanguageMessage() {
        TextView outputArea = findViewById(R.id.output_area);
        outputArea.setText(R.string.select_language_msg);
    }

    protected void showChosenLanguages() {
        TextView outputArea = findViewById(R.id.output_area);
        outputArea.setText(String.format("%s-%s", orignalLang, necessayLang));
    }

    protected String getOriginalLang() {
        return orignalLang;
    }

    protected String getNecessaryLang() {
        return necessayLang;
    }

    public void onClickTranslateButton(View view) {
        TextView inputArea = findViewById(R.id.input_area);
        TextView outputArea = findViewById(R.id.output_area);
        String originalLang = getOriginalLang();
        String necessaryLang = getNecessaryLang();
        String input = inputArea.getText().toString();
        String outputResult;
        if (input.isEmpty()) {
            outputResult = getString(R.string.empty_input_msg);
        } else {
            outputResult = translate(getString(R.string.api_key), input, originalLang,  necessaryLang);
            if (outputResult.isEmpty()) {
                outputResult = getString(R.string.error_msg);
            }
        }
        outputArea.setText(outputResult);
    }

    public void onClickLanguageSelectorButton(MenuItem item) {
        Intent intent = new Intent(this, LanguageSelectorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("translateVariants", (ArrayList<String>) translateVariants);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected String translate(String apiKey, String text, String originalLang, String necessaryLang) {
        String answer;
        try {
            String encText = URLEncoder.encode(text, "UTF-8");
            AsyncTask translateTask = new TranslateTask();
            String[] args = new String[]{apiKey, encText, originalLang, necessaryLang};
            translateTask.execute(args);
            answer = (String) translateTask.get();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            answer = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            answer = null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            answer = null;
        }
        return answer;
    }

    protected List<String> loadTranslateVariants() {
        List<String> translateVariants;
        try {
            GetLangsTask getLangsTask = new GetLangsTask();
            String[] args = new String[]{getString(R.string.api_key)};
            getLangsTask.execute(args);
            translateVariants = getLangsTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            translateVariants = null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            translateVariants = null;
        }
        return translateVariants;
    }

}
