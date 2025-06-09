package com.pdfmanager.cli;

public enum Action {
  CREATE("create"),
  LIST("list"),
  UPDATE("update"),
  DELETE("delete");

  private final String label;

  Action(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static Action fromLabel(String label) {
    for (Action a : values()) {
      if (a.label.equals(label)) {
        return a;
      }
    }
    throw new IllegalArgumentException("Action inválida: " + label);
  }
}