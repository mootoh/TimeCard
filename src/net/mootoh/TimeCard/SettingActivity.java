package net.mootoh.TimeCard;

public final class SettingActivity extends NavigationActivity {
    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.setting);
        titleLabel.setText("Setting");

        final android.content.Context that = this;
        final android.widget.Button button = (android.widget.Button)findViewById(R.id.resetButton);
        button.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                TagStore tagStore = new TagStore(that);
                tagStore.reset();
            }
        });
    }
}