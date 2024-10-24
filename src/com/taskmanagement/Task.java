package com.taskmanagement;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Implementasi sealed class
public sealed class Task implements Comparable<Task>, Serializable
        permits PriorityTask {
    private static final long serialVersionUID = 1L;
    private final int id;
    private final String judul;
    private final String deskripsi;
    private String status;
    private final String tenggatWaktu;
    private String kategori;

    // Record untuk menyimpan detail task
    public record TaskDetail(String judul, String deskripsi, String tenggatWaktu, String kategori) {}

    public Task(int id, TaskDetail detail) {
        this.id = id;
        this.judul = detail.judul();
        this.deskripsi = detail.deskripsi();
        this.tenggatWaktu = detail.tenggatWaktu();
        this.kategori = detail.kategori();
        this.status = "Belum Selesai";
    }

    public Task(int id, String judul, String deskripsi, String tenggatWaktu, String kategori) {
        this(id, new TaskDetail(judul, deskripsi, tenggatWaktu, kategori));
    }

    // Method yang akan di-override
    public String getTaskInfo() {
        return String.format("Task biasa: %s (Due: %s)", judul, tenggatWaktu);
    }

    // Implementasi pattern matching dengan instanceof
    public static String getTaskType(Task task) {
        return switch(task) {
            case PriorityTask pt -> "Priority Task level " + pt.getPriorityLevel();
            case Task t -> "Regular Task";
        };
    }

    @Override
    public int compareTo(Task other) {
        return this.tenggatWaktu.compareTo(other.tenggatWaktu);
    }

    @Override
    public String toString() {
        return String.format("Task[ID=%d, Judul='%s', Status='%s', Tenggat Waktu='%s', Kategori='%s', Type='%s']",
                id, judul, status, tenggatWaktu, kategori, getTaskType(this));
    }

    // Getters
    public int getId() { return id; }
    public String getJudul() { return judul; }
    public String getDeskripsi() { return deskripsi; }
    public String getStatus() { return status; }
    public String getTenggatWaktu() { return tenggatWaktu; }
    public String getKategori() { return kategori; }

    // Setters
    public void setStatus(String status) { this.status = status; }
    public void setKategori(String kategori) { this.kategori = kategori; }
}