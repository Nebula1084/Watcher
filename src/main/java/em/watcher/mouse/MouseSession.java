package em.watcher.mouse;

import em.watcher.control.ControlDef;
import em.watcher.control.ControlPacket;
import em.watcher.control.ControlService;
import em.watcher.device.Device;
import em.watcher.device.DeviceService;
import em.watcher.report.ReportDef;
import em.watcher.report.ReportService;
import em.watcher.report.ReportPacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ReadFieldException extends Exception {
    ReadFieldException(String msg) {
        super(msg);
    }
}

public class MouseSession extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private DeviceService deviceService;
    private ReportService reportService;
    private ControlService controlService;

    private int auth_id;

    private enum MessageType {
        ACK, NACK, LOGIN, REPORT, CONTROL, LOGOUT
    }

    public MouseSession(Socket socket, DeviceService deviceService, ReportService reportService, ControlService controlService) {
        this.socket = socket;
        this.deviceService = deviceService;
        this.reportService = reportService;
        this.controlService = controlService;
    }

    @Override
    public void run() {
        int c;
        System.out.println("connection");
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "ISO-8859-1"));
            out = new PrintWriter(socket.getOutputStream(), true);

            if (login()) {
                System.out.println("Login successfully.");
                while ((c = in.read()) != -1) {
                    System.out.println("Head: " + MessageType.values()[c]);
                    if (c == MessageType.REPORT.ordinal())
                        report();
                    else if (c == MessageType.CONTROL.ordinal())
                        control();
                    else if (c == MessageType.LOGOUT.ordinal())
                        if (logout()) break;
                }
                if (c == -1)
                    throw new ReadFieldException("Read Head Error");
            }

            System.out.println("connection finished.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ReadFieldException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int readInt() throws IOException, ReadFieldException {
        int ret = 0, c;
        for (int i = 0; i < 4; i++) {
            if ((c = in.read()) != -1) {
                ret += c << (8 * i);
            } else throw new ReadFieldException("Read Int Error");
        }
        return ret;
    }

    private float readFloat() throws IOException, ReadFieldException {
        int data = 0, c;
        for (int i = 0; i < 4; i++) {
            if ((c = in.read()) != -1) {
                data += c << (8 * i);
            } else throw new ReadFieldException("Read Float Error");
        }
        return Float.intBitsToFloat(data);
    }

    private double readDouble() throws IOException, ReadFieldException {
        int c;
        long data_double = 0;
        for (int i = 0; i < 8; i++) {
            if ((c = in.read()) != -1) {
                data_double += ((long) c) << (8 * i);
            } else throw new ReadFieldException("Read Double Error");
        }
        return Double.longBitsToDouble(data_double);
    }

    private char[] readKey() throws IOException, ReadFieldException {
        int c;
        char[] key = new char[32];
        for (int i = 0; i < 32; i++) {
            if ((c = in.read()) != -1) {
                key[i] = (char) c;
            } else throw new ReadFieldException("Read Key Error");
        }
        return key;
    }

    private String readString(int length) throws IOException, ReadFieldException {
        int c;
        char[] key = new char[length];
        for (int i = 0; i < length; i++) {
            if ((c = in.read()) != -1) {
                key[i] = (char) c;
            } else throw new ReadFieldException("Read Key Error");
        }
        return String.valueOf(key);
    }

    private void writePacket(int data) throws IOException {
        out.print(data);
    }

    private void writePacket(float data) throws IOException {
        out.print(data);
    }

    private void writePacket(String data) throws IOException {
        out.write(data);
    }

    private void ack() throws IOException {
        out.print((char) MessageType.ACK.ordinal());
        out.flush();
    }

    private void nack() throws IOException {
        out.print((char) MessageType.NACK.ordinal());
        out.flush();
    }

    private void respond(boolean val) throws IOException {
        if (val) ack();
        else nack();
    }

    private boolean login() throws IOException {
        int c;
        boolean ret = true;

        try {
            if ((c = in.read()) != -1) {
                System.out.println("Head: " + MessageType.values()[c]);
                if (c == MessageType.LOGIN.ordinal()) {
                    auth_id = readInt();
                    System.out.println("Device ID: " + auth_id);

                    char[] key = readKey();
                    System.out.print("Key: ");
                    System.out.println(key);

                    Device dev = deviceService.findDevice((long) auth_id);
                    ret = dev.authenticate(String.valueOf(key));
                }
            } else throw new ReadFieldException("Read Head Error");
        } catch (ReadFieldException e) {
            ret = false;
            System.out.println(e.getMessage());
        } catch (Exception e) {
            ret = false;
            if (e instanceof IOException)
                throw e;
            else
                System.out.println(e.getMessage());
        } finally {
            respond(ret);
            return ret;
        }
    }

    protected boolean report() throws IOException {
        int device_id, report_id;
        boolean ret = true;

        try {
            device_id = readInt();
            report_id = readInt();

            ReportDef def = reportService.getReportDef((long) report_id);

            System.out.println("Name: " + def.getName());
            ReportPacket packet = new ReportPacket();
            packet.setAuthId((long) auth_id);
            packet.setDeviceId((long) device_id);
            packet.setDefId(def.getId());

            for (String s : def.getField()) {
                System.out.println(s);
                switch (def.getType(s)) {
                    case ReportDef.TYPE_INT:
                        int data_int = readInt();
                        packet.putField(s, data_int);
                        System.out.println("Type Int: " + data_int);
                        break;
                    case ReportDef.TYPE_FLOAT:
                        float data_float = readFloat();
                        packet.putField(s, data_float);
                        System.out.println("Type Float: " + data_float);
                        break;
                    case ReportDef.TYPE_STRING:
                        int length = def.getLength(s);
                        String data_str = readString(length);
                        packet.putField(s, data_str);
                        System.out.println("Type String: " + data_str);
                        break;
                    default:
                }
            }
            reportService.report(packet);
        } catch (ReadFieldException e) {
            ret = false;
            System.out.println(e.getMessage());
        } catch (Exception e) {
            ret = false;
            if (e instanceof IOException)
                throw e;
            else
                System.out.println(e.getMessage());
        } finally {
            respond(ret);
            return ret;
        }
    }

    protected boolean control() throws IOException {
        boolean ret = true;

        try {
            String tag = readString(1);
            int device_id = readInt();
            int control_id = readInt();

            ControlDef controlDef = controlService.getControlDef((long) control_id);
            ControlPacket packet = new ControlPacket();
            packet.setAuthId((long) auth_id);
            packet.setDeviceId((long) device_id);
            packet.setSR(tag);

            switch (tag) {
                case ControlPacket.Send:
                    int target_id = readInt();
                    Device target = deviceService.findDevice((long) target_id);
                    for (String s : controlDef.getField()) {
                        switch (controlDef.getType(s)) {
                            case ControlDef.TYPE_INT:
                                packet.putField(s, readInt());
                                break;
                            case ControlDef.TYPE_FLOAT:
                                packet.putField(s, readFloat());
                                break;
                            case ControlDef.TYPE_STRING:
                                int length = controlDef.getLength(s);
                                packet.putField(s, readString(length));
                                break;
                            // TODO: ->Double
                            case ControlDef.TYPE_CHAR:
                                packet.putField(s, readString(1));
                                break;
                            default:
                        }
                    }
                    packet.setDefId(controlDef.getId());
                    packet = controlService.recordControl(packet);
                    packet.setTargetId((long) target_id);
                    controlService.sendControl(target, packet);
                    break;

                case ControlPacket.Recv:
                    Device device = deviceService.findDevice((long) device_id);
                    packet.setDefId(controlDef.getId());
                    packet = controlService.recordControl(packet);
                    controlService.recvControl(device, packet);
                    // TODO: send packet data
                    for (String s : controlDef.getField()) {
                        switch (controlDef.getType(s)) {
                            case ControlDef.TYPE_INT:
                                writePacket(Integer.parseInt(packet.getField(s)));
                                break;
                            case ControlDef.TYPE_FLOAT:
                                writePacket(Float.parseFloat(packet.getField(s)));
                                break;
                            case ControlDef.TYPE_STRING:
                                writePacket(packet.getField(s));
                                break;
                            default:
                        }
                    }
                    out.flush();
                    break;
            }
        } catch (ReadFieldException e) {
            ret = false;
            System.out.println(e.getMessage());
        } catch (Exception e) {
            ret = false;
            if (e instanceof IOException)
                throw e;
            else
                System.out.println(e.getMessage());
        } finally {
            respond(ret);
            return ret;
        }
    }

    private boolean logout() throws IOException {
        ack();
        return true;
    }
}