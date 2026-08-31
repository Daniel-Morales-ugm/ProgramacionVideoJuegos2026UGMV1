import greenfoot.*;

public class Bala extends Actor
{
    private int velocidad = 10;
    private boolean direccionIzquierda;

    // Arreglo y contadores para la animación
    private GreenfootImage[] spritesBala;
    private int frameActual = 0;
    private int contadorAnimacion = 0;

    public Bala(boolean izquierda)
    {
        this.direccionIzquierda = izquierda;
        
        // Cargar las imágenes del proyectil
        spritesBala = new GreenfootImage[3];
        spritesBala[0] = new GreenfootImage("bullet1.png");
        spritesBala[1] = new GreenfootImage("bullet2.png");
        spritesBala[2] = new GreenfootImage("bullet3.png");
        
        // Asignar el sprite inicial
        GreenfootImage imgInicial = new GreenfootImage(spritesBala[0]);
        if (direccionIzquierda)
        {
            imgInicial.mirrorHorizontally();
        }
        setImage(imgInicial);
    }

    public void act()
    {
        mover();
        animar();
        verificarColision();
    }

    private void mover()
    {
        if (direccionIzquierda)
        {
            setLocation(getX() - velocidad, getY());
        }
        else
        {
            setLocation(getX() + velocidad, getY());
        }
    }

    private void animar()
    {
        contadorAnimacion++;
        if (contadorAnimacion >= 4) // Ajusta este valor para controlar la velocidad
        {
            frameActual++;
            if (frameActual >= spritesBala.length)
            {
                frameActual = 0;
            }

            GreenfootImage img = new GreenfootImage(spritesBala[frameActual]);
            if (direccionIzquierda)
            {
                img.mirrorHorizontally();
            }
            setImage(img);
            contadorAnimacion = 0;
        }
    }

    private void verificarColision()
    {
        if (isTouching(Plataforma.class) || isAtEdge())
        {
            getWorld().removeObject(this);
        }
    }
}