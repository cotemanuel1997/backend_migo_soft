
package com.conmigo.backend_migo_soft.login.repository;

import com.conmigo.backend_migo_soft.login.models.Pieza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PiezaRepository extends JpaRepository<Pieza, Long>{
    
}
