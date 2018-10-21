package homemade.yandextranslate16;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LanguageSelectorActivity extends AppCompatActivity {

    public static ArrayAdapter<String> arrayAdapter;
    public static List<String> translateVariants;

    protected void onClickLanguageSelectorListItem(AdapterView<?> parent, View view, int position, long id) {
        List<String> langs = new ArrayList<String>(Arrays.asList(translateVariants.get(position).split("\\-", 2)));
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("langs", (ArrayList<String>) langs);
        bundle.putStringArrayList("translateVariants", (ArrayList<String>) translateVariants);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selector);
        getTranslateVariantsFromInstance();
        if (!translateVariants.isEmpty()) {
            ListView listView = findViewById(R.id.language_selector_list);
            bindListView(listView);
        }
    }

    protected void getTranslateVariantsFromInstance() {
        if (getIntent() != null) {
            if (getIntent().hasExtra("translateVariants")) {
                translateVariants = getIntent().getStringArrayListExtra("translateVariants");
            } else {
                translateVariants = null;
            }
        } else {
            translateVariants = null;
        }
    }

    protected void bindListView(ListView listView) {
            createAndbindAdapterToListView(listView);
            setOnItemClickListenerToListView(listView);
    }

    protected void createAndbindAdapterToListView(ListView listView) {
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                translateVariants
        );
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    protected void setOnItemClickListenerToListView(ListView listView) {
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        onClickLanguageSelectorListItem(parent, view, position, id);
                    }
                }
        );
    }

}
