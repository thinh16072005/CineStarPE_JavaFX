package org.de190006.cinestar.cinestarpe_springboot.repositories;

import org.de190006.cinestar.cinestarpe_springboot.models.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Integer> {

}
