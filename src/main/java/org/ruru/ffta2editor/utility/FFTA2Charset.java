package org.ruru.ffta2editor.utility;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;

public class FFTA2Charset {
    public static HashMap<Integer, String> decodingMap = new HashMap<>();
    static {
        decodingMap.put(0x00,"\r");
        decodingMap.put(0x01, " ");
        decodingMap.put(0x02, "A");
        decodingMap.put(0x03, "B");
        decodingMap.put(0x04, "C");
        decodingMap.put(0x05, "D");
        decodingMap.put(0x06, "E");
        decodingMap.put(0x07, "F");
        decodingMap.put(0x08, "G");
        decodingMap.put(0x09, "H");
        decodingMap.put(0x0A, "I");
        decodingMap.put(0x0B, "J");
        decodingMap.put(0x0C, "K");
        decodingMap.put(0x0D, "L");
        decodingMap.put(0x0E, "M");
        decodingMap.put(0x0F, "N");
        decodingMap.put(0x10, "O");
        decodingMap.put(0x11, "P");
        decodingMap.put(0x12, "Q");
        decodingMap.put(0x13, "R");
        decodingMap.put(0x14, "S");
        decodingMap.put(0x15, "T");
        decodingMap.put(0x16, "U");
        decodingMap.put(0x17, "V");
        decodingMap.put(0x18, "W");
        decodingMap.put(0x19, "X");
        decodingMap.put(0x1A, "Y");
        decodingMap.put(0x1B, "Z");
        decodingMap.put(0x1C, "a");
        decodingMap.put(0x1D, "b");
        decodingMap.put(0x1E, "c");
        decodingMap.put(0x1F, "d");
        decodingMap.put(0x20, "e");
        decodingMap.put(0x21, "f");
        decodingMap.put(0x22, "g");
        decodingMap.put(0x23, "h");
        decodingMap.put(0x24, "i");
        decodingMap.put(0x25, "j");
        decodingMap.put(0x26, "k");
        decodingMap.put(0x27, "l");
        decodingMap.put(0x28, "m");
        decodingMap.put(0x29, "n");
        decodingMap.put(0x2A, "o");
        decodingMap.put(0x2B, "p");
        decodingMap.put(0x2C, "q");
        decodingMap.put(0x2D, "r");
        decodingMap.put(0x2E, "s");
        decodingMap.put(0x2F, "t");
        decodingMap.put(0x30, "u");
        decodingMap.put(0x31, "v");
        decodingMap.put(0x32, "w");
        decodingMap.put(0x33, "x");
        decodingMap.put(0x34, "y");
        decodingMap.put(0x35, "z");
        decodingMap.put(0x36, "À");
        decodingMap.put(0x37, "Á");
        decodingMap.put(0x38, "Â");
        decodingMap.put(0x39, "Ä");
        decodingMap.put(0x3A, "Æ");
        decodingMap.put(0x3B, "Ç");
        decodingMap.put(0x3C, "È");
        decodingMap.put(0x3D, "É");
        decodingMap.put(0x3E, "Ê");
        decodingMap.put(0x3F, "Ë");
        decodingMap.put(0x40, "Ì");
        decodingMap.put(0x41, "Í");
        decodingMap.put(0x42, "Î");
        decodingMap.put(0x43, "Ï");
        decodingMap.put(0x44, "Ñ");
        decodingMap.put(0x45, "Ò");
        decodingMap.put(0x46, "Ó");
        decodingMap.put(0x47, "Ô");
        decodingMap.put(0x48, "Ö");
        decodingMap.put(0x49, "Œ");
        decodingMap.put(0x4A, "Ù");
        decodingMap.put(0x4B, "Ú");
        decodingMap.put(0x4C, "Û");
        decodingMap.put(0x4D, "Ü");
        decodingMap.put(0x4E, "ß");
        decodingMap.put(0x4F, "à");
        decodingMap.put(0x50, "á");
        decodingMap.put(0x51, "â");
        decodingMap.put(0x52, "ä");
        decodingMap.put(0x53, "æ");
        decodingMap.put(0x54, "ç");
        decodingMap.put(0x55, "è");
        decodingMap.put(0x56, "é");
        decodingMap.put(0x57, "ê");
        decodingMap.put(0x58, "ë");
        decodingMap.put(0x59, "ì");
        decodingMap.put(0x5A, "í");
        decodingMap.put(0x5B, "î");
        decodingMap.put(0x5C, "ï");
        decodingMap.put(0x5D, "ñ");
        decodingMap.put(0x5E, "ò");
        decodingMap.put(0x5F, "ó");
        decodingMap.put(0x60, "ô");
        decodingMap.put(0x61, "ö");
        decodingMap.put(0x62, "œ");
        decodingMap.put(0x63, "ù");
        decodingMap.put(0x64, "ú");
        decodingMap.put(0x65, "û");
        decodingMap.put(0x66, "ü");
        decodingMap.put(0x67, "0");
        decodingMap.put(0x68, "1");
        decodingMap.put(0x69, "2");
        decodingMap.put(0x6A, "3");
        decodingMap.put(0x6B, "4");
        decodingMap.put(0x6C, "5");
        decodingMap.put(0x6D, "6");
        decodingMap.put(0x6E, "7");
        decodingMap.put(0x6F, "8");
        decodingMap.put(0x70, "9");
        decodingMap.put(0x71, "~");
        decodingMap.put(0x72, "`");
        decodingMap.put(0x73, "!");
        decodingMap.put(0x74, "?");
        decodingMap.put(0x75, "#");
        decodingMap.put(0x76, "%");
        decodingMap.put(0x77, "^");
        decodingMap.put(0x78, "&");
        decodingMap.put(0x79, "*");
        decodingMap.put(0x7A, "/");
        decodingMap.put(0x7B, "_");
        decodingMap.put(0x7C, "+");
        decodingMap.put(0x7D, "=");
        decodingMap.put(0x7E, ",");
        decodingMap.put(0x7F, ".");
        decodingMap.put(0x80, ";");
        decodingMap.put(0x81, ":");
        decodingMap.put(0x82, "¥");
        decodingMap.put(0x83, "'");
        decodingMap.put(0x84, "\"");
        decodingMap.put(0x85, "„");
        decodingMap.put(0x86, "(");
        decodingMap.put(0x87, ")");
        decodingMap.put(0x88, "[");
        decodingMap.put(0x89, "]");
        decodingMap.put(0x8A, "{");
        decodingMap.put(0x8B, "}");
        decodingMap.put(0x8C, "|");
        decodingMap.put(0x8D, "-");
        decodingMap.put(0x8E, "ー");
        decodingMap.put(0x8F, "—");
        decodingMap.put(0x90, "«");
        decodingMap.put(0x91, "»");
        decodingMap.put(0x92, "·");
        decodingMap.put(0x93, "¡");
        decodingMap.put(0x94, "¿");
        decodingMap.put(0x95, "°");
        decodingMap.put(0x96, "\\squareleft\\");
        decodingMap.put(0x97, "\\squareright\\");
        decodingMap.put(0x98, "♪");
        decodingMap.put(0x99, "↓");
        decodingMap.put(0x9A, "←");
        decodingMap.put(0x9B, "↑");
        decodingMap.put(0x9C, "→");
        decodingMap.put(0x9D, "™");
        decodingMap.put(0x9E, "®");
        decodingMap.put(0x9F, "©");
        decodingMap.put(0xA0, "\\small2\\");
        decodingMap.put(0xA1, "\\small3\\");
        decodingMap.put(0xA2, "\\small4\\");
        decodingMap.put(0xA3, "\\small5\\");
        decodingMap.put(0xA4, "<");
        decodingMap.put(0xA5, ">");
        decodingMap.put(0xA6, "≠");
        decodingMap.put(0xA7, "≤");
        decodingMap.put(0xA8, "≥");
        decodingMap.put(0xC000,"\n");
        decodingMap.put(0xC1, "\\end\\");
        decodingMap.put(0xC2, "\\var2:%02X\\");
        decodingMap.put(0xC300, "\\endPage\\");
        decodingMap.put(0xC4, "\\var4:%02X\\");
        decodingMap.put(0xC5, "\\var5:%02X\\");
        decodingMap.put(0xC6, "\\var6:%02X\\");
        decodingMap.put(0xC7, "\\defaultOption:%02X\\");
        decodingMap.put(0xC800, "\\endOption\\");
        decodingMap.put(0xC9, "\\var9:%02X\\");
        decodingMap.put(0xCA, "\\varA:%02X\\"); // Insert variable in text
        decodingMap.put(0xCB, "\\sprite:%02X\\"); // "com_key" related. 0x0 - 0x18 are valid. Invalid defaults to 0x0.
        decodingMap.put(0xCC, "\\varC:%02X\\"); // length = -param
        decodingMap.put(0xCD, "\\varD:%02X\\"); // length = param
        decodingMap.put(0xCE, "\\varE:%02X\\"); // ???
        decodingMap.put(0xCF, "\\varF:%02X\\"); // length = params == 0 ? 6 : 0
    }
    public static HashMap<String, Integer> encodingMap = new HashMap<>();
    static {
        encodingMap.put("\r", 0x00);
        encodingMap.put(" ", 0x01);
        encodingMap.put("A", 0x02);
        encodingMap.put("B", 0x03);
        encodingMap.put("C", 0x04);
        encodingMap.put("D", 0x05);
        encodingMap.put("E", 0x06);
        encodingMap.put("F", 0x07);
        encodingMap.put("G", 0x08);
        encodingMap.put("H", 0x09);
        encodingMap.put("I", 0x0A);
        encodingMap.put("J", 0x0B);
        encodingMap.put("K", 0x0C);
        encodingMap.put("L", 0x0D);
        encodingMap.put("M", 0x0E);
        encodingMap.put("N", 0x0F);
        encodingMap.put("O", 0x10);
        encodingMap.put("P", 0x11);
        encodingMap.put("Q", 0x12);
        encodingMap.put("R", 0x13);
        encodingMap.put("S", 0x14);
        encodingMap.put("T", 0x15);
        encodingMap.put("U", 0x16);
        encodingMap.put("V", 0x17);
        encodingMap.put("W", 0x18);
        encodingMap.put("X", 0x19);
        encodingMap.put("Y", 0x1A);
        encodingMap.put("Z", 0x1B);
        encodingMap.put("a", 0x1C);
        encodingMap.put("b", 0x1D);
        encodingMap.put("c", 0x1E);
        encodingMap.put("d", 0x1F);
        encodingMap.put("e", 0x20);
        encodingMap.put("f", 0x21);
        encodingMap.put("g", 0x22);
        encodingMap.put("h", 0x23);
        encodingMap.put("i", 0x24);
        encodingMap.put("j", 0x25);
        encodingMap.put("k", 0x26);
        encodingMap.put("l", 0x27);
        encodingMap.put("m", 0x28);
        encodingMap.put("n", 0x29);
        encodingMap.put("o", 0x2A);
        encodingMap.put("p", 0x2B);
        encodingMap.put("q", 0x2C);
        encodingMap.put("r", 0x2D);
        encodingMap.put("s", 0x2E);
        encodingMap.put("t", 0x2F);
        encodingMap.put("u", 0x30);
        encodingMap.put("v", 0x31);
        encodingMap.put("w", 0x32);
        encodingMap.put("x", 0x33);
        encodingMap.put("y", 0x34);
        encodingMap.put("z", 0x35);
        encodingMap.put("À", 0x36);
        encodingMap.put("Á", 0x37);
        encodingMap.put("Â", 0x38);
        encodingMap.put("Ä", 0x39);
        encodingMap.put("Æ", 0x3A);
        encodingMap.put("Ç", 0x3B);
        encodingMap.put("È", 0x3C);
        encodingMap.put("É", 0x3D);
        encodingMap.put("Ê", 0x3E);
        encodingMap.put("Ë", 0x3F);
        encodingMap.put("Ì", 0x40);
        encodingMap.put("Í", 0x41);
        encodingMap.put("Î", 0x42);
        encodingMap.put("Ï", 0x43);
        encodingMap.put("Ñ", 0x44);
        encodingMap.put("Ò", 0x45);
        encodingMap.put("Ó", 0x46);
        encodingMap.put("Ô", 0x47);
        encodingMap.put("Ö", 0x48);
        encodingMap.put("Œ", 0x49);
        encodingMap.put("Ù", 0x4A);
        encodingMap.put("Ú", 0x4B);
        encodingMap.put("Û", 0x4C);
        encodingMap.put("Ü", 0x4D);
        encodingMap.put("ß", 0x4E);
        encodingMap.put("à", 0x4F);
        encodingMap.put("á", 0x50);
        encodingMap.put("â", 0x51);
        encodingMap.put("ä", 0x52);
        encodingMap.put("æ", 0x53);
        encodingMap.put("ç", 0x54);
        encodingMap.put("è", 0x55);
        encodingMap.put("é", 0x56);
        encodingMap.put("ê", 0x57);
        encodingMap.put("ë", 0x58);
        encodingMap.put("ì", 0x59);
        encodingMap.put("í", 0x5A);
        encodingMap.put("î", 0x5B);
        encodingMap.put("ï", 0x5C);
        encodingMap.put("ñ", 0x5D);
        encodingMap.put("ò", 0x5E);
        encodingMap.put("ó", 0x5F);
        encodingMap.put("ô", 0x60);
        encodingMap.put("ö", 0x61);
        encodingMap.put("œ", 0x62);
        encodingMap.put("ù", 0x63);
        encodingMap.put("ú", 0x64);
        encodingMap.put("û", 0x65);
        encodingMap.put("ü", 0x66);
        encodingMap.put("0", 0x67);
        encodingMap.put("1", 0x68);
        encodingMap.put("2", 0x69);
        encodingMap.put("3", 0x6A);
        encodingMap.put("4", 0x6B);
        encodingMap.put("5", 0x6C);
        encodingMap.put("6", 0x6D);
        encodingMap.put("7", 0x6E);
        encodingMap.put("8", 0x6F);
        encodingMap.put("9", 0x70);
        encodingMap.put("~", 0x71);
        encodingMap.put("`", 0x72);
        encodingMap.put("!", 0x73);
        encodingMap.put("?", 0x74);
        encodingMap.put("#", 0x75);
        encodingMap.put("%", 0x76);
        encodingMap.put("^", 0x77);
        encodingMap.put("&", 0x78);
        encodingMap.put("*", 0x79);
        encodingMap.put("/", 0x7A);
        encodingMap.put("_", 0x7B);
        encodingMap.put("+", 0x7C);
        encodingMap.put("=", 0x7D);
        encodingMap.put(",", 0x7E);
        encodingMap.put(".", 0x7F);
        encodingMap.put(";", 0x80);
        encodingMap.put(":", 0x81);
        encodingMap.put("¥", 0x82);
        encodingMap.put("'", 0x83);
        encodingMap.put("\"", 0x84);
        encodingMap.put("„", 0x85);
        encodingMap.put("(", 0x86);
        encodingMap.put(")", 0x87);
        encodingMap.put("[", 0x88);
        encodingMap.put("]", 0x89);
        encodingMap.put("{", 0x8A);
        encodingMap.put("}", 0x8B);
        encodingMap.put("|", 0x8C);
        encodingMap.put("-", 0x8D);
        encodingMap.put("ー", 0x8E);
        encodingMap.put("—", 0x8F);
        encodingMap.put("«", 0x90);
        encodingMap.put("»", 0x91);
        encodingMap.put("·", 0x92);
        encodingMap.put("¡", 0x93);
        encodingMap.put("¿", 0x94);
        encodingMap.put("°", 0x95);
        encodingMap.put("\\squareleft\\", 0x96);
        encodingMap.put("\\squareright\\", 0x97);
        encodingMap.put("♪", 0x98);
        encodingMap.put("↓", 0x99);
        encodingMap.put("←", 0x9A);
        encodingMap.put("↑", 0x9B);
        encodingMap.put("→", 0x9C);
        encodingMap.put("™", 0x9D);
        encodingMap.put("®", 0x9E);
        encodingMap.put("©", 0x9F);
        encodingMap.put("\\small2\\", 0xA0);
        encodingMap.put("\\small3\\", 0xA1);
        encodingMap.put("\\small4\\", 0xA2);
        encodingMap.put("\\small5\\", 0xA3);
        encodingMap.put("<", 0xA4);
        encodingMap.put(">", 0xA5);
        encodingMap.put("≠", 0xA6);
        encodingMap.put("≤", 0xA7);
        encodingMap.put("≥", 0xA8);
        encodingMap.put("\n", 0xC0);
        encodingMap.put("\\end\\", 0xC1);
        encodingMap.put("\\var2:", 0xC2);
        encodingMap.put("\\endPage\\", 0xC3);
        encodingMap.put("\\var4:", 0xC4);
        encodingMap.put("\\var5:", 0xC5);
        encodingMap.put("\\var6:", 0xC6);
        encodingMap.put("\\defaultOption:", 0xC7);
        encodingMap.put("\\endOption\\", 0xC8);
        encodingMap.put("\\var9:", 0xC9);
        encodingMap.put("\\varA:", 0xCA);
        encodingMap.put("\\sprite:", 0xCB);
        encodingMap.put("\\varC:", 0xCC);
        encodingMap.put("\\varD:", 0xCD);
        encodingMap.put("\\varE:", 0xCE);
        encodingMap.put("\\varF:", 0xCF);
    }

    public static String decode(ByteBuffer bytes) throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean shouldPrint = false;
        while(bytes.remaining() > 0) {
            int b = 0;
            String s = null;
            int i;
            for (i = 0; i < 4; i++) {
                b = (b  << 8) | Byte.toUnsignedInt(bytes.get());
                s = decodingMap.get(b);
                if (s != null || bytes.remaining() == 0) break;
                //else System.out.println(String.format("unknown: %02X", b));
            }
            if (s == null) {
                bytes.position(bytes.position()-i);
                Byte unknownByte = bytes.get();
                if (unknownByte == 0x00) System.out.println(String.format("%08X", b));
                if (Byte.toUnsignedInt(unknownByte) >= 0xC0) {
                    s = String.format("<unknown:%02X%02X>", unknownByte, bytes.get());
                } else {
                    s = String.format("<unknown:%02X>", unknownByte);
                }
                System.out.println(s);
                //throw new Exception(String.format("Failed to decode: %04X", b));
            //} else if (s.startsWith("\\sprite") || s.startsWith("\\defaultOption") || s.startsWith("\\var")) {
            } else if (s.startsWith("\\") && s.endsWith("%02X\\")) {
                s = String.format(s, bytes.get());
            }
            //if (s == "\r") break;
            if (s.equals("\r") ) continue;
            sb.append(s);
            if (b == 0xCC) shouldPrint = true;
            //if (s == "\\end\\") break;
        }
        if (shouldPrint) {
            System.out.println(String.format("Found in: \"%s\"", sb.toString()));
        }
        return sb.toString();
    }

    public static byte[] encode(String s) throws Exception {
        //char[] chars = s.toCharArray();
        if (s.equals("")) return new byte[]{0x00};
        ByteBuffer encodedBytes = ByteBuffer.allocate((s.length()+1)*4).order(ByteOrder.LITTLE_ENDIAN);
        StringBuilder sb = new StringBuilder(s);
        Integer encodedChar = null;
        Integer lastChar = null;
        for (int i = 0; i < s.length(); i++) {
            for (int j = i; j < s.length()+1; j++) {
                //String subString = sb.substring(i, j);
                encodedChar = encodingMap.get(sb.substring(i, j));
                if (encodedChar != null) {
                    if (encodedChar >= 0xC0) {
                        if (encodedChar >= 0xC2 && encodedChar != 0xC3 && encodedChar != 0xC8) {
                            int value = Integer.parseInt(sb.substring(j, j+2), 16);
                            encodedBytes.put(encodedChar.byteValue());
                            encodedBytes.put((byte)value);
                            j += 3;
                        } else {
                            encodedBytes.putShort((short)encodedChar.shortValue());
                            //byte b = (byte)(encodedChar >>> 8);
                            //encodedBytes.put(b);
                            //encodedBytes.put((byte)(encodedChar.intValue()));
                        }
                    } else {
                        encodedBytes.put(encodedChar.byteValue());
                    }
                    lastChar = encodedChar;
                    i += (j-i)-1;
                    break;
                    //for (int k = 0; k < 4; k++) {
                    //    byte b = (byte)(encodedChar >> k*8);
                    //    encodedBytes.put(b);
                    //    if (b == 0) break;
                    //}
                    //i += (j-i)-1;
                    //break;
                }
                if (j == s.length()) throw new Exception(String.format("Failed to encode unknown character \"%s\" in:\n\"%s\"", sb.substring(i, j), s));
            }
        }
        if (lastChar != 0x00 && lastChar != 0xC1) {
            encodedBytes.put((byte)0);
        }
        return Arrays.copyOf(encodedBytes.array(), encodedBytes.position());
    }
}
