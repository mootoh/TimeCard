package net.mootoh.TimeCard;

public final class TimeCardActivity extends NavigationActivity {
    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        TagStore tagStore = new TagStore(this);
        Tag currentTag = tagStore.currentTag();
        String tagName = currentTag == null ? "No Tag" : currentTag.name;

        android.widget.TextView currentTagNameTextView = (android.widget.TextView)findViewById(R.id.currentTagName);
        currentTagNameTextView.setText(tagName);
    }
}