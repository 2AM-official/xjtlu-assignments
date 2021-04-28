import java.security.SecureRandom;

public class OnetimePad {
    private SecureRandom random = new SecureRandom();
    //generate onetime pads
    public char[] generator(int length) {
        char[] key = new char[length];
        for(int i=0; i<length; i++){
            key[i] = getRandUnicode();
        }
        return key;
    }
    //get random unicode
    private char getRandUnicode(){
        return (char) (random.nextInt() & 0xFF);
    }
    //encode the plaintext
    public byte[] encode(byte[] data, char[] pad) {
        final byte[] encoded = new byte[data.length];
        for(int i=0; i < data.length; i++){
            encoded[i] = (byte) (data[i] ^ pad[i]);
        }
        return encoded;
    }
    //decode the ciphertext
    public byte[] decode(byte[] encoded, char[] pad){
        final byte[] decoded = new byte[encoded.length];
        for (int i=0; i < encoded.length; i++){
            decoded[i] = (byte) (encoded[i] ^ pad[i]);
        }
        return decoded;
    }
}
