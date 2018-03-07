package com.wayrtoo.excursion.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.wayrtoo.excursion.R;
import com.wayrtoo.excursion.adapter.BookingBankListAdapter;
import com.wayrtoo.excursion.adapter.CancellationPolicyAdapter;
import com.wayrtoo.excursion.models.ActivitiesCancellatioPolicyModel;
import com.wayrtoo.excursion.models.BankListModel;
import com.wayrtoo.excursion.util.FontsOverride;
import com.wayrtoo.excursion.util.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingPaymentActivity extends AppCompatActivity {

    @BindView(R.id.tv_back)
    TextView tv_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_date_time)
    TextView tv_date_time;


    @BindView(R.id.tv_duration)
    TextView tv_duration;


    @BindView(R.id.tv_no_of_people)
    TextView tv_no_of_people;


    @BindView(R.id.tv_total)
    TextView tv_total;//

    @BindView(R.id.tv_mr)
    TextView tv_mr;

    @BindView(R.id.tv_ms)
    TextView tv_ms;

    @BindView(R.id.tv_mrs)
    TextView tv_mrs;


    @BindView(R.id.tv_cancellation_policy)
    TextView tv_cancellation_policy;

    @BindView(R.id.fetFullName)
    FormEditText fetFullName;

    @BindView(R.id.fetEmailAddress)
    FormEditText fetEmailAddress;

    @BindView(R.id.fetMobile)
    FormEditText fetMobile;

    @BindView(R.id.tv_cards)
    TextView tv_cards;

    @BindView(R.id.tv_netbanking)
    TextView tv_netbanking;

    @BindView(R.id.tv_upi)
    TextView tv_upi;

    @BindView(R.id.ll_Cards)
    LinearLayout ll_Cards;

    @BindView(R.id.ll_NetBanking)
    LinearLayout ll_NetBanking;

    @BindView(R.id.ll_upi)
    LinearLayout ll_upi;

    @BindView(R.id.fetCardNo)
    FormEditText fetCardNo;

    @BindView(R.id.fetExpiryDate)
    FormEditText fetExpiryDate;

    @BindView(R.id.fetCVV)
    FormEditText fetCVV;

    @BindView(R.id.tv_Select_Bank_Name)
    TextView tv_Select_Bank_Name;

    @BindView(R.id.tv_final_amount)
    TextView tv_final_amount;

    @BindView(R.id.pay_now)
    CardView pay_now;


    private Context mContext;
    private String Select_Gender;
    private SessionManager sessionManager;
    private ArrayList<ActivitiesCancellatioPolicyModel> cancellatioPolicyModels;
    private ArrayList<BankListModel> bankListModels;
    private BankListModel listModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        mContext = this;
        ButterKnife.bind(this);
        sessionManager = new SessionManager(mContext);
        FontsOverride.setDefaultFont(mContext, "SERIF", "fonts/DroidSerif-Regular.ttf");
        Bundle b = getIntent().getExtras();

        fetExpiryDate.setFilters(new InputFilter[]{new CreditCardExpiryInputFilter()});

        bankListModels = new ArrayList<>();
        listModel = new BankListModel();
        listModel.setBank_name("State Bank of India");
        bankListModels.add(listModel);
        listModel = new BankListModel();
        listModel.setBank_name("Axis Bank");
        bankListModels.add(listModel);
        listModel = new BankListModel();
        listModel.setBank_name("ICICI Bank");
        bankListModels.add(listModel);
        listModel = new BankListModel();
        listModel.setBank_name("Citi Bank");
        bankListModels.add(listModel);
        listModel = new BankListModel();
        listModel.setBank_name("Allahabad Bank");
        bankListModels.add(listModel);
        listModel = new BankListModel();
        listModel.setBank_name("Andhra Bank");
        bankListModels.add(listModel);
        listModel = new BankListModel();
        listModel.setBank_name("Bank of Maharashtra");
        bankListModels.add(listModel);
        listModel = new BankListModel();
        listModel.setBank_name("Bank of Baroda");
        bankListModels.add(listModel);
        listModel = new BankListModel();
        listModel.setBank_name("Bank of India");
        bankListModels.add(listModel);
        listModel = new BankListModel();
        listModel.setBank_name("Canara Bank");
        bankListModels.add(listModel);
        listModel = new BankListModel();
        listModel.setBank_name("Union Bank");
        bankListModels.add(listModel);
        listModel = new BankListModel();
        listModel.setBank_name("HDFC Bank");
        bankListModels.add(listModel);
        listModel = new BankListModel();
        listModel.setBank_name("Yes Bank");
        bankListModels.add(listModel);
        

        if(b!=null){

            tv_title.setText(getIntent().getStringExtra("Titile"));
            tv_date_time.setText(getIntent().getStringExtra("Date")+" "+getIntent().getStringExtra("Time"));
            tv_duration.setText("Duration : "+getIntent().getStringExtra("Duration"));
            if(getIntent().getStringExtra("No_of_Child").equalsIgnoreCase("0")){
                tv_no_of_people.setText(getIntent().getStringExtra("No_of_Adult")+" Adult");
            }else{
                tv_no_of_people.setText(getIntent().getStringExtra("No_of_Adult")+" Adult & "+getIntent().getStringExtra("No_of_Child")+" Child");
            }

            tv_total.setText(getIntent().getStringExtra("Total_Cost"));
            tv_final_amount.setText(getIntent().getStringExtra("Total_Cost"));
            cancellatioPolicyModels =(ArrayList<ActivitiesCancellatioPolicyModel>)getIntent().getSerializableExtra("CancellationPolicyModels");

        }else {
            Snackbar.make(findViewById(R.id.rl_container), "Something went wrong. Please try again.", Snackbar.LENGTH_LONG).show();
        }

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_mr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_mr.setTypeface(null, Typeface.BOLD);
                tv_ms.setTypeface(null, Typeface.NORMAL);
                tv_mrs.setTypeface(null, Typeface.NORMAL);
                tv_mr.setTextColor(Color.parseColor("#000000"));
                tv_ms.setTextColor(Color.parseColor("#8C8C8C"));
                tv_mrs.setTextColor(Color.parseColor("#8C8C8C"));
                Select_Gender=tv_mr.getText().toString();
            }
        });
        tv_ms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_mr.setTypeface(null, Typeface.NORMAL);
                tv_ms.setTypeface(null, Typeface.BOLD);
                tv_mrs.setTypeface(null, Typeface.NORMAL);
                tv_mr.setTextColor(Color.parseColor("#8C8C8C"));
                tv_ms.setTextColor(Color.parseColor("#000000"));
                tv_mrs.setTextColor(Color.parseColor("#8C8C8C"));
                Select_Gender=tv_ms.getText().toString();
            }
        });
        tv_mrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_mr.setTypeface(null, Typeface.NORMAL);
                tv_ms.setTypeface(null, Typeface.NORMAL);
                tv_mrs.setTypeface(null, Typeface.BOLD);
                tv_mr.setTextColor(Color.parseColor("#8C8C8C"));
                tv_ms.setTextColor(Color.parseColor("#8C8C8C"));
                tv_mrs.setTextColor(Color.parseColor("#000000"));
                Select_Gender=tv_mrs.getText().toString();

            }
        });



        tv_cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_cards.setTypeface(null, Typeface.BOLD);
                tv_netbanking.setTypeface(null, Typeface.NORMAL);
                tv_upi.setTypeface(null, Typeface.NORMAL);
                tv_cards.setTextColor(Color.parseColor("#5588EE"));
                tv_netbanking.setTextColor(Color.parseColor("#000000"));
                tv_upi.setTextColor(Color.parseColor("#000000"));
                ll_Cards.setVisibility(View.VISIBLE);
                ll_NetBanking.setVisibility(View.GONE);
                ll_upi.setVisibility(View.GONE);
            }
        });
        tv_netbanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_cards.setTypeface(null, Typeface.NORMAL);
                tv_netbanking.setTypeface(null, Typeface.BOLD);
                tv_upi.setTypeface(null, Typeface.NORMAL);
                tv_cards.setTextColor(Color.parseColor("#000000"));
                tv_netbanking.setTextColor(Color.parseColor("#5588EE"));
                tv_upi.setTextColor(Color.parseColor("#000000"));
                ll_Cards.setVisibility(View.GONE);
                ll_NetBanking.setVisibility(View.VISIBLE);
                ll_upi.setVisibility(View.GONE);

            }
        });
        tv_upi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_cards.setTypeface(null, Typeface.NORMAL);
                tv_netbanking.setTypeface(null, Typeface.NORMAL);
                tv_upi.setTypeface(null, Typeface.BOLD);
                tv_cards.setTextColor(Color.parseColor("#000000"));
                tv_netbanking.setTextColor(Color.parseColor("#000000"));
                tv_upi.setTextColor(Color.parseColor("#5588EE"));
                ll_Cards.setVisibility(View.GONE);
                ll_NetBanking.setVisibility(View.GONE);
                ll_upi.setVisibility(View.VISIBLE);
            }
        });

        tv_cancellation_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancellatioPolicyModels.size() > 0) {
                    final Dialog dialogMsg = new Dialog(BookingPaymentActivity.this);
                    dialogMsg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogMsg.setContentView(R.layout.event_detail_cancellation_policy_dialog);
                    dialogMsg.setCancelable(true);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialogMsg.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.gravity = Gravity.CENTER;
                    dialogMsg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialogMsg.getWindow().setAttributes(lp);
                    dialogMsg.show();

                    RecyclerView rv_list = (RecyclerView) dialogMsg.findViewById(R.id.rv_list);
                    CardView cardViewCancel = (CardView) dialogMsg.findViewById(R.id.cardViewCancel);
                    rv_list.setNestedScrollingEnabled(false);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                    rv_list.setLayoutManager(mLayoutManager);
                    CancellationPolicyAdapter cancelAdapter = new CancellationPolicyAdapter(getApplicationContext(), cancellatioPolicyModels);
                    rv_list.setAdapter(cancelAdapter);

                    cardViewCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogMsg.cancel();
                        }
                    });

                } else {
                    Snackbar.make(findViewById(R.id.linear_layout_container), "Cancellation policy record not found. Please try again later.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        tv_Select_Bank_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogMsg = new Dialog(BookingPaymentActivity.this);
                dialogMsg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogMsg.setContentView(R.layout.bank_dialog);
                dialogMsg.setCancelable(true);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialogMsg.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.gravity = Gravity.CENTER;
                dialogMsg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialogMsg.getWindow().setAttributes(lp);
                dialogMsg.show();

                RecyclerView rv_list = (RecyclerView) dialogMsg.findViewById(R.id.rv_list);
                CardView cardViewCancel = (CardView) dialogMsg.findViewById(R.id.cardViewCancel);
                rv_list.setNestedScrollingEnabled(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                rv_list.setLayoutManager(mLayoutManager);
                BookingBankListAdapter cancelAdapter = new BookingBankListAdapter(getApplicationContext(), bankListModels);
                rv_list.setAdapter(cancelAdapter);

                cardViewCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogMsg.cancel();
                    }
                });
            }
        });




        pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean allValid = true;
                FormEditText[] fields = {fetFullName, fetEmailAddress ,fetMobile,fetCardNo ,fetExpiryDate,fetCVV };
                for (FormEditText field : fields) {
                    if (field.testValidity() && allValid) {
                        allValid = field.testValidity() && allValid;
                    } else {
                        field.requestFocus();
                        allValid = false;
                        break;
                    }
                }
                if (allValid) {
                     if (fetMobile.getText().toString().length() < 10) {
                        fetMobile.setError("Mobile number should be 10 digit");
                        fetMobile.requestFocus();
                    } else if (fetCVV.getText().toString().length() < 4) {
                         fetCVV.setError("CVV number should be 4 digit");
                         fetCVV.requestFocus();
                     }else {
                        Snackbar.make(findViewById(R.id.linear_layout_container), "Payment Done Successfully", Snackbar.LENGTH_LONG).show();

                    }

            }

            }
        });
    }
    public class CreditCardExpiryInputFilter implements InputFilter {

        private final String currentYearLastTwoDigits;

        public CreditCardExpiryInputFilter() {
            currentYearLastTwoDigits = new SimpleDateFormat("yy", Locale.US).format(new Date());
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            //do not insert if length is already 5
            if (dest != null & dest.toString().length() == 5) return "";
            //do not insert more than 1 character at a time
            if (source.length() > 1) return "";
            //only allow character to be inserted at the end of the current text
            if (dest.length() > 0 && dstart != dest.length()) return "";

            //if backspace, skip
            if (source.length() == 0) {
                return source;
            }

            //At this point, `source` is a single character being inserted at `dstart`.
            //`dstart` is at the end of the current text.

            final char inputChar = source.charAt(0);

            if (dstart == 0) {
                //first month digit
                if (inputChar > '1') return "";
            }
            if (dstart == 1) {
                //second month digit
                final char firstMonthChar = dest.charAt(0);
                if (firstMonthChar == '0' && inputChar == '0') return "";
                if (firstMonthChar == '1' && inputChar > '2') return "";

            }
            if (dstart == 2) {
                final char currYearFirstChar = currentYearLastTwoDigits.charAt(0);
                if (inputChar < currYearFirstChar) return "";
                return "/".concat(source.toString());
            }
            if (dstart == 4){
                final String inputYear = ""+dest.charAt(dest.length()-1)+source.toString();
                if (inputYear.compareTo(currentYearLastTwoDigits) < 0) return "";
            }

            return source;
        }
    }
}
