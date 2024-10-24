package com.taskmanagement;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("\n=== Sistem Manajemen Task ===");
                System.out.println("1. Tambah Task");
                System.out.println("2. Hapus Task");
                System.out.println("3. Ubah Status Task");
                System.out.println("4. Tampilkan Semua Task");
                System.out.println("5. Cari Task");
                System.out.println("6. Urutkan Task berdasarkan Tenggat Waktu");
                System.out.println("7. Lihat Task berdasarkan Kategori");
                System.out.println("8. Cek Notifikasi Task");
                System.out.println("9. Keluar");
                System.out.print("Pilih menu (1-9): ");

                int pilihan = scanner.nextInt();
                scanner.nextLine(); // Membersihkan buffer

                switch (pilihan) {
                    case 1:
                        System.out.print("Masukkan judul task: ");
                        String judul = scanner.nextLine();
                        System.out.print("Masukkan deskripsi task: ");
                        String deskripsi = scanner.nextLine();
                        System.out.print("Masukkan tengat waktu (yyyy-MM-dd): ");
                        String tenggatWaktu = scanner.nextLine();
                        System.out.print("Masukkan kategori: ");
                        String kategori = scanner.nextLine();
                        System.out.print("Apakah ini task prioritas? (y/n): ");
                        boolean isPriority = scanner.nextLine().equalsIgnoreCase("y");
                        int priorityLevel = 0;
                        if (isPriority) {
                            System.out.print("Masukkan level prioritas (1-5): ");
                            priorityLevel = scanner.nextInt();
                        }
                        manager.tambahTask(judul, deskripsi, tenggatWaktu, kategori,
                                isPriority, priorityLevel);
                        break;

                    case 2:
                        System.out.print("Masukkan ID task yang akan dihapus: ");
                        int idHapus = scanner.nextInt();
                        manager.hapusTask(idHapus);
                        break;

                    case 3:
                        System.out.print("Masukkan ID task: ");
                        int idUbah = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Masukkan status baru (Selesai/Belum Selesai): ");
                        String statusBaru = scanner.nextLine();
                        manager.ubahStatus(idUbah, statusBaru);
                        break;

                    case 4:
                        manager.tampilkanSemuaTask();
                        break;

                    case 5:
                        System.out.print("Masukkan kata kunci pencarian: ");
                        String keyword = scanner.nextLine();
                        List<Task> hasilPencarian = manager.cariTask(keyword);
                        if (hasilPencarian.isEmpty()) {
                            System.out.println("Tidak ditemukan task yang sesuai.");
                        } else {
                            System.out.println("\nHasil Pencarian:");
                            hasilPencarian.forEach(System.out::println);
                        }
                        break;

                    case 6:
                        manager.urutkanTaskBerdasarkanTenggatWaktu();
                        System.out.println("Task telah diurutkan berdasarkan tenggat waktu.");
                        manager.tampilkanSemuaTask();
                        break;

                    case 7:
                        System.out.print("Masukkan kategori: ");
                        String kategoriCari = scanner.nextLine();
                        List<Task> tasksByCategory = manager.getTasksByCategory(kategoriCari);
                        if (tasksByCategory.isEmpty()) {
                            System.out.println("Tidak ada task dalam kategori tersebut.");
                        } else {
                            System.out.println("\nTask dalam kategori " + kategoriCari + ":");
                            tasksByCategory.forEach(System.out::println);
                        }
                        break;

                    case 8:
                        List<Task> taskMendesak = manager.getTaskMendekatiBatasWaktu();
                        if (taskMendesak.isEmpty()) {
                            System.out.println("Tidak ada task yang mendekati batas waktu.");
                        } else {
                            System.out.println("\nTask yang mendekati batas waktu:");
                            taskMendesak.forEach(System.out::println);
                        }
                        break;

                    case 9:
                        System.out.println("Terima kasih telah menggunakan sistem manajemen task!");
                        scanner.close();
                        System.exit(0);
                }
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
                scanner.nextLine(); // Membersihkan buffer
            }
        }
    }
}