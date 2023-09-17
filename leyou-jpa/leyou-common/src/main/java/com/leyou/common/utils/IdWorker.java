package com.leyou.common.utils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * <p>Nom : IdWorker.java</p>
 * <p>Description : ID incrémentiel distribué</p>
 * <pre>
 *     Implémentation JAVA de Twitter Snowflake
 * </pre>
 * Le code central est implémenté dans la classe IdWorker. Le principe de base est structuré comme suit,
 * avec un "0" représentant un bit et des tirets pour séparer les différentes parties :
 * 1 || 0 --- 0000000000 0000000000 0000000000 0000000000 0 --- 00000 --- 00000 --- 000000000000
 * Dans la chaîne ci-dessus, le premier bit est inutilisé (en fait, il peut également servir de signe pour un long),
 * les 41 bits suivants sont l'horodatage en millisecondes, puis 5 bits pour l'identification du datacenter,
 * 5 bits pour l'identifiant de la machine (qui en réalité est l'identifiant du thread),
 * puis 12 bits pour le comptage des millisecondes en cours dans cette milliseconde. En tout, cela fait exactement 64 bits, soit la taille d'un long.
 * Les avantages sont les suivants : les ID sont triés de manière croissante dans l'ensemble en fonction du temps,
 * il n'y aura pas de collision d'ID dans l'ensemble du système distribué (grâce à l'identification du datacenter et de la machine),
 * et l'efficacité est élevée. Les tests montrent que Snowflake peut générer environ 260 000 ID par seconde, ce qui est largement suffisant pour répondre aux besoins.
 * </p>
 * 64-bit ID (42 bits pour les millisecondes + 5 bits pour l'ID de la machine + 5 bits pour le code métier + 12 bits pour l'accumulation répétée)
 *
 * @author Polim
 */

public class IdWorker {
    // Point de repère de début du temps, pris comme référence, généralement basé sur l'heure système la plus récente (une fois défini, il ne peut pas être modifié).
    private final static long twepoch = 1288834974657L;
    // Nombre de bits pour l'identification de la machine
    private final static long workerIdBits = 5L;
    // Nombre de bits pour l'identification du centre de données
    private final static long datacenterIdBits = 5L;
    // La valeur maximale de l'ID de la machine
    private final static long maxWorkerId = -1L ^ (-1L << workerIdBits);
    // La valeur maximale de l'ID du centre de données
    private final static long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    // Bit d'incrémentation interne en millisecondes
    private final static long sequenceBits = 12L;
    // Décalage de l'ID de la machine vers la gauche de 12 positions
    private final static long workerIdShift = sequenceBits;
    // Décalage de l'ID du centre de données vers la gauche de 17 positions
    private final static long datacenterIdShift = sequenceBits + workerIdBits;
    // Décalage des millisecondes de temps vers la gauche de 22 positions
    private final static long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    private final static long sequenceMask = -1L ^ (-1L << sequenceBits);
    /* Horodatage de la dernière production de l'ID */
    private static long lastTimestamp = -1L;
    // 0，contrôle de la concurrence
    private long sequence = 0L;

    private final long workerId;
    // Partie de l'identifiant de données
    private final long datacenterId;

    public IdWorker(){
        this.datacenterId = getDatacenterId(maxDatacenterId);
        this.workerId = getMaxWorkerId(datacenterId, maxWorkerId);
    }
    /**
     * @param workerId
     *
     * @param datacenterId
     *
     */
    public IdWorker(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }
    /**
     * obtenir le prochain Id
     *
     * @return
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            // Si la milliseconde actuelle, alors +1
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                // Si le comptage de millisecondes actuel est plein, alors attendre la prochaine seconde
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        // Combinaison du décalage de l'ID pour générer l'ID final, puis retourner l'ID
        long nextId = ((timestamp - twepoch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift) | sequence;

        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * <p>
     * obtenir maxWorkerId
     * </p>
     */
    protected static long getMaxWorkerId(long datacenterId, long maxWorkerId) {
        StringBuffer mpid = new StringBuffer();
        mpid.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!name.isEmpty()) {
         /*
          * GET jvmPid
          */
            mpid.append(name.split("@")[0]);
        }

        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    /**
     * <p>
     * Partie de l'identifiant de données
     * </p>
     */
    protected static long getDatacenterId(long maxDatacenterId) {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                id = ((0x000000FF & (long) mac[mac.length - 1])
                        | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                id = id % (maxDatacenterId + 1);
            }
        } catch (Exception e) {
            System.out.println(" getDatacenterId: " + e.getMessage());
        }
        return id;
    }


}