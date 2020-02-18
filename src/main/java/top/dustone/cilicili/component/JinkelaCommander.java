package top.dustone.cilicili.component;

import java.util.Scanner;

public class JinkelaCommander {
    public static final String LINE_MARKER = "";
    public static final String END_TOKEN = "~~~";
    public String commandHeader = "DST: ";
    private StringBuffer inputPool;
    private Scanner scanner;
    private CommandRouter commandRouter;
    private static JinkelaCommander jinkelaCommander = new JinkelaCommander();

    private JinkelaCommander() {
        inputPool = new StringBuffer();
        scanner = new Scanner(System.in);
    }

    public static JinkelaCommander getInstance() {
        return jinkelaCommander;
    }

    public void println(String text) {
        System.out.println(text);
    }

    public void print(String text) {
        System.out.print(text);
    }

    public String readFromWindow() {
        inputPool.setLength(0);
        print(commandHeader);
        while (true) {
            String line = scanner.nextLine();
            if (line.indexOf(END_TOKEN) == -1) {
                inputPool.append(line + "\n");
                print(LINE_MARKER);
            } else {
                line = line.replace(END_TOKEN, "");
                inputPool.append(line);
                break;
            }
        }
        return inputPool.toString();
    }

    public void readCommand() {
        commandRouter.route(readFromWindow());
    }

    public CommandRouter getCommandRouter() {
        return commandRouter;
    }

    public void setCommandRouter(CommandRouter commandRouter) {
        this.commandRouter = commandRouter;
    }

    public interface CommandRouter {
        void route(String command);
    }

    public static void main(String[] args) {
        JinkelaCommander jinkelaCommander = JinkelaCommander.getInstance();
        while (true) {
            String input = jinkelaCommander.readFromWindow();
            jinkelaCommander.println("out:" + input);
        }
    }
}
