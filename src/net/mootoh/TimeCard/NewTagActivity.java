package net.mootoh.TimeCard;

import java.sql.SQLException;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public final class NewTagActivity extends NavigationActivity {
   Button selected;

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.newtag);
        titleLabel.setText("New Tag");

        final String tagId = getIntent().getStringExtra("tagId");
        final EditText editText = (EditText)findViewById(R.id.tagNameEdit);

        navigationButton.setText("Save");
        navigationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editText.getText().toString().equals(""))
                    return;

                TagStore tagStore = new TagStore(getApplicationContext());

                try {
                    String tagColor = (String)selected.getTag();
                    tagStore.addTag(tagId, editText.getText().toString(), tagColor);
                } catch (SQLException e) {
                    Log.e(getClass().getSimpleName(), "cannot add this tag:" + tagId);
                }

                finish();
            }
        });

        ColorButton[] colorButtons = {
                new ColorButton((Button)findViewById(R.id.color1Button), "ff7700"),
                new ColorButton((Button)findViewById(R.id.color2Button), "ff007f"),
                new ColorButton((Button)findViewById(R.id.color3Button), "66ff66"),
                new ColorButton((Button)findViewById(R.id.color4Button), "ffff66"),
                new ColorButton((Button)findViewById(R.id.color5Button), "007fff"),
        };

        for (ColorButton colorButton : colorButtons) {
            colorButton.button.setTag(colorButton.color);
            colorButton.button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d(getClass().getSimpleName(), "touched: ");
                    // unselect current selected
                    selected = (Button)v;
                }
            });
        }
        Button bbb = new Button(this);
        bbb.setText("bbb");
        LinearLayout ll = (LinearLayout)findViewById(R.id.colorButtonLayout);
        ll.addView(bbb);
    };

    class ColorButton {
        public Button button;
        public String color;

        public ColorButton(Button button, String color) {
            this.button = button;
            this.color = color;
        }
    }
}