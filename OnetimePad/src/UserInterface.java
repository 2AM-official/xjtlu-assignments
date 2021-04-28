import java.util.Scanner;

public class UserInterface {
    private static Scanner kb = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            showMenu();
        }
    }

    public static void showMenu() {
        OnetimePad onetimePad = new OnetimePad();
        System.out.println("input the message:");
        String message = kb.nextLine();
        //transfer all letters to lowercase and ignore "space" character
        String plaintext = message.toLowerCase().replaceAll(" ", "");
        String regex = "^[a-z0-9]+$";
        //validate the input is only containing numbers and letters
        if (!(plaintext.matches(regex))) {
            System.out.println("Only English characters and numbers are acceptable.");
            return;
        }
        System.out.println("Original Message: "+message);
        System.out.println("Reduced Message: "+plaintext);
        String encoded = "";
        String padCode ="";
        String cipherText = "";
        String decryptionText = "";
        String output = "";
        byte[] messageCode = plaintext.getBytes();
        //get onetime pad
        char[] key = onetimePad.generator(messageCode.length);
        //encode the plaintext
        byte[] cipher = onetimePad.encode(messageCode, key);
        //decode the plaintext
        byte[] decryption = onetimePad.decode(cipher, key);
        for (int i=0; i<messageCode.length; i++){
            System.out.println(plaintext.charAt(i)+": "+toBinary(messageCode[i]));
            encoded += toBinary(messageCode[i])+" ";
            padCode += toBinary(key[i])+" ";
            cipherText += toBinary(cipher[i]) +" ";
            decryptionText += toBinary(decryption[i]) + " ";
            output += (char)decryption[i];
        }
        System.out.println("Encoded: \t"+encoded);
        System.out.println("OneTimePad: "+padCode);
        System.out.println("Ciphertext: "+cipherText);
        System.out.println("Decryption: "+decryptionText);
        System.out.println("Decryption Output: "+output);
        System.out.println("");
    }
    //make 8 bit binary
    private static String toBinary(int num) {
        String cover = Integer.toBinaryString(1 << 8).substring(1);
        String s = Integer.toBinaryString(num);
        if (s.length()<8) return  cover.substring(s.length()) + s;
        else if (s.length()>8) return s.substring(s.length()-8, s.length());
        else return s;
    }
}
