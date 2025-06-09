package com.pdfmanager.core.services;

import java.util.List;

import com.pdfmanager.core.entities.Library;
import com.pdfmanager.core.repositories.LibraryRepository;

public class LibraryService {
  private final LibraryRepository libraryRepository;

  public LibraryService(LibraryRepository libraryRepository) {
    this.libraryRepository = libraryRepository;
  }

  public List<Library> getAll() {
    return this.libraryRepository.getAll();
  }

  public void create(String name, String path) {
    // Checar se path leva a um diretorio existente -> throw Exception

    // Checar se path já foi registrado numa outra biblioteca -> throw Exception

    // Checar se nome já foi registrado numa outra biblioteca -> throw Exception
    this.libraryRepository.create(new Library(name, path));
  }

  public void updateById(int id, String name, String path) {
    Library library = new Library(id, name, path);
    this.libraryRepository.updateById(id, library);
  }

  public void deleteById(int id) {
    this.libraryRepository.deleteById(id);
  }

}
