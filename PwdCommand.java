import java.io.*;

public class PwdCommand implements Command{
    private final TipFisier fisier;
    private final PrintStream output;
    public PwdCommand(PrintStream output, TipFisier fisier)
    {
        this.fisier = fisier;
        this.output = output;
    }
    public void execute()
    {
        fisier.pwd(output);
    }
}
