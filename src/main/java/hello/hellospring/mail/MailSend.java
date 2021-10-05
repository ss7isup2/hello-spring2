package hello.hellospring.mail;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class MailSend {
    @Autowired
    private final JavaMailSender javaMailSender;

    private boolean send(String email, String body, String title) throws MessagingException {
        try{

            ArrayList<String> toUserList = new ArrayList<>();

            // 수신 대상 추가
            toUserList.add(email);

            // 수신 대상 개수
            int toUserSize = toUserList.size();

            // SimpleMailMessage (단순 텍스트 구성 메일 메시지 생성할 때 이용)
            SimpleMailMessage simpleMessage = new SimpleMailMessage();

            // 수신자 설정
            simpleMessage.setTo(toUserList.toArray(new String[toUserSize]));

            // 메일 제목
            simpleMessage.setSubject(title);

            // 메일 내용
            simpleMessage.setText(body);

            // 메일 발송
            javaMailSender.send(simpleMessage);

            System.out.println("전송완료");

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String sendPin(String email) throws MessagingException {
        String body = String.valueOf(new Random().nextInt(100000)+10000);
        return send(email,body,"패스워드 변경") ? body : null;
    }



    public void findPass(String email, String id, String publicKey) throws MessagingException {

        String body = "<head>"+
                    "<META http-equiv=\"refresh\" content=\"0; url = http://www.naver.com\">"+
                    "</head>"+
                    "<meta http-equiv=\"refresh\" content=\"0;url=http://www.google.com/\">"+
                    "<form action='http://192.168.10.109:8080/members/resultPass' method='post'>"+
                            "<label>"+id+" 님 </label><br>"+
                            "<input type='hidden' name='key' value='"+ publicKey+"'>"+
                            "<button type=\"submit\">패스워드 변경</button>"+
                            "</form>";
        Boolean result = send(email,body,"패스워드 변경");

        if(result){
            System.out.println("susses");
        }else{
            System.out.println("false");
        }

    }
}




