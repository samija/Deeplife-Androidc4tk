package deeplife.gcme.com.deeplife.Models;

/**
 * Created by rog on 11/7/2015.
 */
public class Question {

    String id;
    String catagory;
    String description;


    public Question(String id, String catagory, String desc){
        this.id = id;
        this.catagory = catagory;
        this.description = desc;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
