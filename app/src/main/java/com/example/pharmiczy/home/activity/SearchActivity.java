package com.example.pharmiczy.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pharmiczy.DataModels.Medicine;
import com.example.pharmiczy.R;
import com.example.pharmiczy.cache.MedicineCache;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText searchEditText;
    private ListView suggestionsListView;
    private List<Medicine> medicineList = new ArrayList<>(); // Full list of cached medicines
    private List<Medicine> filteredMedicines = new ArrayList<>(); // List of filtered medicines
    private ArrayAdapter<String> suggestionsAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        context = this;
        searchEditText = findViewById(R.id.search_edit_text);
        suggestionsListView = findViewById(R.id.suggestions_list_view);

        // Load cached medicines
        List<Medicine> cached = MedicineCache.getCachedMedicines(this);
        if (cached != null) {
            medicineList.addAll(cached);
        } else {
            Toast.makeText(this, "No medicines cached.", Toast.LENGTH_SHORT).show();
        }

        suggestionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        suggestionsListView.setAdapter(suggestionsAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                showSuggestions(s.toString());
            }
            @Override public void afterTextChanged(Editable s) { }
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN)) {
                    if (!filteredMedicines.isEmpty()) {
                        openMedicineDetails(filteredMedicines.get(0)); // Open first matching
                    }
                    return true;
                }
                return false;
            }
        });

        suggestionsListView.setOnItemClickListener((adapterView, view, position, id) -> {
            if (position >= 0 && position < filteredMedicines.size()) {
                openMedicineDetails(filteredMedicines.get(position));
            }
        });
    }

    private void showSuggestions(String query) {
        List<String> filteredNames = new ArrayList<>();
        filteredMedicines.clear();

        for (Medicine medicine : medicineList) {
            if (medicine.getProductName() != null && medicine.getProductName().toLowerCase().contains(query.toLowerCase())) {
                filteredMedicines.add(medicine);
                filteredNames.add(medicine.getProductName());
            }
        }

        suggestionsAdapter.clear();
        suggestionsAdapter.addAll(filteredNames);
        suggestionsAdapter.notifyDataSetChanged();
    }

    private void openMedicineDetails(Medicine selectedMedicine) {
        ProductDetailActivity.start(context, selectedMedicine);
    }
}
