package dk.kugelberg.hoek_helper.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import dk.kugelberg.hoek_helper.R;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private TableLayout tableLayoutHeader;
    private TableLayout tableLayoutData;

    private String[] headerColumns = new String[11];

    private int numberOfColumns = headerColumns.length;
    private TextView[] textViewsHeaders = new TextView[numberOfColumns];

    private int numberOfRows = 5;
    private TableRow tableRowArray[] = new TableRow[numberOfRows];

    private EditText[][] editTextsArray = new EditText[numberOfRows][numberOfColumns];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayoutHeader = findViewById(R.id.tableLayout_header);
        tableLayoutData = findViewById(R.id.tableLayout_data);

        headerColumns = new String[]{getString(R.string.antal_enheder), getString(R.string.vo), getString(R.string.ko), getString(R.string.so), getString(R.string.ve), getString(R.string.ke), getString(R.string.se), getString(R.string.domk), getString(R.string.doms), getString(R.string.gromk), getString(R.string.udvikling)};
        drawTable();
        setupSharedPreferences();

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Opgave X");
        }
    }

    /**
     * Two overloaded methods.
     * The first one finds the longest text in all the TextViews and EditTexts
     * The second one finds the longest text in the TextView and the EditTexts of a single column
     */

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

    /**
     * The following code draws the table
     */

    public void drawTable() {
        tableLayoutHeader.removeAllViews();
        tableLayoutData.removeAllViews();

        TableRow tableRowHeader = new TableRow(this);

        int longestCell = findLongestCell();

        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) ((longestCell * 14) * scale + 0.5f);

        for (int i = 0; i < numberOfColumns; i++) {
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
//            layoutParams.setMargins(2, 2, 2, 2);

            TextView textView = new TextView(this);
            textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_gray_bg));
            textView.setText(headerColumns[i]);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);

            textView.setWidth(pixels);

            textView.setLayoutParams(layoutParams);

            tableRowHeader.addView(textView);
            textViewsHeaders[i] = textView;
        }

        tableLayoutHeader.addView(tableRowHeader);

        for (int i = 0; i < numberOfRows; i++) {

            // Create TableRows
            TableRow tableRowData = new TableRow(this);
            tableRowArray[i] = tableRowData;

            // Create EditTexts
            for (int j = 0; j < numberOfColumns; j++) {
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//                layoutParams.setMargins(2, 2, 2, 2);

                EditText editText = new EditText(this);

                if (i % 2 == 0)
                    editText.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_white_bg));
                else
                    editText.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_light_gray_bg));

                editText.setText(String.valueOf(0));
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                editText.setTextColor(Color.BLACK);
                editText.setGravity(Gravity.CENTER);
                editText.setPadding(0, 0, 0, 0);
//                editText.setSingleLine(true);

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

            // Create ImageButtons
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            ImageButton imageButton = new ImageButton(this);
            imageButton.setImageResource(android.R.drawable.ic_delete);
            imageButton.setBackgroundColor(getResources().getColor(R.color.transparent));
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    deleteRow();
                }
            });

            imageButton.setLayoutParams(layoutParams);

            tableRowArray[i].addView(imageButton);

            tableLayoutData.addView(tableRowArray[i]);
        }

    }

    /**
     * The following code updates the length of TextViews and EditTexts
     */

    public void updateCellLength(int currentColumn) {
        int longestCell = findLongestCell(currentColumn);

        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) ((longestCell * 14) * scale + 0.5f);

        textViewsHeaders[currentColumn].setWidth(pixels);

        for (int i = 0; i < numberOfRows; i++) {
            editTextsArray[i][currentColumn].setWidth(pixels);
        }
    }

    /**
     * The following code adds rows to the table
     */

    public void addRow(View view) {
        ++numberOfRows;
        tableRowArray = new TableRow[numberOfRows];
        editTextsArray = new EditText[numberOfRows][numberOfColumns];
        drawTable();
    }

    /**
     * The following code deletes rows from the table
     */

    public void deleteRow() {
        --numberOfRows;
        tableRowArray = new TableRow[numberOfRows];
        editTextsArray = new EditText[numberOfRows][numberOfColumns];
        drawTable();
    }

    /**
     * The following code deals with the top right menu
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();

        if (itemThatWasClickedId == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        } else if (itemThatWasClickedId == R.id.indstillinger) {
            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * The following code deals with changing of settings
     */

    private void visibility(SharedPreferences sharedPreferences) {

        if (sharedPreferences.getBoolean(getString(R.string.vis_antal_enheder_key), getResources().getBoolean(R.bool.vis_antal_enheder))) {
            updateCellLength(0);
        } else {
            textViewsHeaders[0].setWidth(0);
            textViewsHeaders[0].setHeight(0);

            for (int i = 0; i < numberOfRows; i++) {
                editTextsArray[i][0].setWidth(0);
                editTextsArray[i][0].setHeight(0);
            }
        }
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        visibility(sharedPreferences);

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.vis_antal_enheder_key))) {
            visibility(sharedPreferences);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}