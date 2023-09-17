package com.leyou.common.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaUtils {
    /**
     * Lire la clé publique depuis un fichier
     *
     * @param filename Chemin de sauvegarde de la clé publique, relatif au classpath.
     * @return Objet de clé publique
     * @throws Exception
     */
    public static PublicKey getPublicKey(String filename) throws Exception {
        byte[] bytes = readFile(filename);
        return getPublicKey(bytes);
    }

    /**
     * Lire la clé depuis un fichier
     *
     * @param filename =classpath
     * @return Objet de clé privé
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String filename) throws Exception {
        byte[] bytes = readFile(filename);
        return getPrivateKey(bytes);
    }

    /**
     * Lire la clé depuis un fichier
     *
     * @param bytes Forme de la clé publique en octets
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(byte[] bytes) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    /**
     * Lire la clé depuis un fichier
     *
     * @param bytes Forme de la clé publique en octets
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(byte[] bytes) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(spec);
    }

    /**
     * Générer une clé RSA publique et privée à partir du texte chiffré, puis les écrire dans un fichier spécifié
     *
     * @param publicKeyFilename  Chemin du fichier de clé publique
     * @param privateKeyFilename Chemin du fichier de clé privé
     * @param secret             Chiffrer le texte de la clé générée
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void generateKey(String publicKeyFilename, String privateKeyFilename, String secret) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom(secret.getBytes());
        keyPairGenerator.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        // Obtenir la clé publique et l'écrire
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        writeFile(publicKeyFilename, publicKeyBytes);
        // Obtenir la clé privée et l'écrire
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        writeFile(privateKeyFilename, privateKeyBytes);
    }

    private static byte[] readFile(String fileName) throws Exception {
        return Files.readAllBytes(new File(fileName).toPath());
    }

    private static void writeFile(String destPath, byte[] bytes) throws IOException {
        File dest = new File(destPath);
        if (!dest.exists()) {
            dest.createNewFile();
        }
        Files.write(dest.toPath(), bytes);
    }
}