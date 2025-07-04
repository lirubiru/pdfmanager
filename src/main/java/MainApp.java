import model.*;
import persistence.LibraryPersistence;
import persistence.EntryPersistence;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class MainApp {
  private static Library currentLibrary;
  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    initializeLibrary();
    showMainMenu();
  }

  private static void initializeLibrary() {
    String savedPath = LibraryPersistence.loadCurrentLibrary();

    if (savedPath != null && new File(savedPath).exists()) {
      currentLibrary = new Library(savedPath);
      System.out.println("Biblioteca carregada: " + savedPath);
    } else {
      createNewLibrary();
    }
  }

  private static void createNewLibrary() {
    System.out.print("Caminho para nova biblioteca: ");
    String path = scanner.nextLine().trim();

    try {
      File dir = new File(path);
      if (!dir.exists())
        dir.mkdirs();

      LibraryPersistence.saveCurrentLibrary(path);
      LibraryPersistence.addLibrary(path);
      currentLibrary = new Library(path);
      System.out.println("Biblioteca criada!");
    } catch (Exception e) {
      System.out.println("Erro: " + e.getMessage());
    }
  }

  private static void showMainMenu() {
    while (true) {
      System.out.println("\n--- MENU PRINCIPAL ---");
      System.out.println("1. Gerenciar Entradas");
      System.out.println("2. Gerenciar Bibliotecas");
      System.out.println("3. Sair");
      System.out.print("Opção: ");

      int option = scanner.nextInt();
      scanner.nextLine(); // Consome newline

      switch (option) {
        case 1:
          manageEntries();
          break;
        case 2:
          manageLibraries();
          break;
        case 3:
          System.exit(0);
        default:
          System.out.println("Opção inválida!");
      }
    }
  }

  private static void manageEntries() {
    while (true) {
      System.out.println("\n--- GERENCIAR ENTRADAS ---");
      System.out.println("1. Adicionar entrada");
      System.out.println("2. Listar entradas");
      System.out.println("3. Buscar entradas");
      System.out.println("4. Editar entrada");
      System.out.println("5. Excluir entrada");
      System.out.println("6. Voltar");
      System.out.print("Opção: ");

      int option = scanner.nextInt();
      scanner.nextLine();

      switch (option) {
        case 1:
          addEntry();
          break;
        case 2:
          listEntries();
          break;
        case 3:
          searchEntries();
          break;
        case 4:
          editEntry();
          break;
        case 5:
          deleteEntry();
          break;
        case 6:
          return;
        default:
          System.out.println("Opção inválida!");
      }
    }
  }

  private static void addEntry() {
    System.out.println("\nTipo de entrada:");
    System.out.println("1. Livro");
    System.out.println("2. Nota de Aula");
    System.out.println("3. Slide");
    System.out.print("Opção: ");
    int type = scanner.nextInt();
    scanner.nextLine();

    System.out.print("Título: ");
    String title = scanner.nextLine();

    System.out.print("Autores (separados por vírgula): ");
    String[] authors = scanner.nextLine().split(",");
    List<Author> authorList = Arrays.stream(authors)
        .map(String::trim)
        .map(Author::new)
        .collect(Collectors.toList());

    System.out.print("Caminho do PDF: ");
    String pdfPath = scanner.nextLine();

    try {
      switch (type) {
        case 1:
          System.out.print("Área de conhecimento: ");
          String area = scanner.nextLine();
          System.out.print("Ano de publicação: ");
          int year = scanner.nextInt();
          scanner.nextLine();
          currentLibrary.addEntry(new Book(title, authorList, pdfPath, area, year));
          break;
        case 2:
          System.out.print("Disciplina: ");
          String discipline = scanner.nextLine();
          currentLibrary.addEntry(new ClassNote(title, authorList, pdfPath, discipline));
          break;
        case 3:
          System.out.print("Disciplina: ");
          String slideDiscipline = scanner.nextLine();
          currentLibrary.addEntry(new Slide(title, authorList, pdfPath, slideDiscipline));
          break;
        default:
          System.out.println("Tipo inválido!");
      }
      System.out.println("Entrada adicionada com sucesso!");
    } catch (Exception e) {
      System.out.println("Erro: " + e.getMessage());
    }
  }

  private static void listEntries() {
    List<LibraryEntry> entries = currentLibrary.getAllEntries();
    if (entries.isEmpty()) {
      System.out.println("\nNenhuma entrada encontrada.");
      return;
    }

    System.out.println("\n--- LISTA DE ENTRADAS ---");
    for (int i = 0; i < entries.size(); i++) {
      LibraryEntry entry = entries.get(i);
      System.out.println((i + 1) + ". " + entry.getTitle() + " (" + entry.getType() + ")");
      System.out.println("   Autores: " + entry.getAuthors().stream()
          .map(Author::getName)
          .collect(Collectors.joining(", ")));
    }
  }

  private static void searchEntries() {
    System.out.print("\nTermo de busca: ");
    String query = scanner.nextLine();

    List<LibraryEntry> results = currentLibrary.searchEntries(query);
    if (results.isEmpty()) {
      System.out.println("Nenhum resultado encontrado.");
      return;
    }

    System.out.println("\n--- RESULTADOS DA BUSCA ---");
    for (int i = 0; i < results.size(); i++) {
      LibraryEntry entry = results.get(i);
      System.out.println((i + 1) + ". " + entry.getTitle() + " (" + entry.getType() + ")");
    }
  }

  private static void editEntry() {
    listEntries();
    System.out.print("\nNúmero da entrada para editar: ");
    int index = scanner.nextInt();
    scanner.nextLine();

    List<LibraryEntry> entries = currentLibrary.getAllEntries();
    if (index < 1 || index > entries.size()) {
      System.out.println("Entrada inválida!");
      return;
    }

    LibraryEntry entry = entries.get(index - 1);

    System.out.println("\nEditando: " + entry.getTitle());
    System.out.print("Novo título (Enter para manter): ");
    String newTitle = scanner.nextLine();
    if (!newTitle.isEmpty())
      entry.setTitle(newTitle);

    System.out.print("Novos autores (separados por vírgula, Enter para manter): ");
    String authorsInput = scanner.nextLine();
    if (!authorsInput.isEmpty()) {
      List<Author> newAuthors = Arrays.stream(authorsInput.split(","))
          .map(String::trim)
          .map(Author::new)
          .collect(Collectors.toList());
      entry.setAuthors(newAuthors);
    }

    System.out.print("Novo caminho do PDF (Enter para manter): ");
    String newPath = scanner.nextLine();
    if (!newPath.isEmpty()) {
      entry.setPdfPath(newPath);
    }

    try {
      currentLibrary.updateEntry(entry);
      System.out.println("Entrada atualizada com sucesso!");
    } catch (Exception e) {
      System.out.println("Erro ao atualizar: " + e.getMessage());
    }
  }

  private static void deleteEntry() {
    listEntries();
    System.out.print("\nNúmero da entrada para excluir: ");
    int index = scanner.nextInt();
    scanner.nextLine();

    List<LibraryEntry> entries = currentLibrary.getAllEntries();
    if (index < 1 || index > entries.size()) {
      System.out.println("Entrada inválida!");
      return;
    }

    LibraryEntry entry = entries.get(index - 1);
    try {
      currentLibrary.deleteEntry(entry.getId());
      System.out.println("Entrada excluída com sucesso!");
    } catch (Exception e) {
      System.out.println("Erro ao excluir: " + e.getMessage());
    }
  }

  private static void manageLibraries() {
    while (true) {
      System.out.println("\n--- GERENCIAR BIBLIOTECAS ---");
      System.out.println("1. Criar nova biblioteca");
      System.out.println("2. Alternar biblioteca");
      System.out.println("3. Excluir biblioteca");
      System.out.println("4. Listar bibliotecas");
      System.out.println("5. Voltar");
      System.out.print("Opção: ");

      int option = scanner.nextInt();
      scanner.nextLine();

      switch (option) {
        case 1:
          createNewLibrary();
          break;
        case 2:
          switchLibrary();
          break;
        case 3:
          deleteLibrary();
          break;
        case 4:
          listLibraries();
          break;
        case 5:
          return;
        default:
          System.out.println("Opção inválida!");
      }
    }
  }

  private static void switchLibrary() {
    listLibraries();
    List<String> libraries = LibraryPersistence.getAllLibraries();

    System.out.print("\nNúmero da biblioteca para carregar: ");
    int index = scanner.nextInt();
    scanner.nextLine();

    if (index < 1 || index > libraries.size()) {
      System.out.println("Biblioteca inválida!");
      return;
    }

    String path = libraries.get(index - 1);
    try {
      LibraryPersistence.saveCurrentLibrary(path);
      currentLibrary = new Library(path);
      System.out.println("Biblioteca carregada: " + path);
    } catch (Exception e) {
      System.out.println("Erro: " + e.getMessage());
    }
  }

  private static void deleteLibrary() {
    listLibraries();
    List<String> libraries = LibraryPersistence.getAllLibraries();

    System.out.print("\nNúmero da biblioteca para excluir: ");
    int index = scanner.nextInt();
    scanner.nextLine();

    if (index < 1 || index > libraries.size()) {
      System.out.println("Biblioteca inválida!");
      return;
    }

    String path = libraries.get(index - 1);
    try {
      if (new Library(path).deleteLibrary()) {
        LibraryPersistence.removeLibrary(path);
        if (currentLibrary.getPath().equals(path)) {
          currentLibrary = null;
          initializeLibrary();
        }
        System.out.println("Biblioteca excluída com sucesso!");
      }
    } catch (Exception e) {
      System.out.println("Erro ao excluir: " + e.getMessage());
    }
  }

  private static void listLibraries() {
    List<String> libraries = LibraryPersistence.getAllLibraries();
    if (libraries.isEmpty()) {
      System.out.println("\nNenhuma biblioteca encontrada.");
      return;
    }

    String currentPath = currentLibrary != null ? currentLibrary.getPath() : "";

    System.out.println("\n--- BIBLIOTECAS DISPONÍVEIS ---");
    for (int i = 0; i < libraries.size(); i++) {
      String marker = libraries.get(i).equals(currentPath) ? " * " : "   ";
      System.out.println((i + 1) + "." + marker + libraries.get(i));
    }
  }
}