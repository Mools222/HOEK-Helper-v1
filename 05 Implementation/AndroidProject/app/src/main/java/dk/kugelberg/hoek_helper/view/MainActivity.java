package dk.kugelberg.hoek_helper.view;

//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//
//import dk.kugelberg.hoek_helper.R;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import dk.kugelberg.hoek_helper.R;

public class MainActivity extends Activity {

    private TableLayout tableLayoutHeader;
    private TableLayout tableLayoutData;

    private String[] headerColumns = {"Antal enheder", "VE", "DOMK", "Udvikling", "Udvikling", "Udvikling", "Udvikling", "Udvikling"};
    private int numberOfColumns = headerColumns.length;
    private TextView[] textViewsHeaders = new TextView[numberOfColumns];

    private int numberOfRows = 100;
    private TableRow tableRowArray[] = new TableRow[numberOfRows];

    private EditText[][] editTextsArray = new EditText[numberOfRows][numberOfColumns];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayoutHeader = (TableLayout) findViewById(R.id.tableLayout_header);
        tableLayoutData = (TableLayout) findViewById(R.id.tableLayout_data);

        drawTable();
    }

    public int findLongestCell() {
        int longestCell = 0;

        for (int i = 0; i < headerColumns.length; i++) {
            int cellLength = headerColumns[i].length();

            if (cellLength > longestCell)
                longestCell = cellLength;
        }

        for (int i = 0; i < editTextsArray.length; i++) {
            for (int j = 0; j < editTextsArray[i].length; j++) {
                if (editTextsArray[i][j] != null) {
                    int cellLength = editTextsArray[i][j].getText().length();

                    if (cellLength > longestCell)
                        longestCell = cellLength;
                }
            }
        }
        return longestCell;
    }

    public int findLongestCell(int column) {
        int longestCell = 0;

        int cellLength = headerColumns[column].length();

        if (cellLength > longestCell)
            longestCell = cellLength;

        for (int i = 0; i < editTextsArray.length; i++) {
            if (editTextsArray[i][column] != null) {
                int cellLength2 = editTextsArray[i][column].getText().length();

                if (cellLength2 > longestCell)
                    longestCell = cellLength2;
            }
        }
        return longestCell;
    }

    public void drawTable() {
        tableLayoutHeader.removeAllViews();
        tableLayoutData.removeAllViews();

        TableRow tableRowHeader = new TableRow(this);

        int longestCell = findLongestCell();

        for (int i = 0; i < numberOfColumns; i++) {
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f);
            layoutParams.setMargins(2, 2, 0, 2);

            TextView textView = new TextView(this);
            textView.setBackgroundColor(Color.WHITE);
            textView.setText(headerColumns[i]);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);
            textView.setTextColor(Color.BLACK);
            textView.setVisibility(View.VISIBLE);
            textView.setGravity(Gravity.CENTER);

            final float scale = getResources().getDisplayMetrics().density;
            int pixels = (int) ((longestCell * 12) * scale + 0.5f);
            textView.setWidth(pixels);

            textView.setLayoutParams(layoutParams);

            tableRowHeader.addView(textView);
            textViewsHeaders[i] = textView;
        }

        tableLayoutHeader.addView(tableRowHeader, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < numberOfRows; i++) {
            TableRow tableRowData = new TableRow(this);
            tableRowData.setVisibility(View.VISIBLE);
            tableRowArray[i] = tableRowData;

            for (int j = 0; j < numberOfColumns; j++) {
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f);
                layoutParams.setMargins(1, 0, 0, 1);

                EditText editText = new EditText(this);
                editText.setBackgroundColor(Color.WHITE);
                editText.setText("" + i + j);
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                editText.setTextColor(Color.BLACK);
                editText.setVisibility(View.VISIBLE);
                editText.setGravity(Gravity.CENTER);

                final float scale = getResources().getDisplayMetrics().density;
                int pixels = (int) ((longestCell * 12) * scale + 0.5f);
                editText.setWidth(pixels);

                editText.setLayoutParams(layoutParams);

                final int currentColumn = j;
                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        updateCellLength(currentColumn);
                    }
                };

                editText.addTextChangedListener(textWatcher);

                tableRowArray[i].addView(editText);
                editTextsArray[i][j] = editText;
            }
        }

        for (int i = 0; i < numberOfRows; i++) {
            tableLayoutData.addView(tableRowArray[i], new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }

    public void updateCellLength(int currentColumn) {
        int longestCell = findLongestCell(currentColumn);

        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) ((longestCell * 12) * scale + 0.5f);
        textViewsHeaders[currentColumn].setWidth(pixels);

        for (int i = 0; i < numberOfRows; i++) {
            editTextsArray[i][currentColumn].setWidth(pixels);
        }

    }
}