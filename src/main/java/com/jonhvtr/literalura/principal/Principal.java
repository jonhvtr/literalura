package com.jonhvtr.literalura.principal;

import com.jonhvtr.literalura.domain.*;
import com.jonhvtr.literalura.domain.dto.AuthorDTO;
import com.jonhvtr.literalura.domain.dto.BookDTO;
import com.jonhvtr.literalura.domain.dto.DataBookDTO;
import com.jonhvtr.literalura.repository.AuthorRepository;
import com.jonhvtr.literalura.repository.BookRepository;
import com.jonhvtr.literalura.service.ConsumingData;
import com.jonhvtr.literalura.service.ConvertData;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final Scanner scan = new Scanner(System.in);
    private final ConsumingData consumingData = new ConsumingData();
    private final ConvertData convertData = new ConvertData();
    private final String ADDRESS = "https://gutendex.com/books?search=";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Principal(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void exibeMenu() {
        int opcao = -1;
        while (opcao != 0) {

            String menu = """
                    -------------
                    Escolha o número de sua opção:
                    
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    6 - Top 10 Livros
                    
                    0 - Sair
                    """;

            System.out.println(menu);
            System.out.print("Opção: ");
            String input = scan.nextLine();

            try {
                opcao = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
                continue;
            }

            switch (opcao) {
                case 1 -> buscarLivro();
                case 2 -> listarLivros();
                case 3 -> listarAutores();
                case 4 -> listarAutoresPorAno();
                case 5 -> listarLivrosPorIdioma();
                case 6 -> livrosMaisBaixados();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida");
            }
        }
    }


    private void buscarLivro() {
        System.out.println("Digite o nome de um livro:");
        String nameBook = scan.nextLine();
        String query = URLEncoder.encode(nameBook, StandardCharsets.UTF_8);
        String json = consumingData.getData(ADDRESS + query);

        if (json == null || json.isBlank()) {
            System.out.println("Nenhuma resposta recebida da API.");
            return;
        }

        DataBookDTO dataBookDTO = convertData.getData(json, DataBookDTO.class);
        List<BookDTO> bookDTOList = dataBookDTO.results();

        for (BookDTO bookDTO : bookDTOList) {
            System.out.println("---- LIVRO ----");
            System.out.println("Título: " + bookDTO.title());
            System.out.println("Autor: " + bookDTO.authors().stream().map(AuthorDTO::name).collect(Collectors.joining()));
            System.out.println("Idioma: " + bookDTO.languages());
            System.out.println("Número de Downloads: " + bookDTO.download());
            System.out.println("------------");
        }

        List<Book> book = bookDTOList.stream()
                .map(bookDTO -> {
                    AuthorDTO authorDTO = bookDTO.authors().isEmpty() ? null : bookDTO.authors().getFirst();

                    Author author = null;
                    if (authorDTO != null) {
                        Optional<Author> optionalAuthors = authorRepository.findByName(authorDTO.name());

                        author = optionalAuthors.orElseGet(() -> {
                            Author newAuthor = new Author(
                                    authorDTO.name(),
                                    authorDTO.birthYear(),
                                    authorDTO.deathYear()
                            );
                            return authorRepository.save(newAuthor);
                        });
                    }

                    return new Book(
                            bookDTO.title(),
                            author,
                            bookDTO.languages().isEmpty() ? "N/A" : bookDTO.languages().getFirst(),
                            bookDTO.download()
                    );
                })
                .toList();

        bookRepository.saveAll(book);
    }

    private void listarLivros() {
        bookRepository.findAll().forEach(book -> {
            System.out.println("---- LIVRO ----");
            System.out.println("Título: " + book.getTitle());
            System.out.println("Autor: " + book.getAuthorOptional()
                    .map(Author::getName).orElse("Autor Desconhecido"));
            System.out.println("Idioma: " + book.getLanguages());
            System.out.println("Número de Downloads: " + book.getDownload());
            System.out.println("------------");
        });
    }

    private void listarAutores() {
        authorRepository.findAll().forEach(author -> {
            System.out.println("---- AUTORES ----");
            System.out.println("Nome: " + author.getName());
            System.out.println("Data de Nascimento: " + author.getBirthYear());
            System.out.println("Data de Falecimento: " + author.getDeathYear());
            System.out.println("Livros: " + author.getBooks());
            System.out.println("------------");
        });
    }

    private void listarAutoresPorAno() {
        System.out.println("Escolhe o número de sua opção:");
        int year = Integer.parseInt(scan.nextLine());
        List<Author> authorList = authorRepository.searchByDeathYear(year);

        authorList.forEach(author -> {
            System.out.println("---- AUTORES ----");
            System.out.println("Nome: " + author.getName());
            System.out.println("Data de Nascimento: " + author.getBirthYear());
            System.out.println("Data de Falecimento: " + author.getDeathYear());
            System.out.println("Livros: " + author.getBooks());
            System.out.println("------------");
        });
    }

    private void listarLivrosPorIdioma() {
        System.out.println("""
                Insira o idioma para realizar a busca:
                es- espanhol
                en- inglês
                fr- francês
                pt- português
                """);
        var sigla = scan.nextLine();
        List<Book> books = bookRepository.findByLanguagesContainingIgnoreCase(sigla);

        books.forEach(book -> {
            System.out.println("---- LIVRO ----");
            System.out.println("Título: " + book.getTitle());
            System.out.println("Autor: " + book.getAuthorOptional()
                    .map(Author::getName).orElse("Autor Desconhecido"));
            System.out.println("Idioma: " + book.getLanguages());
            System.out.println("Número de Downloads: " + book.getDownload());
            System.out.println("------------");
        });
    }

    private void livrosMaisBaixados() {
        System.out.println("---- Top 10 Livros Mais Baixados ----");
        List<Book> books = bookRepository.findTop10ByOrderByDownloadDesc();
        books.forEach(book -> {
            System.out.println("---- LIVRO ----");
            System.out.println("Título: " + book.getTitle());
            System.out.println("Autor: " + book.getAuthorOptional()
                    .map(Author::getName).orElse("Autor Desconhecido"));
            System.out.println("Idioma: " + book.getLanguages());
            System.out.println("Número de Downloads: " + book.getDownload());
            System.out.println("------------");
        });
    }

}
