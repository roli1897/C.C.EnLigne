package com.leyou.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NumberUtils {

    public static boolean isInt(Double num) {
        return num.intValue() == num;
    }

    /**
     * Vérifier si une chaîne est au format numérique
     * @param str
     * @return
     */
    public static boolean isDigit(String str){
        if(str == null || str.trim().equals("")){
            return false;
        }
        return str.matches("^\\d+$");
    }

    /**
     * Arrondir un nombre décimal à un nombre de décimales spécifié
     * @param num
     * @param scale
     * @return
     */
    public static double scale(double num, int scale) {
        BigDecimal bd = new BigDecimal(num);
        return bd.setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    // Rechercher dans une chaîne de caractères en utilisant une expression régulière et retourner un tableau des nombres trouvés
    public static Double[] searchNumber(String value, String regex){
        List<Double> doubles = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        if(matcher.find()) {
            MatchResult result = matcher.toMatchResult();
            for (int i = 1; i <= result.groupCount(); i++) {
                doubles.add(Double.valueOf(result.group(i)));
            }
        }
        return doubles.toArray(new Double[doubles.size()]);
    }

    /**
     * Générer des nombres aléatoires de la longueur spécifiée
     * @param len
     * @return
     */
    public static String generateCode(int len){
        len = Math.min(len, 8);
        int min = Double.valueOf(Math.pow(10, len - 1)).intValue();
        int num = new Random().nextInt(Double.valueOf(Math.pow(10, len + 1)).intValue() - 1) + min;
        return String.valueOf(num).substring(0,len);
    }
}