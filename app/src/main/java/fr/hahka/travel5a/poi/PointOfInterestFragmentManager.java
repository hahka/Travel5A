package fr.hahka.travel5a.poi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.hahka.travel5a.R;

/**
 * Created by thibautvirolle on 27/11/15.
 */
public class PointOfInterestFragmentManager extends Fragment {

    /**
     * Appelée lors de la création du fragment
     * @param inflater : inflater utilisé pour créer la vue
     * @param container : vue mère du fragment
     * @param savedInstanceState : données éventuellement sauvées
     * @return : La vue "inflatée"
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.moments_fragment, container, false);

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (rootView.findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return null;
            }

            // Create an instance of ExampleFragment
            PointOfInterestFragment firstFragment = new PointOfInterestFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getActivity().getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }

        return rootView;
    }

}
