package es.upm.dit.adsw.pract1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.net.URL;

/**
 * Interfaz gráfica.
 *
 * @author Jose A. Manas
 * @version 21.3.2012
 */
public class GUI
        extends JPanel {
    /**
     * Nombre del juego.
     */
    public static final String TITULO = "Laberintos";

    /**
     * Espacio entre la zona de juego y el borde de la ventana.
     */
    private static final int MARGEN = 10;
    /**
     * Ancho de la zona de juego.
     */
    private static final int ANCHO = 500;
    /**
     * Tamano de una celda: pixels.
     */
    private int ladoCelda;

    /**
     * Para que el usuario indique el tamano del laberinto.
     */
    private final JTextField campoLado;

    /**
     * El laberinto.
     */
    private Laberinto laberinto;

    private void nuevoJuego(int lado) {
        ladoCelda = (ANCHO - 2 * MARGEN) / lado;
        laberinto = new Laberinto(lado, this);
        pintame();
    }

    private GUI(Container container) {
        nuevoJuego(15);

        setPreferredSize(new Dimension(ANCHO, ANCHO));
        container.add(this, BorderLayout.CENTER);
        setFocusable(true);
        requestFocusInWindow();

        campoLado = new JTextField(5);
        campoLado.setAlignmentX(JTextField.RIGHT_ALIGNMENT);
        campoLado.setText(String.valueOf(laberinto.getLado()));
        campoLado.setMaximumSize(campoLado.getPreferredSize());

        JToolBar toolBarS = new JToolBar();
        toolBarS.setFloatable(false);
        toolBarS.add(campoLado);
        toolBarS.add(new CreaAction());
        toolBarS.add(new MarcianoAction());
        toolBarS.add(new MarcianoOrientadoAction());
        toolBarS.add(new MarcianoPerseguidorAction());
        toolBarS.add(Box.createHorizontalGlue());
        container.add(toolBarS, BorderLayout.SOUTH);

        addKeyListener(new MyKeyListener());
        addMouseListener(new MyMouseListener());

        repaint();
    }

    /**
     * Constructor.
     *
     * @param applet Applet.
     */
    public GUI(JApplet applet) {
        this(applet.getContentPane());
    }

    /**
     * Constructor.
     *
     * @param frame Pantalla en consola.
     */
    public GUI(JFrame frame) {
        this(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Le dice al thread de swing que deberia refrescar la pantalla.
     * Swing lo hara cuando le parezca bien.
     */
    public void pintame() {
        repaint();
    }

    /**
     * Llamada por java para pintarse en la pantalla.
     *
     * @param g sistema grafico 2D para dibujarse.
     */
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.LIGHT_GRAY);
        int nwx = MARGEN;
        int nwy = MARGEN;
        int lado = laberinto.getLado();

        // pinta las celdas
        for (int x = 0; x < lado; x++) {
            for (int y = 0; y < lado; y++) {
                pintaCelda(g, x, y);
            }
        }

        // pinta el marco
        g.setColor(Color.BLACK);
        g.drawLine(nwx - 1, nwy - 1, nwx - 1, nwy + lado * ladoCelda + 1);
        g.drawLine(nwx + lado * ladoCelda + 1, nwy - 1, nwx + lado * ladoCelda + 1, nwy + lado * ladoCelda + 1);
        g.drawLine(nwx - 1, nwy - 1, nwx + lado * ladoCelda + 1, nwy - 1);
        g.drawLine(nwx - 1, nwy + lado * ladoCelda + 1, nwx + lado * ladoCelda + 1, nwy + lado * ladoCelda + 1);

        // pinta al jugador
        Jugador jugador = laberinto.getJugador();
        pintaImagen((Graphics2D) g, jugador.getImagen(), jugador.getCelda());

        // pinta los marcianitos
        for (Marciano marciano : laberinto.getMarcianos()) {
            Image imagen = marciano.getImagen();
            if (imagen != null) {
                Celda celda = marciano.getCelda();
                pintaImagen((Graphics2D) g, imagen, celda);
            }
        }
    }

    private void pintaImagen(Graphics2D g2d, Image imagen, Celda celda) {
        if (imagen == null) {
            pintaEstado(g2d, celda);
            return;
        }
        int x = celda.getPunto().getX();
        int y = celda.getPunto().getY();
        double escala = 0.9 * ladoCelda / imagen.getWidth(null);
        AffineTransform transform = new AffineTransform(escala, 0, 0, escala, sw_x(x), sw_y(y + 1));
        g2d.drawImage(imagen, transform, null);
    }

    /**
     * Pinta una celda.
     *
     * @param g sistema grafico 2D para dibujarse.
     * @param x columna.
     * @param y fila.
     */
    private void pintaCelda(Graphics g, int x, int y) {
        Celda celda = laberinto.getCelda(x, y);

        // pinta segun el tipo de celda
        int swx = sw_x(x);
        int nwy = sw_y(y + 1);
        Tipo tipo = celda.getTipo();
        switch (tipo) {
            case DESTINO:
                g.setColor(Color.YELLOW);
                g.fillRect(swx, nwy, ladoCelda, ladoCelda);
                break;
            case CEPO:
                g.setColor(Color.BLACK);
                g.fillRect(swx, nwy, ladoCelda, ladoCelda);
                break;
            case LLAVE:
                g.setColor(Color.GREEN);
                g.fillRect(swx, nwy, ladoCelda, ladoCelda);
                break;
        }

        // pinta las paredes de la celda
        g.setColor(Color.RED);
        if (celda.hayPared(Direccion.NORTE))
            g.drawLine(sw_x(x), sw_y(y + 1), sw_x(x + 1), sw_y(y + 1));
        if (celda.hayPared(Direccion.SUR))
            g.drawLine(sw_x(x), sw_y(y), sw_x(x + 1), sw_y(y));
        if (celda.hayPared(Direccion.ESTE))
            g.drawLine(sw_x(x + 1), sw_y(y), sw_x(x + 1), sw_y(y + 1));
        if (celda.hayPared(Direccion.OESTE))
            g.drawLine(sw_x(x), sw_y(y), sw_x(x), sw_y(y + 1));

        // pinta el estado
        // se quita para que no ensucie la imagen
//        pintaEstado(g, celda);
    }

    /**
     * Pinta el contenido de una celda.
     *
     * @param g     sistema grafico 2D para dibujarse.
     * @param celda celda a pintar.
     */
    private void pintaEstado(Graphics g, Celda celda) {
        Estado estado = celda.getEstado();
        if (estado == null || estado == Estado.VACIA)
            return;
        PuntoXY punto = celda.getPunto();
        int x = punto.getX();
        int y = punto.getY();
        int nwx = sw_x(x) + 3;
        int nwy = sw_y(y + 1) + 3;
        int dx = this.ladoCelda - 6;
        int dy = this.ladoCelda - 6;
        g.setColor(estado.getColor());
        g.fillOval(nwx, nwy, dx, dy);
    }

    /**
     * Dada una columna, calcula el vertice inferior izquierdo.
     *
     * @param columna columna.
     * @return abscisa del vertice inferior izquierdo.
     */
    private int sw_x(int columna) {
        return MARGEN + columna * ladoCelda;
    }

    /**
     * Dada una fila, calcula el vertice inferior izquierdo.
     *
     * @param fila fila.
     * @return vertice inferior izquierdo.
     */
    private int sw_y(int fila) {
        int lado = laberinto.getLado();
        return MARGEN + (lado - fila) * ladoCelda;
    }

    /**
     * Pone fin a un juego: imprime un mensaje y genera un juego nuevo.
     *
     * @param msg mensaje explicativo.
     */
    public void fin(final String msg) {
        for (Marciano marciano : laberinto.getMarcianos())
            marciano.setModo(Modo.ESPERA);
        JOptionPane.showMessageDialog(null,
                msg, "Laberinto",
                JOptionPane.INFORMATION_MESSAGE);
        for (Marciano marciano : laberinto.getMarcianos())
            marciano.setModo(Modo.FIN);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                nuevoJuego(laberinto.getLado());
            }
        });
    }

    /**
     * Carga una imagen de un fichero grafico.
     *
     * @param fichero nombre del fichero.
     */
    public static Image setImagen(String fichero) {
        try {
            URL url = Marciano.class.getResource("imgs/" + fichero);
            ImageIcon icon = new ImageIcon(url);
            return icon.getImage();
        } catch (Exception e) {
            System.err.println("no se puede cargar "
                    + Marciano.class.getPackage().getName()
                    + System.getProperty("file.separator")
                    + fichero);
            return null;
        }
    }

    /**
     * Interprete del boton NUEVO.
     */
    private class CreaAction
            extends AbstractAction {
        /**
         * Constructor.
         */
        public CreaAction() {
            super("Nuevo");
        }

        /**
         * Se hace cargo de la pulsacion.
         *
         * @param event evento que dispara la accion.
         */
        public void actionPerformed(ActionEvent event) {
            for (Marciano marciano : laberinto.getMarcianos())
                marciano.setModo(Modo.FIN);
            int lado = Integer.parseInt(campoLado.getText());
            nuevoJuego(lado);
            requestFocus();
        }
    }

    /**
     * Interprete del boton MARCIANO.
     */
    private class MarcianoAction
            extends AbstractAction {
        /**
         * Constructor.
         */
        public MarcianoAction() {
            super("marciano");
        }

        /**
         * Se hace cargo de la pulsacion.
         *
         * @param event evento que dispara la accion.
         */
        public void actionPerformed(ActionEvent event) {
            int lado = laberinto.getLado();
            Celda celda = getCeldaVaciaAleatoria(lado);
            Marciano marciano = new MarcianoNormal(laberinto, celda, 300);
            laberinto.add(marciano);
            marciano.start();
            requestFocus();
        }
    }

    /**
     * Interprete del boton MARCIANO ORIENTADO.
     */
    private class MarcianoOrientadoAction
            extends AbstractAction {
        /**
         * Constructor.
         */
        public MarcianoOrientadoAction() {
            super("marciano orientado");
        }

        /**
         * Se hace cargo de la pulsacion.
         *
         * @param event evento que dispara la accion.
         */
        public void actionPerformed(ActionEvent event) {
            int lado = laberinto.getLado();
            Celda celda = getCeldaVaciaAleatoria(lado);
            Marciano marciano = new MarcianoOrientado(laberinto, celda, 500);
            laberinto.add(marciano);
            marciano.start();
            requestFocus();
        }
    }

    /**
     * Interprete del boton MARCIANO PERSEGUIDOR.
     */
    private class MarcianoPerseguidorAction
            extends AbstractAction {
        /**
         * Constructor.
         */
        public MarcianoPerseguidorAction() {
            super("marciano perseguidor");
        }

        /**
         * Se hace cargo de la pulsacion.
         *
         * @param event evento que dispara la accion.
         */
        public void actionPerformed(ActionEvent event) {
            int lado = laberinto.getLado();
            Celda celda = getCeldaVaciaAleatoria(lado);
            Marciano marciano = new MarcianoRutaOptima(laberinto, celda, 500);
            laberinto.add(marciano);
            marciano.start();
            requestFocus();
        }
    }

    private Celda getCeldaVaciaAleatoria(int lado) {
        do {
            PuntoXY punto = PuntoXY.random(lado);
            Celda celda = laberinto.getCelda(punto);
            if (celda.getEstado() == Estado.VACIA)
                return celda;
        } while (true);
    }

    /**
     * Captura el teclado.
     */
    private class MyKeyListener
            extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent event) {
            Direccion direccion = getDireccion(event);
            if (direccion != null)
                laberinto.getJugador().intentaMover(direccion);
        }

        private Direccion getDireccion(KeyEvent ke) {
            if (ke.getKeyCode() == KeyEvent.VK_UP)
                return Direccion.NORTE;
            if (ke.getKeyCode() == KeyEvent.VK_DOWN)
                return Direccion.SUR;
            if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
                return Direccion.ESTE;
            if (ke.getKeyCode() == KeyEvent.VK_LEFT)
                return Direccion.OESTE;
            return null;
        }
    }

    private class MyMouseListener
            extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            requestFocusInWindow();

            if (e.getX() < MARGEN || e.getY() < MARGEN)
                return;
            int x = (e.getX() - MARGEN) / ladoCelda;
            int y = laberinto.getLado() - 1 - (e.getY() - MARGEN) / ladoCelda;

            Celda celda = laberinto.getCelda(x, y);
            celda.setTipo(Tipo.CEPO);

            repaint();
        }
    }
}
