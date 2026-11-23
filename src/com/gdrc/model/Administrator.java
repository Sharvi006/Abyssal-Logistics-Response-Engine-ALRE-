package com.gdrc.model;

public class Administrator extends Person {
    private static final long serialVersionUID = 1L;
    private String commandDivision;

    public Administrator(String id, String name, int age, String commandDivision) {
        super(id, name, age);
        this.commandDivision = commandDivision;
    }

    public String getCommandDivision() { return commandDivision; }

    @Override
    public String getRoleSummary() {
        return "Administrator - " + commandDivision;
    }
}