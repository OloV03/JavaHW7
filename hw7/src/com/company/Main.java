package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        Server server = new Server();
    }
}





// начинаем общение с подключенным пользователем
//             while (!client.isClosed()){
//                // чтение сообщения от пользователя
//                 String enter = in.readLine();
//                 System.out.println("Read from client = "+enter+"\n");
//
//                 if (enter.equalsIgnoreCase("quit")){
//                    out.println("Bye bye ...");
//                    System.out.println("Server is closed");
//                    break;
//                 }
//
//                 // если условие окончания работы не верно - продолжаем работу - отправляем эхо-ответ  обратно клиенту
//                 out.println("Server reply - "+enter + " - OK");
//                 System.out.println("Server Wrote message to client.");
//
//                 // освобождаем буфер сетевых сообщений
//                 out.flush();
//             }
//            System.out.println("- - - - - - - - ");
//
//            // если условие выхода - верно выключаем соединения
//            System.out.println("Client disconnected");
//            System.out.println("Closing connections & channels.");
//
//            // закрываем каналы сокета
//            in.close();
//            out.close();
//
//            // потом закрываем сам сокет общения на стороне сервера!
//            client.close();
//
//            System.out.println("Closing connections & channels - DONE.");
