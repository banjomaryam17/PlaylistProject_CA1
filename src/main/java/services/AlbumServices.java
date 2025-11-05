package services;

import persistence.ArtistDao;

public class AlbumServices {
    private ArtistDao artistDao;

    public AlbumServices(ArtistDao artistDao){
        this.artistDao = artistDao;
    }
}
