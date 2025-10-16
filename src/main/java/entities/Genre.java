package entities;
import java.util.Objects;

//@author Maryam


public class Genre {
//CREATE TABLE genres (
//                        genreID INT(11) NOT NULL AUTO_INCREMENT,
//                        genreName VARCHAR(50) NOT NULL,
//                        description VARCHAR(255) DEFAULT NULL,
//                        PRIMARY KEY (genreID)
//);
    private int genreID;
    private String genreName;
    private String description;

    public Genre(){

    }

    public Genre(int genreID, String genreName, String description){
        this.genreID = genreID;
        this.genreName = genreName;
        this.description = description;
    }
    public int getGenreID(){
        return genreID;
    }
    public String getGenreName(){
        return genreName;
    }
    public String getDescription(){
        return description;
    }
    public void setGenreID(int genreID){
        this.genreID = genreID;
    }

    public void setGenreName(String genreName){
        this.genreName = genreName;
    }
    public void setDescription(String description){
        this.description= description;
    }

    /**
     * Compares this Genre with another object for equality.
     * Two genres are equal if they have the same ID, name, and description.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o){
        if (this == o){
            return true;
        }
        if(o == null){
            return false;
        }
        if(getClass() != o.getClass()){
            return false;
        }
        Genre other = (Genre) o;
        return genreID == other.genreID &&
                Objects.equals(genreName, other.genreName) &&
                Objects.equals(description,other.description);
    }
    @Override
    public int hashCode(){
        return Objects.hash(genreID,genreName, description);

    }
    @Override
    public String toString(){
        return "Genre{" + "Genre ID: "+ genreID +" ,Genre Name: "+ genreName + " ,Genre Description: "+ description + "}";
    }


}
