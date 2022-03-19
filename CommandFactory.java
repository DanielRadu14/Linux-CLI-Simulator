import java.util.*;
import java.io.*;

public class CommandFactory{
    private static CommandFactory uniqueInstance;
    private CommandFactory(){}
    public static CommandFactory getInstance()
    {
        if (uniqueInstance == null) {
            uniqueInstance = new CommandFactory();
        }
        return uniqueInstance;
    }
    public Command createCommand(String type, Scanner input, PrintStream output, PrintStream errors, SistemDeFisiere fileSystem)
    {
        Command command;
        switch (type) {
            case "ls":
                command = new LsCommand(input, output, errors, fileSystem);
                break;
            case "pwd":
                command = new PwdCommand(output, fileSystem.getFolderCurent());
                break;
            case "mkdir":
                command = new MkDirCommand(input, errors, fileSystem);
                break;
            case "cd":
                command = new CdCommand(input, errors, fileSystem);
                break;
            case "cp":
                command = new CpCommand(input, errors, fileSystem);
                break;
            case "mv":
                command = new MvCommand(input, errors, fileSystem);
                break;
            case "rm":
                command = new RmCommand(input, errors, fileSystem);
                break;
            default:
                command = new TouchCommand(input, errors, fileSystem);
                break;
        }
        return command;
    }
}
