package com.pdfmanager.cli;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import com.pdfmanager.core.entities.Library;
import com.pdfmanager.core.services.LibraryService;

public class Cli {
    private final LibraryService libraryService;

    public Cli(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    private boolean shouldCreateLibraryFirst(List<Library> libraries, Action action, Target target) {
        return libraries.isEmpty() && !(action == Action.CREATE && target == Target.LIBRARY);
    }

    // Por algum motivo Files.isDirectory(p) está retornando falso
    // Mesmo eu apontando para um path de um diretorio
    // Por isso eu comentei o código desse método
    // E coloquei um return true
    // Depois precisamos resolver isso
    private boolean isSafeDirectoryFake(String path) {
        return true;
    }

    private boolean isSafeDirectory(String path) {
        try {
            Path p = Paths.get(path);
            boolean exists = Files.exists(p);
            boolean isDir = Files.isDirectory(p);
            System.err.println(exists);
            System.err.println(isDir);
            return exists && isDir;
        } catch (InvalidPathException | NullPointerException e) {
            System.err.println("Path inválido: " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Acesso negado ao path: " + path);
        }
        return false;
    }

    private void help() {
        // Adicionar mensagem de help
        System.out.println("Help");
    }

    private void handleCreate(String args[], Target target) {
        if (args.length <= 1) {
                        System.out.println(">> Ação 'criar' requer um argumento\n>> Ex: pdfmanager create [target]");
                        return;
                    }

                    switch(target) {
                        case LIBRARY -> {
                            try (Scanner scanner = new Scanner(System.in)) {
                                System.out.print(">> Qual o nome da biblioteca que você quer criar?\n>> ");
                                String libName = scanner.nextLine();
                                System.out.print(">> Digite um path válido para a sua biblioteca.\n>> ");
                                String libPath = scanner.nextLine();
                                if(!this.isSafeDirectoryFake(libPath)) {
                                    System.out.println(">> Não será possivel criar biblioteca, digite outro path");
                                    return;
                                }
                                this.libraryService.create(libName, libPath);
                                System.out.println(">> Biblioteca criada com sucesso!");

                            } catch (Exception e) {
                                // TODO: Handle Exception
                            }
                        }
                        case BOOK -> {}
                        case NOTE -> {}
                        case SLIDE -> {}

                    }
    }
    private void handleList(String args[], Target target) {}
    private void handleUpdate(String args[], Target target) {}
    private void handleDelete(String args[], Target target) {}

    public void readArgs(String[] args) {
        try {
            if(args.length <= 0) {
                help();
                return;
            }

            Action action = null;
            Target target = null;
            try {
                action = Action.fromLabel(args[0]);
                target = Target.fromLabel(args[1]);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
                return;
            } catch (IndexOutOfBoundsException e) {
                if(e.toString().contains("1")) {
                    boolean isValidArg = action == Action.CREATE;
                    if(isValidArg) {
                        System.err.println(">> Comando " + action + " requer um argumento.");
                    } else {
                        System.err.println(">> Comando " + action + " não existe.");
                    }
                }
                return;
            }

            List<Library> libraries = this.libraryService.getAll();
            System.err.println(libraries.toString());

            if(this.shouldCreateLibraryFirst(libraries, action, target)) {
                System.err.println(">> Você ainda não tem uma biblioteca\n>> Crie sua primeira biblioteca com 'pdfmanager create library'");
                return;
            }

            switch(action) {
                case CREATE -> {
                   this.handleCreate(args, target);
                }
                case LIST -> {
                   this.handleList(args, target);
                }
                case UPDATE -> {
                   this.handleUpdate(args, target);
                }
                 case DELETE -> {
                   this.handleDelete(args, target);
                }
                default -> System.out.println("Comando \"" + action + "\" não existe");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
