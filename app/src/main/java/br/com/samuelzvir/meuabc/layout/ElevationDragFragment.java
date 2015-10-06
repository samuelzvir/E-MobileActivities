//package br.com.samuelzvir.meuabc.layout;
//
//
//import com.example.android.common.logger.Log;
//
//import android.graphics.Outline;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewOutlineProvider;
//
//public class ElevationDragFragment extends Fragment {
//
//    public static final String TAG = "ElevationDragFragment";
//
//    /* The circular outline provider */
//    private ViewOutlineProvider mOutlineProviderCircle;
//
//    /* The current elevation of the floating view. */
//    private float mElevation = 0;
//
//    /* The step in elevation when changing the Z value */
//    private int mElevationStep;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        mOutlineProviderCircle = new CircleOutlineProvider();
//
//        mElevationStep = getResources().getDimensionPixelSize(R.dimen.elevation_step);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.activity_drag_and_drop, container, false);
//
//        /* Find the {@link View} to apply z-translation to. */
//        final View floatingShape = rootView.findViewById(R.id.circle);
//
//        /* Define the shape of the {@link View}'s shadow by setting one of the {@link Outline}s. */
//        floatingShape.setOutlineProvider(mOutlineProviderCircle);
//
//        /* Clip the {@link View} with its outline. */
//        floatingShape.setClipToOutline(true);
//
//        DragFrameLayout dragLayout = ((DragFrameLayout) rootView.findViewById(R.id.main_layout));
//
//        dragLayout.setDragFrameController(new DragFrameLayout.DragFrameLayoutController() {
//
//            @Override
//            public void onDragDrop(boolean captured) {
//                /* Animate the translation of the {@link View}. Note that the translation
//                 is being modified, not the elevation. */
//                floatingShape.animate()
//                        .translationZ(captured ? 50 : 0)
//                        .setDuration(100);
//                Log.d(TAG, captured ? "Drag" : "Drop");
//            }
//        });
//
//        dragLayout.addDragView(floatingShape);
//
//        /* Raise the circle in z when the "z+" button is clicked. */
//        rootView.findViewById(R.id.raise_bt).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mElevation += mElevationStep;
//                Log.d(TAG, String.format("Elevation: %.1f", mElevation));
//                floatingShape.setElevation(mElevation);
//            }
//        });
//
//        /* Lower the circle in z when the "z-" button is clicked. */
//        rootView.findViewById(R.id.lower_bt).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mElevation -= mElevationStep;
//                // Don't allow for negative values of Z.
//                if (mElevation < 0) {
//                    mElevation = 0;
//                }
//                Log.d(TAG, String.format("Elevation: %.1f", mElevation));
//                floatingShape.setElevation(mElevation);
//            }
//        });
//
//        return rootView;
//    }
//
//    /**
//     * ViewOutlineProvider which sets the outline to be an oval which fits the view bounds.
//     */
//    private class CircleOutlineProvider extends ViewOutlineProvider {
//        @Override
//        public void getOutline(View view, Outline outline) {
//            outline.setOval(0, 0, view.getWidth(), view.getHeight());
//        }
//    }
//
//}