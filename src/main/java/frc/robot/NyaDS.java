package frc.robot;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class NyaDS extends WebSocketServer {
    // Variables
    public Timer m_timer = new Timer();
    public boolean m_executing = false;
    
    // Sockets
    private ArrayList<WebSocket> sockets = new ArrayList<WebSocket>();

    public NyaDS() {
        super(new InetSocketAddress(9072));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("eexx");
        sockets.add(conn);
        SmartDashboard.putBoolean("NyaDS Client", true);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("eexx");
        sockets.remove(conn);
        SmartDashboard.putBoolean("NyaDS Client", false);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (conn == null)
            return;

        JsonObject json = JsonParser.parseString(message).getAsJsonObject();
        String event = json.get("event").getAsString();
        JsonArray schedule = json.get("data").getAsJsonArray();
        System.out.println(event);
        if (event.equals("auto_execute")) {
            execute(schedule);
        } else if (event.equals("auto_reverse")) {
            reverse(schedule);
        } else {
            String id = json.get("id").getAsString();
            save(schedule, id);
        }

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
    }

    @Override
    public void onStart() {
        SmartDashboard.putBoolean("NyaDS Server", true);
    }

    // NyaDS 1.1: Logging
    public void log(String msg) {
        sockets.forEach((s) -> {
            s.send(msg);
        });
    }

    public void execute(JsonArray schedule) {
        log("Executing...");
        m_timer.start();

        m_executing = true;

        for (JsonElement cmd : schedule) {
            JsonObject data = cmd.getAsJsonObject();
            String type = data.get("type").getAsString();
            double time = data.get("time").getAsDouble();
            double speed = data.get("speed").getAsDouble();

            // TODO: Check if the drive is inverted or normal
            if (type.equals("Drive")) {
                RobotContainer.m_drive.swerve(speed, 0, 0);
                Timer.delay(time);
                RobotContainer.m_drive.swerve(0, 0, 0);
            } else if (type.equals("Rotate")) {
                // ! Unused for now
            } else if (type.equals("Length")) {
                // ! Unused for now
            } else if (type.equals("Delay")) {
                Timer.delay(time);
            } else if (type.equals("Intaker")) {
                // ! Unused for now
            } else if (type.equals("Mouth")) {
                // ! Unused for now
            } else if (type.equals("Open")) {
                // ! Unused for now
            } else if (type.equals("Close")) {
                // ! Unused for now
            } else if (type.equals("Balance")) {
                // ! Unused for now
            }
        }
        m_executing = false;
    }

    public void reverse(JsonArray schedule) {
        log("Reversing...");
        m_executing = true;
        for (int i = schedule.size() - 1; i >= 0; i--) {
            JsonElement cmd = schedule.get(i);
            JsonObject data = cmd.getAsJsonObject();
            String type = data.get("type").getAsString();
            double time = data.get("time").getAsDouble();
            double speed = data.get("speed").getAsDouble();

            if (type.equals("Drive")) {
                RobotContainer.m_drive.swerve(-speed, 0, 0);
                Timer.delay(time);
                RobotContainer.m_drive.swerve(0, 0, 0);
            } else if (type.equals("Rotate")) {
                // ! Unused for now
            } else if (type.equals("Length")) {
                // ! Unused for now
            } else if (type.equals("Delay")) {
                Timer.delay(time);
            }
        }
        m_executing = false;
    }

    public void save(JsonArray schedule, String id) {
        // ! Unused for 1.1 since the client makes the ID
        // String id = String.format("%04d", new Random().nextInt(10000));

        // Convert to JSON string then save
        com.google.gson.Gson gson = new GsonBuilder().create();
        String json = gson.toJson(schedule, JsonArray.class);

        try {
            Files.write(Paths.get("/home/lvuser/nyads/" + id + ".json"), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        log("Saved as " + id + ".json!");
    }

    public JsonArray load(String path) {
        try {
            // Load file into a String
            String pathData = new String(Files.readAllBytes(Paths.get("/home/lvuser/" + path)));
            Gson gson = new Gson();

            // Put the actions into the Array
            return gson.fromJson(pathData, JsonArray.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonArray();
        }
    }
}