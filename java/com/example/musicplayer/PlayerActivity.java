  package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

  public class PlayerActivity extends AppCompatActivity {

      private Button btn_next,btn_pause,btn_previous;
      TextView songTextLabel;
      SeekBar songSeekBar;

      static MediaPlayer myMediaPlayer;
      int position;
      String sname;

      ArrayList<File> mySongs;
      Thread updateseekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btn_next = (Button)findViewById(R.id.next);
        btn_pause = (Button)findViewById(R.id.pause);
        btn_previous = (Button)findViewById(R.id.previous);
        songTextLabel = (TextView)findViewById(R.id.songLabel);
        songSeekBar = (SeekBar)findViewById(R.id.seekBar);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // update seekbar postion


        updateseekBar = new Thread()
        {
            @Override
            public void run()
            {
              int totalDuration = myMediaPlayer.getDuration();
              int currentPosition = 0;

              while(currentPosition<totalDuration)
              {
                  try
                  {
                      sleep(300);
                      currentPosition = myMediaPlayer.getCurrentPosition();
                      songSeekBar.setProgress(currentPosition);

                  }
                  catch (InterruptedException e)
                  {
                   e.printStackTrace();
                  }
              }
            }
        };

        if(myMediaPlayer!=null)
        {
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle =i.getExtras();

        mySongs=(ArrayList) bundle.getParcelableArrayList("songs");

        sname =mySongs.get(position).getName().toString();

        String songName = i.getStringExtra("songname");

        songTextLabel.setText(songName);

        songTextLabel.setSelected(true);

        position = bundle.getInt("pos",0);

        Uri u = Uri.parse(mySongs.get(position).toString());

        myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

        myMediaPlayer.start();

        songSeekBar.setMax(myMediaPlayer.getDuration());

        updateseekBar.start();

        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                myMediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songSeekBar.setMax(myMediaPlayer.getDuration());

                if(myMediaPlayer.isPlaying())
                {
                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    myMediaPlayer.pause();
                }else
                {
                    btn_pause.setBackgroundResource(R.drawable.icon_pause);
                    myMediaPlayer.start();
                }



            }
        });
        btn_next.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {

                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = ((position+1)%mySongs.size());

                Uri u = Uri.parse(mySongs.get(position).toString());

                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                sname = mySongs.get(position).getName().toString();
                songTextLabel.setText(sname);

                myMediaPlayer.start();

            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer.stop();
                myMediaPlayer.release();

                position = ((position-1)<0)?(mySongs.size()-1):(position-1);

                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                sname = mySongs.get(position).getName().toString();
                songTextLabel.setText(sname);

                myMediaPlayer.start();

            }
        });


    }

      @Override
      public boolean onOptionsItemSelected(@NonNull MenuItem item) {
          if(item.getItemId() == android.R.id.home)
          {
              onBackPressed();
          }

        return super.onOptionsItemSelected(item);

      }
  }