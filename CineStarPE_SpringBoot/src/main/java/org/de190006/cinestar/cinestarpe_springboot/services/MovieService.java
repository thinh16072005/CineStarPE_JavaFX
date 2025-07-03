package org.de190006.cinestar.cinestarpe_springboot.services;

import org.de190006.cinestar.cinestarpe_springboot.enums.Status;
import org.de190006.cinestar.cinestarpe_springboot.models.Movie;
import org.de190006.cinestar.cinestarpe_springboot.repositories.DirectorRepository;
import org.de190006.cinestar.cinestarpe_springboot.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private DirectorRepository directorRepository;


    public List<Movie> getAllMovies() {
        return movieRepository.findAll(Sort.by(Sort.Direction.DESC, "movieID"));
    }
    

    public Optional<Movie> getMovieById(int id) {
        return movieRepository.findById(id);
    }
    
 
    public List<Movie> searchMovies(String keyword) {
        return movieRepository.findByMovieNameContainingIgnoreCaseOrDirector_DirectorNameContainingIgnoreCase(keyword, keyword);
    }
    
    public List<Movie> searchByMovieName(String movieName) {
        return movieRepository.findByMovieNameContainingIgnoreCase(movieName);
    }
    
    public List<Movie> searchByDirector(String directorName) {
        return movieRepository.findByDirector_DirectorNameContainingIgnoreCase(directorName);
    }
    
    public List<Movie> searchByActor(String actor) {
        return movieRepository.findByActorContainingIgnoreCase(actor);
    }
    
    public List<Movie> searchByStatus(Status status) {
        return movieRepository.findByStatus(status);
    }

    public String addMovie(String movieName, int duration, String actor, Status status, int directorId) {
       if (!isValidMovieName(movieName)) {
            return "Movie name must start with uppercase letters and be less than 20 characters";
        }
        if (!isValidDuration(duration)) {
            return "Duration must be between 75 and 120 minutes";
        }
        Movie movie = new Movie();
        movie.setMovieName(movieName.trim());
        movie.setDuration(duration);
        movie.setActor(actor.trim());
        movie.setStatus(status);
        movie.setDirector(directorRepository.findById(directorId).orElse(null));
        movieRepository.save(movie);
        
        return null;
    }
    

    public String updateMovie(int id, String movieName, int duration, String actor, Status status, int directorId) {

        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isEmpty()) {
            return "Movie not found";
        }
        
        if (!isValidMovieName(movieName)) {
            return "Movie name must start with uppercase letters and be less than 20 characters";
        }
        if (!isValidDuration(duration)) {
            return "Duration must be between 75 and 120 minutes";
        }

        Movie movie = optionalMovie.get();
        movie.setMovieName(movieName.trim());
        movie.setDuration(duration);
        movie.setActor(actor.trim());
        movie.setStatus(status);
        movie.setDirector(directorRepository.findById(directorId).orElse(null));
        movieRepository.save(movie);
        
        return null; 
    }
    

    public void deleteMovie(int id) {
        movieRepository.deleteById(id);
    }
    

    public boolean isValidMovieName(String movieName) {
        return movieName.matches("([A-Z][a-z]+\\s?)+") && movieName.length() <= 20;
    }

    public boolean isValidDuration(int duration) {
        return duration >= 75 && duration <= 120;
    }
}
