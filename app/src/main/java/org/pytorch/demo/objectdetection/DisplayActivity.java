package org.pytorch.demo.objectdetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import org.pytorch.demo.objectdetection.data.DatabaseHandler;
import org.pytorch.demo.objectdetection.models.Building_Info;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DisplayActivity extends AppCompatActivity {

    ImageView capimg,mapimg;
    ImageButton arrow,arrow1,arrow2,arrowd;
    LinearLayout hiddenView,hiddenView2;
    LinearLayout hiddenView1,hiddenViewd;
    CardView cardView,cardView1,cardView2,cardViewd;
    ArrayList<ListData> modelArrayList;

    public String type="";
    TextView title,estabinfo,floorinfo,history,loctext;
    DatabaseHandler db;
    RecyclerView rv,rv1,rv2;
    String blockname="";
    MyListAdapter ml;
    TextToSpeech textToSpeech;
    ImageButton cb;
    ImageButton la,ra,ua,da,relocate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Intent intent = getIntent();
        blockname=intent.getStringExtra("blockname");
        title=findViewById(R.id.title);
        title.setText(blockname);
        cb=findViewById(R.id.camerabtn);
        estabinfo=findViewById(R.id.estab_info);
        floorinfo=findViewById(R.id.floor_info);
//        ra=findViewById(R.id.rightarrow);
//        la=findViewById(R.id.leftarrow);
//        ua=findViewById(R.id.uparrow);
//        da=findViewById(R.id.downarrow);
//        loctext=findViewById(R.id.loctext);
//        relocate=findViewById(R.id.relocate);
        Pronunciation p= new Pronunciation(getApplicationContext());

        db= new DatabaseHandler(this);
        rv = findViewById(R.id.officedatarv);
        rv1=findViewById(R.id.officedatarv1);
        rv2=findViewById(R.id.officedatarv2);

        rv.setHasFixedSize(false);

        modelArrayList = new ArrayList<ListData>();

        cardView = findViewById(R.id.base_cardview);
        arrow = findViewById(R.id.arrow_button);
        hiddenView = findViewById(R.id.hidden_view);
        cardView1 = findViewById(R.id.base_cardview1);
        arrow1 = findViewById(R.id.arrow_button1);
        hiddenView1 = findViewById(R.id.hidden_view1);
        cardView2 = findViewById(R.id.base_cardview2);
        arrow2 = findViewById(R.id.arrow_button2);
        hiddenView2 = findViewById(R.id.hidden_view2);
        cardViewd = findViewById(R.id.desc_cardview);
        arrowd = findViewById(R.id.arrow_button_d);
        hiddenViewd = findViewById(R.id.hidden_view_d);
        history=findViewById(R.id.history);
      //  mapimg=findViewById(R.id.mapimg);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Your code to run after a delay. in my case in another handler
                p.speak("You are in front of "+blockname+". To know more about the block, please refer to the details below.", TextToSpeech.QUEUE_FLUSH);
            }
        }, 3000);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        relocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso
                        .get()
                        .load("https://firebasestorage.googleapis.com/v0/b/mobile-app-da42b.appspot.com/o/block2.png?alt=media&token=812f4a39-b71f-4ea7-a37d-86ae9d9c857c")
                        .into(mapimg);

                ra.setVisibility(View.VISIBLE);
                la.setVisibility(View.VISIBLE);
                da.setVisibility(View.VISIBLE);
                ua.setVisibility(View.VISIBLE);
            }

        });



//        ra.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                ra.setAlpha((float)1.0);
//                return false;
//            }
//        });

//        ra.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Picasso
//                        .get()
//                        .load("https://firebasestorage.googleapis.com/v0/b/mobile-app-da42b.appspot.com/o/christ_football_ground.jpg?alt=media&token=9f9e3498-62c1-4bd4-9943-222f3492d102")
//                        .into(mapimg);
//
//                loctext.setText("Football Ground");
//                loctext.setVisibility(View.VISIBLE);
//                ra.setVisibility(View.GONE);
//                la.setVisibility(View.VISIBLE);
//                p.speak("To your right is the football ground", TextToSpeech.QUEUE_FLUSH);
//            }
//        });
//
//        la.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Picasso
//                        .get()
//                        .load("https://firebasestorage.googleapis.com/v0/b/mobile-app-da42b.appspot.com/o/kiosk.jpeg?alt=media&token=cb1da068-7556-414c-834d-40885503de6b")
//                        .into(mapimg);
//
//                loctext.setText("Kiosk");
//                loctext.setVisibility(View.VISIBLE);
//                la.setVisibility(View.GONE);
//                ra.setVisibility(View.VISIBLE);
//                p.speak("To your left is the kiosk, which serves a variety of refreshments", TextToSpeech.QUEUE_FLUSH);
//
//            }
//        });

        //dropdown code

        arrowd.setOnClickListener(view -> {
            // If the CardView is already expanded, set its visibility
            // to gone and change the expand less icon to expand more.
            //speakData();
            if (hiddenViewd.getVisibility() == View.VISIBLE) {
                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition
                TransitionManager.beginDelayedTransition(cardViewd, new AutoTransition());
                hiddenViewd.setVisibility(View.GONE);
                arrowd.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);

            }

            // If the CardView is not expanded, set its visibility to
            // visible and change the expand more icon to expand less.
            else {
                TransitionManager.beginDelayedTransition(cardViewd, new AutoTransition());
                hiddenViewd.setVisibility(View.VISIBLE);
                hiddenView1.setVisibility(View.GONE);
                hiddenView2.setVisibility(View.GONE);
                arrowd.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
                arrow1.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                arrow2.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);

            }
        });
        arrow.setOnClickListener(view -> {
            // If the CardView is already expanded, set its visibility
            // to gone and change the expand less icon to expand more.
            //speakData();
            if (hiddenView.getVisibility() == View.VISIBLE) {
                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenView.setVisibility(View.GONE);
                arrow.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);

            }

            // If the CardView is not expanded, set its visibility to
            // visible and change the expand more icon to expand less.
            else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenView.setVisibility(View.VISIBLE);
                hiddenView1.setVisibility(View.GONE);
                type="office";
                hiddenView2.setVisibility(View.GONE);
                arrow.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
                arrow1.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                arrow2.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                setData(type);
            }
        });

        arrow1.setOnClickListener(view -> {

            if (hiddenView1.getVisibility() == View.VISIBLE) {
                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition
                TransitionManager.beginDelayedTransition(cardView1, new AutoTransition());
                hiddenView1.setVisibility(View.GONE);
                arrow1.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);

            }

            // If the CardView is not expanded, set its visibility to
            // visible and change the expand more icon to expand less.
            else {
                TransitionManager.beginDelayedTransition(cardView1, new AutoTransition());
                hiddenView1.setVisibility(View.VISIBLE);
                hiddenView.setVisibility(View.GONE);
                hiddenView2.setVisibility(View.GONE);
                arrow1.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
                arrow.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                arrow2.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                type="department";
                setData(type);
            }
        });

        arrow2.setOnClickListener(view -> {

            if (hiddenView2.getVisibility() == View.VISIBLE) {
                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition
                TransitionManager.beginDelayedTransition(cardView2, new AutoTransition());
                hiddenView2.setVisibility(View.GONE);
                arrow2.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);

            }

            // If the CardView is not expanded, set its visibility to
            // visible and change the expand more icon to expand less.
            else {
                TransitionManager.beginDelayedTransition(cardView2, new AutoTransition());
                hiddenView2.setVisibility(View.VISIBLE);
                hiddenView1.setVisibility(View.GONE);
                hiddenView.setVisibility(View.GONE);
                arrow2.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
                arrow1.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                arrow.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                type="others";
                setData(type);
            }
        });

        Bitmap bitmap = BitmapData.getInstance().getBitmap();
        capimg=findViewById(R.id.capimg);
        capimg.setImageBitmap(bitmap);

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DisplayActivity.this,ObjectDetectionActivity.class);
                startActivity(i);
            }
        });

        if(blockname.equals("Central Block"))
        {
            estabinfo.setText("2010");
            history.setText("The Central Block is one of the most important buildings in the main campus of the university. It was inaugurated in the year 2010. This building houses some of the important infrastructure provided by the university such as the Knowledge Centre (PG Library) and Chapel, established in 2011.");
            floorinfo.setText("10");
        }
        else if(blockname.equals("Block 1"))
        {
            estabinfo.setText("1969");
            floorinfo.setText("2");
            history.setText("Block 1 was constructed and inaugurated in the year 1969. This marked a momentous occasion as it was blessed in preparation for the commencement of the first academic year on July 15 at 9:30 AM. The institution proudly initiated its educational journey by introducing one-year Pre-University Courses (PUC) under Bangalore University, setting the stage for a promising and enriching academic experience for the incoming students.");
        }
        else if(blockname.equals("Block 2")){
            estabinfo.setText("1990");
            history.setText("N/A");
            floorinfo.setText("3");


        }


    }

    void setData(String type)
    {

        List<Building_Info> data = db.getAllBuilding_Info(blockname.toLowerCase(),type);
        modelArrayList.clear();
        //Toast.makeText(getApplicationContext(),blockname.toLowerCase()+type,Toast.LENGTH_SHORT).show();
        for (Building_Info cn : data) {

            //   Toast.makeText(getApplicationContext(),cn.getName(),Toast.LENGTH_SHORT).show();
            modelArrayList.add(new ListData(cn.getName(),cn.getRoomNo()));
            ml = new MyListAdapter( modelArrayList);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            if (type.equals("office"))
            {
                rv.setLayoutManager(linearLayoutManager);
                rv.setAdapter(ml);
            }
            else if (type.equals("department")) {
                rv1.setLayoutManager(linearLayoutManager);
                rv1.setAdapter(ml);
            }
            else{
                rv2.setLayoutManager(linearLayoutManager);
                rv2.setAdapter(ml);
            }

        }
    }
//    public  void speakData()
//    {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            textToSpeech.speak(blockname,TextToSpeech.QUEUE_FLUSH,null,null);
//        } else {
//            textToSpeech.speak(blockname, TextToSpeech.QUEUE_FLUSH, null);
//        }
//    }

}