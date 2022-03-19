public class CommandControl implements Command{
    private static CommandControl uniqueInstance;
    private Command command;
    private CommandControl(){}
    public static CommandControl getInstance()
    {
        if (uniqueInstance == null) {
            uniqueInstance = new CommandControl();
        }
        return uniqueInstance;
    }
    public void setCommand(Command command)
    {
        this.command = command;
    }
    public void execute()
    {
        command.execute();
    }
}
