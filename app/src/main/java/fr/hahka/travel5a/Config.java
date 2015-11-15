package fr.hahka.travel5a;

/**
 * Created by thibautvirolle on 08/11/15.
 */
public final class Config {

    /**
     * Adresse de l'hote pour récupérer les fichiers par FTP
     */
    public static final String HOST = "ftp.cluster017.ovh.net";

    /**
     * Adresse du fichier utilisé pour uploader les fichiers sur le serveur
     */
    public static final String UPLOAD_URL = "http://thibautvirolle.fr/projet5atravel/uploads.php";


    /**
     * Nom du dossier dans lequel les fichiers (photos/vidéos/enregistrement sonores?)
     * vont être enregistrer. On pourra ainsi les afficher sans les retélécharger
     */
    public static final String IMAGE_DIRECTORY_NAME = "Travel";


    /**
     * Code utilisé pour créer un fichier lorsque celui est une image
     */
    public static final int MEDIA_TYPE_IMAGE = 1;

    /**
     * Code utilisé pour créer un fichier lorsque celui est une vidéo
     */
    public static final int MEDIA_TYPE_VIDEO = 2;


    /* Codes utilisés lors des startActivityForResult */

    /**
     * Code de l'intent camera pour prendre des photos
     */
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    /**
     * Code de l'intent camera pour prendre des vidéos
     */
    public static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    /**
     * Code de l'intent pour poster une nouvelle publication
     */
    public static final int NEW_PUBLICATION_CODE = 300;

    /**
     * Version de la base de donnée.
     * A changer lors de modifications de la structure de la base
     */
    public static final int DATABASE_VERSION = 1;

    /**
     * Classe utilitaire : pas de constructeur par défaut ou public
     */
    private Config() { }
}