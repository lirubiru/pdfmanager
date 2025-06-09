package com.pdfmanager.cli;

public enum Target {
  LIBRARY("library"),
  BOOK("book"),
  NOTE("note"),
  SLIDE("slide");

  private final String label;

  Target(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static Target fromLabel(String label) {
    for (Target t : values()) {
      if (t.label.equals(label)) {
        return t;
      }
    }
    throw new IllegalArgumentException("Target inválido: " + label);
  }
}
