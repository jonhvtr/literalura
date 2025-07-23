package com.jonhvtr.literalura.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    private Author author;

    private String languages;

    private double download;

    public Book(String title, Author author, String languages, double download) {
        this.title = title;
        this.author = author;
        this.languages = languages;
        this.download = download;
    }

    public Optional<Author> getAuthorOptional() {
        return Optional.ofNullable(author);
    }

    @Override
    public String toString() {
        return title;
    }
}
