package fr.hahka.travel5a;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by thibautvirolle on 17/11/15.
 */
public class POIOptionsDialog extends DialogFragment {

    private int dataId;
    /**
     * Permet de récupérer une nouvelle instance du dialogFragment en lui passant un paramètre
     * @param dataId L'id de l'élément sur lequel on a cliqué
     * @return (POIOptionsDialog) Le fragment créé
     */
    public static POIOptionsDialog newInstance(int dataId) {

        POIOptionsDialog dialogFragment = new POIOptionsDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("dataId", dataId);

        dialogFragment.setArguments(bundle);

        return dialogFragment;

    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder//.setTitle(R.string.app_name)
                .setItems(R.array.poi_dialog_items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        getActivity().getIntent().putExtra("value", "delete");
                        getActivity().getIntent().putExtra("dataId", getArguments().getInt("dataId"));

                        String[] items = getResources().getStringArray(R.array.poi_dialog_items);

                        // On effectue une action en fonction de l'élément cliqué dans la popup
                        if (items[which].equals(getResources().getString(R.string.delete))) {
                            getTargetFragment().onActivityResult(
                                    getTargetRequestCode(),
                                    Activity.RESULT_OK,
                                    getActivity().getIntent());

                        }
                    }
                });
        return builder.create();
    }

}
