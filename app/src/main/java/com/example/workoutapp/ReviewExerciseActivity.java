package com.example.workoutapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ReviewExerciseActivity extends AppCompatActivity {


    private String userID;
    private String date;
    private ArrayList selectedExercise;

    private ArrayList<ArrayList<Integer>> totalWeightList;
    private ArrayList<ArrayList<Integer>> totalTimesList;
    private String exerciseTime;
    private String newOrOld;

    //------------------------------------------------------------------------------------------------------
    private Button btnCompletion;
    private Button btnRoutineStore;
    private EditText edtWeight;
    private EditText edtRoutineName;
    private RatingBar rateExercise;
    private TextView txtExerciseList;
    private TextView txtWeightSum;
    private int sum;


    ArrayList<String> strTimesList;
    ArrayList<String> strWeightList;
    ArrayList<String> strSetList;

    private int volumeSum;
    private float ExerciseRating;

    private boolean isStored = false;
    private int routineStoreCount;

    HomeFragment homeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_exercise);
        routineStoreCount = 0;

        getIntentData();

        initView();

        txtExercise();

        sum = 0;

        for(int i=0; i < totalWeightList.size(); i++){
            for(int j=0; j<totalWeightList.get(i).size(); j++){
                sum += totalWeightList.get(i).get(j) * totalTimesList.get(i).get(j);
            }
        }
        String weight = "오늘 든 무게: " + String.valueOf(sum) + "kg";
        txtWeightSum.setText(weight);


        if(newOrOld.equals("old")){
            edtRoutineName.setVisibility(View.GONE);
            btnRoutineStore.setVisibility(View.GONE);
        }

        disableButton();

        /** 별점 저장 */
        rateExercise.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ExerciseRating = rating;
                Log.d("Rating","Rating" + ExerciseRating);
            }
        });
        // 중량 총합
        ArrayList<Integer> multipliedArray = new ArrayList<>();
        int result = 0;

        for(int i=0; i<totalWeightList.size(); i++){
            for(int j=0; j<totalWeightList.get(i).size(); j++){
                result = totalWeightList.get(i).get(j) * totalTimesList.get(i).get(j);
                multipliedArray.add(result);
            }
        }

        volumeSum = 0;
        for (int num : multipliedArray) {
            volumeSum += num;
        }


        //------------------------------------------------------------------------------------------------------
    }

    /*
    * 루틴 입력란 만들기 convertToStringList
    * */
    View.OnClickListener routineStore = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isStored = true;
            strTimesList = convertToStringList(totalTimesList);
            strWeightList = convertToStringList(totalWeightList);
            strSetList = new ArrayList<>();
            String setlist = "";

            isSaved();
            for(int i=0; i<totalTimesList.size(); i++){
                for(int j=0; j<totalTimesList.get(i).size(); j++){
                    setlist += j+1;
                    setlist += ", ";
                }
                StringBuilder sb = new StringBuilder(setlist);
                if (sb.length() > 0) {
                    sb.setLength(sb.length() - 2); // 마지막 쉼표와 공백 제거
                }
                strSetList.add(sb.toString());
                setlist = "";

            }
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if(success){
                            toastText();
                        }else {
                            Toast.makeText(getApplicationContext(),"루틴 저장에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };

            for(int idx=0; idx<selectedExercise.size(); idx++){
                RoutineIsRequest routineIsRequest = new RoutineIsRequest(edtRoutineName.getText().toString(), userID,
                        String.valueOf(selectedExercise.get(idx)),
                        strSetList.get(idx), strTimesList.get(idx),
                        strWeightList.get(idx), String.valueOf(idx+1), responseListener);
                RequestQueue queue = Volley.newRequestQueue(ReviewExerciseActivity.this);
                queue.add(routineIsRequest);
            }

        }
    };

    /** 공백란 입력 시 버튼 비활성화 */
    private void disableButton(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 텍스트 변경 중 이벤트
                String weightString = edtWeight.getText().toString();
                String routineNameString = edtRoutineName.getText().toString();

                // 버튼 활성화 또는 비활성화
                boolean submitButtonEnable = !weightString.isEmpty();
                btnCompletion.setEnabled(submitButtonEnable);

                boolean saveRoutineButtonEnable = !routineNameString.isEmpty();
                btnRoutineStore.setEnabled(saveRoutineButtonEnable);
            }
            @Override
            public void afterTextChanged(Editable s) {            }
        };

        edtWeight.addTextChangedListener(textWatcher);
        edtRoutineName.addTextChangedListener(textWatcher);
    }

    private void toastText(){
        if(routineStoreCount == 0){
            Toast.makeText(getApplicationContext(),"루틴을 저장 하였습니다.",Toast.LENGTH_SHORT).show();
            routineStoreCount++;
        }
    }

    private void isSaved(){
        if(isStored){
            btnRoutineStore.setEnabled(false);
        }
    }
    private void getIntentData(){

        Intent intent = getIntent();
        userID = getIntent().getStringExtra("userID");
        date = getIntent().getStringExtra("Date");
        exerciseTime = getIntent().getStringExtra("ExerciseTime");
        totalWeightList = (ArrayList) intent.getSerializableExtra("weightList");
        totalTimesList = (ArrayList) intent.getSerializableExtra("timesList");
        selectedExercise = (ArrayList) intent.getSerializableExtra("exerciseList");
        newOrOld = getIntent().getStringExtra("NewOrOld");
    }

    private void initView(){
        btnCompletion = findViewById(R.id.btnCompletion);
        btnCompletion.setOnClickListener(calenderStore);

        btnRoutineStore = findViewById(R.id.btnRoutineStore);
        btnRoutineStore.setOnClickListener(routineStore);

        edtRoutineName = findViewById(R.id.edtRoutineName);

        edtWeight = findViewById(R.id.edtWeight);

        rateExercise = findViewById(R.id.rateExercise);

        txtExerciseList = findViewById(R.id.txtExerciseList);

        txtWeightSum = findViewById(R.id.txtWeightSum);

        btnCompletion.setEnabled(false);
        btnRoutineStore.setEnabled(false);

        limitedKey(edtWeight);
    }

    private void txtExercise(){
        String routine = "";
        for(int i=0; i<selectedExercise.size(); i++){
            routine += selectedExercise.get(i) + "\n\n";
            for(int j=0; j<totalWeightList.get(i).size(); j++){
                routine += String.valueOf(j+1) + "세트 " + totalWeightList.get(i).get(j) + "kg "
                        + totalTimesList.get(i).get(j) + "회\n";
            }
            routine += "\n\n";
        }
        txtExerciseList.setText(routine);
    }


    private void finishActivity(){
        Intent intent = new Intent(ReviewExerciseActivity.this,MainActivity.class);
        intent.putExtra("userID",userID);
        finish();
    }

    View.OnClickListener calenderStore = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String routine = "";
            for(int i=0; i<selectedExercise.size(); i++){
                routine += selectedExercise.get(i) + "\n\n";
                for(int j=0; j<totalWeightList.get(i).size(); j++){
                    routine += String.valueOf(j+1) + "세트 " + totalWeightList.get(i).get(j) + "kg "
                                    + totalTimesList.get(i).get(j) + "회\n";
                }
                routine += "\n\n";
            }

            String finalRoutine = routine;

        Response.Listener<String> volListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");



                    if(success){
                            String calenderVolume = jsonObject.getString("calenderVolume");
                            int resultVolume = volumeSum + Integer.parseInt(calenderVolume);

                        Response.Listener<String> bmiListener = new Response.Listener<String>() {
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");

                                    if(success){
                                        String userHeight = jsonObject.getString("userHeight");

                                        String userWeight = String.valueOf(Double.parseDouble(edtWeight.getText().toString()));
                                        double height = Double.parseDouble(userHeight);
                                        double weight = Double.parseDouble(userWeight);
                                        double bmiuh = height/100;
                                        double result = weight / Math.pow(bmiuh,2);
                                        String bmi = String.format("%.2f", result);

                                        //분석 볼륨

                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    boolean success = jsonObject.getBoolean("success");
                                                    if(success){
                                                        Toast.makeText(getApplicationContext(),"운동 완료 하였습니다.",Toast.LENGTH_SHORT).show();
                                                        finishActivity();
                                                    }else {
                                                        Toast.makeText(getApplicationContext(),"운동 완료에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        };


                                        CalenderIsRequest calenderIsRequest = new CalenderIsRequest(date, userID, "",
                                                exerciseTime, String.valueOf(ExerciseRating), edtWeight.getText().toString(),
                                                String.valueOf(resultVolume), finalRoutine, bmi, responseListener);
                                        RequestQueue queue1 = Volley.newRequestQueue(ReviewExerciseActivity.this);
                                        queue1.add(calenderIsRequest);
                                    }else {

                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        MemberRequest memberRequest = new MemberRequest(userID, bmiListener);
                        RequestQueue queue = Volley.newRequestQueue(ReviewExerciseActivity.this);
                        queue.add(memberRequest);

                        finishActivity();
                    }else {
                        Toast.makeText(getApplicationContext(),"운동 완료에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    Response.Listener<String> bmiListener = new Response.Listener<String>() {
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if(success){
                                    String userHeight = jsonObject.getString("userHeight");

                                    String userWeight = String.valueOf(Double.parseDouble(edtWeight.getText().toString()));
                                    double height = Double.parseDouble(userHeight);
                                    double weight = Double.parseDouble(userWeight);
                                    double bmiuh = height/100;
                                    double result = weight / Math.pow(bmiuh,2);
                                    String bmi = String.format("%.2f", result);
                                   //int resultVolume = volumeSum + Integer.parseInt(userVolume);


                                    //분석 볼륨

                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");
                                                if(success){
                                                    Toast.makeText(getApplicationContext(),"운동 완료 하였습니다.",Toast.LENGTH_SHORT).show();
                                                    finishActivity();
                                                }else {
                                                    Toast.makeText(getApplicationContext(),"운동 완료에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    };


                                    CalenderIsRequest calenderIsRequest = new CalenderIsRequest(date, userID, "",
                                            exerciseTime, String.valueOf(ExerciseRating), edtWeight.getText().toString(),
                                            String.valueOf(volumeSum), finalRoutine, bmi, responseListener);
                                    RequestQueue queue1 = Volley.newRequestQueue(ReviewExerciseActivity.this);
                                    queue1.add(calenderIsRequest);
                                }else {

                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    MemberRequest memberRequest = new MemberRequest(userID, bmiListener);
                    RequestQueue queue = Volley.newRequestQueue(ReviewExerciseActivity.this);
                    queue.add(memberRequest);
                    e.printStackTrace();
                }

            }
        };
        CalendersRequest calendersRequest = new CalendersRequest(date,userID,volListener);
        RequestQueue vqueue = Volley.newRequestQueue(ReviewExerciseActivity.this);
        vqueue.add(calendersRequest);



        }
    };

    /** 숫자만 입력가능하게 키보드 제한 */
    private void limitedKey(EditText editText){
        editText.setKeyListener(new DigitsKeyListener(false,true){
            @Override
            public int getInputType(){
                return InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL;
            }
            @Override
            protected char[] getAcceptedChars(){
                return new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9','.'};
            }
        });
    }

    public static ArrayList<String> convertToStringList(ArrayList<ArrayList<Integer>> twoDimensionalArrayList) {
        ArrayList<String> convertedList = new ArrayList<>();

        for (ArrayList<Integer> row : twoDimensionalArrayList) {
            StringBuilder sb = new StringBuilder();

            for (int value : row) {
                sb.append(value).append(", ");
            }

            if (sb.length() > 0) {
                sb.setLength(sb.length() - 2); // 마지막 쉼표와 공백 제거
            }

            convertedList.add(sb.toString());
        }

        return convertedList;
    }


    /** 뒤로가기 버튼 기능 구현 */
    private long backKeyPressedTime = 0;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReviewExerciseActivity.this,MainActivity.class);
        intent.putExtra("userID",userID);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit);
        backKeyPressedTime = System.currentTimeMillis();
        return;
    }
}

