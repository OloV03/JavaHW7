package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server {
    private List<ClientHandler> clients = new ArrayList<>();
    public List<Cat> catsList = new ArrayList<Cat>();

    public Server() {
        Socket clientSocket = null;
        ServerSocket serverSocket = null;

        catsList.add(new Cat("Васян"));
        catsList.add(new Cat("Пушок"));

        try {
            serverSocket = new ServerSocket(5511);
            System.out.println("Сервер запущен!");

            while (true) {
                clientSocket = serverSocket.accept();   // ожидаем подключения клиента
                ClientHandler client = new ClientHandler(this, clientSocket);
                clients.add(client);                    // добавляем пользователя в общий список

                // "запускаем" пользователя в чат
                new Thread(client).start();

                if (clientSocket.isClosed())
                    continue;

                // передаем файл с супер важной инфой
                client.sendMes("*Перед использованием сервера, прошу ознакомиться с важной информацией из файла*");
                sendFile(client, "example.txt");

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // закрываем подключение
            try {
                clientSocket.close();
                System.out.println("Сервер остановлен");
                serverSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // отправка сообщения всем клиентам
    public void sendMessageToAllClients(String msg) {
        for (ClientHandler o : clients) {
            o.sendMes(msg);
        }
    }

    // удаление клиента из коллекции при выходе из чата
    public void removeClient(ClientHandler client) {
        System.out.println("Клиет " + client.getSocket().getInetAddress() + " покинул чат");
        clients.remove(client);
    }

    // отправка файла
    public void sendFile(ClientHandler client, String nameFile) throws IOException {
        FileReader fileReader = new FileReader(nameFile);
        client.buffer = new BufferedReader(fileReader);
        client.acceptFile("NEW" + nameFile);

        System.out.println("Файл успешно передан клиенту " + client.getSocket().getInetAddress());
    }

}