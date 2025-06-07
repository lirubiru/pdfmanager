package com.pdfmanager.cli;

import java.util.List;

import com.pdfmanager.core.entities.Library;
import com.pdfmanager.core.services.LibraryService;

public class Cli {
    private final LibraryService libraryService;

    public Cli(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    private boolean shouldCreateLibraryFirst(List<Library> libraries, String action, String target) {
        return libraries.isEmpty() && !action.equals("create") && !target.equals("library");
    }

    private void help() {
        // Adicionar mensagem de help
        System.out.println("Help");
    }

    public void readArgs(String[] args) {
        try {
            if(args.length <= 0) {
                help();
                return;
            }

            List<Library> libraries = this.libraryService.getAll();

            String action;
            String target;
            try {
                action = args[0];
                target = args[1];
            } catch (IndexOutOfBoundsException e) {
                System.err.println(e.getMessage());
                return;
            }

            if(this.shouldCreateLibraryFirst(libraries, action, target)) {
                System.err.println(">> Você ainda não tem uma biblioteca\n >> Crie sua primeira biblioteca com 'pdfmanager create library'");
                return;
            }

            // TODO: Transformar action em Enum, adicionar todas as opções de action
            switch(action) {
                case "create" -> {
                    if (args.length <= 1) {
                        System.out.println(">> Ação 'criar' requer um argumento\n >> Ex: pdfmanager create [target]");
                        return;
                    }
                    // Checar valor de 'target' para decidir o que criar
                }
                case "list" -> {

                }
                default -> System.out.println("Comando \"" + action + "\" não existe");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
