package auxiliar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class validations {

    public static String sha512(String cadena) {
        StringBuilder sb = new StringBuilder();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(cadena.getBytes());
            byte[] mb = md.digest();

            for (int i = 0; i < mb.length; i++) {
                sb.append(Integer.toString((mb[i] & 0xff) + 0x100, 16).substring(1));
            }

        } catch (NoSuchAlgorithmException ex) {
            /*.....*/
        }
        return sb.toString();
    }

    //metodo para validar la operacion
    public static Integer operacionCedula(String selected) {
        int c, suma = 0, acum, resta = 0, banderaCedula;

        for (int i = 0; i < selected.length() - 1; i++) {
            c = Integer.parseInt(selected.charAt(i) + "");
            if (i % 2 == 0) {
                c = c * 2;
                if (c > 9) {
                    c = c - 9;
                }
            }
            suma = suma + c;
        }
        if (suma % 10 != 0) {
            acum = ((suma / 10) + 1) * 10;
            resta = acum - suma;
        }
        int ultimo = Integer.parseInt(selected.charAt(9) + "");
        if (ultimo == resta) {
            banderaCedula = 1;
        } else {
            banderaCedula = 0;
        }
        return banderaCedula;
    }

}
