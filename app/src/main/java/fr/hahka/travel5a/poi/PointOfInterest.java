package fr.hahka.travel5a.poi;

/**
 * Created by thibautvirolle on 13/11/15.
 */
public class PointOfInterest {

    private int id;

    private int userId;

    private String description;

    private String imagePath;

    private String soundPath;

    private double latitude;

    private double longitude;


    /**
     * Constructeur par défaut
     */
    public PointOfInterest() { }


    /**
     * Constructeur complet
     * @param id : id de la ressource (poi)
     * @param userId : id de l'utilisateur ayant posté cette publication
     * @param description : champ de texte accompagnant la publication
     * @param imagePath : chemin pour télécharger l'image sur le ftp
     * @param soundPath : chemin pour télécharger lenregistrement sonore sur le ftp
     * @param latitude : latitude de la publication
     * @param longitude : longitude de la publication
     */
    public PointOfInterest(int id, int userId, String description,
                           String imagePath, String soundPath,
                           double latitude, double longitude) {

        setId(id);
        setUserId(userId);
        setDescription(description);

        setImagePath(imagePath);
        setSoundPath(soundPath);

        setLatitude(latitude);
        setLongitude(longitude);

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSoundPath() {
        return soundPath;
    }

    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
