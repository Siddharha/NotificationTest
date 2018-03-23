package in.creativelizard.notificationtest;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {

    private int FM_NOTIFICATRION_ID = 0;
    private Button btnNotification,btnRemove;
    private String channelId;
    NotificationManager manager;
    NotificationCompat.Builder builderNOtification;
    long[] vibrationPattern = {100, 200, 300, 400};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        onActionPerform();
    }

    private void initialize() {
        channelId = "1";
        builderNOtification = new NotificationCompat.Builder(this,channelId);
        btnNotification = findViewById(R.id.btnNotification);
        btnRemove = findViewById(R.id.btnRemove);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void onActionPerform() {
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupCreateNotificationDetails().show();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAllNotifications();
            }
        });
    }

    private AlertDialog popupCreateNotificationDetails() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(5,5,5,5);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText title = new EditText(this);
        title.setHint("Enter Title");
        final EditText content = new EditText(this);
        content.setHint("Enter Content of Notification");

        LinearLayout linearLayoutH = new LinearLayout(this);
        linearLayoutH.setOrientation(LinearLayout.HORIZONTAL);

        final EditText fcmIdText = new EditText(this);
        fcmIdText.setInputType(InputType.TYPE_CLASS_NUMBER);
        fcmIdText.setHint("Enter ID");
        Button createNotificationBtn = new Button(this);
        createNotificationBtn.setText("Create");

        createNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //channelId = fcmIdText.getText().toString();
                if(!channelId.isEmpty()) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        try {
                            if (manager.getActiveNotifications()[0].getId() != Integer.parseInt(channelId)) {


                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    if(!(manager.getNotificationChannel(channelId).getId()).equalsIgnoreCase(fcmIdText.getText().toString())){
                                        removeAllNotifications();
                                        createNotification(title.getText().toString(),
                                                content.getText().toString(),
                                                Integer.parseInt(fcmIdText.getText().toString()));
                                    }else {
                                        Toast.makeText(MainActivity.this, "Already This Notification present in TaskBar!", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    removeAllNotifications();
                                    createNotification(title.getText().toString(),
                                            content.getText().toString(),
                                            Integer.parseInt(fcmIdText.getText().toString()));
                                }

                            } else {
                                Toast.makeText(MainActivity.this, "Already This Notification present in TaskBar!", Toast.LENGTH_SHORT).show();
                            }
                        }catch (IndexOutOfBoundsException e){
                            removeAllNotifications();
                            createNotification(title.getText().toString(),
                                    content.getText().toString(),
                                    Integer.parseInt(fcmIdText.getText().toString()));
                        }
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Enter Notification ID!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        linearLayoutH.addView(fcmIdText);
        linearLayoutH.addView(createNotificationBtn);

        linearLayoutH.setLayoutParams(params);

        linearLayout.addView(title);
        linearLayout.addView(content);
        linearLayout.addView(linearLayoutH);
        linearLayout.setLayoutParams(params);


        builder.setTitle("Enter Information")
                .setMessage("Please Enter All info to create notification!")
                .setView(linearLayout);
        return builder.create();
    }

    private void removeAllNotifications() {
        manager.cancelAll();
    }

    private void createNotification(String s, String toString, int i) {

        channelId = String.valueOf(i);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
           // NotificationChannel privateMessagesChannel = new NotificationChannel(PRIVATE_MESSAGES_CHANNEL_ID,getString(R.string.pm_channel_name),
                 //   NotificationManager.IMPORTANCE_DEFAULT);

            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel privateMessagesChannel = new NotificationChannel(channelId, getString(R.string.pm_channel_name), importance);
            privateMessagesChannel.enableLights(true);

            manager.createNotificationChannel(privateMessagesChannel);

           privateMessagesChannel.setLightColor(Color.RED);
            privateMessagesChannel.enableVibration(true);
            privateMessagesChannel.setVibrationPattern(vibrationPattern);
            manager.createNotificationChannel(privateMessagesChannel);

            builderNOtification.setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setSound(null)
                    // .setContent(contentView)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    //.setLargeIcon(picture)
                    //.setTicker(sTimer)
                    //.setContentIntent(timerListIntent)
                    .setAutoCancel(false);
           // manager.notify(i,builderNOtification.build());
            manager.notify((int)(System.currentTimeMillis()/1000), builderNOtification.build());
            }else {
              builderNOtification.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(s)
                .setContentText(toString);

        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builderNOtification.setOngoing(true);
        builderNOtification.setContentIntent(pendingIntent);
        manager.notify(i,builderNOtification.build());
        }




    }

    private void removeNotification(int i){
        manager.cancel(i);
    }
}
