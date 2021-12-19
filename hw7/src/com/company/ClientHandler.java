package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientHandler implements Runnable {

    private Server server;
    private PrintWriter outMes;
    private Scanner inMes;
    private Socket clientSocket = null;
    public BufferedReader buffer;

    public ClientHandler(Server server, Socket socket) throws IOException {
        this.server = server;
        this.clientSocket = socket;
        this.outMes = new PrintWriter(socket.getOutputStream());
        this.inMes = new Scanner(socket.getInputStream());

        // проверка пароля
        outMes.print("Password: ");
        outMes.flush();

        if (!inMes.nextLine().equals("pass")) {
            try {
                disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        while (true & !clientSocket.isClosed()) {
            // сервер отправляет сообщение
            server.sendMessageToAllClients("Новый участник "+clientSocket.getInetAddress()+" вошёл в чат!");
            break;
        }

        while (true & !clientSocket.isClosed()) {
            if (inMes.hasNext()){
                String messageFromClient = clientSocket.getInetAddress()+": "+inMes.nextLine();

                // отключение клиента от сервера
                if (messageFromClient.equalsIgnoreCase(clientSocket.getInetAddress()+": "+"quit")){
                    try {
                        disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                // отправка данных о котах
                if (messageFromClient.equals(clientSocket.getInetAddress()+": "+"about cat!")){
                    try {
                        dataAboutCat();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                // вывод сообщения клиента в консоль
                System.out.println(messageFromClient);
                // отправка всем пользователям
                server.sendMessageToAllClients(messageFromClient);
            }
        }

        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // отправка сообщения на сервер
    public void sendMes(String mes) {
        outMes.println(mes);
        outMes.flush();
    }

    // отключение пользователя от сервера
    public void disconnect() throws IOException {
        server.sendMessageToAllClients("Клиент "+clientSocket.getInetAddress()+" отключился");
        clientSocket.close();       //  закрываем сокет клиента
        server.removeClient(this);  // удаляем клиента из списка
    }

    // получение файла от сервера
    public void acceptFile(String nameFile) throws IOException {
        File file = new File("../"+nameFile);
        FileWriter fileWriter = new FileWriter(file);

        buffer.lines().forEach(x-> {
            try {
                fileWriter.write(x+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        fileWriter.close();
    }

    // получение данных о котах
    public void dataAboutCat() throws IOException, ClassNotFoundException {
        sendMes("Какой кот Вас интересует?\n1 - Васян\t2 - Пушок");
        int choice = Integer.parseInt(inMes.nextLine());
        String nameFileAboutCat = serializeCat(server.catsList.get(choice-1));
        server.sendFile(this, nameFileAboutCat);
        Cat catFromServer = deserialization(nameFileAboutCat);
        sendMes(catFromServer.getName());

    }

    // сериализация "кота"
    public String serializeCat(Cat cat) throws IOException {
        File myFile = new File(cat.getName()+".cat");
        FileOutputStream fileOutputStream = new FileOutputStream(myFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(cat);

        return myFile.getName();
    }

    // десериализация "кота"
    public Cat deserialization(String nameFile) throws IOException, ClassNotFoundException {
        String myFileName = nameFile;
        FileInputStream fileInputStream = new FileInputStream(myFileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        FileReader fileReader = new FileReader(nameFile);
        BufferedReader buffer = new BufferedReader(fileReader);

        buffer.lines().forEach(x -> System.out.println(x));
        return  (Cat) objectInputStream.readObject();
    }
}
