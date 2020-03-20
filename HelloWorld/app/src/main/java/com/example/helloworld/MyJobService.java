package com.example.helloworld;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {

    private static final String TAG = MyJobService.class.getSimpleName();
    private boolean jobCancelled = false;
    Context context;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob: ");
        Toast.makeText(getApplicationContext(), "Job Start", Toast.LENGTH_SHORT).show();
        context = getApplicationContext();
        doBackground(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "onStopJob: cancel");
        Toast.makeText(getApplicationContext(), "Job Cancel", Toast.LENGTH_SHORT).show();
        jobCancelled = true;
        return true;
    }

    public void doBackground(final JobParameters parameters){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i<=10; i++){
                    Log.i(TAG, "run: " + i);

                    final int finalI = i;
                    Handler mHandler = new Handler(getMainLooper());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "handler run: "+ finalI);
                            Toast.makeText(getApplicationContext(), "Handler Run: "+finalI, Toast.LENGTH_SHORT).show();
                        }
                    });

                    if(jobCancelled){
                        return;
                    }
                    try {
                        Thread.sleep(3000);
                    }catch (InterruptedException e){
                        Log.e(TAG, "InterruptedException: ",e.getCause() );
                    }
                }

                Log.d(TAG, "run: JOB FINISHED");
                Handler finishHandler = new Handler(getMainLooper());
                finishHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Job Finished", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).start();
    }
}
