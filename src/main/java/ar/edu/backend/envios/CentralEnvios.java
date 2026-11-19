package ar.edu.backend.envios;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CentralEnvios {
    private final List<Envio> envios;
    public CentralEnvios(List<Envio> envios) { this.envios = List.copyOf(envios); }
    public int total() { return envios.stream().mapToInt(Envio::costo).sum(); }
    public List<Envio> filtrar(Predicate<Envio> criterio) {
        return envios.stream().filter(criterio).toList();
    }
    public Map<String, Integer> totalesPorZona() {
        return envios.stream().collect(Collectors.groupingBy(Envio::getZona, Collectors.summingInt(Envio::costo)));
    }

    // Se agrego cantidadEnvios() y conteoPorModalidad() a la salida de panorama()
    public String panorama() {
        return "Objetos: " + cantidadEnvios()
                + " | Total: " + total()
                + " | por modalidad: " + new java.util.TreeMap<>(conteoPorModalidad())
                + " | por zona: " + new java.util.TreeMap<>(totalesPorZona())
                + " | hasta 1000 g: " + filtrar(e -> e.getGramos() <= 1000).size();
    }

    // Creo metodo para contar los envios por modalidad
    public Map<String, Long> conteoPorModalidad() {
        return envios.stream()
                .collect(Collectors.groupingBy(
                        Envio::getModalidad,
                        Collectors.counting()));
    }
    // pide q se devuelva la cantidad de envios
    public int cantidadEnvios() {
        return envios.size();
    }
}
