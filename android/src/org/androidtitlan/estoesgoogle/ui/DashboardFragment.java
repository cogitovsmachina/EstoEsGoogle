package org.androidtitlan.estoesgoogle.ui;

import org.androidtitlan.estoesgoogle.R;
import org.androidtitlan.estoesgoogle.provider.ScheduleContract;
import org.androidtitlan.estoesgoogle.ui.phone.ScheduleActivity;
import org.androidtitlan.estoesgoogle.ui.tablet.ScheduleMultiPaneActivity;
import org.androidtitlan.estoesgoogle.ui.tablet.SessionsMultiPaneActivity;
import org.androidtitlan.estoesgoogle.util.AnalyticsUtils;
import org.androidtitlan.estoesgoogle.util.UIUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DashboardFragment extends Fragment {

    public void fireTrackerEvent(String label) {
        AnalyticsUtils.getInstance(getActivity()).trackEvent(
                "Home Screen Dashboard", "Click", label, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container);

        // Attach event handlers
        root.findViewById(R.id.home_btn_schedule).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fireTrackerEvent("Schedule");
                if (UIUtils.isHoneycombTablet(getActivity())) {
                    startActivity(new Intent(getActivity(), ScheduleMultiPaneActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), ScheduleActivity.class));
                }
                
            }
            
        });

        root.findViewById(R.id.home_btn_sessions).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fireTrackerEvent("Sessions");
                // Launch sessions list
                if (UIUtils.isHoneycombTablet(getActivity())) {
                    startActivity(new Intent(getActivity(), SessionsMultiPaneActivity.class));
                } else {
                    final Intent intent = new Intent(Intent.ACTION_VIEW,
                            ScheduleContract.Tracks.CONTENT_URI);
                    intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.title_session_tracks));
                    intent.putExtra(TracksFragment.EXTRA_NEXT_TYPE,
                            TracksFragment.NEXT_TYPE_SESSIONS);
                    startActivity(intent);
                }

            }
        });

        root.findViewById(R.id.home_btn_starred).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fireTrackerEvent("Starred");
                // Launch list of sessions and vendors the user has starred
                startActivity(new Intent(getActivity(), StarredActivity.class));                
            }
        });

        root.findViewById(R.id.home_btn_map).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Launch map of conference venue
                fireTrackerEvent("Map");
                startActivity(new Intent(getActivity(),
                        UIUtils.getMapActivityClass(getActivity())));
            }
        });
        return root;
    }
}
