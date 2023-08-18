package oeg.crec.model;

/**
 * Skill
 * @author victor
 */
public class Skill {
    public String id = "";
    public String infoURL = "";
    public String name = "";
    public String description = "";

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the infoURL
     */
    public String getInfoURL() {
        return infoURL;
    }

    /**
     * @param infoURL the infoURL to set
     */
    public void setInfoURL(String infoURL) {
        this.infoURL = infoURL;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
