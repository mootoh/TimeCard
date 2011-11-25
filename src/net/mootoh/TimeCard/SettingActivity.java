package net.mootoh.TimeCard;

public final class SettingActivity extends android.app.Activity {
	@Override
    public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(android.view.Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.setting);
        getWindow().setFeatureInt(android.view.Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

        final android.widget.TextView textView = (android.widget.TextView)findViewById(R.id.titleLabel);
        textView.setText("Setting");
	}
}