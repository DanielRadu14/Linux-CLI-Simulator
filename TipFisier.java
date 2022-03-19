import java.io.*;

public interface TipFisier{
    public void ls(PrintStream output, PrintStream errors, SistemDeFisiere fileSystem);
    public void lsR(PrintStream output, PrintStream errors, SistemDeFisiere fileSystem);
    public void pwd(PrintStream output);
    public String getNume();
    public void setParinte(Folder parinte);
    public Folder getParinte();
}
