package com.homesoftwaretools.portmone.activities;
/*
 * Created by Wild on 05.05.2015.
 */

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import com.homesoftwaretools.portmone.DatePickerFragment;
import com.homesoftwaretools.portmone.R;
import com.homesoftwaretools.portmone.domain.CashType;
import com.homesoftwaretools.portmone.domain.Income;
import com.homesoftwaretools.portmone.domain.IncomeType;
import com.homesoftwaretools.portmone.provider.PortmoneContract;
import com.homesoftwaretools.portmone.utils.EditTextHashtagWatcher;
import com.homesoftwaretools.portmone.utils.FormValidator;
import com.homesoftwaretools.portmone.utils.FormatUtils;
import com.homesoftwaretools.portmone.utils.HashTagTokenizer;
import com.homesoftwaretools.portmone.utils.LibAutocompleteManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IncomeEditorActivity extends AppCompatActivity {


    private LibAutocompleteManager autocompleteManager;

    public static Intent createIntent(Context context, Income income) {
        Intent intent = new Intent(context, IncomeEditorActivity.class);
        intent.putExtra(EXTRA_ID, income.getId());
        intent.putExtra(EXTRA_PLANNED, income.isPlanned());
        intent.putExtra(EXTRA_CASH_TYPE, income.getCashType().getName());
        intent.putExtra(EXTRA_INCOME_TYPE, income.getIncomeType().getName());
        intent.putExtra(EXTRA_SUM, income.getSum());
        intent.putExtra(EXTRA_DATE, income.getDate().getTime());
        intent.putExtra(EXTRA_DESCRIPTION, income.getDescription());
        intent.putExtra(EXTRA_WEB_ID, income.getWebId());
        return intent;
    }

    public static Income getIncomeFromIntent(Intent intent) {
        Date date = new Date(intent.getLongExtra(EXTRA_DATE, 0));
        boolean planned = intent.getBooleanExtra(EXTRA_PLANNED, false);
        CashType cashType = new CashType(intent.getStringExtra(EXTRA_CASH_TYPE));
        IncomeType incomeType = new IncomeType(intent.getStringExtra(EXTRA_INCOME_TYPE));
        float sum = intent.getFloatExtra(EXTRA_SUM, 0f);
        String description = intent.getStringExtra(EXTRA_DESCRIPTION);
        String webId = intent.getStringExtra(EXTRA_WEB_ID);
        return new Income(date, planned, cashType, incomeType, sum, description, webId);
    }

    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_WEB_ID = "extra_web_id";
    public static final String EXTRA_PLANNED = "extra_planned";
    public static final String EXTRA_DATE = "extra_date";
    public static final String EXTRA_CASH_TYPE = "extra_cash_type";
    public static final String EXTRA_INCOME_TYPE = "extra_income_type";
    public static final String EXTRA_SUM = "extra_sum";
    public static final String EXTRA_DESCRIPTION = "extra_description";
    private static final SimpleDateFormat sdf = FormatUtils.JOURNAL_ITEM_EDITOR_FORMAT;

    private CheckBox plannedCheckBox;
    private AutoCompleteTextView dateText;
    private AutoCompleteTextView cashTypeTextView;
    private AutoCompleteTextView incomeTypeTextView;
    private EditText sumEditText;
    private MultiAutoCompleteTextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_editor);

        autocompleteManager = new LibAutocompleteManager(this, getLoaderManager());
        findViews();
        if (savedInstanceState == null) {
            initFields(getIntent().getExtras());
        }
    }

    private void initFields(Bundle extras) {
        if (extras == null) return;

        plannedCheckBox.setChecked(extras.getBoolean(EXTRA_PLANNED, false));
        dateText.setText(sdf.format(new Date(extras.getLong(EXTRA_DATE))));
        dateText.setOnClickListener(new DateTextClickListener());
        cashTypeTextView.setText(extras.getString(EXTRA_CASH_TYPE));
        incomeTypeTextView.setText(extras.getString(EXTRA_INCOME_TYPE));
        sumEditText.setText(String.valueOf(extras.getFloat(EXTRA_SUM)));
        descriptionTextView.setText(extras.getString(EXTRA_DESCRIPTION));
    }

    private void findViews() {
        plannedCheckBox = (CheckBox) findViewById(R.id.plannedCheckBox);
        dateText = (AutoCompleteTextView) findViewById(R.id.dateTextView);
        cashTypeTextView = (AutoCompleteTextView) findViewById(R.id.cashType);
        incomeTypeTextView = (AutoCompleteTextView) findViewById(R.id.incomeType);
        sumEditText = (EditText) findViewById(R.id.sumEditText);
        descriptionTextView = (MultiAutoCompleteTextView) findViewById(R.id.descriptionTextView);
        descriptionTextView.addTextChangedListener(new EditTextHashtagWatcher());
        findViewById(R.id.doneButton).setOnClickListener(new DoneButtonClickListener());
        autocompleteManager
                .addAutocompleteTextView(cashTypeTextView, PortmoneContract.CashTypes.CONTENT_URI)
                .addAutocompleteTextView(incomeTypeTextView, PortmoneContract.IncomeTypes.CONTENT_URI)
                .addMultiAutocompleteTextView(descriptionTextView, PortmoneContract.Tags.CONTENT_URI, new HashTagTokenizer());
    }

    private class DoneButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (validForm())
                sendResult();
        }
    }

    private void sendResult() {

        try {
            Date date = sdf.parse(dateText.getText().toString());
            float sum = Float.parseFloat(sumEditText.getText().toString());
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_ID, getIntent().getLongExtra(EXTRA_ID, -1));
            resultIntent.putExtra(EXTRA_WEB_ID, getIntent().getStringExtra(EXTRA_WEB_ID));
            resultIntent.putExtra(EXTRA_PLANNED, plannedCheckBox.isChecked());
            resultIntent.putExtra(EXTRA_DATE, date.getTime());
            resultIntent.putExtra(EXTRA_CASH_TYPE, cashTypeTextView.getText().toString());
            resultIntent.putExtra(EXTRA_INCOME_TYPE, incomeTypeTextView.getText().toString());
            resultIntent.putExtra(EXTRA_SUM, sum);
            resultIntent.putExtra(EXTRA_DESCRIPTION, descriptionTextView.getText().toString());
            setResult(RESULT_OK, resultIntent);
            finish();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean validForm() {
        final FormValidator fv = new FormValidator()
                .validDate(dateText, "Уточните дату прихода, пожалуйста", sdf)
                .notEmpty(cashTypeTextView, "Укажите вид денежных средств, на которые зачисляем сумму")
                .notEmpty(incomeTypeTextView, "Придумайте вид для этого прихода")
                .positiveNumber(sumEditText, "Сумма прихода должна быть позитивной");
        return fv.isValid();
    }

    private class DateTextClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            DatePickerFragment.show(getFragmentManager(), getCurrentDate(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    final Calendar c = Calendar.getInstance();
                    c.set(year, monthOfYear, dayOfMonth);
                    dateText.setText(sdf.format(c.getTime()));
                }
            });
        }

        private Date getCurrentDate() {
            try {
                return sdf.parse(dateText.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
                return new Date();
            }
        }
    }

}
