import greenfoot.*;

public class Mundo extends World
{
    // Ajustamos la resolución según el ratio del escenario (800x600 o 800x500)
    public Mundo()
    {
        super(800, 600, 1);
        prepararMundo();
    }

    private void prepararMundo()
    {
        GreenfootImage fondo = new GreenfootImage("fondo_escenario.png");
        fondo.scale(getWidth(), getHeight()); // Escalar la imagen al tamaño de la pantalla
        setBackground(fondo);
        // Agregar Jugador
        Jugador jugador = new Jugador();
        addObject(jugador, 120, 420);

        // Agregar varias plataformas
        addObject(new Plataforma(150, 35), 250, 440);
        addObject(new Plataforma(150, 35), 450, 400);
        addObject(new Plataforma(150, 35), 650, 360);
    }
}