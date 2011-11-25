package net.mootoh.TimeCard;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public final class NewTagActivity extends NavigationActivity {
    private static final String PREFS_NAME = "net.mootoh.TouchTracker";

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleLabel.setText("New Tag");

        final String tagId = getIntent().getStringExtra("tagId");

        // show a form to input the name or something for this tag
        setContentView(R.layout.newtag);

        final EditText editText = (EditText)findViewById(R.id.tagNameEdit);
        final Button button = (Button)findViewById(R.id.saveNameButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editText.getText().equals(""))
                    return;

                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = prefs.edit();
                Log.d("aa", "text = " + editText.getText().toString());
                editor.putString(tagId, editText.getText().toString());

                editor.putBoolean("tracking", true);
                editor.commit();

                finish();
            }
        });
    };
}
