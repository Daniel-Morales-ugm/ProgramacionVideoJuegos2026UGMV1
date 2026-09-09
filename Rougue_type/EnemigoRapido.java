import greenfoot.*;

// Enemigo Rápido: frágil pero veloz
public class EnemigoRapido extends SoldadoEnemigo
{
    public EnemigoRapido(Jugador objetivo, int vida, int velocidad, int danio)
    {
        super(objetivo, (int)(vida * 0.6), velocidad + 2, danio);
        // Opcional: Asignar imagen propia o tinte
    }
}