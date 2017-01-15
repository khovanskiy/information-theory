package ru.ifmo.ctddev.khovanskiy.information.android;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author warrior
 * @author victor
 * @since 1.0.0
 */
public class MainActivity extends ListActivity {

    private static final Object[] TASKS = {
            "Entropy",
            "Redundancy",
            "Average length",
            "Arithmetic",
            "Arithmetic Decode",
            R.string.binarySymmetricChannel
    };
    private static final Class[] ACTIVITIES = {
            EntropyActivity.class,
            RedundancyActivity.class,
            AverageLength.class,
            ArithmeticActivity.class,
            ArithmeticDecodeActivity.class,
            BinarySymmetricChannel.class
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        String[] labels = new String[TASKS.length];
        for (int i = 0; i < labels.length; ++i) {
            Object object = TASKS[i];
            if (object instanceof String) {
                labels[i] = (String) object;
            } else if (object instanceof Integer) {
                int resourceId = (int) object;
                labels[i] = getString(resourceId);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, labels);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Class activityClass = ACTIVITIES[position];
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}
