package ru.ifmo.ctddev.khovanskiy.information.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.fraction.Fraction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author warrior
 */
@Slf4j
public class ArithmeticActivity extends Activity {

    private FractionList fractionList;
    private EditText coding;
    private TextView codingResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arithmetic);

        fractionList = (FractionList) findViewById(R.id.fractions);
        coding = (EditText) findViewById(R.id.coding);
        codingResult = (TextView) findViewById(R.id.coding_result);

        findViewById(R.id.calculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Fraction> fractions = fractionList.getFractions();
                List<String> symbols = new ArrayList<>(fractions.size());
                LinkedHashMap<String, Fraction> probabilities = new LinkedHashMap<>();
                for (int i = 0; i < fractions.size(); i++) {
                    String symbol = String.valueOf((char) (i + 'a'));
                    symbols.add(symbol);
                    probabilities.put(symbol, fractions.get(i));
                }

                String str = coding.getText().toString();
                String result = null;
                try {
                    result = Utils.arithmeticCoding(probabilities, str);
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
                if (result != null) {
                    codingResult.setText(result);
                } else {
                    codingResult.setText(R.string.error);
                }
            }
        });
    }
}
