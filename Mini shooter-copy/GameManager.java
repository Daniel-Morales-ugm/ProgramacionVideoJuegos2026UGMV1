import java.util.*;

public class GameManager
{
    private static GameManager instancia;
    private int puntos = 0;
    private boolean gameOver = false;

    private List<Observador> observadores = new ArrayList<>();


    private GameManager() {}


    public static GameManager getInstancia()
    {
        if (instancia == null) {
            instancia = new GameManager();
        }

        return instancia;
    }


    public void suscribir(Observador o)
    {
        observadores.add(o);
    }


    public void sumarPuntos(int p)
    {
        puntos += p;
        notificar();
    }


    public void declararGameOver()
    {
        gameOver = true;
    }


    public boolean esGameOver()
    {
        return gameOver;
    }


    public void reiniciar()
    {
        puntos = 0;
        gameOver = false;
        observadores.clear();
    }


    public int getPuntos()
    {
        return puntos;
    }


    private void notificar()
    {
        for (Observador o : observadores) {
            o.actualizar(puntos);
        }
    }
}