package net.mootoh.TimeCard;

public class NavigationActivity extends android.app.Activity {
    protected android.widget.Button navigationButton;
    protected android.widget.TextView titleLabel;

    public void onCreate(android.os.Bundle savedInstanceState, int contentViewId) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(android.view.Window.FEATURE_CUSTOM_TITLE);
        setContentView(contentViewId);
        getWindow().setFeatureInt(android.view.Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        navigationButton = (android.widget.Button)findViewById(R.id.navigationButton);
        titleLabel = (android.widget.TextView)findViewById(R.id.titleLabel);
    }
}