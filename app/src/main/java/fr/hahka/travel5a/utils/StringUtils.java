package fr.hahka.travel5a.utils;

/**
 * Created by thibautvirolle on 11/11/15.
 */
public final class StringUtils {

    private StringUtils() { }

    /**
     * Renvoie une url permettant d'accéder à un ftp avec un identifiant et un mot de passe
     * @param user le nom d'utilisateur
     * @param password le mot de passe de l'utilisateur
     * @param host l'adresse du serveur auquel on veut accéder
     * @param path le chemin de la ressource à laquelle on veut accéder
     * @return (String) l'url construit correctement
     */
    public static String getFtpUrl(String user, String password, String host, String path) {
        return "ftp://" + user + ":" + password + "@" + host + "/" + path;
    }


}
