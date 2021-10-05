package hello.hellospring.encryption;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Component
public class SHA {
    public String encoding(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest mdSHA256 = MessageDigest.getInstance("SHA-256");

        // " Java 마스터! " 문자열 바이트로 메시지 다이제스트를 갱신
        mdSHA256.update(data.getBytes("UTF-8"));

        // 해시 계산 반환값은 바이트 배열
        byte[] sha256Hash = mdSHA256.digest();

        // 바이트배열을 16진수 문자열로 변환하여 표시
        StringBuilder hexSHA256hash = new StringBuilder();
        for(byte b : sha256Hash) {
            String hexString = String.format("%02x", b);
            hexSHA256hash.append(hexString);
        }
        return hexSHA256hash.toString();

    }
}
