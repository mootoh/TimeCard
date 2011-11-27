package net.mootoh.TimeCard;

public final class TimeCardActivity extends NavigationActivity {
    final TagStore tagStore;

    public TimeCardActivity() {
        tagStore = new TagStore(this);
    }

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.main);

        navigationButton.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                android.content.Intent settingIntent = new android.content.Intent();
                settingIntent.setClass(getApplicationContext(), SettingActivity.class);
                startActivity(settingIntent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        displayCurrentTag();
    }

    private void displayCurrentTag() {
        Tag currentTag = tagStore.currentTag();
        String tagInfo = "No Tag";
        if (currentTag != null) {
            tagInfo  = currentTag.name;
            tagInfo += ":  " + getElapsedTime(currentTag.timeStamp);
        }

        String history = "";
        try {
            String[] histories = tagStore.getHistoryAll();
            for (String hist : histories)
                history += hist + "\n";
        } catch (Exception e) {
            android.util.Log.e(getClass().getSimpleName(), "failed in getting history");
        }

        android.widget.TextView currentTagNameTextView = (android.widget.TextView)findViewById(R.id.currentTagName);
        currentTagNameTextView.setText(tagInfo + "\n" + history);
    }

    private String getElapsedTime(java.util.Date currentDate) {
        long lastTime = currentDate.getTime();
        long curTime  = System.currentTimeMillis();
        long elapsed = (curTime - lastTime);

        long elapsedHour = elapsed / (60*60*1000);
        elapsed -= elapsedHour * (60*60*1000);

        long elapsedMin = elapsed / (60*1000);
        elapsed -= elapsedMin * 60*1000;
        long elapsedSec = elapsed / 1000;

        return String.format("%02d:%02d:%02d", elapsedHour, elapsedMin, elapsedSec);
    }
}