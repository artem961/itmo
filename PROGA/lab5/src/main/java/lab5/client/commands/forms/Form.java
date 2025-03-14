package lab5.client.commands.forms;

import lab5.client.console.Console;

import java.io.IOException;

public abstract class Form {
    protected final Console console;

    public Form(Console console) {
        this.console = console;
    }

    public abstract Object run();
}
