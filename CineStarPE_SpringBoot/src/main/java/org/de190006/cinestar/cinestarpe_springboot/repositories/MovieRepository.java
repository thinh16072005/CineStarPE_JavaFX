package org.de190006.cinestar.cinestarpe_springboot.repositories;

import org.de190006.cinestar.cinestarpe_springboot.enums.Status;
import org.de190006.cinestar.cinestarpe_springboot.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> findByMovieNameContainingIgnoreCaseOrDirector_DirectorNameContainingIgnoreCase(String name, String director);
    List<Movie> findByMovieNameContainingIgnoreCase(String movieName);
    List<Movie> findByDirector_DirectorNameContainingIgnoreCase(String directorName);
    List<Movie> findByActorContainingIgnoreCase(String actor);
    List<Movie> findByStatus(Status status);
}
