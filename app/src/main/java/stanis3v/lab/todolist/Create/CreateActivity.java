package stanis3v.lab.todolist.Create;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import stanis3v.lab.todolist.MainActivity;
import stanis3v.lab.todolist.R;

import static stanis3v.lab.todolist.R.layout.activity_create;

public class CreateActivity extends AppCompatActivity {

    TextView txtDate, txtTime;
    Button btnApply, btnCancel;
    EditText edtTitle, edtDes;
    CheckBox cbRemind;

    int dayC1, monthC1, yearC1, hourC1, minuteC1, second;

    Calendar c;
    Calendar c2;

    public static final String CHANNEL_ID = "channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(activity_create);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Create New Task");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createNotificationChannels();

        txtDate = findViewById(R.id.textDate);
        txtTime = findViewById(R.id.textTime);
        btnApply = findViewById(R.id.btnApply);
        btnCancel = findViewById(R.id.btnCancel);
        edtTitle = findViewById(R.id.editInputTitle);
        edtDes = findViewById(R.id.editInputDescription);
        cbRemind = findViewById(R.id.checkBRemind);

        txtDate.setOnClickListener(v -> PickDate());
        txtTime.setOnClickListener(v -> PickTime());

        final Animation animButton= AnimationUtils.loadAnimation(this,R.anim.anim_applycancel);
        btnApply.setOnClickListener(v -> {
            v.startAnimation(animButton);
            String title = edtTitle.getText().toString().trim();
            String des = edtDes.getText().toString().trim();
            String remind;
            String time = txtTime.getText().toString().trim();
            String date = txtDate.getText().toString().trim();
            if (cbRemind.isChecked()) {
                remind = "Remind me" + " at " + time + " (" + date + ")";
            } else {
                remind = "Not remind me";
            }
            if (title.equals("")) {
                Toast.makeText(CreateActivity.this, "Set the title", Toast.LENGTH_SHORT).show();
            } else {
                if (date.equals("Date")) {
                    Toast.makeText(CreateActivity.this, "Set the date", Toast.LENGTH_SHORT).show();
                } else if (time.equals("Hour")) {
                    Toast.makeText(CreateActivity.this, "Set the time", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(this::Notify).start();
                    Toast.makeText(CreateActivity.this, "Create Succesful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("des", des);
                    bundle.putString("remind", remind);
                    bundle.putString("date", date);
                    bundle.putString("time", time);
                    intent.putExtra("data", bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                }
            }

        });
        btnCancel.setOnClickListener(v -> {
            v.startAnimation(animButton);
            startActivity(new Intent(CreateActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        });
    }

    private void PickDate() {
        c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int date = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            yearC1 = year1;
            monthC1 = month1;
            dayC1 = dayOfMonth;
            c.set(year1, month1, dayOfMonth);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            txtDate.setText(simpleDateFormat.format(c.getTime()));
        }, year, month, date);
        datePicker.show();
    }

    private void PickTime() {
        c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        TimePickerDialog timePicker = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            hourC1 = hourOfDay;
            minuteC1 = minute;
            c.set(0, 0, 0, hourOfDay, minute);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            txtTime.setText(simpleDateFormat.format(c.getTime()));
        }, hour, minutes, true);
        timePicker.show();
    }

    private void NotifyCountTimeCPick() {
        c = Calendar.getInstance();
        c.set(yearC1, monthC1, dayC1, hourC1, minuteC1);
    }

    private void NotifyCountTimeCNow() {
        NotifyCountTimeCPick();
        c2 = Calendar.getInstance();
        int hour = c2.get(Calendar.HOUR_OF_DAY);
        int minutes = c2.get(Calendar.MINUTE);
        int year = c2.get(Calendar.YEAR);
        int date = c2.get(Calendar.DAY_OF_MONTH);
        int month = c2.get(Calendar.MONTH);
        second = c2.get(Calendar.SECOND);
        c2.set(year, month, date, hour, minutes);
    }

    public void Notify() {
        NotifyCountTimeCNow();
        int count = (int) (c.getTimeInMillis() - c2.getTimeInMillis())-(second)*1000;
        try {
            Thread.sleep(count);
            String content = "Go go go";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon_v5)
                    .setContentTitle("Hey, you have a task now!")
                    .setContentText(edtTitle.getText().toString().trim())
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(edtTitle.getText().toString().trim()))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);


            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("toDoNow", content);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("This is channel notify");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CreateActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }

}
