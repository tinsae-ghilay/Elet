package com.tgk.Elet;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.tgk.Elet.databinding.FragmentBahreHasabBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class BahreHasabFragment extends Fragment {

    private FragmentBahreHasabBinding HasabBinding;
    listAdapter adapter;
    ArrayList<String> baalat;
    ArrayList<String> dates;
    ListView listView;
    TextInputEditText text;
    String[] days;
    int gy;
    InputMethodManager imm;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        HasabBinding = FragmentBahreHasabBinding.inflate(inflater, container, false);
        return HasabBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GeezDate today=GeezDate.now();
        gy=today.getYear();
        //--------------------------------------
        days = getResources().getStringArray(R.array.holy_days);
        listView= HasabBinding.list;
        baalat=new ArrayList<>(Arrays.asList(days));
        BahreHasab hasab=new BahreHasab(gy);
        dates=hasab.getArrayOfHolidays(requireContext());
        adapter=new listAdapter(getContext(),baalat,dates,false);
        listView.setAdapter(adapter);
        //************************************************************
        text= HasabBinding.yearText;
        text.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId==5){
                calculateBahreHasab();
                return true;
            }
            return false;
        });
        MaterialButton calculate= HasabBinding.calculate;
        calculate.setOnClickListener(v ->
            calculateBahreHasab()
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        HasabBinding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void calculateBahreHasab()  {
        String input= Objects.requireNonNull(text.getText()).toString().trim();
        if (!TextUtils.isEmpty(input)) {
            int year;
            try {
                year=Integer.parseInt(String.valueOf(text.getText()));
            }catch (Exception exception){
                Log.d("Bahre hasab calculation", text.getText().toString()+" received"+exception.getMessage());
                year=GeezDate.now().getYear();
                Toast.makeText(requireContext(),"Geez year must only contain numbers",Toast.LENGTH_LONG).show();
            }
            BahreHasab h=new BahreHasab(year);
            dates=h.getArrayOfHolidays(requireContext());
            adapter=new listAdapter(getContext(),baalat,dates,false);
            listView.setAdapter(adapter);
            try {
                imm = (InputMethodManager) requireContext().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
            } catch (Exception e) {
                Log.d("Trying to hide keyboard",e.getMessage());
                //no need to catch.
            }
        }
    }
}