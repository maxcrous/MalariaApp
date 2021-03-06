package malaria.com.malaria.activities.results;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import malaria.com.malaria.R;
import malaria.com.malaria.activities.base.BaseActivity;
import malaria.com.malaria.activities.camera.AnalysisCameraActivity;
import malaria.com.malaria.activities.guide.GuideActivity;
import malaria.com.malaria.dagger.MalariaComponent;
import malaria.com.malaria.interfaces.IModelAnalysisService;
import malaria.com.malaria.models.ImageFeature;

public class ResultsActivity extends BaseActivity {

    @BindView(R.id.wbcTV)
    TextView wbcTV;

    @BindView(R.id.parasitesTV)
    TextView parasitesTV;

    @BindView(R.id.fieldsTV)
    TextView fieldsTV;

    @BindView(R.id.resultTV)
    TextView resultTV;

    @BindView(R.id.conclusionTxt)
    TextView conclusionTxt;

    @BindView(R.id.status_dot)
    ImageView statusDot;


    @BindView(R.id.startBtn)
    Button finishBtn;

    @BindView(R.id.seeEvidenceBtn)
    Button seeEvidenceBtn;

    @Inject
    IModelAnalysisService service;


    public ResultsActivity() {
        super(R.layout.activity_results);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageFeature features = service.getTotalAggregation();
        int nWBC = features.getnWhiteBloodCells();
        int nParasites = features.getnParasites();
        int fields = service.getTotalFields();
        int result = service.getParasitePerMicrolitre();

        wbcTV.setText(String.valueOf(nWBC));
        parasitesTV.setText(String.valueOf(nParasites));
        fieldsTV.setText(String.valueOf(fields));
        resultTV.setText(String.valueOf(result));

        conclusionTxt.setText(this.getConclusionText(result));

        finishBtn.setOnClickListener(v -> {
            finishActivity();
        });

        seeEvidenceBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, EvidenceActivity.class));
        });


    }

    private String getConclusionText(int parasitesPerMicrolitre) {
        int textId = R.string.conclusion_no_malaria;
        if (parasitesPerMicrolitre >= 5 && parasitesPerMicrolitre < 100) {
            textId = R.string.conclusion_remark_0;
            statusDot.setImageResource(R.drawable.green_dot);
        } else if (parasitesPerMicrolitre >= 100 && parasitesPerMicrolitre < 10_000) {
            textId = R.string.conclusion_remark_1;
            statusDot.setImageResource(R.drawable.yellow_dot);
        } else if (parasitesPerMicrolitre >= 10_000 && parasitesPerMicrolitre < 100_000) {
            textId = R.string.conclusion_remark_2;
            statusDot.setImageResource(R.drawable.yellow_dot);
        } else if (parasitesPerMicrolitre >= 100_000 && parasitesPerMicrolitre < 250_000) {
            textId = R.string.conclusion_remark_4;
            statusDot.setImageResource(R.drawable.orange_dot);
        } else if(parasitesPerMicrolitre >= 250_000){
            textId = R.string.conclusion_remark_5;
            statusDot.setImageResource(R.drawable.red_dot);
        } else {
            statusDot.setImageResource(R.drawable.green_dot);
        }
        return this.getString(textId);
    }

    @Override
    public void onInject(MalariaComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    private void finishActivity() {
        Intent intent = new Intent(this, GuideActivity.class);
        intent.putExtra("last_slide", true);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finishActivity();
        }
        return super.onKeyDown(keyCode, event);
    }
}
