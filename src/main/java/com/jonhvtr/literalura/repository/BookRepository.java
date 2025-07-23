package com.jonhvtr.literalura.repository;

import com.jonhvtr.literalura.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByLanguagesContainingIgnoreCase(String sigla);

    List<Book> findTop10ByOrderByDownloadDesc();
}
