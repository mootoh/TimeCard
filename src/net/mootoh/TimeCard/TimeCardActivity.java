package net.mootoh.TimeCard;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public final class TimeCardActivity extends NavigationActivity {
    final TagStore tagStore;
    TagHistoryAdapter tagHistoryAdapter;

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

        ListView listView = (ListView)findViewById(R.id.historyList);
        tagHistoryAdapter = new TagHistoryAdapter(this, tagStore);
        listView.setAdapter(tagHistoryAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        tagHistoryAdapter.notifyDataSetChanged();

        displayCurrentTag();
    }

    private void displayCurrentTag() {
        Tag currentTag = tagStore.currentTag();
        String tagInfo = "No Tag";
        if (currentTag != null) {
            tagInfo  = currentTag.name;
            tagInfo += ":  " + getElapsedTime(currentTag.timeStamp);
        }
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

    class TagHistoryView extends LinearLayout {
        public View tagColorView;
        public TextView tagNameView;
        public TextView tagElapsedView;

        public TagHistoryView(Context context) {
            super(context);

            LayoutParams layoutParams = new LayoutParams(48, 48);
            layoutParams.setMargins(4, 0, 4, 4);

            tagColorView = new View(context);
            tagColorView.setLayoutParams(layoutParams);
            addView(tagColorView);

            layoutParams = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 48);
            layoutParams.setMargins(12, 0, 8, 4);
            tagNameView = new TextView(context);
            tagNameView.setTextColor(Color.parseColor("#303030"));
            tagNameView.setLayoutParams(layoutParams);
            addView(tagNameView);

            tagElapsedView = new TextView(context);
            tagElapsedView.setTextColor(Color.parseColor("#636363"));
            tagElapsedView.setLayoutParams(layoutParams);
            addView(tagElapsedView);
        }

    }
    class TagHistoryAdapter extends BaseAdapter {
        final private Context context_;
        final private TagStore tagStore_;
        private ArrayList<String[]> histories;

        public TagHistoryAdapter(Context context, TagStore tagStore) {
            context_  = context;
            tagStore_ = tagStore;
        }

        private void updateHistory() {
            try {
                histories = tagStore_.getHistoryAll();
            } catch (Exception e) {
                android.util.Log.e(getClass().getSimpleName(), "failed in getting history");
            }
        }

        public int getCount() {
            updateHistory();
            return histories.size();
        }

        public Object getItem(int position) {
            updateHistory();
            return histories.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TagHistoryView tagHistoryView;
            if (convertView == null) {
                tagHistoryView = new TagHistoryView(context_);
            } else {
                tagHistoryView = (TagHistoryView)convertView;
            }
            updateHistory();
            tagHistoryView.tagColorView.setBackgroundColor(Color.parseColor("#ff7f00"));
            tagHistoryView.tagNameView.setText(histories.get(position)[0]);
            tagHistoryView.tagElapsedView.setText(histories.get(position)[1]);
            return tagHistoryView;
        }
    }
}