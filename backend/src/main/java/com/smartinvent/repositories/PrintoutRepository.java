package com.smartinvent.repositories;


import com.smartinvent.models.Printout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrintoutRepository extends JpaRepository<Printout, Long> {
}
