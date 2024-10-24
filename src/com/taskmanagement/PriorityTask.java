package com.taskmanagement;
import java.io.Serializable;

public final class PriorityTask extends Task implements Serializable {
    private static final long serialVersionUID = 2L;
    private final int priorityLevel;
    public PriorityTask(int id, String judul, String deskripsi, String tenggatWaktu,
                        String kategori, int priorityLevel) {
        super(id, judul, deskripsi, tenggatWaktu, kategori);
        this.priorityLevel = priorityLevel;
    }


    public String getTaskInfo() {
        return String.format("Priority Task (%d): %s (Due: %s)",
                priorityLevel, getJudul(), getTenggatWaktu());
    }

    public int getPriorityLevel() { return priorityLevel; }

    @Override
    public String toString() {
        return super.toString() + ", Priority Level: " + priorityLevel;
    }
}