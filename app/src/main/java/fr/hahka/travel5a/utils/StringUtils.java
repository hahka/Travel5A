package fr.hahka.travel5a.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    /**
     * Fonction pour transformer une String grâce à l'algorythme de hachage MD5
     * @param s La chaîne de caractères à encrypter
     * @return La chaîne encryptée
     */
    public static String md5(String s) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(), 0, s.length());
            return new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
