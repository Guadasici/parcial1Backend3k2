package ar.edu.backend.envios;

public class Envio {
    private final String id;
    private final String zona;
    private final int gramos;
    // agrego nuevo parametro
    private final String modalidad;

    // contructor original
    // Se delega al constructor nuevo this(id, zona, gramos, "NORMAL") para que se ejecuten
    // todas las validaciones de invariantes (nulidad/espacios en blanco de id y zona, gramos > 0 y <= 30000).
    public Envio(String id, String zona, int gramos) {
        this(id, zona, gramos, "NORMAL");
    }

    public Envio(String id, String zona, int gramos, String modalidad) {
        if (id == null || id.isBlank() || zona == null || zona.isBlank()) {
            throw new IllegalArgumentException("id y zona obligatorios");
        }
        if (gramos <= 0 || gramos > 30000) {
            throw new IllegalArgumentException("gramos fuera de rango: " + gramos);
        }
        // verifico q modalidad no sea vacio o null
        if (modalidad == null || modalidad.isBlank()) {
            throw new IllegalArgumentException("modalidad obligatorios");
        }
        // le quito los espacios a modalidad
        String modLimpia = modalidad.strip();
        // si tiene la columna extra, si o si debe ser o normal o prioritario
        if (!modLimpia.equals("NORMAL") && !modLimpia.equals("PRIORITARIO")) {
            throw new IllegalArgumentException("modalidad desconocida");
        }
        // verifico si es priopritario y si no supera el maximo de gramos
        if (modLimpia.equals("PRIORITARIO") && gramos > 5000) {
            throw new IllegalArgumentException("el peso neto excede el maximo de 5000g");
        }

        // le saco espacios
        this.id = id.strip();
        this.zona = zona.strip();
        this.gramos = gramos;
        // pasar la modalidad limpia
        this.modalidad = modLimpia;
    }

    public static Envio desdeCampos(String[] c) {
        // agregar la verificacion de q si o si sean o 5 o 6 columnas
        if (c.length != 5 && c.length != 6) {
            throw new IllegalArgumentException("se esperan 5 o 6 columnas");
        }

        int bruto = Integer.parseInt(c[2]);
        int embalaje = Integer.parseInt(c[3]);
        if (bruto <= 0 || bruto > 50000 || embalaje < 0 || embalaje >= bruto) {
            throw new IllegalArgumentException("pesos incompatibles: " + c[2] + "/" + c[3]);
        }
        // agregar modalidad, si son 5 columnas es normal x defecto,
        // si son 6 columnas, es lo q diga en la posicion esa
        String modalidad = "NORMAL";
        if (c.length == 6) {
           modalidad = c[5];
        }
        // agregar modalidad al return
        return new Envio(c[0], c[1], bruto - embalaje, modalidad);
    }

    public String getId() { return id; }
    public String getZona() { return zona; }
    public int getGramos() { return gramos; }
    // agregar getter de modalidad
    public String getModalidad() {
        return modalidad;
    }
    // si es normal se queda como estaba el costo,
    // si la modalidad es prioritario se agregan 400
    public int costo() {
        int costoNormal = 500 + 200 * ((gramos + 999) / 1000);
        if (modalidad.equals("PRIORITARIO")) {
            costoNormal = costoNormal + 400;
        }
        return costoNormal;
    }
}
