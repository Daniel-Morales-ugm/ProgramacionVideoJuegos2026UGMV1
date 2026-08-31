import greenfoot.*;

public class Jugador extends Actor
{
    private enum Estado
    {
        INTRO,
        QUIETO,
        CAMINANDO_DERECHA,
        CAMINANDO_IZQUIERDA,
        SALTANDO,
        CAYENDO
    }

    private Estado estado = Estado.INTRO;
    
    // Sprites estándar
    private GreenfootImage imagenQuieto;
    private GreenfootImage imagenSalto;
    private GreenfootImage imagenCaida;
    private GreenfootImage[] caminar;
    
    // Sprites de disparo
    private GreenfootImage imagenQuietoDisparo;
    private GreenfootImage imagenSaltoDisparo;
    private GreenfootImage imagenCaidaDisparo;
    private GreenfootImage[] caminarDisparo;

    // Sprites de intro
    private GreenfootImage[] animacionIntro;
    private int frameIntro = 0;
    private int contadorIntro = 0;
    private final int CANTIDAD_SPRITES_INTRO = 25; 
    
    private int frameActual = 0;
    private int contadorAnimacion = 0;
    
    // FÍSICA
    private boolean enElAire = false;
    private double vY = 0;
    private double fuerzaSalto = -12.0;
    private double gravedad = 0.8;
    private int pisoBase = 500;

    // ALTO FIJO PARA EVITAR EL TEMBLOR AL CAMBIAR DE SPRITE
    private final int ALTO_JUGADOR = 40; 

    private boolean mirandoIzquierda = false;
    
    // Disparo
    private int cooldownDisparo = 0;
    private final int TIEMPO_ESPERA_DISPARO = 15;
    private int timerAnimacionDisparo = 0;

    public Jugador()
    {
        cargarImagenes();
    }

    private void cargarImagenes()
    {
        imagenQuieto = new GreenfootImage("idle.png");
        imagenSalto = new GreenfootImage("jump.png");
        imagenCaida = new GreenfootImage("fall.png");
        
        caminar = new GreenfootImage[4];
        caminar[0] = new GreenfootImage("walk1.png");
        caminar[1] = new GreenfootImage("walk2.png");
        caminar[2] = new GreenfootImage("walk3.png");
        caminar[3] = new GreenfootImage("walk4.png");

        imagenQuietoDisparo = new GreenfootImage("idle_shoot.png");
        imagenSaltoDisparo = new GreenfootImage("jump_shoot.png");
        imagenCaidaDisparo = new GreenfootImage("fall_shoot.png");

        caminarDisparo = new GreenfootImage[4];
        caminarDisparo[0] = new GreenfootImage("walk_shoot1.png");
        caminarDisparo[1] = new GreenfootImage("walk_shoot2.png");
        caminarDisparo[2] = new GreenfootImage("walk_shoot3.png");
        caminarDisparo[3] = new GreenfootImage("walk_shoot4.png");

        animacionIntro = new GreenfootImage[CANTIDAD_SPRITES_INTRO];
        for (int i = 0; i < 21; i++)
        {
            animacionIntro[i] = new GreenfootImage("intro" + (i + 1) + ".png");
        }
        
        setImage(animacionIntro[0]);
    }

    public void act()
    {
        if (estado == Estado.INTRO)
        {
            reproducirIntro();
            return;
        }

        controlarMovimientoHorizontal();
        controlarSalto();
        aplicarFisicaYColisiones();
        controlarDisparo();
        actualizarAnimacion();
    }

    private void reproducirIntro()
    {
        contadorIntro++;

        if (contadorIntro >= 3)
        {
            contadorIntro = 0;
            frameIntro++;

            if (frameIntro < animacionIntro.length)
            {
                GreenfootImage imgActual = animacionIntro[frameIntro];
                setImage(imgActual);

                // Alinea la parte inferior del sprite actual con la línea del suelo
                int nuevoY = pisoBase;
                setLocation(getX(), nuevoY);
            }
            else
            {
                // Termina la intro y pasa a estado normal
                estado = Estado.QUIETO;
                setImage(imagenQuieto);
            
                // Alinea el sprite de idle al suelo al finalizar
                int nuevoY = pisoBase - (imagenQuieto.getHeight() / 2);
            setLocation(getX(), nuevoY);
            }
        }
    }   

    private void controlarMovimientoHorizontal()
    {
        if (Greenfoot.isKeyDown("right"))
        {
            setLocation(getX() + 4, getY());
            mirandoIzquierda = false;
            if (!enElAire) estado = Estado.CAMINANDO_DERECHA;
        }
        else if (Greenfoot.isKeyDown("left"))
        {
            setLocation(getX() - 4, getY());
            mirandoIzquierda = true;
            if (!enElAire) estado = Estado.CAMINANDO_IZQUIERDA;
        }
        else
        {
            if (!enElAire) estado = Estado.QUIETO;
        }
    }

    private void controlarSalto()
    {
        if (Greenfoot.isKeyDown("space") && !enElAire)
        {
            vY = fuerzaSalto;
            enElAire = true;
            estado = Estado.SALTANDO;
        }
    }

    private void aplicarFisicaYColisiones()
    {
        // 1. Aplicar gravedad
        vY += gravedad;
        setLocation(getX(), (int)(getY() + vY));

        // 2. Colisión con Plataformas usando ALTO_JUGADOR constante
        Actor plataforma = getOneIntersectingObject(Plataforma.class);
        if (plataforma != null && vY >= 0)
        {
            int piesJugador = getY() + (ALTO_JUGADOR / 2);
            int topePlataforma = plataforma.getY() - (plataforma.getImage().getHeight() / 2);

            if (piesJugador >= topePlataforma - 6 && piesJugador <= topePlataforma + 12)
            {
                setLocation(getX(), topePlataforma - (ALTO_JUGADOR / 2));
                vY = 0;
                enElAire = false;
            }
        }

        // 3. Colisión con Suelo Base
        if (getY() >= pisoBase)
        {
            setLocation(getX(), pisoBase);
            vY = 0;
            enElAire = false;
        }

        // 4. Determinar si sigue cayendo libremente
        if (vY != 0 && getOneIntersectingObject(Plataforma.class) == null && getY() < pisoBase)
        {
            enElAire = true;
        }

        // 5. Asignar estado aéreo únicamente cuando esté flotando/cayendo
        if (enElAire)
        {
            estado = (vY < 0) ? Estado.SALTANDO : Estado.CAYENDO;
        }
    }

    private void controlarDisparo()
    {
        if (cooldownDisparo > 0) cooldownDisparo--;
        if (timerAnimacionDisparo > 0) timerAnimacionDisparo--;

        if (Greenfoot.isKeyDown("z") && cooldownDisparo == 0)
        {
            int offsetX = mirandoIzquierda ? -20 : 20;
            Bala bala = new Bala(mirandoIzquierda);
            getWorld().addObject(bala, getX() + offsetX, getY());
            
            cooldownDisparo = TIEMPO_ESPERA_DISPARO;
            timerAnimacionDisparo = 12;
        }
    }

    private void actualizarAnimacion()
    {
        boolean estaDisparando = (timerAnimacionDisparo > 0);

        if (estado == Estado.QUIETO)
        {
            GreenfootImage imgBase = estaDisparando ? imagenQuietoDisparo : imagenQuieto;
            GreenfootImage img = new GreenfootImage(imgBase);
            if (mirandoIzquierda) img.mirrorHorizontally();
            setImage(img);
        }
        else if (estado == Estado.SALTANDO)
        {
            GreenfootImage imgBase = estaDisparando ? imagenSaltoDisparo : imagenSalto;
            GreenfootImage img = new GreenfootImage(imgBase);
            if (mirandoIzquierda) img.mirrorHorizontally();
            setImage(img);
        }
        else if (estado == Estado.CAYENDO)
        {
            GreenfootImage imgBase = estaDisparando ? imagenCaidaDisparo : imagenCaida;
            GreenfootImage img = new GreenfootImage(imgBase);
            if (mirandoIzquierda) img.mirrorHorizontally();
            setImage(img);
        }
        else if (estado == Estado.CAMINANDO_DERECHA)
        {
            animarCaminata(false, estaDisparando);
        }
        else if (estado == Estado.CAMINANDO_IZQUIERDA)
        {
            animarCaminata(true, estaDisparando);
        }
    }

    private void animarCaminata(boolean izquierda, boolean disparando)
    {
        contadorAnimacion++;
        if (contadorAnimacion >= 6)
        {
            frameActual++;
            GreenfootImage[] arregloUsar = disparando ? caminarDisparo : caminar;

            if (frameActual >= arregloUsar.length)
            {
                frameActual = 0;
            }

            GreenfootImage imagen = new GreenfootImage(arregloUsar[frameActual]);
            if (izquierda) imagen.mirrorHorizontally();

            setImage(imagen);
            contadorAnimacion = 0;
        }
    }
}