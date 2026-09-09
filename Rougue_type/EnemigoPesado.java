import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class EnemigoPesado extends SoldadoEnemigo
{
    public EnemigoPesado(Jugador objetivo, int vida, int velocidad, int danio)
    {
        super(objetivo, vida * 2, Math.max(1, velocidad - 1), danio + 5);
    }
}