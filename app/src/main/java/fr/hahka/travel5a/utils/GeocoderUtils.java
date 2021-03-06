package fr.hahka.travel5a.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by thibautvirolle on 23/11/15.
 */
public final class GeocoderUtils {

    private GeocoderUtils() { }

    /**
     * Récupère les adresses en fonction de la latitude et de la longitude
     * @param context : context...
     * @param latitude : latitude de la position
     * @param longitude : longitude de la position
     * @return : les adresses
     */
    public static ArrayList<String> getAddressFromLocation(Context context, double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> yourAddresses;
        ArrayList<String> address = new ArrayList<>();
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            yourAddresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (yourAddresses.size() > 0)
            {
                address.add(0, yourAddresses.get(0).getAddressLine(0));
                address.add(1, yourAddresses.get(0).getAddressLine(1));
                address.add(2, yourAddresses.get(0).getAddressLine(2));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }



    /**
     * Récupère une adresse en fonction de la latitude et de la longitude
     * @param context : context...
     * @param latitude : latitude de la position
     * @param longitude : longitude de la position
     * @param format : le format désiré de l'adresse
     * @return : l'adresse (première proposition)
     */
    public static String getFormatedAddressFromLocation(
            Context context,
            double latitude,
            double longitude,
            String format) {

        ArrayList<String> address = getAddressFromLocation(context, latitude, longitude);

        String formatedAddress = "Adresse inconnue";
        /*
            Ad = address
            Ci = City
            Co = Country
         */
        if (address.size() > 1) {
            switch (format) {

                case "Ci, Co":
                    formatedAddress = address.get(1) + ", " + address.get(2);
                    break;

                case "Co":
                    formatedAddress = address.get(2);
                    break;

                default:
                    formatedAddress = address.get(0) + ", " + address.get(1) + ", " + address.get(2);
                    break;


            }
        }

        return formatedAddress;

    }

}
