package ru.ifmo.ctddev.khovanskiy.information.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import lombok.extern.slf4j.Slf4j;

/**
 * @author victor
 * @since 1.1.0
 */
@Slf4j
public class BinarySymmetricChannel extends AppCompatActivity {

    private EditText probabilityView;
    private EditText eraseErrorView;
    private TextView capacityView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.binary_symmetric_channel);

        probabilityView = (EditText) findViewById(R.id.probability);
        eraseErrorView = (EditText) findViewById(R.id.eraseError);
        capacityView = (TextView) findViewById(R.id.capacity);

        findViewById(R.id.calculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String probabilityString = probabilityView.getText().toString();
                    double probabilityValue = Double.parseDouble(probabilityString);
                    String eraseErrorString = eraseErrorView.getText().toString();
                    double eraseErrorValue = 1;
                    if (!eraseErrorString.isEmpty()) {
                        eraseErrorValue = Double.parseDouble(eraseErrorString);
                    }
                    double capacity = Utils.informationalCapacity(probabilityValue, eraseErrorValue);
                    capacityView.setText(getString(R.string.resultDouble, capacity));
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                    Toast.makeText(BinarySymmetricChannel.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
