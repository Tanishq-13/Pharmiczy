package com.example.pharmiczy.order_placing;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.pharmiczy.DataModels.AddressesModel;
import com.example.pharmiczy.R;
import com.example.pharmiczy.order_placing.adapter.AddressesAdapter;

import java.util.ArrayList;
import java.util.List;

public class Address extends AppCompatActivity {

    private RecyclerView myaddressesRecyclerView;
    private static AddressesAdapter addressesAdapter;
    private Button deliverHereBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myaddressesRecyclerView = findViewById(R.id.addresses_recyclerview);
        deliverHereBtn = findViewById(R.id.deliver_here_btn);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myaddressesRecyclerView.setLayoutManager(linearLayoutManager);
        List<AddressesModel> addressesModelList = new ArrayList<>();
        addressesModelList.add(new AddressesModel("Parth Maniar","Ahmedabad","380001",true));
        addressesModelList.add(new AddressesModel("Rudresh Mehta","Ahmedabad","380001",false));
        addressesModelList.add(new AddressesModel("Rahul Mehta","Ahmedabad","380001",false));
        addressesModelList.add(new AddressesModel("Parth Maniar","Ahmedabad","380001",false));
        addressesModelList.add(new AddressesModel("Rudresh Mehta","Ahmedabad","380001",false));
        addressesModelList.add(new AddressesModel("Rahul Mehta","Ahmedabad","380001",false));
        addressesModelList.add(new AddressesModel("Parth Maniar","Ahmedabad","380001",false));
        addressesModelList.add(new AddressesModel("Rudresh Mehta","Ahmedabad","380001",false));
        addressesModelList.add(new AddressesModel("Rahul Mehta","Ahmedabad","380001",false));

        int mode = getIntent().getIntExtra("MODE",-1);
        if(true){
            deliverHereBtn.setVisibility(View.VISIBLE);
        }else{
            deliverHereBtn.setVisibility(View.GONE);
        }
        addressesAdapter = new AddressesAdapter(addressesModelList,mode);
        myaddressesRecyclerView.setAdapter(addressesAdapter);
        ((SimpleItemAnimator)myaddressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        addressesAdapter.notifyDataSetChanged();
    }

    public static  void refreshItem(int deSelect, int select){
        addressesAdapter.notifyItemChanged(deSelect);
        addressesAdapter.notifyItemChanged(select);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
