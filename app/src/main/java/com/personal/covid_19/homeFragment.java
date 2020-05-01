package com.personal.covid_19;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.LinkedHashMap;
import java.util.List;


import co.blankkeys.animatedlinegraphview.AnimatedLineGraphView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ir.farshid_roohi.linegraph.ChartEntity;


import com.github.mikephil.charting.charts.BarChart;


import com.github.mikephil.charting.components.Legend;
import ir.farshid_roohi.linegraph.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.personal.covid_19.model.dailyrjstatus;
import com.personal.covid_19.model.india_Data;
import  com.personal.covid_19.utils.*;
import com.ramijemli.percentagechartview.PercentageChartView;




/**
 * A simple {@link Fragment} subclass.
 */
public class homeFragment extends Fragment {
    AnimatedLineGraphView affectedGraphView,activegraphview,graphfcon,graphscon,graphtcon,deadgraph,recovgraph;
    LineChart chart;
    TextView affected,active,fconame,factive,sconame,sactive,tconame,tactive,recovered,dead;

    Float affected1,recovered1,dead1,active1;
    PercentageChartView percentageChartView;
//    JSONObject jsonValuecases,jsonValuedeath
//            ,jsonValuerecover= null;
    TextView readmore;


//    List<String> casesdata = new ArrayList<>();
//    List<String> deathdata = new ArrayList<>();
//    List<String> recoverdata = new ArrayList<>();
//    String cars[];
//    List<String> topthreecon=new ArrayList<>();
//    List<countrydailydata> countrydatadaily1=null;
//    List<countrydailydata> countrydatadaily2=null;
//    List<countrydailydata> countrydatadaily3=null;

//    ObjectMapper objectMapper = new ObjectMapper();
    HashMap<Integer, String> statecases=new HashMap<>();
    LinkedHashMap<Integer, String> sortedCASES = new LinkedHashMap<>();
    List<String> top3states=new ArrayList<>();
    List<Integer> top3statecases=new ArrayList<>();
    ImageView indian;
//    Boolean indianbutton=false;
    TextView top3tview;

    boolean checkin=false;
    ArrayList<BarEntry> data;
    Legend legend;






    private static String TAG="Check";


    public homeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

     ViewGroup   root=(ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);
        deadgraph=root.findViewById(R.id.deadrgraph);
        recovgraph=root.findViewById(R.id.recovergraph);
        dead=root.findViewById(R.id.dead);
        recovered=root.findViewById(R.id.recover);

        affectedGraphView=root.findViewById(R.id.affectedgraph);
        activegraphview=root.findViewById(R.id.activegraph);

        affected=root.findViewById(R.id.textaffected);
        active=root.findViewById(R.id.textactive);
        chart=root.findViewById(R.id.lineChart);
        float graph1[]=  new float[]{0, 0, 0, 0, 0, 0, 0};
        String array[]=new String[]{"S", "S", "S", "S", "S", "S", "S"};

        ChartEntity first= new ChartEntity(Color.YELLOW,graph1);
        ArrayList<ChartEntity> list=new ArrayList<>();
        list.add(first);
        chart.setLegend(Arrays.asList(array));
        chart.setList(list);














//        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("mode", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//
//        indian.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(pref.getBoolean("india",false))
//                {   editor.putBoolean("india",false);
//                    editor.commit();
//                    Log.d(TAG, "onClick: "+pref.getBoolean("india",false));
//
//                }
//                else if(!pref.getBoolean("india",false)){
//                    editor.putBoolean("india",true);
//                    editor.commit();
//
//                }
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.detach(homeFragment.this).attach(homeFragment.this).commit();
//            }
//        });


//    if(!pref.getBoolean("india",false)) {
//        top3tview.setText("Top 3 Country");
//        indian.setImageResource(R.drawable.flagind);
//        globalcases();
//    }


        indiancases();




        // Inflate the layout for this fragment
        return root;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private void rajasthandat(){
        utils apiinterfaceindia=RetrofitClient.getindiaretrofit(getContext()).create(utils.class);
        apiinterfaceindia.rajasthandata().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<dailyrjstatus>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(dailyrjstatus dailyrjstatus) {
                        Log.d(TAG, "onNext: "+dailyrjstatus.getStates_daily().get(5).getStatus().toString());
                        makerjgraphs(dailyrjstatus);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void makerjgraphs(dailyrjstatus dailyrjstatus) {

        int size=dailyrjstatus.getStates_daily().size();
        Log.d(TAG, "makerjgraphs: "+size);


        float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getRj()),
                affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getRj()),
                affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getRj()),
                affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getRj()),
                affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getRj()),
                affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getRj())

        };


        affectedGraphView.setData(afectedcasep);
        float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getRj()),
                dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getRj()),
                dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getRj()),
                dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getRj()),
                dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getRj()),
                dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getRj())

        };


        deadgraph.setData(deceased);
        float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getRj()),
                recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getRj()),
                recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getRj()),
                recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getRj()),
                recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getRj()),
                recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getRj())

        };
      recovgraph.setData(null);// for example
       recovgraph.setData(recov);
        float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getRj()),
                active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getRj()),
                active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getRj()),

                active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getRj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getRj()),




        };

        activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getRj())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getRj())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getRj())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getRj())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getRj())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getRj())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
        float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getRj()),
                Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getRj()),
                Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getRj()),
                Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getRj()),
                Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getRj()),
                Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getRj()),
                Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getRj())
               };
        String array[]=new String[]{dailyrjstatus.getStates_daily().get(size-3).getDate(),
                dailyrjstatus.getStates_daily().get(size-6).getDate(),
                dailyrjstatus.getStates_daily().get(size-9).getDate(),
                dailyrjstatus.getStates_daily().get(size-12).getDate(),
                dailyrjstatus.getStates_daily().get(size-15).getDate(),
                dailyrjstatus.getStates_daily().get(size-18).getDate(),
                dailyrjstatus.getStates_daily().get(size-21).getDate()};
        ChartEntity first= new ChartEntity(Color.WHITE,graph1);
        ArrayList<ChartEntity> list=new ArrayList<>();
        list.add(first);
        chart.setLegend(Arrays.asList(array));
        chart.setList(list);




    }

    private void indiancases() {
        utils apiinterfaceindia=RetrofitClient.getindiaretrofit(getContext()).create(utils.class);
        apiinterfaceindia.allindiadata().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<india_Data>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onNext(india_Data india_data) {
                        DecimalFormat precision = new DecimalFormat("0.0");

                        affected.setText(india_data.getStatewise().get(5).getConfirmed());
                        active.setText(india_data.getStatewise().get(5).getActive());
                        dead.setText(india_data.getStatewise().get(5).getDeaths());
                       recovered.setText(india_data.getStatewise().get(5).getRecovered());
                       affected1=Float.parseFloat(india_data.getStatewise().get(5).getConfirmed());
                       active1=Float.parseFloat(india_data.getStatewise().get(5).getActive());
                       dead1=Float.parseFloat(india_data.getStatewise().get(5).getDeaths());
                       recovered1= Float.parseFloat(india_data.getStatewise().get(5).getRecovered());


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("hiee error", "onError: "+e.getLocalizedMessage() );
                    }

                    @Override
                    public void onComplete() {
                        List<Integer> l=new ArrayList<>(sortedCASES.keySet());
                        Log.d(TAG, "onComplete: "+l.size());
                        rajasthandat();


                    }
                });
    }

    private void makegraph(india_Data india_data) {
        int size=india_data.getCases_time_series().size();
        float[] casedata= new float[10];
        casedata[0]=100;

        affectedGraphView.setData(null);// for example
        affectedGraphView.setData(casedata);
        float activecasep[]= new float[]{
                Float.valueOf(india_data.getCases_time_series().get(size-1).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-1).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-1).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-2).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-2).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-2).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-3).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-3).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-3).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-4).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-4).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-4).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-5).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-5).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-5).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-6).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-6).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-6).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-7).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-7).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-7).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-8).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-8).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-8).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-9).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-9).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-9).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-10).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-10).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-10).getTotalrecovered())};
        activegraphview.setData(null);
        activegraphview.setData(activecasep);
    }




}


