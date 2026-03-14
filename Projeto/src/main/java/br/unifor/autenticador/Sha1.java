package br.unifor.autenticador;

/**
 * Classe utilitaria para gerar hashes SHA-1.
 *
 * <p>Este arquivo concentra a funcao de dispersao usada pelo trabalho. Ele nao
 * representa uma estrutura de dados; apenas oferece um metodo estatico que
 * transforma uma string em um hash hexadecimal.
 *
 * <p>Uso esperado:
 * <ul>
 * <li>nao criar instancias desta classe;</li>
 * <li>usar {@link #hash(String)} sempre que for necessario gerar o SHA-1 de um
 * texto.</li>
 * </ul>
 */
final class Sha1 {
    /**
     * Construtor privado para impedir instanciacao da classe utilitaria.
     */
    private Sha1() {
    }

    /**
     * Gera o hash SHA-1 de uma string e devolve o resultado em hexadecimal.
     *
     * @param input texto de entrada
     * @return representacao hexadecimal do hash SHA-1
     */
    static String hash(String input) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            byte[] bytes = digest.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            // Converte cada byte do hash em dois caracteres hexadecimais.
            for (byte b : bytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            // Em ambientes Java padrao isso nao deve acontecer, mas a falha e tratada.
            throw new IllegalStateException("SHA-1 nao disponivel", e);
        }
    }
}
