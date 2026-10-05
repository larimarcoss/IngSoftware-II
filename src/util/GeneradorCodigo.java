package util;

import java.util.HashMap;
import java.util.Map;

public class GeneradorCodigo {
    private static final Map<String, Integer> contadores = new HashMap<>();

    // Ej: siguiente("ASI") -> "ASI-0001", "ASI-0002", ...
    public static String siguiente(String prefijo) {
        int n = contadores.merge(prefijo, 1, Integer::sum);
        return String.format("%s-%04d", prefijo, n);
    }
}
