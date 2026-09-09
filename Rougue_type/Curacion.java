import greenfoot.*;

public class Curacion extends Actor
{
    private int cantidadCura = 25; // Cuánta vida recupera
    private int vidaUtil = 300; // Tiempo que permanece en el suelo antes de desaparecer

    public Curacion()
    {
        // Crear un sprite simple de un botiquín (cuadrado verde con cruz blanca)
        GreenfootImage img = new GreenfootImage(20, 20);
        img.setColor(Color.GREEN);
        img.fill();
        img.setColor(Color.WHITE);
        img.fillRect(8, 4, 4, 12); // Línea vertical de la cruz
        img.fillRect(4, 8, 12, 4); // Línea horizontal de la cruz
        setImage(img);
    }

    public void act()
    {
        if (getWorld() == null) return;

        vidaUtil--;

        // Detectar si el jugador pasa por encima
        Jugador jugador = (Jugador) getOneIntersectingObject(Jugador.class);
        if (jugador != null)
        {
            // 1. Curar al jugador
            jugador.curar(cantidadCura);
        
            // 2. Crear la respuesta visual flotante justo por encima del jugador
            getWorld().addObject(new TextoCuracion(cantidadCura), getX(), getY() - 20);
        
            // 3. Eliminar el botiquín del suelo
            getWorld().removeObject(this); 
            return;
        }

        // Desaparecer si se acaba el tiempo
        if (vidaUtil <= 0 && getWorld() != null)
        {
            getWorld().removeObject(this);
        }
    }
}