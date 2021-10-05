package hello.hellospring.websocket;


import com.google.gson.Gson;
import hello.hellospring.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
@Setter
public class WebSocketMessageSend extends Thread {

    public boolean stop = false;
    public boolean work = true;
    private final ChatSessionList chatSessionList;
    private Object message;
    private final int maxCore = Runtime.getRuntime().availableProcessors();

    @SneakyThrows
    public void start(){
        ExecutorService executorService = Executors.newFixedThreadPool(maxCore);
        System.out.println("ThreadA ==> run");
        ChatMessage chatMessage = (ChatMessage) message;
        ArrayList<WebSocketSession> keySet = chatSessionList.get(chatMessage.getRoomId());

        for (WebSocketSession session : keySet) {
            Runnable runnable = new Runnable() {
                @SneakyThrows
                @Override
                public void run() {

                    Gson gson = new Gson();
                    String message = gson.toJson(chatMessage);
                    session.sendMessage(new TextMessage(message));



                    //스레드에게 시킬 작업 내용
                    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;

                    int poolSize = threadPoolExecutor.getPoolSize();//스레드 풀 사이즈 얻기
                    String threadName = Thread.currentThread().getName();//스레드 풀에 있는 해당 스레드 이름 얻기

                    System.out.println("[총 스레드 개수:" + poolSize + "] 작업 스레드 이름: "+threadName);

                    //일부로 예외 발생 시킴
//                    int value = Integer.parseInt("예외");
                }


            };

            //스레드풀에게 작업 처리 요청
            executorService.execute(runnable);
            //executorService.submit(runnable);
            executorService.shutdown();
//            executorService.awaitTermination(20, TimeUnit.SECONDS);




        }

        //스레드풀 종료




    }


}
