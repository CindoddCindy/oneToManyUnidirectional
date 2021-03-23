package com.onetomanyunidirectional.onetomanyunidirectional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ComicRepository extends JpaRepository<Comics,Integer> {

    Page<Comics> findByLibraryId(Integer libraryId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comics c WHERE c.library.id = ?1")
    void deleteByLibraryId(Integer libraryId);
}
