package com.example.Pfeproject.repository;

import com.example.Pfeproject.entity.SavedCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedCarRepository extends JpaRepository<SavedCar,Long> {

    List<SavedCar> findAllByUserId(Long userId);

    Long countByUserId(Long userId);
}
