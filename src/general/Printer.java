package general;

import cofres.Cofre;
import pedido.Pedido;
import robopuerto.Robopuerto;
import robot.Robot;
import sistema.Colonia;
import sistema.Red;

public class Printer {
    private static final int COL_1 = 10;   // ancho para la 1º columna

    private static void linea(String formato, Object... data) {
        System.out.printf(formato + "%n", data);
    }

    public static void oferta(Pedido ofrecido) {
        linea("\n\n🟢 %-"+COL_1+"s %d unidades de %s",
              txtCofre(ofrecido.getCofre(),"ofreció"),
              ofrecido.getCantidad(),
              ofrecido.getItem());
    }

    public static void solicitud(Pedido solicitado) {
        linea("\n🔵 %-"+COL_1+"s %d unidades de %s",
              txtCofre(solicitado.getCofre(),"solicitó"),
              solicitado.getCantidad(),
              solicitado.getItem());
    }

    public static void asignacionRobot(Robot r, int cant, String item) {
        linea("\n\t🤖Robot %d llevará %d %s", r.getId(), cant, item);
    }

    public static void estadoRobot(String ctx, Robot r) {
    	String ubicacion = String.format("(X:%d;Y:%d)", 
                r.getUbicacion().getX(), 
                r.getUbicacion().getY());

    	int energiaActual = r.getBateria().getCelulas();
		int energiaMaxima = r.getBateria().getCelulasMaximas();

		System.out.printf("\t%s %s | Batería: %3d/%-3d\n ",
				ctx, ubicacion, energiaActual, energiaMaxima);
    }

    public static void excesoSinDemandaTrasEntregasActivo(Pedido pedido) {
        Cofre cofre = pedido.getCofre();
        System.out.printf(""
        		+ "\n\t⚠️ El %s %d no pudo completar la entrega de %d unidades de %s."
        		+ "\n\t➤ Ya se hicieron entregas, pero no hay más cofres que lo soliciten."
        		+ "\n\t➤ Se intentará llevar el exceso a un cofre de almacenamiento.\n",
            cofre.getTipo(), cofre.getId(),
            pedido.getCantidad(), pedido.getItem());
    }

    public static void excesoSinDemandaInicialActivo(Pedido pedido) {
        Cofre cofre = pedido.getCofre();
        System.out.printf(""
        	   + "\n\t⚠️ El %s %d no pudo entregar %d unidades de %s."
               + "\n\t➤ No hay cofres que soliciten este ítem."
               + "\n\t➤ Se intentará llevar el ofrecimiento completo a un cofre de almacenamiento.\n",
            cofre.getTipo(), cofre.getId(),
            pedido.getCantidad(), pedido.getItem());
    }
    public static void excesoSinDemandaTrasEntregasPasivo(Pedido pedido) {
        Cofre cofre = pedido.getCofre();
        System.out.printf(""
               + "\n\t⚠️ El %s %d no pudo completar la entrega de %d unidades de %s."
               + "\n\t➤ Ya se entregó parcialmente, pero no hay más solicitantes."
               + "\n\t➤ Como el cofre ofrece de forma pasiva, no se llevará al almacenamiento."
               + "\n\t➤ Dejará de ofrecer el ítem.",
            cofre.getTipo(), cofre.getId(),
            pedido.getCantidad(), pedido.getItem());
    }

    public static void excesoSinDemandaInicialPasivo(Pedido pedido) {
        Cofre cofre = pedido.getCofre();
        System.out.printf(""
               + "\n\t⚠️ El %s %d no pudo entregar %d unidades de %s."
               + "\n\t➤ No hay cofres que soliciten este ítem."
               + "\n\t➤ Como el cofre ofrece de forma pasiva, no se llevará al almacenamiento."
               + "\n\t➤ Dejará de ofrecer el ítem.",
            cofre.getTipo(), cofre.getId(),
            pedido.getCantidad(), pedido.getItem());
    }

    public static void resumen(String titulo) {
        linea("\n===== %s =====", titulo);
    }
    public static void irARecarga(robopuerto.Robopuerto rp) {
        System.out.printf("\n\t🔋Va a recargarse al Robopuerto %d en %s%n",
                          rp.getId(), rp.getUbicacion());
    }

    public static void tomaItems(Robot r, Cofre c, int cant, String item) {
        System.out.printf("\n\t📦%s tomó %d %s del %s %d%n",
                          txtRobot(r), cant, item, c.getTipo(), c.getId());
    }

    public static void entregaItems(Robot r, Cofre c, int cant, String item) {
        System.out.printf("\n\t📤%s dejó %d %s en %s %d%n",
                          txtRobot(r), cant, item, c.getTipo(), c.getId());
    }

    public static void trasladoExcedente(Robot r, Cofre destino,
                                         int cant, String item) {
        System.out.printf("\n\t🤖%s llevará %d %s (excedente) al %s %d %n",
                          txtRobot(r), cant, item,
                          destino.getTipo(), destino.getId());
    }
    public static void mostrarCarga(int antes, int despues) {
        int max = Math.max(antes, despues);
        int bloques = 10;

        String barraAntes = barraCarga(antes, max, bloques);
        String barraDespues = barraCarga(despues, max, bloques);

        String estado = despues == max ? "Carga completa" : "Carga parcial";

        System.out.printf("\t⚡Cargando... %-12s %3d/%-3d --> %-12s %3d/%-3d - %s%n\n",
            barraAntes, antes, max,
            barraDespues, despues, max,
            estado);
    }
    private static String barraCarga(int valor, int max, int bloques) {
        int llenos = Math.min(valor * bloques / max, bloques);
        StringBuilder barra = new StringBuilder("[");
        for (int i = 0; i < bloques; i++) {
            barra.append(i < llenos ? "█" : "░");
        }
        barra.append("]");
        return barra.toString();
    }

    public static void advertencia(String msg) {
        System.out.println("   ⚠️  " + msg);
    }

    public static void info(String msg) {
        System.out.println("   ℹ️  " + msg);
    }

    public static void pendiente(Pedido p) {
        System.out.printf("  •%s%n", p.toString());
    }

    /* helper extra */
    private static String txtRobot(Robot r) {
        return "Robot " + r.getId();
    }
    public static void mostrarColonia(Colonia colonia) {
        System.out.println("\n🏙️  Colonia cargada:");

        for (Red red : colonia.getRedes()) {
            System.out.println("\n🔸 Red " + red.getId() + ":");

            // Robopuertos
            System.out.println("  🛰️  Robopuertos:");
            int count = 0;
            for (Robopuerto rp : red.getRobopuertos()) {
                System.out.printf("    Robopuerto %d (X:%d; Y:%d)  ", rp.getId(), 
                    rp.getUbicacion().getX(), rp.getUbicacion().getY());
                count++;
                if (count % 3 == 0) System.out.println();
            }
            if (count % 3 != 0) System.out.println(); // salto si no cerró justo

            // Robots
            System.out.println("  🤖 Robots:");
            count = 0;
            for (Robot robot : red.getRobots()) {
                System.out.printf("    Robot %d (Robopuerto %d)  ", robot.getId(), 
                    robot.getRobopuerto().getId());
                count++;
                if (count % 3 == 0) System.out.println();
            }
            if (count % 3 != 0) System.out.println();

            // Cofres
            System.out.println("  📦 Cofres:");
            count = 0;
            for (Cofre cofre : red.getCofres()) {
                System.out.printf("    %s %d (X:%d; Y:%d)  ", cofre.getTipo(), cofre.getId(), 
                    cofre.getUbicacion().getX(), cofre.getUbicacion().getY());
                count++;
                if (count % 3 == 0) System.out.println();
            }
            if (count % 3 != 0) System.out.println();
        }
    }

    private static String txtCofre(Cofre c, String verbo) {
        return String.format("El %s %d %s", c.getTipo(), c.getId(), verbo);
    }
}
