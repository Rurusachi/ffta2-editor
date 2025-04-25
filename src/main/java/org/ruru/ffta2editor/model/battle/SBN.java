package org.ruru.ffta2editor.model.battle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Comparator;

import org.ruru.ffta2editor.utility.BinaryTree;
import org.ruru.ffta2editor.utility.LZSS;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SBN {

    public static class Command {
        public int address;

        public byte opcode;
        public byte size;
        public byte[] parameters;
        
        public Command(ByteBuffer buffer) {
            this.address = buffer.position();
            this.opcode = buffer.get();
            this.size = buffer.get();
            this.parameters = new byte[this.size-2];
            buffer.get(parameters);
        }

        public Command(int address, byte opcode, byte size, byte[] parameters) {
            this.address = address;
            this.opcode = opcode;
            this.size = size;
            this.parameters = parameters;
        }

        public byte[] toBytes() {
            ByteBuffer newSbn = ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN);
            newSbn.put(opcode);
            newSbn.put(size);
            newSbn.put(parameters);
            return newSbn.array();
        }
    }

    public BinaryTree<Command> tree;
    public ArrayList<Command> commands = new ArrayList<>();
    private short numJumps;
    private short unknown1;
    private int startAddress;
    public int endAddress;
    private int unknown2;

    public SBN(ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.numJumps = buffer.getShort();
        this.unknown1 = buffer.getShort();
        this.startAddress = buffer.getInt();
        this.endAddress = buffer.getInt();
        this.unknown2 = buffer.getInt();
        
        tree = new BinaryTree<>(buffer.slice().order(ByteOrder.LITTLE_ENDIAN));

        buffer.position(startAddress);
        while (buffer.position() < endAddress) {
            commands.add(new Command(buffer));
        }

        tree.traverseInOrder(node -> commands.stream().filter(t -> t.address == node.offset*0x10).findFirst().ifPresent(t -> node.value = t));
    }

    public byte[] toBytes() {
        return toByteBuffer().array();
    }

    public ByteBuffer toByteBuffer() {
        tree.traverseInOrder(node -> node.offset = node.value.address / 0x10);
        byte[] treeBytes = tree.toBytes(); // Just for size

        ByteBuffer newSbn = ByteBuffer.allocate(0x10 + treeBytes.length + commands.stream().mapToInt(c -> c.size).sum()).order(ByteOrder.LITTLE_ENDIAN);
        numJumps = (short)tree.size;
        startAddress = 0x10 + treeBytes.length;
        endAddress = newSbn.capacity();


        newSbn.putShort(numJumps);
        newSbn.putShort(unknown1);
        newSbn.putInt(startAddress);
        newSbn.putInt(endAddress);
        newSbn.putInt(unknown2);

        newSbn.position(startAddress);

        for (Command c : commands) {
            c.address = newSbn.position();
            newSbn.put(c.toBytes());
        }
        if (newSbn.position() < newSbn.limit()) {
            System.err.println(String.format("Incorrect SBN size: %d < %d", newSbn.position(), newSbn.limit()));
        }

        newSbn.position(0x10);
        tree.traverseInOrder(node -> node.offset = node.value.address / 0x10);
        treeBytes = tree.toBytes(); // Command addresses have been updated
        newSbn.put(treeBytes);

        return newSbn.rewind();
    }
}
