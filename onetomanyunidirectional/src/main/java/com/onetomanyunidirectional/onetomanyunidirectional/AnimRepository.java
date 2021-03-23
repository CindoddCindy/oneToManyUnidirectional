package com.onetomanyunidirectional.onetomanyunidirectional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface AnimRepository extends JpaRepository<Anim,Integer> {

    Page<Anim> findByBookId(Integer bookId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Anim a WHERE a.book.id = ?1")
    void deleteByBookId(Integer bookId);
}
