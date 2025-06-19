package model;

import persistence.EntryPersistence;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Library {
  private String path;
  private List<LibraryEntry> entries = new ArrayList<>();

  public Library(String path) {
    this.path = path;
    new File(path).mkdirs();
    loadEntries();
  }

  private void loadEntries() {
    this.entries = EntryPersistence.loadEntries(path);
  }

  public void addEntry(LibraryEntry entry) throws Exception {
    entry.saveToLibrary(path);
    entries.add(entry);
    EntryPersistence.saveEntry(path, entry);
  }

  public void deleteEntry(UUID id) throws Exception {
    LibraryEntry entry = getEntryById(id);
    if (entry != null) {
      entry.deleteFile();
      entries.remove(entry);
      EntryPersistence.deleteEntry(path, id);
    }
  }

  public void updateEntry(LibraryEntry updatedEntry) throws Exception {
    LibraryEntry existingEntry = getEntryById(updatedEntry.getId());
    if (existingEntry != null) {
      // Atualiza os campos
      existingEntry.setTitle(updatedEntry.getTitle());
      existingEntry.setAuthors(updatedEntry.getAuthors());

      // Atualiza arquivo PDF se necessário
      if (!existingEntry.originalPdfPath.equals(updatedEntry.originalPdfPath)) {
        existingEntry.deleteFile();
        existingEntry.originalPdfPath = updatedEntry.originalPdfPath;
        existingEntry.saveToLibrary(path);
      }

      // Salva alterações
      EntryPersistence.saveEntry(path, existingEntry);
    }
  }

  public LibraryEntry getEntryById(UUID id) {
    return entries.stream()
        .filter(e -> e.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  public List<LibraryEntry> searchEntries(String query) {
    return entries.stream()
        .filter(e -> e.getTitle().contains(query) ||
            e.getAuthors().stream().anyMatch(a -> a.getName().contains(query)) ||
            e.getCategory().contains(query))
        .collect(Collectors.toList());
  }

  public List<LibraryEntry> getAllEntries() {
    return new ArrayList<>(entries);
  }

  public String getPath() {
    return path;
  }

  public boolean deleteLibrary() {
    EntryPersistence.deleteAllEntries(path);
    return deleteDirectory(new File(path));
  }

  private boolean deleteDirectory(File directory) {
    if (directory.exists()) {
      File[] files = directory.listFiles();
      if (files != null) {
        for (File file : files) {
          if (file.isDirectory()) {
            deleteDirectory(file);
          } else {
            file.delete();
          }
        }
      }
    }
    return directory.delete();
  }
}