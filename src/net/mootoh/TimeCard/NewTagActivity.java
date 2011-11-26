package net.mootoh.TimeCard;

import java.sql.SQLException;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public final class NewTagActivity extends NavigationActivity {
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

                TagStore tagStore = new TagStore(getApplicationContext());
                try {
                    tagStore.addTag(tagId, editText.getText().toString(), "#ff0000");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                finish();
            }
        });
    };
}