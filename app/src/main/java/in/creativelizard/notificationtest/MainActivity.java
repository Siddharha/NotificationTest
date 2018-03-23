package in.creativelizard.notificationtest;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    private int FM_NOTIFICATRION_ID = 0;
    private Button btnNotification;
    private String channelId;
    NotificationManager manager;
    NotificationCompat.Builder builderNOtification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        onActionPerform();
    }

    private void initialize() {
        builderNOtification = new NotificationCompat.Builder(this,channelId);
        btnNotification = findViewById(R.id.btnNotification);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void onActionPerform() {
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupCreateNotificationDetails().show();
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

                String notificationId = fcmIdText.getText().toString();
                if(!notificationId.isEmpty()) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        try {
                            if (manager.getActiveNotifications()[0].getId() != Integer.parseInt(notificationId)) {
                                createNotification(title.getText().toString(),
                                        content.getText().toString(),
                                        Integer.parseInt(notificationId));
                            } else {
                                Toast.makeText(MainActivity.this, "Already This Notification present in TaskBar!", Toast.LENGTH_SHORT).show();
                            }
                        }catch (IndexOutOfBoundsException e){
                            createNotification(title.getText().toString(),
                                    content.getText().toString(),
                                    Integer.parseInt(notificationId));
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

    private void createNotification(String s, String toString, int i) {

        builderNOtification.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(s)
                .setContentText(toString);

        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builderNOtification.setContentIntent(pendingIntent);
        removeNotification(i);
        manager.notify(i,builderNOtification.build());

    }

    private void removeNotification(int i){
        manager.cancel(i);
    }
}
