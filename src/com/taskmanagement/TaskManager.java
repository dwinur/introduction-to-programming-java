package com.taskmanagement;

import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class TaskManager {
    private List<Task> tasks;
    private int nextId;
    private static final String FILE_PATH = "tasks.dat";

    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.nextId = 1;
        loadTasksFromFile();
    }

    // Exception Handling untuk file operations
    private void loadTasksFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            tasks = (List<Task>) ois.readObject();
            nextId = tasks.stream()
                    .mapToInt(Task::getId)
                    .max()
                    .orElse(0) + 1;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Tidak ada file data yang ditemukan. Membuat baru...");
            tasks = new ArrayList<>();
        }
    }

    // Exception Handling untuk penyimpanan file
    private void saveTasksToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            throw new TaskManagementException("Gagal menyimpan data: " + e.getMessage());
        }
    }

    // Lambda Expression untuk filtering tasks
    public List<Task> cariTask(String keyword) {
        return tasks.stream()
                .filter(task -> task.getJudul().toLowerCase().contains(keyword.toLowerCase()) ||
                        task.getDeskripsi().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Lambda Expression untuk sorting
    public void urutkanTaskBerdasarkanTenggatWaktu() {
        Collections.sort(tasks, (t1, t2) -> t1.getTenggatWaktu().compareTo(t2.getTenggatWaktu()));
    }

    // Exception class kustom
    public class TaskManagementException extends RuntimeException {
        public TaskManagementException(String message) {
            super(message);
        }
    }

    // Metode untuk mendapatkan notifikasi
    public List<Task> getTaskMendekatiBatasWaktu() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return tasks.stream()
                .filter(task -> {
                    LocalDate deadline = LocalDate.parse(task.getTenggatWaktu(), formatter);
                    long daysUntilDeadline = today.until(deadline).getDays();
                    return daysUntilDeadline >= 0 && daysUntilDeadline <= 3;
                })
                .collect(Collectors.toList());
    }

    // Metode untuk menambah task dengan kategori
    private void processTask(Task task) {
        switch (task) {
            case PriorityTask pt when pt.getPriorityLevel() > 3 -> {
                System.out.println("High priority task detected: " + pt.getTaskInfo());
                // Kirim notifikasi khusus untuk task prioritas tinggi
                sendHighPriorityNotification(pt);
            }
            case PriorityTask pt -> {
                System.out.println("Normal priority task: " + pt.getTaskInfo());
                // Proses normal untuk task prioritas
                processNormalPriorityTask(pt);
            }
            case Task t -> {
                System.out.println("Regular task: " + t.getTaskInfo());
                // Proses untuk task reguler
                processRegularTask(t);
            }
        }
    }

    // Helper methods untuk processTask
    private void sendHighPriorityNotification(PriorityTask task) {
        System.out.println("URGENT: High priority task needs immediate attention - " + task.getJudul());
    }

    private void processNormalPriorityTask(PriorityTask task) {
        System.out.println("Processing priority task: " + task.getJudul());
    }

    private void processRegularTask(Task task) {
        System.out.println("Processing regular task: " + task.getJudul());
    }

    // Menggunakan processTask dalam metode tambahTask
    public void tambahTask(String judul, String deskripsi, String tenggatWaktu,
                           String kategori, boolean isPriority, int priorityLevel) {
        try {
            validateTaskInput(judul, deskripsi, tenggatWaktu);
            Task task;
            if (isPriority) {
                task = new PriorityTask(nextId++, judul, deskripsi, tenggatWaktu,
                        kategori, priorityLevel);
            } else {
                task = new Task(nextId++, judul, deskripsi, tenggatWaktu, kategori);
            }
            tasks.add(task);
            // Menggunakan processTask untuk memproses task baru
            processTask(task);
            saveTasksToFile();
            System.out.println("Task berhasil ditambahkan!");
        } catch (TaskManagementException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Validasi input dengan Exception Handling
    private void validateTaskInput(String judul, String deskripsi, String tenggatWaktu) {
        if (judul == null || judul.trim().isEmpty()) {
            throw new TaskManagementException("Judul tidak boleh kosong");
        }
        if (deskripsi == null || deskripsi.trim().isEmpty()) {
            throw new TaskManagementException("Deskripsi tidak boleh kosong");
        }
        try {
            LocalDate.parse(tenggatWaktu, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            throw new TaskManagementException("Format tanggal tidak valid (gunakan yyyy-MM-dd)");
        }
    }

    // Metode untuk mencari task berdasarkan kategori
    public List<Task> getTasksByCategory(String kategori) {
        return tasks.stream()
                .filter(task -> task.getKategori().equalsIgnoreCase(kategori))
                .collect(Collectors.toList());
    }

    private boolean isValidStatus(String status) {
        return switch (status.toLowerCase()) {
            case "selesai", "done", "completed" -> true;
            case "belum selesai", "pending", "in progress" -> true;
            default -> false;
        };
    }

    public void tampilkanSemuaTask() {
        if (tasks.isEmpty()) {
            System.out.println("Tidak ada task yang tersedia.");
            return;
        }
        tasks.stream()
                .map(Task::getTaskInfo)  // Method reference
                .forEach(System.out::println);  // Method reference
    }

    public void hapusTask(int id) {
        tasks.removeIf(task -> task.getId() == id);
        saveTasksToFile();
        System.out.println("Task berhasil dihapus!");
    }

    public void ubahStatus(int id, String status) {
        if (!isValidStatus(status)) {
            throw new TaskManagementException("Status tidak valid");
        }

        tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .ifPresentOrElse(
                        task -> {
                            task.setStatus(status);
                            saveTasksToFile();
                            System.out.println("Status task berhasil diubah!");
                        },
                        () -> System.out.println("Task tidak ditemukan!")
                );
    }
}
