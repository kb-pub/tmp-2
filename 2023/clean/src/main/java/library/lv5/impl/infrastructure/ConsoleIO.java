package library.lv5.impl.infrastructure;

import library.lv4.controller.console.IO;

import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleIO implements IO {
    private final Scanner in = new Scanner(System.in);
    private final PrintStream out = System.out;

    @Override
    public String read() {
//        return System.console().readLine();
        return in.nextLine();
    }

    @Override
    public void print(Object o) {
        out.print(o);
    }

    @Override
    public void println(Object o) {
        out.println(o);
    }
}
