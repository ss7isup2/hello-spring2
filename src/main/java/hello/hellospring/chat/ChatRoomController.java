package hello.hellospring.chat;

import hello.hellospring.controller.CookieController;
//import hello.hellospring.kafka.Consumer;
//import hello.hellospring.kafka.Producer;

import hello.hellospring.members.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("chat")
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;
    private final HttpSession httpSession;
    private final CookieController cookieController;
//    private final Producer producer;
//    private final Consumer consumer;
    //    private final ChatRoom chatRoom;


    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("MEMBER"));
        return "chat/room";
    }

    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatRoomRepository.findAllRoom();
    }

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        System.out.println("ChatRoomController ==> createRoom");
        return chatRoomRepository.createChatRoom(name);
    }

    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        System.out.println("ChatRoomController ==> roomDetail");
        model.addAttribute("Id", "g1");
        return "chat/roomdetail";
    }

    @PostMapping("/logout")
    @ResponseBody
    public String logout(){
        System.out.println("loginout!!");
        return null;
    }



    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }
}






















