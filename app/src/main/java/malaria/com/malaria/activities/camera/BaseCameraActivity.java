package malaria.com.malaria.activities.camera;

import android.os.Bundle;

import com.google.android.cameraview.CameraView;

import javax.inject.Inject;

import butterknife.BindView;
import malaria.com.malaria.R;
import malaria.com.malaria.activities.base.BaseActivity;
import malaria.com.malaria.dagger.MalariaComponent;
import malaria.com.malaria.interfaces.ICameraService;
import malaria.com.malaria.interfaces.OnPictureTakenListener;

public abstract class BaseCameraActivity extends BaseActivity implements OnPictureTakenListener {

    @Inject
    ICameraService cameraService;

    @BindView(R.id.camera)
    CameraView mCameraView;

    public BaseCameraActivity(int layoutId) {
        super(layoutId);
    }

    private void setUpCameraService() {
        cameraService.setListener(this);
        cameraService.setUpCameraView(mCameraView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpCameraService();
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraService.checkCameraPermissions();
    }

    @Override
    public void onPause() {
        cameraService.stopCameraView();
        super.onPause();
    }

    @Override
    public void onInject(MalariaComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
