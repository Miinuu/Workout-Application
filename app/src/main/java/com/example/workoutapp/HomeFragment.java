package com.example.workoutapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private TextView txtToday;
    private Button btnExercise;
    private int selectedyear;
    private int selectedmonth;
    private int seletedday;
    private LocalDate dateFor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtToday = view.findViewById(R.id.txtToday);
        btnExercise = view.findViewById(R.id.btnExerciseToday);
        // txtToday 꾸미는 함수
        setTxtToday();

        btnExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalDate today = LocalDate.now();
                Intent intent = new Intent(getActivity(),CreatePlanActivity.class);
                intent.putExtra("Date",today.toString());
                startActivity(intent);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_right_exit);

            }
        });
        return view;
    }

    /** txtToday에 오늘 날짜 보이게 하고, 요일만 다른 색깔로 보이게 함 */
    private void setTxtToday(){
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd.");
        String dayOfWeekInKorean = now.getDayOfWeek().getDisplayName(
                TextStyle.FULL,
                Locale.KOREAN
        );
        String todayText = now.format(formatter) + " " + dayOfWeekInKorean ;

        SpannableString spannableString = new SpannableString(todayText);
        int spanStartIndex = todayText.lastIndexOf(dayOfWeekInKorean);
        int spanEndIndex =  spanStartIndex + dayOfWeekInKorean.length();
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(getActivity(),R.color.blue));
        spannableString.setSpan(foregroundColorSpan, spanStartIndex, spanEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtToday.setText(spannableString);
    }
}